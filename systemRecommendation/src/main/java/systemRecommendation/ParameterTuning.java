/* ParameterTuning.java
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

/* This class is used for tuning the parameters defining the influence of
 * the pronom relevance factors. */
public class ParameterTuning {
	
	private static String directory
	    = "D:\\\\Bibliotheken\\\\Studium\\\\Bachelorarbeit\\\\testData\\\\";

	public static void main(String args[]) throws IOException
	{
		/* If a different directory is given, use this one instead. */
		if (args.length == 1)
		{
			directory = args[0];
		}

		PronomStatistics pronomStats = SystemRecommendation.createPronomStats(
				directory + "siegfriedData\\\\");
		
		SystemStatistics systemStats = SystemRecommendation.createSystemStats(
			directory + "testSystems\\\\test02.txt");

		ArrayList<Disk> disks = defineDisks();
			
		// TODO add more parameter options
		double parameterOptions[][] = {{0.333, 0.333, 0.333}, 
				                       {0.4, 0.4, 0.2},
				                       {0.4, 0.2, 0.4},
				                       {0.2, 0.4, 0.4},
				                       {0.8, 0.1, 0.1},
				                       {0.1, 0.8, 0.1},
				                       {0.1, 0.1, 0.8},
				                       {0.41, 0.41, 0.18},
				                       {0.45, 0.45, 0.1},
				                       {0.5, 0.4, 0.1},
				                       {0.4, 0.5, 0.1},
				                       {0.5, 0.5, 0.0}};
		double bestOption[] = parameterTuning(pronomStats, systemStats, disks,
			parameterOptions);
		System.out.println("The best parameter combination of the given "
			+ "options is: " + bestOption[0] + ", " + bestOption[1] + ", "
			+ bestOption[2] + "\nThe achieved score is " + bestOption[3]
			+ "/" + bestOption[4]);
	}
	
	
	/* From a given list of options for parameter values, this function
	 * chooses the one that satisfies the most conditions defined by
	 * the example scenarios.
	 *
	 * OPTIONS	A nested list. Each list is an option for parameter values,
	 *          where the first value defines the duplicate parameter,
	 *          the second one the size parameter and the third one
	 *          the frequency parameter.
	 *
	 * RETURNS  The best of the offered combinations of parameter values.
	 */
	private static double[] parameterTuning(PronomStatistics pronomStats,
		SystemStatistics systemStats, ArrayList<Disk> disks,
		double[][] options) throws IOException
	{
		/* Index of the best option found yet in the options array. */
		int bestOption = 0;
		/* Score of the best option found yet. */
		int bestOptionsScore = 0;

		/* Try each parameter combination option. */
		for (int option = 0; option < options.length; option++)
		{
			int optionScore = calculateOptionScore(pronomStats, systemStats,
				disks, options[option]);
			
			/* Check whether the current parameter combination
			 * is the best one so far. */
			if (optionScore > bestOptionsScore)
			{
				bestOptionsScore = optionScore;
				bestOption = option;
			}
		}
		return new double[] {options[bestOption][0], options[bestOption][1],
			options[bestOption][2], (double) bestOptionsScore,
			(double) disks.size()};
	}
	
	/* This function calculates the score for a given parameter set.
	 *
	 * For given pronom statistics and a given list of disks along with
	 * the most important pronom on each disk, the parameter set will be
	 * evaluated.
	 * 
	 * That says, the allegedly most important pronom on each disk calculated
	 * using the given parameter set will be compared to the actual most
	 * important pronom stored in the disk.
	 * 
	 * For each time those two are the same, the parameter set (option)
	 * gets a credit, i.e. th score is incremented.
	 * Obviously, a large score is desirable. A score equal to disks.size()
	 * is a perfect one.
	 */
	private static int calculateOptionScore(PronomStatistics pronomStats,
		SystemStatistics systemStats, ArrayList<Disk> disks, double[] option)
	{
		int score = 0;
		/* Calculate the relevance of each disk with this parameter option. */
		for (Disk disk : disks)
		{
			HashMap<String, Double> relevances
			    = PronomRelevance.pronomRelevances(
			    	disk, pronomStats, systemStats, option[0],
			    	option[1], option[2]);
			/* If the pronom with the largest relevance is the correct one,
			 * increment the score of this option. */
			if(validRelevances(relevances, disk)) { score++; }
		}
		return score;
	}
	
	
	
	
	/* Assume the relevance of the pronoms on a disk are calculated and stored
	 * in a hashmap.
	 * Then the maximum value in this hashmap can be found and checked whether
	 * this pronom is infact the most relevant one, if this information has
	 * been noted in disk.mostRelevantPronom.
	 *
	 * RELEVANCES  A hashmap mapping from pronoms on a disk to their
	 *             relevances.
	 *
	 * DISK        The disk these pronoms are from. Make sure that
	 *             disk.mostRelevantPronom is instantiated.
	 *
	 * RETURNS     True if the most relevant pronom actually has the highest
	 *             value and false else.
	 */
	private static boolean validRelevances(HashMap<String, Double> relevances,
		Disk disk)
	{
		Double largestRelevance = -1.0;
		String mostRelevantPronom = "";

		/* Check for each pronom whether its relevance value is higher. */
		for(String key : relevances.keySet())
		{
			if (relevances.get(key) > largestRelevance)
			{
				/* A more relevant pronom has been found. */
				largestRelevance = relevances.get(key);
				mostRelevantPronom = key;
			}
		}
		return mostRelevantPronom == disk.getMostRelevantPronom();
	}
	
