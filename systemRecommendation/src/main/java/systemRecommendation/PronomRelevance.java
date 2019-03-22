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

import java.util.Collections;
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
	 * DEPTHPAR	        Defines the influence the depth of a path has
	 * 					on the relevance of a file. 
	 * 					(Large value is a small influence.)
	 * 
	 * PRONOMFREQPAR	Defines the influence of statistical pronom
	 * 					frequencies on the relevance.
	 * 					(Large value is a large influence.)
	 * 
	 * SYSTEMFREQPAR	Defines the influence of statistical system
	 * 					frequencies on the relevance.
	 * 					(Large value is a large influence.)
	 * */
	public static HashMap<String, Double> pronomRelevances(
		    Disk disk, PronomStatistics pronomStats,
		    SystemStatistics systemStats,
		    double depthPar, double pronomFreqPar,
		    double systemFreqPar)
	{
		/* Start with a relevance value of 100% for the entire disk
		 * and share this relevance between all matches of all files. */
		fileRelevance(disk.disk, 1.0, depthPar);

		HashMap<String, Double> relevances = sumMatchRelevances(disk);
		
		/* Project relevances to [0, 1]. */
		normalizeRelevances(relevances);
		
		/* Add a relevance value for the rarity of the pronoms. */
		addPronomRarity(relevances, pronomStats, pronomFreqPar);
		
		/* Add a relevance value for the rarity of systems being able to read
		 * the pronom. */
		addSystemRarity(relevances, systemStats, systemFreqPar);		
		
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
		PronomStatistics pronomStats, double pronomFreqPar)
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
			Double rarityBonus = pronomFreqPar
				* (1 - pronomStats.getRelativeFrequency(pronom));

			/* If there is any information about the pronom rarity,
			 * add a relevance bonus.
			 * The rarer the pronom, the larger the bonus. */
			relevances.put(pronom,
				oldRelevance + rarityBonus);
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
		SystemStatistics systemStats, double systemFreqPar)
	{
		/* Iterate over all pronoms and calculate their rarity bonus. */
		Iterator<String> iter = relevances.keySet().iterator();
		while (iter.hasNext())
		{
			String pronom = iter.next();
			
			/* The relevance value without any system rarity bonus. */
			Double oldRelevance = relevances.get(pronom);
			
			/* The number of systems able to read the pronom. */
			Integer systems = systemStats.readingSystems.get(pronom);
			
			if (systems == null) { continue; }
			if (systems <= 0) { continue; }
			
			/* If there is any information about the system rarity,
			 * add a relevance bonus.
			 * The rarer the systems, the larger the bonus. */
			relevances.put(pronom,
				oldRelevance + (1.0 / (double) systems) * systemFreqPar);
		}
	}
	
	/* This function accepts a hashmap of relevance values for pronoms
	 * and normalizes these relevances by projecting them on [0, 1]. */
	private static void normalizeRelevances(
		HashMap<String, Double> relevances)
	{
		if (relevances.size() == 0) { return; }

		/* 1. Find the largest relevance value. */
		Double max = Collections.max(relevances.values());
		
		if (max <= 0 || max == null) { return; }
		
		/* 2. Perform the projections. */
		Iterator<String> iter = relevances.keySet().iterator();
		while (iter.hasNext())
		{
			String pronom = iter.next();
			Double oldRelevance = relevances.get(pronom);
			relevances.put(pronom, oldRelevance == null ? null : oldRelevance / max);
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
	
	/*
	 * RELEVANCE  The relevance value that shall be shared between the files and the subfolders.
	 */
	private static void fileRelevance(Folder folder, double relevance,
		double depthPenalty)
	{
		double folderSize = getFolderSize(folder, depthPenalty);
		
		/* Each file gets a relevance according to its share
		 * of the folder size. */
		for (SiegfriedFile file : folder.files())
		{
			file.setRelevance(relevance * (file.fileSize() / folderSize));
		}
		
		/* Each subfolder gets a relevance according to its share
		 * of the folder size recursively. */
		for (Folder subfolder : folder.folders().values())
		{
			fileRelevance(subfolder, relevance * (subfolder.size(depthPenalty)
				/ folderSize), depthPenalty);
		}
	}
	
	/* This function returns the size of the given folder as the sum of all
	 * its subfolders and files. */
	private static double getFolderSize(Folder folder, double depthPenalty)
	{
		double size = 0;
		for (Folder subfolder : folder.folders().values())
		{
			size += subfolder.size(depthPenalty);
		}
		for (SiegfriedFile file : folder.files())
		{
			size += file.fileSize();
		}
		return size;
		
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
