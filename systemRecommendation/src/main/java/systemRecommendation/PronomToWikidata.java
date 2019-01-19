/* PronomToWikidata.java
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

/* Note: This class is currently not used. */

package systemRecommendation;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.QueryResults;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;


/* This class fetches the wikidata QID of a given pronom.
 * 
 */
public class PronomToWikidata {

	public static void main(String[] args) {
		
		// Example:
		System.out.println(pronomToWikidata("x-fmt/273"));
		
		
	}
	
	/* For a given pronom (e.g. from Siegfried) this function will get the QID
	 * of wikidata of the corresponding file format.
	 * 
	 * It will return null if no wikidata entity exists or isn't
	 * linked with the "pronom file format identifier".
	 * 
	 */
	public static String pronomToWikidata(String pronom)
	{
		SPARQLRepository sparqlRepository
		    = new SPARQLRepository("https://query.wikidata.org/sparql");
		sparqlRepository.initialize();

		RepositoryConnection sparqlConnection
		    = sparqlRepository.getConnection();
		
		String query = "SELECT ?item ?itemlabel"
			     + "WHERE"
			     + "{"
			     + "	?item wdt:P31 wd:Q235557 ."
			     + "	?item wdt:P2748 \"" + pronom + "\" ."
			     // e.g. "	?item wdt:P2748 \"x-fmt/273\" ."
			     + "}";
		
		/* Fetch the actual query results. */
		TupleQuery tupleQuery
		    = sparqlConnection.prepareTupleQuery(QueryLanguage.SPARQL, query);
		
		String result = null;
		
		/* Transform the result into a string. */
		for (BindingSet bs : QueryResults.asList(tupleQuery.evaluate())) {
		    result = bs.toString();
		}

		/* If there's no entity for the given pronom just return null. */
		if (result == null) { return null; }

		/* Return the QID of the entity matching the pronom. */
		return result.substring(37, result.length() - 1);
	}
}