	/* This function creates some Siegfried files and some disks,
	 * i.e. collections of those. 
	 * For each disk, it's most important pronom is defined intuitevly.
	 * This can be used to test how well different parameter settings
	 * perform with respect to pronom relevance rating.
	 * */
	private static ArrayList<Disk> defineDisks()
	{
		/* fmt/17 		pdf 1.3
		 * fmt/18		pdf 1.4
		 * x-fmt/111	txt
		 * fmt/132		wma
		 * fmt/133		wmv
		 * fmt/134		mp3
		 * x-fmt/418	ico
		 * fmt/212		inf
		 * fmt/276		pdf 1.7
		 * fmt/899		exe
		 */
		SiegfriedFile file01 = new SiegfriedFile(1024, "", new PronomMatch[] {new PronomMatch("fmt/18")});
		SiegfriedFile file02 = new SiegfriedFile(2048, "", new PronomMatch[] {new PronomMatch("fmt/18")});
		SiegfriedFile file03 = new SiegfriedFile(1024, "", new PronomMatch[] {new PronomMatch("fmt/17")});
		SiegfriedFile file04 = new SiegfriedFile(2048, "", new PronomMatch[] {new PronomMatch("fmt/17")});
		SiegfriedFile file05 = new SiegfriedFile(1024, "", new PronomMatch[] {new PronomMatch("x-fmt/111")});
		SiegfriedFile file06 = new SiegfriedFile(10240, "", new PronomMatch[] {new PronomMatch("x-fmt/111")});
		SiegfriedFile file07 = new SiegfriedFile(4096, "", new PronomMatch[] {new PronomMatch("fmt/18")});
		SiegfriedFile file08 = new SiegfriedFile(12288, "", new PronomMatch[] {new PronomMatch("fmt/18")});
		SiegfriedFile file09 = new SiegfriedFile(15360, "", new PronomMatch[] {new PronomMatch("fmt/18")});
		SiegfriedFile file10 = new SiegfriedFile(10240, "", new PronomMatch[] {new PronomMatch("fmt/134")});
		SiegfriedFile file11 = new SiegfriedFile(8594231, "", new PronomMatch[] {new PronomMatch("fmt/20")});
		SiegfriedFile file12 = new SiegfriedFile(52, "", new PronomMatch[] {new PronomMatch("fmt/212")});
		
		SiegfriedFile file13 = new SiegfriedFile(25888, "", new PronomMatch[] {new PronomMatch("x-fmt/418")});
		SiegfriedFile file14 = new SiegfriedFile(593, "", new PronomMatch[] {new PronomMatch("x-fmt/111")});
		SiegfriedFile file15 = new SiegfriedFile(39, "", new PronomMatch[] {new PronomMatch("fmt/212")});
		SiegfriedFile file16 = new SiegfriedFile(441038, "", new PronomMatch[] {new PronomMatch("fmt/20")});
		SiegfriedFile file17 = new SiegfriedFile(15229994, "", new PronomMatch[] {new PronomMatch("fmt/276")});
		SiegfriedFile file18 = new SiegfriedFile(47, "", new PronomMatch[] {new PronomMatch("fmt/212")});
		SiegfriedFile file19 = new SiegfriedFile(768110306, "", new PronomMatch[] {new PronomMatch("fmt/899")});
		SiegfriedFile file20 = new SiegfriedFile(438054, "", new PronomMatch[] {new PronomMatch("x-fmt/418")});
		
		SiegfriedFile file21 = new SiegfriedFile(266480778, "", new PronomMatch[] {new PronomMatch("fmt/20")});
		SiegfriedFile file22 = new SiegfriedFile(326224597, "", new PronomMatch[] {new PronomMatch("fmt/899")});
		
		// TODO create more files


		ArrayList<Disk> disks = new ArrayList<Disk>();
		disks.add(new Disk("fmt/18", new SiegfriedFile[] {file01}, ""));
		disks.add(new Disk("fmt/18", new SiegfriedFile[] {file01, file02,
			file03}, ""));
		disks.add(new Disk("fmt/18", new SiegfriedFile[] {file02, file03}, ""));
		disks.add(new Disk("fmt/17", new SiegfriedFile[] {file02, file03,
			file03}, ""));
		disks.add(new Disk("fmt/17", new SiegfriedFile[] {file01, file04}, ""));
		disks.add(new Disk("fmt/17", new SiegfriedFile[] {file01, file04,
			file05}, ""));
		disks.add(new Disk("x-fmt/111", new SiegfriedFile[] {file01, file04,
			file06}, ""));
		disks.add(new Disk("fmt/17", new SiegfriedFile[] {file01, file04, 
			file04, file04, file07}, ""));
		disks.add(new Disk("fmt/17", new SiegfriedFile[] {file03, file03,
			file03, file03, file03, file03, file03, file03, file03, file03,
			file08}, ""));
		disks.add(new Disk("fmt/17", new SiegfriedFile[] {file03, file03,
			file03, file03, file03, file03, file03, file03, file03,
			file03, file09}, ""));
		disks.add(new Disk("fmt/134", new SiegfriedFile[] {file06, file10}, ""));
		disks.add(new Disk("fmt/20", new SiegfriedFile[] {file11, file12}, ""));
		disks.add(new Disk("fmt/276", new SiegfriedFile[] {file13, file14,
			file15, file16, file16, file16, file16, file17, file17, file17,
			file17, file17, file17, file17, file17, file17, file17, file17,
			file17, file17, file17, file17, file17, file17, file17, file17,
			file17, file17, file17, file17, file17, file17}, ""));
		disks.add(new Disk("fmt/899", new SiegfriedFile[] {file18, file19,
				file20}, ""));
		disks.add(new Disk("fmt/899", new SiegfriedFile[] {file21, file22}, ""));
		// TODO create more disks
		
		

		
		return disks;
	}
	
}
