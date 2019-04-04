/* SystemRecommendationTest.java
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
import org.junit.Test;

public class SystemRecommendationTest {
	
	public static void localTest(String directory) throws IOException
	{
		String systems2 = directory + "testSystems\\\\test02.txt";
		
		String pronomStat = directory + "siegfriedData\\\\";
		
		String disk1 = directory + "siegfriedData\\\\39002105324017.ISO";
		
		String args6_1[] = {systems2, pronomStat, disk1, "0.7"};
		String args6_2[] = {systems2, pronomStat, disk1, "0.9"};
		String args3_1[] = {systems2, pronomStat, disk1};

		SystemRecommendation.main(args6_1);	
		assert(24 == SystemRecommendation.chosenSystems[0]
			|| 25 == SystemRecommendation.chosenSystems[0]);
		SystemRecommendation.main(args6_2);	
		assert(29 == SystemRecommendation.chosenSystems[0]
			|| 30 == SystemRecommendation.chosenSystems[0]);
		SystemRecommendation.main(args3_1);
		assert(34 == SystemRecommendation.chosenSystems[0]
			|| 35 == SystemRecommendation.chosenSystems[0]);
	}
	
	@Test
	public void main() throws IOException
	{
		String directory = "D:\\\\Bibliotheken\\\\Studium\\\\Bachelorarbeit\\\\testData\\\\";

		String systems = directory + "testSystems\\\\thesis_archive.txt";
		
		String pronomStat = directory + "siegfriedData\\\\";
		
		String disk = directory + "siegfriedData\\\\39002105324017.ISO";

		String args[] = {systems, pronomStat, disk};

		SystemRecommendation.main(args);
		assert(2 == SystemRecommendation.chosenSystems[0] ||
			   3 == SystemRecommendation.chosenSystems[0]  ||
			   5 == SystemRecommendation.chosenSystems[0] ||
		       6 == SystemRecommendation.chosenSystems[0]);
		
		String disk2 = directory + "siegfriedData\\\\39002123858236.ISO";

		String args2[] = {systems, pronomStat, disk2};
		
		/* The archive contains 7 systems. With each execution the system counter increases by 7. */
		SystemRecommendation.main(args2);
		assert(2 + 7 == SystemRecommendation.chosenSystems[0] ||
			   6 + 7 == SystemRecommendation.chosenSystems[0]);		
	}
}
