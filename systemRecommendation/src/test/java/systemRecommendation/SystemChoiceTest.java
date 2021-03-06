/* SystemChoiceTest.java
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class SystemChoiceTest {

	public static void test()
	{
		/* Create some systems. */

		HashSet<String> programs0 = new HashSet<String>();	
		/* Microsoft Word */
		programs0.add("Q11261");
		SystemImage system0 = new SystemImage(programs0); /* id 4 */

		HashSet<String> programs1 = new HashSet<String>();
		/* Notepad */
		programs1.add("Q274098");
		/* Windows Media Player, version 10 */
		programs1.add("Q10393867");
		/* IrfanView */
		programs1.add("Q324957");
		SystemImage system1 = new SystemImage(programs1); /* id 5 */

		HashSet<String> programs2 = new HashSet<String>();
		/* Wordpad */
		programs2.add("Q29482");
		/* Notepad */
		programs2.add("Q274098");
		/* IrfanView */
		programs2.add("Q324957");
		SystemImage system2 = new SystemImage(programs2); /* id 6 */

		HashSet<String> programs3 = new HashSet<String>();
		SystemImage system3 = new SystemImage(programs3); /* id 7 */

		ArrayList<SystemImage> systems = new ArrayList<SystemImage>();
		systems.add(system0);
		systems.add(system1);
		systems.add(system2);
		systems.add(system3);


		/* Create disks with pronoms and their relevances. */

		HashMap<String, Double> disk1 = new HashMap<String, Double>();

		/* Since the disk is empty, don't recommend anything. */
		SystemChoice sysChoice = new SystemChoice(systems, disk1);
		assertTrue(sysChoice.bestSystem() == -1);

		/* OpenDocument Text, version 1.1 (Q27203404) */
		disk1.put("fmt/290", 0.04);
		/* Recommend the system with Microsoft Word. */
		sysChoice = new SystemChoice(systems, disk1);
		assertTrue(sysChoice.bestSystem() == 4);

		/* Portable Bitmap (Q28206679) */
		disk1.put("fmt/409", 0.07);
		/* Recommend a system with IrfanView, 
		 * since the bitmap is more important. */
		sysChoice = new SystemChoice(systems, disk1);
		assertTrue(sysChoice.bestSystem() == 5
			|| sysChoice.bestSystem() == 6);

		/* Increase OpenDocument relevance. */
		disk1.put("fmt/290", 0.08);
		/* Recommend the system with Microsoft Word again. It's more important now. */	
		sysChoice = new SystemChoice(systems, disk1);
		assertTrue(sysChoice.bestSystem() == 4);

		/* Rich text format (Q29944786) */
		disk1.put("fmt/355", 0.012);
		/* Recommend the system which reads both the Bitmap as well as the rich text.
		 * Together they're slightly more important. */
		sysChoice = new SystemChoice(systems, disk1);
		assertTrue(sysChoice.bestSystem() == 6);

		ArrayList<SystemImage> emptySystem = new ArrayList<SystemImage>();
		/* If there are no systems given, recommend -1. */
		sysChoice = new SystemChoice(emptySystem, disk1);
		assertTrue(sysChoice.bestSystem() == -1);
	}
}
