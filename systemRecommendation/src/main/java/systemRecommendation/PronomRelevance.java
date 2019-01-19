/* PronomRelevance.java
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
import java.util.HashMap;
import java.util.Iterator;

/* This class deals with the calculation of the relevance of pronoms of
 * a given disk.
 * The higher the relevance value, the more important the pronom will
 * be when recommending a system for the disk.
 * 
 * Requires: the pronoms of the files on the disk, their size and
 * empirical values of the pronoms.
 * 
 */
public class PronomRelevance
{
	/* This function computes the relevances of the pronoms occuring in a list
	 * of files. Those relevances are partly based on pronom statistics
	 * that have to be given as well.
	 * 
	 * FILES     A list of SiegfriedFiles representing a whole disk of files.
	 *           The relevance of the pronoms of these files will be
	 *           evaluated.
	 * 
	 * PRONOMSTATS Statistics on the relative frequency of pronoms based on
	 *             a (hopefully) large dataset. Do NOT only use the files
	 *             above.
	 * 
	 * RETURN    A HashMap mapping from pronoms occuring in the files
	 *           to a relevance value for each of them.
	 */
	public static HashMap<String, Double> pronomRelevances(
	    ArrayList<SiegfriedFile> files, PronomStatistics pronomStats)
	{
		HashMap<String, Double> relevances = initializeRelevanceMap(files);
		
		HashMap<String, Double> rootedPronomFrequencies
		    = cubicRootOfPronomFrequencies(pronomStats);
		
		/* For each SiegfriedFile... */
		for (int i = 0; i < files.size(); i++)
		{
			SiegfriedFile file = files.get(i);

			/* ... calculate a file dependent relevance factor... */
			double fileRelevance 
			    = Math.sqrt(file.fileSize()) / file.matchCount();
			
			/* ... and for each match... */
			for (int match = 0; match < file.matchCount(); match++)
			{
				/* ... increment the relevance of this pronom. */
				String pronom = file.matches().get(match);

				double newRelevance = relevances.get(pronom) 
				    + fileRelevance / getRootedPronomFrequency(pronom,
				        rootedPronomFrequencies,
				        pronomStats.getNumberOfMatches());

				relevances.put(pronom, newRelevance);
			}
		}
		return relevances;
	}

	
	/* This function implements an alternative relevance metric
	 * than pronomRelevances().
	 * 
	 * FILES     A list of SiegfriedFiles representing a whole disk of files.
	 *           The relevance of the pronoms of these files will be
	 *           evaluated.
	 * 
	 * PRONOMSTATS Statistics on the relative frequency of pronoms based on
	 *             a (hopefully) large dataset. Do NOT only use the files
	 *             above.
	 *
	 * DUPLICATEPARAMETER	Defines the influence of duplicates on
	 *						the relevance. 
	 * 
	 * SIZEPARAMETER		Defines the influence of filesizes on
	 * 						the relevance.
	 * 
	 * FREQUENCYPARAMETER	Defines the influence of statistical pronom
	 * 						frequencies on the relevance.
	 * 
	 *  Use positive values for all parameters. The use of 0 is valid and will
	 *  result in no influence of the parameter.
	 * 
	 * RETURN    A HashMap mapping from pronoms occuring in the files
	 *           to a relevance value for each of them.
	 */
	public static HashMap<String, Double> relativePronomRelevances(
	    ArrayList<SiegfriedFile> files, PronomStatistics pronomStats,
	    double duplicateParameter, double sizeParameter,
	    double frequencyParameter)
	{
		int fileNumber = files.size();
		int diskSize = 0;

		HashMap<String, Double> relevances = initializeRelevanceMap(files);
		
		/* Counts how often each format occurs in 'files'. */
		HashMap<String, Integer> formatInstances
		    = initializeFormatInstancesMap(files);
		
		/* Counts the size of all instances of a format in 'files'. */
		HashMap<String, Integer> formatSizes
		    = initializeFormatInstancesMap(files);
		
		/* Compute the number of instances of all pronoms
		 * and the sizes of their files. */
		for (int i = 0; i < fileNumber; i++)
		{
			SiegfriedFile file = files.get(i);
			diskSize += file.fileSize();
			
			for (int match = 0; match < file.matchCount(); match++)
			{
				String pronom = file.matches().get(match);
				formatInstances.put(pronom,
					formatInstances.get(pronom) + 1 / file.matchCount());
				formatSizes.put(pronom,
					formatSizes.get(pronom)
					    + file.fileSize() / file.matchCount());
			}
		}
		
		/* Compute the sum of the relative frequencies of all occuring pronoms
		 * on the disk. */
		Iterator<String> iter = relevances.keySet().iterator();
		double sumOfRelativeFrequencies = 0.0;
		while (iter.hasNext())
		{
			sumOfRelativeFrequencies
			    += getPronomFrequency(iter.next(), pronomStats);
		}
		
		/* Combine the relevance factors. */
		Iterator<String> iter2 = relevances.keySet().iterator();
		while (iter2.hasNext())
		{
			String pronom = iter2.next();
			double relevance = 0;
			relevance += duplicateParameter
				* ((double) formatInstances.get(pronom)) / fileNumber;
			relevance += sizeParameter * formatSizes.get(pronom) / diskSize;

			if (sumOfRelativeFrequencies > 0)
			{
				relevance += frequencyParameter
					* (1 - getPronomFrequency(pronom, pronomStats)
						/ sumOfRelativeFrequencies);
			}
			relevances.put(pronom, relevance);	
		}
		return relevances;
	}
	
