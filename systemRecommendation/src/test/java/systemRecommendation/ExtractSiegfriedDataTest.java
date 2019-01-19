/* ExtractSiegfriedDataTest.java
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

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

public class ExtractSiegfriedDataTest {

	public static void testExtractPronoms() throws IOException
	{
		String directory = "D:\\\\Bibliotheken\\\\Studium\\\\Bachelor"
			    + "arbeit\\\\sfdata\\\\unitTests\\\\01Test\\\\";

		ArrayList<ArrayList<SiegfriedFile>> result
		    = ExtractSiegfriedData.extractPronoms(directory);

		if (result.size() == 3)
		{
			/* 1.ISO */
			assertEquals(result.get(0).get(0).get(0), "fmt/18");
	
			/* 3.ISO */
			assertEquals(result.get(2).get(0).get(0), "fmt/212");
		}		
	}

	public static void testExtractSiegfriedData()
	{
		/* An invalid JSON string shall throw a JSONException, nothing else. */
		boolean exception = false;
		try 
		{
		    ExtractSiegfriedData.extractSiegfriedDataFromString("");
		}
		catch(org.json.JSONException e)
		{
			exception = true;
		}
		assertTrue(exception);

		/* Extract from empty JSON object. */
		assertTrue(ExtractSiegfriedData.extractSiegfriedDataFromString("{}").size() == 0);

		/* Extract from JSON object with two files and two matches for one of those. */
		String json = "{\"files\":[{\"matches\":[{\"id\":\"fmt/18\"}, {\"id\":\"fmt/19\"}]}, {\"matches\":[{\"id\":\"fmt/18\"}]}]}";
		ArrayList<SiegfriedFile> pronoms = ExtractSiegfriedData.extractSiegfriedDataFromString(json);
		
		assertTrue(pronoms.size() == 2);
		assertTrue(pronoms.get(0).matchCount() == 2);
		assertTrue(pronoms.get(1).matchCount() == 1);
		assertEquals("fmt/18", pronoms.get(0).get(0));
		assertEquals("fmt/19", pronoms.get(0).get(1));
		assertEquals("fmt/18", pronoms.get(1).get(0));
	
	}

	public static void testExtractSiegfriedDataFromFile() throws IOException
	{
    	String path = "D:\\\\Bibliotheken\\\\Studium\\\\Bachelor"
    			    + "arbeit\\\\sfdata\\\\unitTests\\\\1.ISO";
    	try
    	{
    	    ArrayList<SiegfriedFile> pronoms
    	    = ExtractSiegfriedData.extractSiegfriedDataFromFile(path);
    	    assertTrue(pronoms.size() == 3);
    	    assertEquals("fmt/18", pronoms.get(0).get(0));
    	    assertEquals("fmt/18", pronoms.get(1).get(0));
    	    assertEquals("fmt/18", pronoms.get(2).get(0));
    	}
    	catch(java.nio.file.NoSuchFileException e)
    	{
    		/* Not running on my machine. */
    	}
	}
}
