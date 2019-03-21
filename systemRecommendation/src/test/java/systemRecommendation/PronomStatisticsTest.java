/* PronomStatisticsTest.java
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

import static org.junit.Assert.*;

import java.io.IOException;

public class PronomStatisticsTest {
	
	
	public static void testPronomStatistics(String directory)
		throws IOException
	{
		String directory1 = directory + "partialSiegfriedData\\\\01Test\\\\";
		PronomStatistics stats1 = new PronomStatistics(directory1);

		assertEquals(10, stats1.getRelativeFrequencyMapSize());

        assertEqualDoubles(stats1.getRelativeFrequency("fmt/212"),
        		0.0129, 0.001);

		assertEqualDoubles(stats1.getRelativeFrequency("fmt/18"),
				0.0519, 0.001);
		
		assertEqualDoubles(stats1.getRelativeFrequency("fmt/276"),
				0.0129, 0.001);
		
		assertEqualDoubles(stats1.getRelativeFrequency("fmt/682"),
				0.0064, 0.001);

		assertEqualDoubles(stats1.getRelativeFrequency("fmt/354"),
				0.0259, 0.001);

		assertEqualDoubles(stats1.getRelativeFrequency("fmt/17"),
				0.4870, 0.001);
	
		assertEqualDoubles(stats1.getRelativeFrequency("fmt/20"),
				0.3636, 0.001);

		assertEqualDoubles(stats1.getRelativeFrequency("fmt/353"),
				0.0064, 0.001);

		assertEqualDoubles(stats1.getRelativeFrequency("x-fmt/413"),
				0.0064, 0.001);

		assertEqualDoubles(stats1.getRelativeFrequency("UNKNOWN"),
				0.0259, 0.001);	
		

        String directory2 = directory + "partialSiegfriedData\\\\02Test\\\\";
		PronomStatistics stats2 = new PronomStatistics(directory2);

		assertEquals(0, stats2.getRelativeFrequencyMapSize());


		String directory3 = directory + "partialSiegfriedData\\\\03Test\\\\";
		PronomStatistics stats3 = new PronomStatistics(directory3);
		
        assertEqualDoubles(stats3.getRelativeFrequency("fmt/212"),
        		           0.1, 0.001);
        
        assertEqualDoubles(stats3.getRelativeFrequency("fmt/18"),
		           0.3, 0.001);
        
        assertEqualDoubles(stats3.getRelativeFrequency("fmt/276"),
		           0.2, 0.001);
        
        assertEqualDoubles(stats3.getRelativeFrequency("UNKNOWN"),
		           0.4, 0.001);


		String directory4 = directory + "partialSiegfriedData\\\\04Test\\\\";
		PronomStatistics stats4 = new PronomStatistics(directory4);
		
		assertEquals(3, stats4.getRelativeFrequencyMapSize());
		
		assertEqualDoubles(stats4.getRelativeFrequency("fmt/212"),
		           0.1428, 0.0001);
		
		assertEqualDoubles(stats4.getRelativeFrequency("fmt/276"),
		           0.2857, 0.0001);
		
		assertEqualDoubles(stats4.getRelativeFrequency("UNKNOWN"),
		           0.5714, 0.0001);
	}
	
	
	/* This method deals with the issue of comparing floating point
	 *  numbers. 
	 */
	private static boolean assertEqualDoubles(double first, double second,
			                           double precision)
	{
		double difference = first - second;

		return (difference > -precision && difference < precision);
	}

}
