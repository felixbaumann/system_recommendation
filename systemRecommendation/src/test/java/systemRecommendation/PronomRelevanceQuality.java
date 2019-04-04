/* PronomRelevanceQuality.java
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
import java.util.HashMap;

/* This class contains a collection of functions used to compare
 * the relevance estimations with some of the relevance indicators.
 */
public class PronomRelevanceQuality {

	/* This is function performs the SystemRecommendation steps without
	 * the final system choice.
	 * Instead, it yields the relevances of the occuring pronoms.
	 * 
	 * ARGS[0]			Path to a file with systems represented by
	 * 					the wikidata QIDs of the installed programs.
	 * 
	 * ARGS[1]			Directory of Siegfried outputs used to calculate
	 * 					empirical values on the pronoms.
	 * 
	 * ARGS[2]			Path to the file with the Siegfried output of the
	 * 					given disk for which a system shall be recommended.
	 *
	 * RETURN			Prints the number of the recommended system to
	 * 					standard output. The number corresponds to the line
	 * 					of the systems file. Counting starts with 0.
	 */
	public static void main(String[] args) throws IOException
	{
		/* Path to the file containing the available systems. */
		String systemsFile = args[0];

		/* Directory with files containing Siegfried output
		 * for statistical purposes. */
		String siegfriedOutputs = args[1];

		/* Path to the file with the Siegfried output for the given disk
		 * for which a system shall be recommended. */
		String siegfriedOutputForDisk = args[2];
		
		/* Default values for the parameters weighting
		 * the relevance influencers. */
		double depthParam = 0.7;

		SystemStatistics systemStats = SystemRecommendation.createSystemStats(systemsFile);
		PronomStatistics pronomStats = SystemRecommendation.createPronomStats(siegfriedOutputs);

		/* Get the content on the disk. */
		Disk disk
		    = ExtractSiegfriedData.extractSiegfriedDataFromFile(
		    	siegfriedOutputForDisk, "");

		/* Calculate the relevance of each pronom occuring on the disk. */
		HashMap<String, Double> relevances
		    = PronomRelevance.pronomRelevances(
		    	disk, pronomStats, systemStats, depthParam);
		
		printPronomRelevances(relevances, disk, pronomStats);
		// printRarestPronom(disk, pronomStats);
		/*
		String result = "";
		result += printMostRelevantPronom(relevances);
		result += "\t" + printMostOccuredPronom(disk);
		result += "\t\t" + printLargestPronom(disk);
		result += "\t" + printMostProminent(disk);
		result += "\t" + printRarestPronom(disk, pronomStats);
		//result += "\t\t" + printRarestSystem(disk, systemStats);
		System.out.println(result);
		*/
	}

	private static String printRarestSystem(Disk disk, SystemStatistics stats)
	{
		HashMap<String, Integer> generalFrequency = initializeIntegerMap(disk);
        for (String pronom : generalFrequency.keySet())
        {
        	generalFrequency.put(pronom, stats.numberOfReadingSystems(pronom));
        }
        // System.out.println("Rarest readable pronoms here is: " + getMinInteger(generalFrequency));
        return getMinInteger(generalFrequency);
	}
	
	private static String printRarestPronom(Disk disk, PronomStatistics stats)
	{
		HashMap<String, Double> generalFrequency = initializeDoubleMap(disk);
		
        for (String pronom : generalFrequency.keySet())
        {
        	generalFrequency.put(pronom, stats.getRelativeFrequency(pronom));
        }

        //System.out.println("Rarest of the occured pronoms in general is: " + getMinDouble(generalFrequency));
        return getMinDouble(generalFrequency);
	}
	
	private static String printMostProminent(Disk disk)
	{
		HashMap<String, Integer> occurences = initializeIntegerMap(disk);
		for (SiegfriedFile file : disk.files)
		{
			for (PronomMatch match : file.matches())
			{
				occurences.put(match.pronom(), occurences.get(match.pronom()) + 1);
			}
		}
		
		/* Get the average depth of all files of the same pronom. */
		HashMap<String, Double> depthSum = initializeDoubleMap(disk);
		
		for (SiegfriedFile file : disk.files)
		{
			for (PronomMatch match : file.matches())
			{
				double depth = file.getDirectory().length;
				Double oldDepthSum = depthSum.get(match.pronom());
				depthSum.put(match.pronom(), oldDepthSum + depth / occurences.get(match.pronom()));
			}
		}
		//System.out.println("Most prominent position in average by: " + getMinDouble(depthSum));
		/*
		for (String pronom : depthSum.keySet())
		{
			System.out.println(pronom + "    " + depthSum.get(pronom));
		}
		*/
		return getMinDouble(depthSum);
	}
	
