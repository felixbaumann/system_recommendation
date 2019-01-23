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
	
	/* The name of the most relevant pronom on the disk.
	 * Used for parameter tuning. */
	private String mostRelevantPronom = "";
	
	/* Regular constructor. */
	public Disk(SiegfriedFile[] someFiles)
	{
		files = someFiles;
	}
	
	/* Constructor for parameter tuning. */
	public Disk(String mostImportantPronom, SiegfriedFile[] someFiles)
	{
		mostRelevantPronom = mostImportantPronom;
		files = someFiles;
	}
	
	public String getMostRelevantPronom()
	{
		return mostRelevantPronom;
	}
}
