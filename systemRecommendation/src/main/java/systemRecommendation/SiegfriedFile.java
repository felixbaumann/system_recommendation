/* SiegfriedFile.java
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

/* Represents a single file within a Siegfried output. */
public class SiegfriedFile {

	/* Simple file size. */
	private final int size;
	
	/* Size penalized by the depth in the file system. */
	private int penalizedSize;
	
	/* Contains the path to the file.
	 * (Including the file itself) */
	private String filePath;
	
	private String filename;
	
	/* Contains the directory of the file (not including the file itself).
	 * The path is split into folders and subfolders.
	 * Note that some of the entries may be empty (""). 
	 */
	private String[] directory;
	
	/* Contains the pronoms of all possible matches of this file. */
	private ArrayList<PronomMatch> matches = new ArrayList<PronomMatch>();
	
	public SiegfriedFile(int fileSize, String filepath)
	{
		size = (fileSize >= 0 ? fileSize : -1);
		filePath = filepath;
		extractDirectory(filepath);
	}
	
	/* Constructor where some matches are already known. */
	public SiegfriedFile(int fileSize, String filepath,
		PronomMatch pronomMatches[])
	{
		size = (fileSize >= 0 ? fileSize : -1);
		filePath = filepath;
		extractDirectory(filepath);

		if (pronomMatches == null) {return; }
		for (PronomMatch match : pronomMatches)
		{
			addMatch(match);
		}
	}
	
	/* Add a match to the list of matches for this file. */
	public void addMatch(PronomMatch match)
	{
		matches.add(match);
	}
	
	/* Get the match of a given index. */
	public PronomMatch getMatch(int index)
	{
		return matches.get(index);
	}
	
	/* Get all the matches. */
	public ArrayList<PronomMatch> matches() { return matches; }
	
	/* Gets the current number of matches for this file. */
	public int matchCount() { return matches.size(); }
	
	/* Get the file size. */
	public int fileSize() { return size; }

	public int getPenalizedSize() { return penalizedSize; }
	
	public void setPenalizedSize(int penalizedSize)
	{
		this.penalizedSize = penalizedSize;
	}
	
	/* Get the directory (without the filename). */
	public String[] getDirectory() { return directory; }
	
	/* Get the file path (with the filname). */
	public String getFilePath() { return filePath; }
	
	public String getFilename() { return filename; }
	
	/* A given relevance value is shared accross all pronom matches. */
	public void setRelevance(double relevance)
	{
		if (matches.size() == 0) { return; }

		for (PronomMatch match : matches)
		{
			match.setRelevance(relevance / matches.size());
		}
	}
	
	/* If the file size is unknown, it will be set to -1. */
    public boolean validSize()
    {
    	return size != -1;
    }

    /* The filepath includes the filename itself.
     * The directory is the same yet split into substrings
     * and without the filename.
     * 
     * FILEPATH   The path to the file including its name.
     * 
     * RETURNS    The path to the folder containing the file.
     *            The path (directory) is stored as a list of strings,
     *            each representing a subfolder.
     *            Note that the directory may contain empty strings ("").
     */
    private void extractDirectory(String filepath)
    {
    	if (filepath == null)
    	{
    		this.filename = "";
    		this.directory = new String[] {};
    		return;
    	}

    	/* Each slash indicates a subfolder. */
    	String[] directory = filepath.split("/");
    	
    	/* The filename is the last substring of the filepath. */
    	this.filename = directory[directory.length -1];

    	/* Remove the filename from the directory. */
    	directory[directory.length -1] = "";

    	this.directory = directory;
    }
}
