/* PronomRelevanceTest.java
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
import java.util.HashMap;
import java.util.Iterator;

public class PronomRelevanceTest {

	public static void test(String directory) throws IOException
	{
		/* Create some pronom statistics. */
		String directory1 = directory + "partialSiegfriedData\\\\01Test\\\\";
		PronomStatistics stats1 = new PronomStatistics(directory1);
		
		String directory2 = directory + "partialSiegfriedData\\\\02Test\\\\";
		PronomStatistics stats2 = new PronomStatistics(directory2);
		
		String directory3 = directory + "partialSiegfriedData\\\\03Test\\\\";
		PronomStatistics stats3 = new PronomStatistics(directory3);
		
		String directory4 = directory + "partialSiegfriedData\\\\04Test\\\\";
		PronomStatistics stats4 = new PronomStatistics(directory4);

		String directory5 = directory + "siegfriedData\\\\";
		PronomStatistics stats5 = new PronomStatistics(directory5);


		/* Create some SiegfriedFiles. */
		SiegfriedFile file0 = new SiegfriedFile(0, "");
        
        SiegfriedFile file1 = new SiegfriedFile(1024, "");
        file1.addMatch(new PronomMatch("fmt/18"));
        
        SiegfriedFile file2 = new SiegfriedFile(1024, "");
        file2.addMatch(new PronomMatch("fmt/276"));
        
        SiegfriedFile file3 = new SiegfriedFile(1024, "");
        file3.addMatch(new PronomMatch("NeverSeen"));
        
        SiegfriedFile file4 = new SiegfriedFile(1024, "");
        file4.addMatch(new PronomMatch("UNKNOWN"));
        
        SiegfriedFile file5 = new SiegfriedFile(1024, "");
        file5.addMatch(new PronomMatch("fmt/212"));
        
        SiegfriedFile file6 = new SiegfriedFile(1024, "");
        file6.addMatch(new PronomMatch("fmt/17"));
        
        SiegfriedFile file7 = new SiegfriedFile(1024, "");
        file7.addMatch(new PronomMatch("fmt/20"));
        
        SiegfriedFile file8 = new SiegfriedFile(1024, "");
        file8.addMatch(new PronomMatch("x-fmt/413"));
            
        SiegfriedFile file9 = new SiegfriedFile(2048, "");
        file9.addMatch(new PronomMatch("fmt/18"));
        
        /* 1 MB pdf */
        SiegfriedFile file10 = new SiegfriedFile(1048576, "");
        file10.addMatch(new PronomMatch("fmt/18"));
 
        /* 1 MB exotic file only occuring once in 01Test */
        SiegfriedFile file11 = new SiegfriedFile(1048576, "");
        file11.addMatch(new PronomMatch("x-fmt/413"));
        
        /* 1 GB pdf */
        SiegfriedFile file12 = new SiegfriedFile(1073741824, "");
        file12.addMatch(new PronomMatch("fmt/18"));
        
        /* 1 GB exotic file only occuring once in 01Test */
        SiegfriedFile file13 = new SiegfriedFile(1073741824, "");
        file13.addMatch(new PronomMatch("x-fmt/413"));
        
        /* 2 KB with multiple pdf matches */
        SiegfriedFile file14 = new SiegfriedFile(2048, "");
        file14.addMatch(new PronomMatch("fmt/17"));
        file14.addMatch(new PronomMatch("fmt/18"));
        file14.addMatch(new PronomMatch("fmt/20"));
        
        /* 4 KB with two pdf matches */
        SiegfriedFile file15 = new SiegfriedFile(4096, "");
        file15.addMatch(new PronomMatch("fmt/17"));
        file15.addMatch(new PronomMatch("fmt/20"));
        
        SiegfriedFile file16 = new SiegfriedFile(8192, "");
        file16.addMatch(new PronomMatch("x-fmt/17"));
        file16.addMatch(new PronomMatch("fmt/17"));
        file16.addMatch(new PronomMatch("fmt/18"));
        file16.addMatch(new PronomMatch("fmt/20"));
        file16.addMatch(new PronomMatch("fmt/276"));

        /* 8 KB txt */
        SiegfriedFile file17 = new SiegfriedFile(8192, "");
        file17.addMatch(new PronomMatch("fmt/111"));
        
        /* 10 KB txt */
        SiegfriedFile file18 = new SiegfriedFile(10240, "");
        file18.addMatch(new PronomMatch("fmt/111"));
        
        /* 3 MB txt */
        SiegfriedFile file19 = new SiegfriedFile(3145728, "");
        file19.addMatch(new PronomMatch("fmt/111"));
        
        /* 8 KB htm */
        SiegfriedFile file20 = new SiegfriedFile(8192, "");
        file20.addMatch(new PronomMatch("fmt/96"));
        
        /* 8 KB vector markup language */
        SiegfriedFile file21 = new SiegfriedFile(8192, "");
        file21.addMatch(new PronomMatch("fmt/583"));
        
        /* 3 MB vector markup language */
        SiegfriedFile file22 = new SiegfriedFile(3145728, "");
        file22.addMatch(new PronomMatch("fmt/583"));
        
        /* 8 KB portable executable */
        SiegfriedFile file23 = new SiegfriedFile(8192, "");
        file23.addMatch(new PronomMatch("fmt/899"));
        
        /* 12 MB portable executable */
        SiegfriedFile file24 = new SiegfriedFile(12582912, "");
        file24.addMatch(new PronomMatch("fmt/899"));
        
        /* 8 KB xml property list*/
        SiegfriedFile file25 = new SiegfriedFile(8192, "");
        file25.addMatch(new PronomMatch("fmt/979"));
        
        /* 12 MB xml property list*/
        SiegfriedFile file26 = new SiegfriedFile(12582912, "");
        file26.addMatch(new PronomMatch("fmt/979"));


        /* Create some disks containing files. */
        Disk disk1 = new Disk(new SiegfriedFile[] {}, "");
        Disk disk2 = new Disk(new SiegfriedFile[] {file1}, "");
        Disk disk3 = new Disk(new SiegfriedFile[] {file1, file2}, "");       
        Disk disk4 = new Disk(new SiegfriedFile[] {file1, file2, file3}, "");
        Disk disk5 = new Disk(new SiegfriedFile[] {file1, file1, file1,
        	file2, file4, file5, file6, file7, file13}, "");

        Disk disk6 = new Disk(new SiegfriedFile[] {file0}, "");
        Disk disk7 = new Disk(new SiegfriedFile[] {file19, file19, file19,
        	file19, file19, file24, file26}, "");

        Disk disk8 = new Disk(new SiegfriedFile[] {file1, file2, file3,
            file4, file5, file6, file7, file8, file9, file10, file11, file12,
            file13, file14, file15, file16, file17, file18, file19, file20,
            file21, file22, file23, file24, file25, file26}, "");

        Disk disk9 = new Disk(new SiegfriedFile[] {file21, file21, file7,
        	file9}, "");

        Disk disk10 = new Disk(new SiegfriedFile[] {file4, file5, file6,
            file4, file4, file6}, "");

        Disk disk11 = new Disk(new SiegfriedFile[] {file24, file12}, "");

        Disk disk12 = new Disk(new SiegfriedFile[] {file4, file4, file9,
                file9, file4, file19}, "");

        Disk disk13 = new Disk(new SiegfriedFile[] {file24, file25}, ""); 

        Disk disk14 = new Disk(new SiegfriedFile[] {file6, file6, file7,
            	file12}, "");

        Disk disk15 = new Disk(new SiegfriedFile[] {file12, file12, file12,
            	file13}, "");   

        Disk disk16 = new Disk(new SiegfriedFile[] {file16, file16, file16,
            	file14, file14}, "");


        /* Use disk 1-16 for statistics 1-5 */
        System.out.println("\nDisk 1, stats 1-5\n");
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk1, stats1, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk1, stats2, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk1, stats3, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk1, stats4, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk1, stats5, 0.33333, 0.33333, 0.33333));

        System.out.println("\nDisk 2, stats 1-5\n");
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk2, stats1, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk2, stats2, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk2, stats3, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk2, stats4, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk2, stats5, 0.33333, 0.33333, 0.33333));
        
        System.out.println("\nDisk 3, stats 1-5\n");
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk3, stats1, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk3, stats2, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk3, stats3, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk3, stats4, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk3, stats5, 0.33333, 0.33333, 0.33333));
        
        System.out.println("\nDisk 4, stats 1-5\n");
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk4, stats1, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk4, stats2, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk4, stats3, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk4, stats4, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk4, stats5, 0.33333, 0.33333, 0.33333));
        
        System.out.println("\nDisk 5, stats 1-5\n");
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk5, stats1, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk5, stats2, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk5, stats3, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk5, stats4, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk5, stats5, 0.33333, 0.33333, 0.33333));
        
        System.out.println("\nDisk 6, stats 1-5\n");
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk6, stats1, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk6, stats2, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk6, stats3, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk6, stats4, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk6, stats5, 0.33333, 0.33333, 0.33333));
        
        System.out.println("\nDisk 7, stats 1-5\n");
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk7, stats1, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk7, stats2, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk7, stats3, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk7, stats4, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk7, stats5, 0.33333, 0.33333, 0.33333));

        System.out.println("\nDisk 8, stats 1-5\n");
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk8, stats1, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk8, stats2, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk8, stats3, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk8, stats4, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk8, stats5, 0.33333, 0.33333, 0.33333));

        System.out.println("\nDisk 9, stats 1-5\n");
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk9, stats1, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk9, stats2, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk9, stats3, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk9, stats4, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk9, stats5, 0.33333, 0.33333, 0.33333));

        System.out.println("\nDisk 10, stats 1-5\n");
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk10, stats1, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk10, stats2, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk10, stats3, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk10, stats4, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk10, stats5, 0.33333, 0.33333, 0.33333));

        System.out.println("\nDisk 11, stats 1-5\n");
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk11, stats1, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk11, stats2, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk11, stats3, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk11, stats4, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk11, stats5, 0.33333, 0.33333, 0.33333));
        
        System.out.println("\nDisk 12, stats 1-5\n");
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk12, stats1, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk12, stats2, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk12, stats3, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk12, stats4, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk12, stats5, 0.33333, 0.33333, 0.33333));
        
        System.out.println("\nDisk 13, stats 1-5\n");
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk13, stats1, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk13, stats2, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk13, stats3, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk13, stats4, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk13, stats5, 0.33333, 0.33333, 0.33333));
        
        System.out.println("\nDisk 14, stats 1-5\n");
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk14, stats1, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk14, stats2, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk14, stats3, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk14, stats4, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk14, stats5, 0.33333, 0.33333, 0.33333));
        
        System.out.println("\nDisk 15, stats 1-5\n");
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk15, stats1, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk15, stats2, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk15, stats3, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk15, stats4, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk15, stats5, 0.33333, 0.33333, 0.33333));
        
        System.out.println("\nDisk 16, stats 1-5\n");
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk16, stats1, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk16, stats2, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk16, stats3, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk16, stats4, 0.33333, 0.33333, 0.33333));
        printRelevances(PronomRelevance.relativePronomRelevances(
        		disk16, stats5, 0.33333, 0.33333, 0.33333));
	}


	private static void printRelevances(HashMap<String, Double> relevances)
	{
		System.out.println("\nPronom       Relevance\n");
		Iterator<String> iter = relevances.keySet().iterator();
		while (iter.hasNext())
		{
			String pronom = iter.next();
			System.out.println(pronom + "    " + relevances.get(pronom));
		}
		System.out.println("\n");
	}
}
