package systemRecommendation;

import static org.junit.Assert.*;

public class SiegfriedFileTest {

	public static void test()
	{		
		SiegfriedFile file0 = new SiegfriedFile(
			1024, "C:/this/is/the/directory/filename.iso");
		assertEqualLists(new String[] {"C:", "this", "is", "the", "directory", ""}, file0.getDirectory());

		SiegfriedFile file1 = new SiegfriedFile(1024, "filename.iso");
		assertEqualLists(file1.getDirectory(), new String[] {""});
		
		SiegfriedFile file2 = new SiegfriedFile(1024, "");
		assertEqualLists(file2.getDirectory(), new String[] {""});
		
		SiegfriedFile file3 = new SiegfriedFile(1024, "/filename.iso");
		assertEqualLists(file3.getDirectory(), new String[] {"", ""});
	}
	
	
	private static void assertEqualLists(String[] first, String[] second)
	{
		if (first == null) { assert(second == null);}
		if (second == null) { assert(first == null);}

		assert(first.length == second.length);

		for (int i = 0; i < first.length; i++)
		{
			assertEquals(first[i], second[i]);
		}
	}
}
