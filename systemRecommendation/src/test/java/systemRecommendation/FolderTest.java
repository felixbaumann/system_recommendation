package systemRecommendation;

import static org.junit.Assert.*;

public class FolderTest {

	public static void test()
	{
		Folder folder = new Folder("my folder");
		assertTrue(0.0 == folder.size(1));

		folder.addFolder("my subfolder");

		SiegfriedFile file0 = new SiegfriedFile(20, "");
		folder.getFolder("my subfolder").addFile(file0);
	    assertTrue(20 == folder.getFolder("my subfolder").size(1));
	    assertTrue(20 == folder.size(1));

		SiegfriedFile file1 = new SiegfriedFile(10, "");
		folder.addFile(file1);
		assertTrue(20 == folder.getFolder("my subfolder").size(1));
		assertTrue(30 == folder.size(1));
	}
}
