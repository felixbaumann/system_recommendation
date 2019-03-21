/* SystemChoice.java
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


/* This class chooses a system environment according to the relevance of the
 * pronoms on the given disk.
 * 
 * It accepts the pronoms with their relevances along with the systems
 * and the pronoms they can deal with.
 * It will then choose the systems that maximize the sum of relevances
 * of pronoms it can deal with.
 * 
 * Create a SystemChoice object and access the choice by 'bestSystem()'
 * for a single choice or 'chooseSystems()' by a number of recommended
 * systems in order of decreasing suitability.
 */
public class SystemChoice
{
	/* Contains all systems with a 100% score. 
	 * Plus eventually some from 'candidateSystems'. */
	private ArrayList<SystemImage> chosenSystems
	    = new ArrayList<SystemImage>();

	/* Contains all systems with 0% < score < 100%. */
    private ArrayList<SystemImage> candidateSystems
        = new ArrayList<SystemImage>();

	/* Creating a SystemChoice object involves calculating the suitability
	 * scores of all systems and choosing the best ones.
	 * 
	 * All perfect systems will be chosen and placed at the beginning of
	 * the list. If those are less than 5%, the list will be filled
	 * with some imperfect systems in order of decreasing suitability.
	 * 
	 * Systems with no suitability will never be chosen.
	 * 
	 * SYSTEMS		The available system images.
	 * 
	 * PRONOMS		The relevances of all pronoms occuring on the disk.
	 */
	public SystemChoice(ArrayList<SystemImage> systems,
            HashMap<String, Double> pronoms)
	{
		/* No systems or pronoms cases. */
		if (systems == null) { return; }
		if (systems.size() == 0) {return; }

		if (pronoms == null) { return; }
		if (pronoms.size() == 0) {return; }
		
		for (SystemImage system : systems)
		{
			/* 1. Calculate the system's suitability score. */ 
			system.suitability = systemScore(system, pronoms);

			/* 2. Is the system perfect for this disk? */
			if (system.suitability > 0.999999)
			{
				chosenSystems.add(system);
				continue;
			}
			
			/* 3. Is the system at least somehow useful for this disk? */
			if (system.suitability > 0.000001)
			{
				candidateSystems.add(system);
			}
		}

		/* We desire the best 5% of the systems. */
		int desiredNumberOfSystems = (int) Math.ceil(systems.size() / 20.0);
		
		/* If less than 5% of the systems are perfect,
		 * we additionaly recommend some of the best imperfect ones. */
		while (chosenSystems.size() < desiredNumberOfSystems)
		{
			SystemImage candidate = chooseBestCandidate();
			if (candidate == null) { return; }
			chosenSystems.add(candidate);
			candidateSystems.remove(candidate);
		}
	}

	/* Getter for the list of chosen systems. 
	 * Returns [-1] if there is no useful system. */
	public int[] chooseSystems()
	{
		/* There are no chosen systems. Return -1. */
		if (chosenSystems.size() == 0)
		{
			int [] noSystem = {-1}; 
			return noSystem;
		}

		/* There's at least on chosen system. */
		int[] ids = new int[chosenSystems.size()];
		
		for (int i = 0; i < chosenSystems.size(); i++)
		{
			ids[i] = chosenSystems.get(i).id();
		}
		return ids;
	}

	/* Getter for the best system. 
	 * Returns -1 if there is no useful system. */
	public int bestSystem()
	{
		if (chosenSystems.size() == 0) { return -1; }

		return chosenSystems.get(0).id();
	}

	/* This method finds the system in 'candidateSystems' with the highest
	 * score. */
	private SystemImage chooseBestCandidate()
	{
		/* Find the system with highest score. */
		SystemImage bestSystem = null;
		double bestScoreSoFar = 0.0;

		for (SystemImage system : candidateSystems)
		{
			if (system.suitability > bestScoreSoFar)
			{
				bestSystem = system;
				bestScoreSoFar = system.suitability;
			}
		}

		/* Return the system if it's actually useful. */
		if (bestScoreSoFar > 0.000001)
		{
			return bestSystem;
		}
		return null;
	}	

	/* This function calculates the score of a system
	 * by summing up the relevances of all the pronoms it can read.
	 * 
     * SYSTEM	the system image whose score shall be calculated
     * 
     * PRONOMS	all pronoms occuring on the given disk
     *          mapped to their relevances
     *          
     * RETURNS	the score
	 */
	private static double systemScore(SystemImage system,
									  HashMap<String, Double> pronoms)
	{
		double systemScore = 0;
		for (String pronom : pronoms.keySet())
		{
			if (system.readable(pronom))
			{
				systemScore += pronoms.get(pronom);
			}
		}
		return systemScore;
	}
}
