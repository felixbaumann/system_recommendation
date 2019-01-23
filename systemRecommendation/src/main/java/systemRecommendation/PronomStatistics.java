/* PronomStatistics.java
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* With a set of pronoms of a set of Siegfried outputs and some information
 * about the rarity of systems able to read the formats associated with
 * these pronoms, this class will calculate some general statistics for
 * pronoms. Those may be used to calculate the relevance of a pronom
 * when a single disk is given.
 * 
 */
public class PronomStatistics
{
	/* Contains the total number of matches of all files in all images. */
	private int numberOfMatches = 0;

	/* Total occurences of a given pronom. */
	private HashMap<String, Integer> generalFrequency
	    = new HashMap<String, Integer>();

	/* Fraction of the number of matches of a pronom
	 * compared to all the matches of any pronom. */
	public HashMap<String, Double> generalRelativeFrequency
	    = new HashMap<String, Double>();

	public PronomStatistics(String directory) throws IOException
	{
		analyzeDirectory(directory);
	}

	/* This method calculates pronom statistics for a given directory. */
    private void analyzeDirectory(String directory) throws IOException
    {
    	ArrayList<Disk> diskImages
    		= ExtractSiegfriedData.extractPronoms(directory);

    	/* Count pronom frequencies. */  	
    	for (int image = 0; image < diskImages.size(); image++)
    	{
    		for (int file = 0; file < diskImages.get(image).files.length;
    			file++)
    		{  			
    			for (int match = 0;
    				match < diskImages.get(image).files[file].matchCount();
    				match++)
    			{
    				String pronom
    					= diskImages.get(image).files[file].get(match);
    				if (pronom != "UNKNOWN")
    				{
    			        numberOfMatches ++;
        				incrementPronomFrequency(pronom);
    				}
    			}
    		}
    	}

    	/* Calculate relative pronom frequencies. */
    	Iterator<String> it = generalFrequency.keySet().iterator();
    	while (it.hasNext())
    	{
    		String pronom = it.next();
    		generalRelativeFrequency.put(pronom,
                ((double) generalFrequency.get(pronom)) / numberOfMatches);
    	}
    }
    
    /* This method increments the general frequency of a given pronom.
     * If there's no frequency for this pronom yet, 
     * it will create an entry in the frequency map. 
     */
    private void incrementPronomFrequency(String pronom)
    {
    	Integer freq = generalFrequency.get(pronom);
		if (freq == null)
		{
			freq = 1;
		}
		else 
		{
			freq += 1;
		}
		generalFrequency.put(pronom, freq);
    }
    
    public int getNumberOfMatches()
    {
    	return numberOfMatches;
    }
}
