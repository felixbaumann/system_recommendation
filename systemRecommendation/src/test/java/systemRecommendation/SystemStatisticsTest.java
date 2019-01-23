/* SystemStatisticsTest.java
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
import java.nio.file.Path;
import java.nio.file.Paths;

public class SystemStatisticsTest {

	/* Wikidata entity ids of some Programs
	 * Q29482 Wordpad
	 * Q11261 Word
	 * Q28018460 Windows Media Player 12
	 * Q28018527 Windows Media Player 11
	 * Q10393867 Windows Media Player 10
	 * Q11272 Excel
	 * Q324957 Irfan View
	 * Q171477 VLC
	 * Q9589 iTunes
	 */
	
	public static void test1()
	{
		SystemStatistics systems1 = new SystemStatistics("");
		/* wordpad */
		SystemStatistics systems2 = new SystemStatistics("Q29482");
		/* wordpad, microsoft word, windows media player*/
		SystemStatistics systems3 = new SystemStatistics(
			"Q29482, Q11261, Q28018460");
		SystemStatistics systems4 = new SystemStatistics("Q29482;");
		SystemStatistics systems5 = new SystemStatistics("Q29482, Q11261"
			+ ";Q28018460, Q29482 ;Q11261 ;  Q29482, Q11261, Q28018460");
		
		assertEquals(systems1.systems.size(), 0);
		assertEquals(systems2.systems.size(), 1);
		assertEquals(systems3.systems.size(), 1);
		assertEquals(systems4.systems.size(), 1);
		assertEquals(systems5.systems.size(), 4);
		
		/* No systems at all, therefore no system able to read anything. */
		assertEquals(systems1.readingSystems.keySet().size(), 0);
		/* Only one system including wordpad,
		 * therefore exactly one system able to read txt files. */
		assertEquals(systems2.numberOfReadingSystems("x-fmt/111"), (Integer) 1);
		assertEquals(systems2.numberOfWritingSystems("x-fmt/111"), (Integer) 1);
		/* pdf */
		assertEquals(systems2.numberOfReadingSystems("fmt/18"), (Integer) 0);
		assertEquals(systems2.numberOfWritingSystems("fmt/18"), (Integer) 0);

		/* Only one system yet multiple programs reading txt files. */
		assertEquals(systems3.numberOfReadingSystems("x-fmt/111"), (Integer) 1);
		
		/* Empty systems shall be ignored. */
		assertEquals(systems4.systems.size(), 1);
	}
	
	public static void test2(String directory)
	{
		directory += "testSystems\\\\test01.txt";
		Path path = Paths.get(directory);

		try
		{
		    SystemStatistics systems0 = new SystemStatistics(path);
		    /* Empty systems shall be ignored. */
			assertEquals(systems0.systems.size(), 5);
		}
		catch (IOException e)
		{
			/* File doesn't exist or access denied. */
		}
	}
}
