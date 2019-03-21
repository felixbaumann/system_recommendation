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
	private HashMap<String, Integer> frequency
	    = new HashMap<String, Integer>();

	/* Fraction of the number of matches of a pronom
	 * compared to all the matches of any pronom. */
	private HashMap<String, Double> relativeFrequency
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
    	for (Disk image : diskImages)
    	{
    		for (SiegfriedFile file : image.files)
    		{  			
    			for (PronomMatch match : file.matches())
    			{
    				if (match.pronom() != "UNKNOWN")
    				{
    			        numberOfMatches ++;
        				incrementPronomFrequency(match.pronom());
    				}
    			}
    		}
    	}

    	/* Calculate relative pronom frequencies. */
    	for (String pronom : frequency.keySet())
    	{
    		relativeFrequency.put(pronom,
                ((double) frequency.get(pronom)) / numberOfMatches);
    	}
    }
    
    /* This method increments the general frequency of a given pronom.
     * If there's no frequency for this pronom yet, 
     * it will create an entry in the frequency map. 
     */
    private void incrementPronomFrequency(String pronom)
    {
    	Integer freq = frequency.get(pronom);
		if (freq == null)
		{
			freq = 1;
		}
		else 
		{
			freq += 1;
		}
		frequency.put(pronom, freq);
    }
    
    public int getNumberOfMatches()
    {
    	return numberOfMatches;
    }
    
    /* This function returns the relative frequency of a fiven pronom
     * in the analyzed dataset.
     * If 40% of all pronoms in the dataset are "fmt/19",
     * the function will return 0.4 for this pronom.
     * The function performs a lookup in the corresponding
     * hashmap and avoids exceptions. */
    public double getRelativeFrequency(String pronom)
    {
    	Double freq = relativeFrequency.get(pronom);
    	
    	/* The pronom occured at least once in the dataset. */
    	if (freq != null) { return freq; }
    	
    	/* The pronom didn't occur, but others occured. */
    	if (numberOfMatches != 0) { return 1 / numberOfMatches; }
    	
    	/* There was not a single pronom in the dataset.
    	 * Return 1 to avoid any pronom rarity bonus, since we don't know
    	 * anything about pronom rarity. */
    	return 0;
    }
    
    public int getRelativeFrequencyMapSize()
    {
    	return relativeFrequency.size();
    }
}
