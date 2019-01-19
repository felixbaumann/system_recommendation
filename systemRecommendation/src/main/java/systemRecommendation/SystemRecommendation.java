/* SystemRecommendation.java
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
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

/* This is the main class dealing with the recommendation of a system
 * for a given disk.
 * 
 * Initially, a set of representations of systems has to be given,
 * along with either already statistics for pronoms, or a set of
 * Siegfried outputs so the statistics can be calculated now.
 * 
 * After this setup, the given Siegfried output of a single disk
 * is interpreted, the pronoms extracted, their relevance calculated
 * and a system chosen, that can read/write as many of the files on the disk
 * as possible, weighted by their relevance.
 * 
 */
public class SystemRecommendation
{	
	/* The recommended system number will be stored here making it accessible
	 * for the test class. */
	public static int chosenSystem;
	
	/* This is the main class for system recommendation.
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
	 * OPTIONAL:
	 * ARGS[3]			Parameter for the influence of duplicates on
	 * 					the relevance formular. Default is 0.33333.
	 * 
	 * ARGS[4]			Parameter for the influence of filesizes on
	 * 					the relevance formular. Default is 0.33333.
	 * 
	 * ARGS[5]			Parameter for the influence of statistical pronom
	 *					frequencies on the relevance formular.
	 *					Default is 0.33333.
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
		double duplicateParameter = 0.33333;
		double sizeParameter = 0.33333;
		double frequencyParameter = 0.33333;

		/* DEBUG Print arguments. */
		// System.out.println("\n" + systemsFile);
		// System.out.println(siegfriedOutputs);
		// System.out.println(siegfriedOutputForDisk);
		
		/* If values for those parameters are given,
		 * obviously use the given ones instead. */
		if (args.length == 6)
		{
			duplicateParameter = Double.parseDouble(args[3]);
			sizeParameter = Double.parseDouble(args[4]);
			frequencyParameter = Double.parseDouble(args[5]);
			
			/* DEBUG Print arguments. */
			// System.out.println("\nDuplicate parameter:  " + args[3]);
			// System.out.println("Size parameter:       " + args[4]);
			// System.out.println("Frequency parameter:  " + args[5] + "\n");
		}
		
		SystemStatistics systemStats = createSystemStats(systemsFile);
		PronomStatistics pronomStats = createPronomStats(siegfriedOutputs);

		/* Get the content on the disk. */
		ArrayList<SiegfriedFile> disk
		    = ExtractSiegfriedData.extractSiegfriedDataFromFile(
		    	siegfriedOutputForDisk);

		/* Calculate the relevance of each pronom occuring on the disk. */
		HashMap<String, Double> relevances
		    = PronomRelevance.relativePronomRelevances(
		    	disk, pronomStats, duplicateParameter, sizeParameter,
		    	frequencyParameter);
		
		/* Recommend a system maximizing the relevance.
		 * The third argument is the number of the default system. */
		chosenSystem = SystemChoice.chooseSystem(systemStats.systems,
			relevances, 0);
		
		/* Inform about the id of the recommended system.
		 * Note that the ids start with 0, therefore id+1 is the line in
		 * the systems file representing the chosen system. */
		System.out.println(chosenSystem);
	}
	
	/* This function creates system statistics from a given file containing
	 * system representations.
	 */
	private static SystemStatistics createSystemStats(String path)
		throws IOException
	{
		SystemStatistics systemStats;
		try
		{
		    systemStats = new SystemStatistics(Paths.get(path));
		}
		catch(IOException e)
		{
			throw new IOException("The path to the file with systems is "
				+ "invalid or the file not accessible.");
		}
		return systemStats;
	}
	
	/* This function creates pronom statistics from a given directory of files
	 * with Siegfried output.
	 */
	private static PronomStatistics createPronomStats(String path)
		throws IOException
	{
		PronomStatistics pronomStats;
		try
		{
		    pronomStats = new PronomStatistics(path);
		}
		catch(IOException e)
		{
			throw new IOException("Directory of the Siegfried outputs is "
				+ "flawed or contains unreadable files.");
		}
		return pronomStats;
	}
}
