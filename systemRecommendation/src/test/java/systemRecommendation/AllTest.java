/* AllTest.java
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

public class AllTest {

/* Set this variable to false,
 * if the local directories with test data don't exist. */
private boolean directoriesExist = false;

/* Default directory for test data. */
private static String directory
    = "D:\\\\Bibliotheken\\\\Studium\\\\Bachelorarbeit\\\\testData\\\\";

	@Test
	public void runAllTests() throws IOException
	{
		/* Machine independent tests. */
		SystemImageTest.test();
		SystemChoiceTest.test();
		SystemStatisticsTest.test1();
		ExtractSiegfriedDataTest.testExtractSiegfriedData();
		SiegfriedFileTest.test();
		FolderTest.test();

		/* Tests requiring certain test files in certain directories. */
		if (directoriesExist)
		{
			ExtractSiegfriedDataTest.testExtractPronoms(directory);
			ExtractSiegfriedDataTest.testExtractSiegfriedDataFromFile(directory);
			PronomRelevanceTest.test(directory);
			PronomStatisticsTest.testPronomStatistics(directory);
			SystemStatisticsTest.test2(directory);
			SystemRecommendationTest.localTest(directory);
		}
	}
}
