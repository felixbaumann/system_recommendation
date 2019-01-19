This is a work in progress for recommending a system image for a given disk.

First of all, some system images have to be available.
It is assumed that the wikidata entity IDs of the programs installed on these systems are known.
By accessing wikidata it is possible to fetch the PRONOM IDs of the file formats 
that can be read and / or written by any of the installed programs.

The given disk has to be analyzed by the SIEGFRIED tool yielding an output in JSON which includes
the PRONOM IDs of the file formats occuring on the disk.

This project simply recommends a system that can read all of the formats occuring on the disk.
Unfortunately, such a system may quite often not be available. Instead a system image that can deal
at least with the important formats should be recommended. 
A good recommendation therefore requires to figure out what formats are "important" on the specific disk.
This is estimated by calculating a relevance value for each pronom on the disk. 
Using this, the system that maximizes the sum of relevance values of pronoms it can read is recommended.
