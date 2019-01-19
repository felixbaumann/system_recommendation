/* SystemStatistics.java
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
import java.nio.file.Files;
import java.nio.file.Path;
//import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/* This class accepts the representations of a whole archive of systems
 * and calculates some statistics about how common the readability of
 * a pronom is.
 * 
 * It therefore provides the systems, the pronoms of formats they can
 * read and how rare the knowledge about a pronom is.
 * 
 */
public class SystemStatistics
{

	/* This list contains all the system images in the correct order
	 * according to their occurence and their ID.
	 */
	public ArrayList<SystemImage> systems;

	/* These maps contain the number of systems that can read/write a given
	 * pronom. The larger the number, the more common the pronom obviously
	 * is. The lower the number, the less common and therefore more important
	 * the pronom is.
	 */
	public HashMap<String, Integer> readingSystems;
	public HashMap<String, Integer> writingSystems;
	
	/* Returns the number of systems able to read a given pronom.*/
	public Integer numberOfReadingSystems(String pronom)
	{
		return (readingSystems.get(pronom) == null ?
				0 : readingSystems.get(pronom));
	}
	
	/* Returns the number of systems able to write a given pronom.*/
	public Integer numberOfWritingSystems(String pronom)
	{
		return (writingSystems.get(pronom) == null ?
				0 : writingSystems.get(pronom));
	}
	
	/* Constructor if the systems are given as string.
	 * 
	 * SOURCE	the systems divided by semicolons.
	 * 			Each system is represented by the wikidata QIDs
	 * 			of the installed programs divided by commas.
	 * 			Spaces are ignored. 
	 */
	public SystemStatistics(String source)
	{	
		systems = importSystemsFromString(source);
		readingSystems = calculatePronomCommonness(true);
		writingSystems = calculatePronomCommonness(false);
	}
	
	/* Constructor if the systems are given by the path to a file.
	 * 
	 * SOURCE	the path to the file.
	 * 			Each line in the file represents a system with each
	 * 			system represented by the wikidata QIDs of the installed
	 * 			programs. The QIDs have to be seperated by commas. Spaces are
	 * 			ignored.
	 */
	public SystemStatistics(Path source) throws IOException
	{
		systems = importSystemsFromFile(source);
		readingSystems = calculatePronomCommonness(true);
		writingSystems = calculatePronomCommonness(false);
	}
	
	/* This function reads an archive of system environments from a string.
	 * Each part of the string shall represent a system environment,
	 * divided by a semicolon.
	 * Each part consists of the QIDs of the programs installed on the system.
	 * The QIDs are divided by commas. Spaces are ignored.
	 * 
	 * Alternative to importSystemsFromFile.
	 * 
	 * STRING	the string containing the system representations.
	 * 
	 * RETURNS	a list whose objects represent systems, each being a set of
	 * 			the installed programs.
	 */
	private static ArrayList<SystemImage> importSystemsFromString(
		String string)
	{			
		return extractPrograms(Arrays.asList(string.split(";")));
	}
	
	/* This function reads an archive of system environments from a file.
	 * Each line in the file shall represent a system environment.
	 * Each line consists of the QIDs of the programs installed on the system.
	 * The QIDs are divided by commas. Spaces are ignored.
	 * 
	 * Alternative to importSystemsFromString.
	 * 
	 * PATH		the path to the file containing the system representations.
	 * 
	 * RETURNS	a list whose objects represent systems, each being a set of
	 * 			the installed programs.
	 */
	private static ArrayList<SystemImage> importSystemsFromFile(Path path)
		throws IOException
	{
		/* 1. Read the file and put all lines in a list,
		 * each representing a system. */
		List<String> systems = new ArrayList<String>();
		systems = Files.readAllLines(path);

		/* 2. Extract the QIDs of the programs of each system. */
		return extractPrograms(systems);
	}

	/* This function extracts the QIDs of all programs of all systems
	 * represented by a list of string.
	 * 
	 * The QIDs have to be seperated by commas, spaces are ignored.
	 * 
	 * SYSTEMS	contains strings, each representing a system by the QIDs of
	 * 			the installed programs.
	 * 
	 * RETURNS	an ArrayList of HashSets with the QIDs
	 */
	private static ArrayList<SystemImage> extractPrograms(
		List<String> systems)
	{
		ArrayList<SystemImage> systemImages = new ArrayList<SystemImage>();

		for (int system = 0; system < systems.size(); system++)
		{
			/* Get rid of leading and trailing whitespaces */
			systems.set(system, systems.get(system).trim());

			/* A system without programs shouldn't even be created. */
			if (systems.get(system).length() < 2) { continue; }

			/* Create SystemImages according to the installed programs. */
			HashSet<String> programs = new HashSet<String>(
				Arrays.asList(systems.get(system).split("\\s*,\\s*")));
			
			/* Yet first, delete all programs that definitely aren't
			 * wikidata entity ids. */
			ArrayList<String> invalidPrograms = new ArrayList<String>();
			Iterator<String> iter = programs.iterator();
			while (iter.hasNext())
			{
				String program = iter.next();
				/* wikidata entity ids always start with a 'Q' followed
				 * by some digits */
				if (!program.matches("Q\\d+"))
				{
					invalidPrograms.add(program);
				}
			}
			/* Delete all flawed entity ids. */
			for (int i = 0; i < invalidPrograms.size(); i++)
			{
				programs.remove(invalidPrograms.get(i));
			}
			
			/* Create SystemImages with all possibly valid programs. */
			systemImages.add(new SystemImage(programs));
		}
		return systemImages;
	}

	/* Counts how many systems are able to read/write a file format
	 * of a given pronom.
	 * 
	 * READ		true for reading systems, false for writing systems
	 * 
	 * RETURNS	a HashMap containing the number of reading/writing systems
	 * 			for each occuring pronom
	 * 
	 */
	private HashMap<String, Integer> calculatePronomCommonness(boolean read)
	{
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		if (read) /* readable pronoms */
		{
			/* For each system... */
			for (int system = 0; system < systems.size(); system++)
			{
				Iterator<String> it
				    = systems.get(system).readablePronoms().iterator();
				/* ... consider each pronom... */
				while (it.hasNext())
				{
					String current = it.next();
					Integer value = map.get(current);
					/* ... and increment it's counter. */
					map.put(current, ((value != null) ? value + 1 : 1));
				}
			}
		}
		else /* writabel pronoms */
		{
			/* For each system... */
			for (int system = 0; system < systems.size(); system++)
			{
				Iterator<String> it
				    = systems.get(system).writablePronoms().iterator();
				/* ... consider each pronom... */
				while (it.hasNext())
				{
					String current = it.next();
					Integer value = map.get(current);
					/* ... and increment it's counter. */
					map.put(current, ((value != null) ? value + 1 : 1));
				}			
			}
		}		
		return map;
	}
}
