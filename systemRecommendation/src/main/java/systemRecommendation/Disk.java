/* Disk.java
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

/* Instances of this class represent disks containing multiple files.
 * SystemRecommendation recommends a system for this kind of disks.
 */
public class Disk {
	
	/* All the files on the disk. */
	public SiegfriedFile[] files;
	
	/* This folder represents the files of the entire disk.
	 * It contains all the SiegfriedFiles from 'files', yet ordered according
	 * to their directories. */
	public Folder disk;

	/* Regular constructor. */
	public Disk(SiegfriedFile[] files, String diskName)
	{
		this.files = files;
		name = diskName;
		disk = orderFiles(files);
	}
	
	/* This function orders a list of files according to their directories
	 * in folders ans subfolders.
	 */
	private Folder orderFiles(SiegfriedFile[] files)
	{
		Folder disk = new Folder("");
		
		/* Add each file, one after the other.*/
		for (SiegfriedFile file : files)
		{
			if (file == null) {continue; }

			/* Iterate over the subfolders of the directory of the current file.
			 * If the subfolder name is valid, add it if necessary and proceed
			 *  to the next subfolder.
			 */
			Folder currentSubfolder = disk;
			for (String subfolder : file.getDirectory())
			{
				if (subfolder != "")
				{
					currentSubfolder = currentSubfolder.addFolder(subfolder);
				}
			}
			/* We've reached the correct subfolder. Now add the file. */
			currentSubfolder.addFile(file);
		}
		return disk;
	}
	
	
	/* The name of the most relevant pronom on the disk.
	 * Used for parameter tuning. */
	private String mostRelevantPronom = "";

	/* Disk name (usually xy.ISO) */
	public String name = "";	
	
	/* Constructor for parameter tuning. */
	public Disk(String mostImportantPronom, SiegfriedFile[] someFiles, String diskName)
	{
		mostRelevantPronom = mostImportantPronom;
		files = someFiles;
		name = diskName;
	}
	
	public String getMostRelevantPronom()
	{
		return mostRelevantPronom;
	}
}
