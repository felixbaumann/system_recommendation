/* WikidataAccess.java
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

import java.util.ArrayList;
//import java.util.Collections;
import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;


import org.wikidata.wdtk.datamodel.interfaces.EntityDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemIdValue;
//import org.wikidata.wdtk.datamodel.interfaces.PropertyIdValue;
import org.wikidata.wdtk.datamodel.interfaces.Statement;
//import org.wikidata.wdtk.datamodel.interfaces.StatementGroup;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;

public class WikidataAccess {

	public static void main(String[] args)
		throws MediaWikiApiErrorException, WikidataInvalidIDException {

        //ArrayList<String> a = getWikidataStatementTargets("Q11261", "P1072");
	}

	/* Given an entity and a kind of statements, both identified by their IDs,
	 * this function returns all the entity QIDs that are linked with this
	 * kind of statements from the given entity.
	 * 
	 * Example: Given the entity of Microsoft Word and the statement kind
	 * readableFileFormat (getWikidataStatementTargets("Q11261", "P1072")),
	 *
	 * the function returns an ArrayList of the QIDs of all file formats that
	 * can be read by Microsoft Word according to Wikidata.
	 *
	 * In this case, those would be the following:
	 *
	 * Q686498
	 * Q3033641
	 * Q467454
	 * Q1145976
	 * Q26208225
	 * Q1727359
	 * Q27203404
	 * Q27203601
	 * 
	 * In case the given statement ID either doesn't exist for the given
	 * entity or not at all, a WikidataInvalidIDException will be thrown.
	 * 
	 * In case the given entity ID doesen't exist,
	 * a NoSuchEntityErrorException will be thrown.
	 */
	public static ArrayList<String> getWikidataStatementTargets(
		String entityId, String statementId) 
			throws MediaWikiApiErrorException, WikidataInvalidIDException
	{
		// Define a data fetcher that can fetch Wikidata pages.
		WikibaseDataFetcher fetcher 
		    = WikibaseDataFetcher.getWikidataDataFetcher();

		// Get the wikidata document of a given QID.
		EntityDocument doc = fetcher.getEntityDocument(entityId);

		// Try to import all the statements of this kind of this document.
		List<Statement> statements;
		try
		{
		statements = ((ItemDocument) doc).findStatementGroup(statementId)
			.getStatements();
		}
		catch(java.lang.NullPointerException e)
		{
			throw new WikidataInvalidIDException(
					"The given statementId doesn't exist for this "
					+ "entity or not at all.");
		}

		// Define a target where the readable files QIDs can be stored
		ArrayList<String> readableFiles = new ArrayList<String>();
	
		// Get the QIDs of all readableFileFormats and store them in an array.
        for (int i = 0; i < statements.size(); i++)
        {
        	readableFiles.add(((ItemIdValue)statements.get(i).getValue())
        	    .getId().toString());
        }

        return readableFiles;
	} 
}
