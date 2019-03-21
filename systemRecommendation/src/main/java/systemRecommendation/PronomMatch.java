/* PronomMatch.java
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

/* The instances of this class represent pronom matches of files
 * (see SiegfriedFile).
 * They each have a Pronom ID (the id of the format) and a relevance.
 */
public class PronomMatch {

	/* The 'Persistent Unique IDentifier of the PRONOM technical registry
	 * allows to identify a file format. */
	private String PUID;
	private double relevance;
	
	public PronomMatch(String pronom)
	{
		PUID = pronom;
		relevance = 0;
	}
	
	public double getRelevance()
	{
		return relevance;
	}
	
	public void setRelevance(double rel)
	{
		relevance = rel;
	}

	public String pronom()
	{
		return PUID;
	}
}
