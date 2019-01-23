/* SystemImage.java
 * 
 * Copyright (c) 2019 Felix Baumann
 *
 * felix.baumann.freiburg@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files
 * (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS IN THE SOFTWARE.
 */

package systemRecommendation;

import java.util.HashMap;
//import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
//import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;

/* Instances of this class represent system environments that could
 * potentially be used for the given disk.
 * 
 */
public class SystemImage {

	/* Create a tracker for SystemImage ids. */
	private static final AtomicInteger idTracker = new AtomicInteger(0);
	
	/* To distinguish systems, they should have a unique ID. */
	private final int systemID = idTracker.getAndIncrement();

	/* We track the pronoms of the files each program can read/write.
	 * If a program is installed on multiple systems, we don't have to access
	 * wikidata several times.
	 */
	private static HashMap<String, HashSet<String>> programReads
	    = new HashMap<String, HashSet<String>>();
	private static HashMap<String, HashSet<String>> programWrites
	    = new HashMap<String, HashSet<String>>();

	/* The pronoms of all file formats that can be read or written
	 * by the system or by programs installed on the system. */
	private HashSet<String> readablePronoms;
	private HashSet<String> writablePronoms;


	/* Constructor for a system image representing a system environment.
	 * 
	 * Preferred constructor.
	 */
	public SystemImage(HashSet<String> readsPronoms,
			           HashSet<String> writesPronoms)
	{
		readablePronoms 
		    = (readsPronoms != null ? readsPronoms : new HashSet<String>());
		writablePronoms 
		    = (writesPronoms != null ? writesPronoms : new HashSet<String>());
	}

	/* Constructor for a system image representing a system environment.
	 * 
	 * Secondary constructor (much less efficient).
	 */
	public SystemImage(HashSet<String> installedPrograms)
	{
		readablePronoms = new HashSet<String>();
		writablePronoms = new HashSet<String>();

		/* Iterate over the wikidata QIDs of all installed programs
		 * and fetch their readable and writable file formats from wikidata
		 * if necessary.
		 */
		Iterator<String> iter = installedPrograms.iterator();
		while (iter.hasNext())
		{
			String current = iter.next();

			/* If the current program never occured before in any other
			 * system, we first have to access wikidata and fetch
			 * the pronoms.
			 */	
			if (!programReads.containsKey(current))
			{
				getPronomsFromWikidata(current);
			}

			/* Now the pronoms for the current program
			 * are definitely in the archive, so a simple lookup
			 * is enough work to add them to the current system.
			 */		    
			addPronomsFromArchive(current);
		}
	}

	/* This method looks up the readable/writable pronoms of a program
	 * given by it's wikidata QID and adds those pronoms to the
	 * readablePronoms/writablePronoms sets of this system.
	 * 
	 * Make sure the pronoms of the given program QID have been fetched
	 * from wikidata before, otherwise this method will fail.
	 */
	private void addPronomsFromArchive(String programQID)
	{
		readablePronoms.addAll(programReads.get(programQID));
		writablePronoms.addAll(programWrites.get(programQID));
	}

	/* This function fetches the pronoms of the readable and writable
	 * file formats of a program given by it's wikidata QID and adds those
	 * pronoms to the local archive of pronoms of the current program.
	 * 
	 * If the program then appears on any system, the pronoms can be looked
	 * up in the archive instead of accessing wikidata again.
	 * 
	 */
	private void getPronomsFromWikidata(String programQID)
	{
		/* Initialize SPARQL repository and connection. */
		SPARQLRepository sparqlRepository
		    = new SPARQLRepository("https://query.wikidata.org/sparql");
		sparqlRepository.initialize();
	
		RepositoryConnection sparqlConnection
		    = sparqlRepository.getConnection();


		/* Get the readable pronoms. */

		/* Query for pronoms of readable formats of the given program. */
		String query = "SELECT ?item ?itemlabel"
				     + "WHERE"
				     + "{"
				     + "wd:" + programQID + " wdt:P1072 ?format ."
				     + "?format wdt:P2748 ?item" 
				     + "}";	

		/* Fetch the actual query results. */
		TupleQuery tupleQuery
		    = sparqlConnection.prepareTupleQuery(QueryLanguage.SPARQL, query);

		String result = null;

		/* Initialize the set of readable formats of this program. */
		programReads.put(programQID, new HashSet<String>());

		/* Transform the result into strings and add the pronom to the set. */
		for (BindingSet bs : QueryResults.asList(tupleQuery.evaluate()))
		{
		    result = bs.toString();
		    
		    if (result != null)
		    {
		    	programReads.get(programQID).add(result.substring(7, result.length() - 2));
		    }
		}


		/* Get the writable pronoms. */

		/* Adjust the query to "writable". */
		query = "SELECT ?item ?itemlabel"
			+ "WHERE"
			+ "{"
			+ "wd:" + programQID + " wdt:P1073 ?format ."
			+ "?format wdt:P2748 ?item" 
			+ "}";

		/* Fetch the new query results. */
		tupleQuery = sparqlConnection.prepareTupleQuery(QueryLanguage.SPARQL, query);

		/* Initialize the set of writable formats of this program. */
		programWrites.put(programQID, new HashSet<String>());

		/* Transform the result into strings and add the pronom to the set. */
		for (BindingSet bs : QueryResults.asList(tupleQuery.evaluate())) {
		    result = bs.toString();

		    if (result != null)
		    {
		    	programWrites.get(programQID).add(result.substring(7, result.length() - 2));
		    }
		}
	}

	/* Return the set of pronoms this system can read. */
	public HashSet<String> readablePronoms()
	{
		return readablePronoms;
	}

	/* Return the set of pronoms this system can write. */
	public HashSet<String> writablePronoms()
	{
		return writablePronoms;
	}

	/* Checks whether the given pronom is readable by the system. */
	public boolean readable(String pronom)
	{
		return readablePronoms.contains(pronom);
	}

	/* Checks whether the given pronom is writable by the system. */
	public boolean writable(String pronom)
	{
		return writablePronoms.contains(pronom);
	}

	/* ID getter */
	public int id()
	{
		return systemID;
	}
}
