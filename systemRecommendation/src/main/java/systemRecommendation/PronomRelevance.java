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

	/* This function implements the current relevance metric.
	 * 
	 * DISK		The disk for which systems shall be recommended. 
	 * 
	 * PRONOMSTATS Statistics on the relative frequency of pronoms based on
	 *             a (hopefully) large dataset. Do NOT only use the files
	 *             above.
	 *             
	 * SYSTEMSTATS Statistics on the rarity of the ability to read certain
	 *             pronoms, based on the available systems.
	 *             
	 * DEPTHPAR	   Defines the influence the depth of a path has
	 * 			   on the relevance of a file. 
	 * 			   (Large value is a small influence.)
	 */
	public static HashMap<String, Double> pronomRelevances(
		    Disk disk, PronomStatistics pronomStats,
		    SystemStatistics systemStats,
		    double depthPar)
	{
		/* Start with a relevance value of 100% for the entire disk
		 * and share this relevance between all matches of all files. */
		fileRelevance(disk, 1.0, depthPar);

		HashMap<String, Double> relevances = sumMatchRelevances(disk);
		
		/* Project relevances to [0, 1]. */
		normalizeRelevances(relevances);
		
		/* Add a relevance value for the rarity of the pronoms. */
		addPronomRarity(relevances, pronomStats);
		
		/* Add a relevance value for the rarity of systems being able to read
		 * the pronom. */
		addSystemRarity(relevances, systemStats);		
		
		/* Normalize again. Yes, twice is necessary. */
		normalizeRelevances(relevances);

		return relevances;
	}

	/* This function modifies the given pronom relevances by adding a bonus
	 * for pronom rarity.
	 * If the pronom appears only a few times in a large dataset, it's
	 * rather exotic and therefore the bonus is rather large,
	 * otherwise comparably low.
	 * There is no bonus, if the whole dataset consists only of a single
	 * pronom.
	 * The largest bonus is given to pronoms that appear only once in
	 * the dataset. */
	private static void addPronomRarity(HashMap<String, Double> relevances,
		PronomStatistics pronomStats)
	{
		/* Iterate over all pronoms and calculate their rarity bonus. */
		Iterator<String> iter = relevances.keySet().iterator();
		while (iter.hasNext())
		{
			String pronom = iter.next();
			
			/* The relevance value without any pronom rarity bonus. */
			Double oldRelevance = relevances.get(pronom);
			
			/* Since relevance drops with a higher frequency, we use
			 * (1 - frequency). */
			Double rarityBonus = 1 - pronomStats.getRelativeFrequency(pronom);
			
			/* If the relative frequency is 1 since all files have the same format,
			 * dont' add a rarity bonus, since it would yield a product of 0.*/
			if (rarityBonus < 0.00000000000001) { continue; }
			/* If there is any information about the pronom rarity,
			 * add a relevance bonus.
			 * The rarer the pronom, the larger the bonus. */
			relevances.put(pronom,
				oldRelevance * rarityBonus);
		}
	}
	
	
	/* This function modifies the given pronom relevances by adding a bonus
	 * for system rarity.
	 * If only few systems can read the pronom, the bonus is rather large,
	 * otherwise comparably low.
	 * There is no bonus for pronoms that can't be
	 * read by any system at all. Those won't matter anyway.
	 * The largest bonus is given to pronoms that can only be read by one
	 * single system. */
	private static void addSystemRarity(HashMap<String, Double> relevances,
		SystemStatistics systemStats)
	{
		/* If there aren't any systems at all, don't ruin the relevance
		 * computations. */
		if (systemStats.systems.size() == 0) { return; }
		
		/* Iterate over all pronoms and calculate their rarity bonus. */
		Iterator<String> iter = relevances.keySet().iterator();
		while (iter.hasNext())
		{
			String pronom = iter.next();
			
			/* The relevance value without any system rarity bonus. */
			Double oldRelevance = relevances.get(pronom);
			
			/* The number of systems able to read the pronom. */
			Integer readSystems = systemStats.readingSystems.get(pronom);
			
			if (readSystems == null) { continue; }
			if (readSystems <= 0) { continue; }
			
			/* If there is any information about the system rarity,
			 * add a relevance bonus.
			 * The rarer the systems, the larger the bonus. */
			relevances.put(pronom,
				oldRelevance * (readSystems / (double) systemStats.systems.size()));
		}
	}
	
	/* This function accepts a hashmap of relevance values for pronoms
	 * and normalizes these relevances by projecting to values
	 * between 0 and 1. */
	private static void normalizeRelevances(
		HashMap<String, Double> relevances)
	{
		if (relevances.size() == 0) { return; }

		/* 1. Sum up the relevance values. */
		Double sum = 0.0;
		
		for (String pronom : relevances.keySet())
		{
			if (pronom == null) { continue; }
			if (pronom.equals("UNKNOWN")) { continue; }

			Double value = relevances.get(pronom);

			sum += (value == null ? 0.0 : value);
		}

		if (sum < 0.0000000001) { return; }
		
		/* 2. Perform the projections. */
		Iterator<String> iter = relevances.keySet().iterator();
		while (iter.hasNext())
		{
			String pronom = iter.next();
			if (pronom.equals("UNKNOWN"))
			{
				relevances.put(pronom, 0.0);
				continue;
			}
			Double oldRelevance = relevances.get(pronom);
			relevances.put(pronom, oldRelevance == null ? null : oldRelevance / sum);
		}
	}
	
	/* This function sums up the relevances of all matches
	 * of the same pronom across the disk.
	 * 
	 * RETURNS	A hashmap containing the relevances for each pronom.
	 */
	private static HashMap<String, Double> sumMatchRelevances(Disk disk)
	{
		HashMap<String, Double> relevances = initializeRelevanceMap(disk);
		
		/* For all matches of all files:
		 * Sum up the relevances for each pronom. */
		for (SiegfriedFile file : disk.files)
		{
			for (PronomMatch match : file.matches())
			{
				String pronom = match.pronom();
				relevances.put(pronom,
					relevances.get(pronom) + match.getRelevance());
			}
		}
		return relevances;
	}
	
	/* This function calculates the relevance values of each file according
	 * to its size and penalized by its depth in the file system.
	 * 
	 * DISK		  The disk whose files' relevances shall be calculated.
	 * 
	 * RELEVANCE  The relevance value that shall be shared between the files.
	 * 
	 * DEPTHPENALTY Choose a value between 0 and 1.
	 *              1 means no effect of the depth.
	 *              0 means files in subfolders have no value at all.
	 *              Everything in between penalizes files in sub-subfolders
	 *              more than files in regular subfolders.
	 */
	private static void fileRelevance(Disk disk, double relevance,
		double depthPenalty)
	{
		/* Calculate penalized size of each file according to its depth
		 * in the file system. */
		double sumOfPenalizedSizes = 0;
		for (SiegfriedFile file : disk.files)
		{
			/* First, reduce the influence of file sizes by rooting them. */
			int size = (int) Math.sqrt(file.fileSize());

			/* For each subfolder, penalize the size even more. */
			for (int i = 0; i < file.getDirectory().length; i++)
			{
				size *= depthPenalty;
			}
			file.setPenalizedSize(size);
			sumOfPenalizedSizes += size;
		}

		/* Calculate the relevance of each file according to its size
		 * penalized by the depth in the file system. */
		for (SiegfriedFile file : disk.files)
		{
			double sizeShare
			    = ((double) file.getPenalizedSize()) / sumOfPenalizedSizes;

			double rel = sizeShare / relevance;

			file.setRelevance(rel);
		}
	}
	
	/* This function creates a Map containing a key for every pronom
	 * occuring in the Siegfried files.
	 * Default value is 0.
	 */
	private static HashMap<String, Double> initializeRelevanceMap(
	   Disk disk)
	{
		HashMap<String, Double> emptyRelevances
		    = new HashMap<String, Double>();

		for (SiegfriedFile file : disk.files)
		{
			for (PronomMatch match : file.matches())
			{
				emptyRelevances.put(match.pronom(), 0.0);
			}
		}
		return emptyRelevances;
	}
}
