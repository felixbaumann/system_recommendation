/* ExtractSiegfriedData.java
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

import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import org.javatuples.Pair;
import org.json.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

/* This class is supposed to extract useful information from a Siegfried
 * analysis given as JSON.
 * 
 * Use extractSiegfriedDataFromFile(path) to get the MIME-Types from the file
 * at PATH.
 * 
 * Use extractSiegfriedData(text) to get the MIME-Types from the string
 * in TEXT.
 * 
 */
public class ExtractSiegfriedData
{
    /* This function extracts all pronoms of all files in a directory,
     * assuming they're all Siegfried outputs.
     * 
     * DIRECTORY	the directory with the Siegfried output files.
     * 
     * RETURNS		a nested ArrayListlist e.g. 
     * 				[[[fmt/18], [fmt/18], [fmt/18]], [[fmt/18]]]
     * 				here representing two Siegfried output files,
     * 				first one with three files, second one with only one file.
     * 				All files got only one match whose pronom is fmt/18
     */
    public static ArrayList<ArrayList<SiegfriedFile>> extractPronoms(
    	String directory) throws IOException
    {
    	/* 1. Get all files in the directoy. */
    	File folder = new File(directory);
    	File[] files = folder.listFiles() ;
    	
    	/* 2. Extract Siegfried data from each file. */
    	ArrayList<ArrayList<SiegfriedFile>> result
    	    = new ArrayList<ArrayList<SiegfriedFile>>();
    	
    	if (files == null) { return result; }
    	
    	/* For each file in the given directory... */
    	for (int index = 0; index < files.length; index++)
    	{
    		/* Try to extract the pronoms from the Siegfried output. */
    		try 
    		{
    			result.add(extractSiegfriedDataFromFile(
    				directory + files[index].getName()));
    		}
    		/* File content is corrupted. Proceed with next file. */
    		catch(org.json.JSONException e)
    		{
    			continue;
    		}
    	}
    	return result;
    }
	
	
    /* This function extracts the pronoms
     * from a given file containing a Siegfried-JSON-output.
     * 
     * PATH		the path to the file containing the Siegfried-output
     * 
     * RETURNS	a list of Siegfried files, for the given Siegfried output.
     * 			Each Siegfried file contains guesses for pronoms for the file
     * 			as well as the size of the file.
     * 
     * EXCEPTION	IOException if the file doesn't exist or isn't accessible
     * EXCEPTION	JSONException if the file doesn't contain a valid JSON 
     * 				object.
     * 
     */
    public static ArrayList<SiegfriedFile> 
        extractSiegfriedDataFromFile(String path)
        	throws IOException, org.json.JSONException
    {
    	/* 1. Read the given file and store it's content as a string. */
   	    String siegfriedJSON
   	        = new String(Files.readAllBytes(Paths.get(path)));
    	
    	/* 2. Use the function below to extract the data. */
    	return extractSiegfriedDataFromString(siegfriedJSON);
    }
	
	
	/* This function extracts the pronoms and filesizes 
	 * from a given Siegfried-JSON-output.
	 * 
	 * SIEGFRIEDJSON	the Siegfried output in JSON
	 * 
	 * RETURNS			the pronoms from the given Siegfried output
	 * 
	 * EXCEPTION		if the input string isn't in proper JSON
	 */
	public static ArrayList<SiegfriedFile> 
	    extractSiegfriedDataFromString(String siegfriedJSON)
	    	throws org.json.JSONException
	{
		/* Transform the Siegfried output into an accessible JSON object */
		JSONObject object = new JSONObject(siegfriedJSON);

		/* Create a list that will store the pronoms of all files
		 * of the given JSON.
		 */
		return extractInfoFromSiegfried(object);
	}
	
	
	/* This function extracts the pronoms of all listed files
	 * in the JSON output of Siegfried.
	 * 
	 * It returns a list with each entry representing a file,
	 * containing a list of all possible matches as well as the filesize.
	 * Usually, there just seems to be a single match or none at all.
	 * Those matches are given as string of their pronoms.
	 * 
	 * OBJECT	the JSONObject to extract data from
	 * 
	 */
	private static ArrayList<SiegfriedFile> extractInfoFromSiegfried
	(JSONObject object)
	{
		/* Create a list for each file containing a list for each match. */
		ArrayList<SiegfriedFile> result = new ArrayList<SiegfriedFile>();

        /* Check whether the object and it's file list actually exist. */
        if (listNotExistent(object, "files")) { return result; }

		/* Consider all files in the Siegfried output. */
        int fileCount = object.getJSONArray("files").length();

		for (int file = 0; file < fileCount; file++)
		{
			/* Create a list for all matches of the current file. */
			result.add(
				extractInfoOfSingleFile(
					object.getJSONArray("files").getJSONObject(file)));
		}
		return result;
	}
	
	
	/* Accepts a JSON object from the part of a Siegfried output that
	 * represents a single file. Returns a SiegfriedFile object containing
	 * the file's size and the pronoms of all possible matches.
	 */
	private static SiegfriedFile extractInfoOfSingleFile(JSONObject file)
	{
		/*1. Get the file size. Note that if unknown it will be set to -1. */
		SiegfriedFile result = new SiegfriedFile(getFileSize(file));
		
		
		/* 2. Get the pronom matches. */
		
		/* Check whether the file and it's match list actually exist. */
		if (listNotExistent(file, "matches")) { return result; }
		
		/* Consider each match for each file. */
		int matchCount = file.getJSONArray("matches").length();
		
		for (int match = 0; match < matchCount; match++)
		{
			/* Check whether the match and it's id entry actually exist. */
			if (stringNotExistent(file.getJSONArray("matches")
					.getJSONObject(match), "id"))
			{
				/* If there's no pronom, store 'UNKNOWN' */
				result.addMatch("UNKNOWN");
			}
			else
			{
			/* Add the current match to the list of matches of
			 * the current file. */
			    result.addMatch(file.getJSONArray("matches")
					.getJSONObject(match).getString("id"));
			}
		}
		return result;
	}


	/* This function checks, whether the given JSONObject even exists
	 * and whether it contains a JSONArray of a given name.
	 * 
	 * RETURNS true if either the object or the array do NOT exist.
	 */
	private static boolean listNotExistent(JSONObject object, String property)
	{
		try 
		{
			object.getJSONArray(property).length();
		}
		catch(java.lang.NullPointerException e)
		{
			/* No object was given. */
			return true;
		}
		catch(org.json.JSONException e)
		{
			/* The object doesn't contain an array property of the given name.
			 */
			return true;
		}
		
		return false;
	}
	
	/* This function checks, whether the given JSONObject actually exists
	 * and whether it contains a string property of a given name.
	 * 
	 * RETURNS true if either the object or the string do NOT exist.
	 * 
	 */
	private static boolean stringNotExistent(JSONObject object,
		String property)
	{	
		try 
		{
			object.getString(property);
		}
		catch(java.lang.NullPointerException e)
		{
			/* No object was given. */
			return true;
		}
		catch(org.json.JSONException e)
		{
			/* The object doesn't contain a property of the given name. */
			return true;
		}
		return false;
	}


	/* This method simply extracts the integer value with the key "filesize"
	 * from the given JSON object.
	 * If no such property exists, it will return -1. 
	 */
	private static int getFileSize(JSONObject object)
	{
		try 
		{
			return object.getInt("filesize");
		}
		catch(java.lang.NullPointerException e)
		{
			/* No object was given. */
			return -1;
		}
		catch(org.json.JSONException e)
		{
			/* The object doesn't contain a file size.*/
			return -1;
		}
	}
}
