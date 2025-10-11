What Do You Call a Stupid British Person Who Stages Your Changes?

READ ME:

Git:
Two options for creating the repository, if no path given, or if invalid path, git will be created in current folder.
File.separator is used for paths in order to ensure code works on multiple operating systems
Hash function explains what it's doing in the method (I took this from online I cannot explain it to you sorry)
constructTreesFromIndex does what it suggests; it constructs the tree files from what is in the index. It calls other methods:
- createWorkingDirectory() makes a WD
- genTreeFile() makes a singular tree file given an arrayList of arrayList of strings which is when the path is broken up
- genParsedEntry() turns a String into an ArrayList that separates an entry in the index/WD 
- **The overall strategy of constructTreesFromIndex is to repeatedly iterate over the WD and grab all the files which will become a tree. Repeat this process until the WD has just one file left in it (the root) and then finish**

GitTester:
Method to verify if all required repo files were initialized (.repoExists)
Method that iterates through folder and removes all files, including anything potentially added later in project process (.cleanup)
Multiple tests to see if different cases produce errors. Details documented in tester.
Large test for creating BLOB files and indexes. Details in tester, and methods implemented to make any future testing easier.