	/* This function performs a lookup in the rootedPronomFrequency map
	 * for a given pronom. The map was created with data from the pronom
	 * statistics dataset where not every possible pronom appeared.
	 * Therefore it is not uncommon that there is no value for a pronom
	 * on a disk.
	 * In this case we will treat this pronom as if it appeared just once.
	 * 
	 */
	private static double getRootedPronomFrequency(
		String pronom,
	    HashMap<String, Double> rootedPronomFrequencies,
	    int totalNumberOfMatches)
	{
		/* If the pronom occured in the statistics dataset,
		 * there is a rooted frequency which can be returned. */
		if (rootedPronomFrequencies.get(pronom) != null)
		{
			return rootedPronomFrequencies.get(pronom);
		}
		/* If the pronom has never occured in the dataset, it's rather exotic
		 * and will be treated as if it appeared just once. */
		return Math.cbrt(1 / totalNumberOfMatches);
	}
	
	/* This function performs a lookup in the pronom statistics.
	 * for a given pronom. The map was created with data from the pronom
	 * statistics dataset where not every possible pronom appeared.
	 * Therefore it is not uncommon that there is no value for a pronom
	 * on a disk.
	 * In this case we will treat this pronom as if it appeared just once.
	 */
	private static double getPronomFrequency(
		String pronom,
	    PronomStatistics pronomStats)
	{
		/* If the pronom occured in the statistics dataset,
		 * there is a frequency which can be returned. */
		if (pronomStats.generalRelativeFrequency.get(pronom) != null)
		{
			return pronomStats.generalRelativeFrequency.get(pronom);
		}
		/* If the pronom has never occured in the dataset, it's rather exotic
		 * and will be treated as if it appeared just once.
		 * If the dataset was entirely empty, no frequencies are known
		 * and all pronoms will be treated equally. 
		 */
		int trainingMatches = pronomStats.getNumberOfMatches();
		return (trainingMatches > 0 ? 1 / trainingMatches : 0);
	}
	
	/* This function computes the cubic roots
	 * of all general relative pronom frequencies. 
	 */
	private static HashMap<String, Double> cubicRootOfPronomFrequencies(
	    PronomStatistics stats)
	{
		HashMap<String, Double> result = new HashMap<String, Double>();
		
		Iterator<String> iter
		    = stats.generalRelativeFrequency.keySet().iterator();

		/* For each pronom key in stats compute the cubic root of its value
		 * and add this pair to the resulting map.
		 */
		while (iter.hasNext())
		{
			String pronom = iter.next();
			result.put(pronom,
			    Math.cbrt(stats.generalRelativeFrequency.get(pronom)));
		}
		
		return result;
	}
	

	/* This function creates a Map containing a key for every pronom
	 * occuring in the Siegfried files.
	 * Default value is 0.
	 */
	private static HashMap<String, Double> initializeRelevanceMap(
	    ArrayList<SiegfriedFile> files)
	{
		HashMap<String, Double> emptyRelevances
		    = new HashMap<String, Double>();
		
		for (int i = 0; i < files.size(); i++)
		{
			for (int k = 0; k < files.get(i).matchCount(); k++)
			{
				emptyRelevances.put(files.get(i).matches().get(k), 0.0);
			}
		}

		return emptyRelevances;
	}
	
	/* This function creates a Map containing a key for every pronom
	 * occuring in the Siegfried files.
	 * Default value is 0.
	 */
	private static HashMap<String, Integer> initializeFormatInstancesMap(
	    ArrayList<SiegfriedFile> files)
	{
		HashMap<String, Integer> emptyRelevances
		    = new HashMap<String, Integer>();
		
		for (int i = 0; i < files.size(); i++)
		{
			for (int k = 0; k < files.get(i).matchCount(); k++)
			{
				emptyRelevances.put(files.get(i).matches().get(k), 0);
			}
		}
		return emptyRelevances;
	}
}
