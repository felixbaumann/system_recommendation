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
import java.util.Iterator;


/* This class chooses a system environment according to the relevance of the
 * pronoms on the given disk.
 * 
 * It accepts the pronoms with their relevances along with the systems
 * and the pronoms they can deal with.
 * 
 * It will then choose a system that maximizes the sum (?) of relevances
 * of pronoms it can deal with.
 */
public class SystemChoice
{
    /* This function chooses the system that maximizes
     * the sum of the pronom relevances.
     * 
     * SYSTEMS	all available system images from which one shall be chosen
     * 
     * PRONOMS	all pronoms occuring on the given disk
     *          mapped to their relevances
     *          
     * DEFAULTSYSTEM	ID of the system recommended if no pronoms are given
     *                  or no system can read any of the formats.
     * 
     * RETURNS	the id of the recommended system
     * 			or -1 if there are no systems given
     */
	public static int chooseSystem(ArrayList<SystemImage> systems,
			                       HashMap<String, Double> pronoms,
			                       int defaultSystem)
	{
		/* No systems or pronoms cases. */
		if (systems == null) { return -1; }
		if (systems.size() == 0) {return -1; }
		
		if (pronoms == null) { return defaultSystem; }
		if (pronoms.size() == 0) {return defaultSystem; }
		
		/* ID of the best system and its score found yet */
		int bestSystemSoFar = 0;
		double bestScoreSoFar = 0;
		
		/* Find the system with largest score. */
		for (int sys = 0; sys < systems.size(); sys++)
		{
			double currentScore = systemScore(systems.get(sys), pronoms);
			
			if (currentScore > bestScoreSoFar)
			{
				bestSystemSoFar = sys;
				bestScoreSoFar = currentScore;
			}
		}

		return (bestScoreSoFar > 0 ? bestSystemSoFar : defaultSystem);
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
		
		Iterator<String> iter = pronoms.keySet().iterator();
		while(iter.hasNext())
		{
			String pronom = iter.next();
			if (system.readable(pronom))
			{
				systemScore += pronoms.get(pronom);
			}
		}
		return systemScore;
	}
}
