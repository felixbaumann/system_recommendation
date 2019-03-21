/* Folder.java
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

import java.util.ArrayList;
import java.util.HashMap;

public class Folder {
	
	/* Name of the folder. */
	private final String name;
	
	/* Size of the folder in bytes. */
	private double size = -1;
	
	/* Contains all subfolders of this folder. */
	private HashMap<String, Folder> folders = new HashMap<String, Folder>();
	
	/* Contains all files of this folder that are NOT in a subfolder. */
	private ArrayList<SiegfriedFile> files = new ArrayList<SiegfriedFile>();

	/* Tracks changes in the file system. Whenever a file is added somewhere,
	 * this variable will be incremented. */
    private static int journal = 0;
    
    /* This is the value the journal held the last time the size 
     * of this folder was calculated.
     * If it is smaller than the current value of journal,
     * a file has been added and the folder size has to be recalculated.
     */
    private int fileSystemVersion = -1;

	public Folder(String name)
	{
		this.name = name;
	}
	
	/* Adds a subfolder and returns it.
	 * If the folder alreay exists, returns the existing one. 
	 */
    public Folder addFolder(String foldername)
    {
    	/* Get the folder in case it already exists. */
    	Folder folder = folders.get(foldername);
    	
    	/* If it doesn't, create a new one. */
    	if (folder == null)
    	{
    		folders.put(foldername, new Folder(foldername));
    		folder = folders.get(foldername);
    	}

    	return folder;
    }
    
    /* Adds a file to this folder. */
    public void addFile(SiegfriedFile file)
    {
    	files.add(file);
    	journal++;
    }
    
    public String name() { return name; }
    
    public Folder getFolder(String name) { return folders.get(name); }
    
    public HashMap<String, Folder> folders() { return folders; }
    
    public ArrayList<SiegfriedFile> files() { return files; }
    
    /* Returns the size of this folder and all its content.
     * 
     * Use a value betwenn 0 and 1 for depthPenalty.
     * The lower, the larger the size penalty for subfolders.
     */
    public double size(double depthPenalty)
    {
    	/* If no file has been added anywhere since the last size calculation
    	 * of this folder, we don't have to recalculate it. */
    	if (journal == fileSystemVersion) { return size; }
    	
    	/* A file has been added lately.
    	 * Recalculate the folder size. */
    	size = 0;

    	/* Sum up the sizes of all subfolders. */
    	for (Folder folder : folders.values())
    	{
    		size += depthPenalty * folder.size(depthPenalty);
    	}

    	for (SiegfriedFile file : files)
    	{
    		size += file.fileSize();
    	}

    	/* The folder size doesn't have to be recalculated
    	 * until another file will be added. */
    	fileSystemVersion = journal;

    	return size;
    }

}