	private static String printLargestPronom(Disk disk)
	{
		/* Get the sums of the sizes of files with the same pronom. */
		HashMap<String, Integer> size = initializeIntegerMap(disk);

		for (SiegfriedFile file : disk.files)
		{
			for (PronomMatch match : file.matches())
			{
				Integer oldSize = size.get(match.pronom());
				size.put(match.pronom(), oldSize + (file.fileSize() * file.fileSize()));
			}
		}
        //System.out.println("Largest size taken by: " + getMaxInteger(size));
		return getMaxInteger(size);
	}
	
	/* This funtion figures out which pronom has the most occurences and prints it. */
	private static String printMostOccuredPronom(Disk disk)
	{
		HashMap<String, Integer> occurences = initializeIntegerMap(disk);
		for (SiegfriedFile file : disk.files)
		{
			for (PronomMatch match : file.matches())
			{
				occurences.put(match.pronom(), occurences.get(match.pronom()) + 1);
			}
		}
		// System.out.println("Most occured: " + getMaxInteger(occurences));
		return getMaxInteger(occurences);
	}
	
	private static HashMap<String, Integer> initializeIntegerMap(Disk disk)
	{
		HashMap<String, Integer> map
	    = new HashMap<String, Integer>();

		for (SiegfriedFile file : disk.files)
		{
			for (PronomMatch match : file.matches())
			{
				map.put(match.pronom(), 0);
			}
		}
		return map;
	}
	
	private static HashMap<String, Double> initializeDoubleMap(Disk disk)
	{
		HashMap<String, Double> map
	    = new HashMap<String, Double>();

		for (SiegfriedFile file : disk.files)
		{
			for (PronomMatch match : file.matches())
			{
				map.put(match.pronom(), 0.0);
			}
		}
		return map;
	}
	
	public static void printPronomRelevances(HashMap<String, Double> relevances, Disk disk, PronomStatistics stats)
	{
		/* Find the number of occurences for each pronom. */
		HashMap<String, Integer> occurences = initializeIntegerMap(disk);
		for (SiegfriedFile file : disk.files)
		{
			for (PronomMatch match : file.matches())
			{
				occurences.put(match.pronom(), occurences.get(match.pronom()) + 1);
			}
		}
		
		for (String pronom : relevances.keySet())
		{
			if (pronom.equals("UNKNOWN")) { continue; }

			// System.out.println(pronom + "\t\t" + occurences.get(pronom) + "\t\t" + relevances.get(pronom));
			System.out.println(pronom + "\t\t" + stats.getRelativeFrequency(pronom) + "\t\t" + relevances.get(pronom));
		}
	}
	
	/* This function figures out which pronom is the most relevant one and prints it. */
	private static String printMostRelevantPronom(HashMap<String, Double> relevances)
	{
		// System.out.println("Most relevant according to this method: " + getMaxDouble(relevances));
		return getMaxDouble(relevances);
	}

    private static String getMaxInteger(HashMap<String, Integer> map)
    {
		String maxArg = "";
		Integer max = 0;  
				
		for (String pronom : map.keySet())
		{
			
			if (map.get(pronom) > max && !pronom.equals("UNKNOWN"))
			{
				maxArg = pronom;
				max = map.get(pronom);
			}
		}
		return maxArg;
    }
    
    private static String getMaxDouble(HashMap<String, Double> map)
    {
		String maxArg = "";
		Double max = 0.0;  
				
		for (String pronom : map.keySet())
		{
			
			if (map.get(pronom) > max && !pronom.equals("UNKNOWN"))
			{
				maxArg = pronom;
				max = map.get(pronom);
			}
		}
		return maxArg;
    }
    
    private static String getMinDouble(HashMap<String, Double> map)
    {
		String minArg = "";
		Double min = Double.MAX_VALUE;  
				
		for (String pronom : map.keySet())
		{
			
			if (map.get(pronom) < min && !pronom.equals("UNKNOWN"))
			{
				minArg = pronom;
				min = map.get(pronom);
			}
		}
		return minArg;
    }
    
    private static String getMinInteger(HashMap<String, Integer> map)
    {
		String minArg = "";
		Integer min = Integer.MAX_VALUE;  
				
		for (String pronom : map.keySet())
		{
			
			if (map.get(pronom) < min && !pronom.equals("UNKNOWN"))
			{
				minArg = pronom;
				min = map.get(pronom);
			}
		}
		return minArg;
    }
}
