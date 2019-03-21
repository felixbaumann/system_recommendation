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

import static org.junit.Assert.assertEquals;

import java.io.IOException;

public class SystemRecommendationTest {
	
	public static void localTest(String directory) throws IOException
	{
		String systems2 = directory + "testSystems\\\\test02.txt";
		
		String pronomStat = directory + "siegfriedData\\\\";
		
		String disk1 = directory + "siegfriedData\\\\39002105324017.ISO";
		
		String args6_1[] = {systems2, pronomStat, disk1, "0.7", "0.2", "0.1"};
		String args6_2[] = {systems2, pronomStat, disk1, "0.9", "0.3", "0.2"};
		String args3_1[] = {systems2, pronomStat, disk1};

		SystemRecommendation.main(args6_1);	
		assertEquals(23, SystemRecommendation.chosenSystems[0]);
		SystemRecommendation.main(args6_2);	
		assertEquals(28, SystemRecommendation.chosenSystems[0]);
		SystemRecommendation.main(args3_1);
	    assertEquals(33, SystemRecommendation.chosenSystems[0]);
	}
}
