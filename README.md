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

GitWrapper Class: 
Overview: This class runs the functionalities of Git we coding. First, initializing Git itself through the init() method. Second, the add(String filePath) method stages a file by creating a BLOB and writing the hash of the file along with the file path in the index file. Finally, the commit (String author, String message) method creates a file that essentially creates a "snapshot" of your code. The file consists of the hash of the tree root, the hash of the previous (aka parent) commit file (if applicable), the author, the date when the commit was made, and the message the user put into the commit. All this information in the commit file is hashed and this commit file is then reffered to (aka called) by its hash and is stored in the objects folder. When you open this file in the objects folder, you should see all of its content. 
- What functionality was missing? Did you implement any missing functionality?
Originally, the Git class created a tree file for the root, but it did not have a method that returned the hash that this tree root file was called. Working with the person who coded the creation of the tree (Miles) I was able to change the constructTreesFromIndex() to return the hash of the root tree. Having this hash is important when making commits. 
- What bugs did you discover, and which ones did you fix?
Most likely because this computer is a PC and the previous coders for this code have MACs, there were some issues when methods called getLast() or getFirtst(), and with using File.seperator (I believe that is what it is called). With help from Miles, I was able to change all instances of getLast() and getFirst() to get.(0) or something like that so that when I ran the code I didn't get the error that getLast()/getFirst() were methods that didn't exist. For some reason, the other people who coded this had the methods getLast() and getFirst() built into Java, but I don't. Also, we got rid of File.seperator in a method that was erroring and replaced it with line.split['\\\\'] to represent \\ which is the orientation of slashes that PCs use (and Macs don't I think). Also, when testing the GitWrapper class I tried to stage the exact same file twice and saw that that a blob was made for the same file, and that same file was referenced again in the index file when it was staged for the second time. Based on the instructions given, this should not be the case. So, to solve this, in the addFile(File file) method I put an if-statement that checks if the file already exists before making it into a blob and adding it to the index file. There is a comment that says "Joya added this" next to that line.