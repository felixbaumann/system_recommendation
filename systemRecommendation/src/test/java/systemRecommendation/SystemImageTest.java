/* SystemImageTest.java
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

import java.util.HashSet;

public class SystemImageTest {


	public static void test() {
		HashSet<String> programs = new HashSet<String>();
		
		/* Wordpad */
		programs.add("Q29482");
		
		/* Microsoft Word */
		programs.add("Q11261");

		/* Constructor using a program set */
		SystemImage si = new SystemImage(programs);
		
		/* ID getter */
		assertEquals(0, si.id());
		
		/* plain text format */
		assertEquals(true, si.readablePronoms().contains("x-fmt/111"));
		
		/* Neither Wordpad nor Word can write MP3 files. */
		assertEquals(false, si.writablePronoms().contains("fmt/134"));
		
		assertEquals(true, si.readable("x-fmt/111"));
		
		/* Open document Text version 1.1 */
		assertEquals(true, si.writable("fmt/290"));
		
		
		HashSet<String> readable = new HashSet<String>();
		HashSet<String> writable = new HashSet<String>();
		
		/* mp3 */
		readable.add("fmt/134");
		writable.add("fmt/134");
		
		/* quick time file format*/
		readable.add("x-fmt/384");
		
		/* Constructor using readable and writable format sets */
		SystemImage si2 = new SystemImage(readable, writable);
		
		assertEquals(1, si2.id());
		assertEquals(true, si2.readable("fmt/134"));
		assertEquals(true, si2.writable("fmt/134"));
		assertEquals(true, si2.readable("x-fmt/384"));
		assertEquals(false, si2.writable("x-fmt/384"));
		assertEquals(false, si2.readable("fmt/589"));
		
		
		
		HashSet<String> morePrograms = new HashSet<String>();
		
		/* Wordpad */
		morePrograms.add("Q29482");
		
		/* Microsoft Word */
		morePrograms.add("Q11261");
		
		/* Windows Media Player version 12 */
		morePrograms.add("Q28018460");
		
		/* Constructor using a program set
		 * 
		 *  Since Wordpad and Microsoft Word have been fetched from wikidata
		 *  before, there will be a addPronomsFromArchive() call this time.
		 */
		SystemImage si3 = new SystemImage(morePrograms);
		
		/* Looking up pronoms in local archive */
		assertEquals(true, si3.readable("x-fmt/111"));
		assertEquals(true, si3.writable("x-fmt/111"));
		
		/* Looking up additional pronoms of unknown programs from wikidata */
		assertEquals(true, si3.readable("fmt/134"));
		assertEquals(false, si3.writable("fmt/134"));
	
		
		/* Special cases */
		
		/* null string */
		assertEquals(false, si3.readable(null));
		
		/* broken QID */
		HashSet<String> noPrograms = new HashSet<String>();		
		noPrograms.add("invalidWikidataQId");
		SystemImage si4 = new SystemImage(noPrograms);

		assertEquals(0, si4.readablePronoms().size());
		
		
	}

}
