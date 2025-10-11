import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Date;

public class Git {

    private File git;
    private File objects;
    private File index;
    private File HEAD;
    private String path;
    private static LinkedList commits = new LinkedList<String>();

    public Git() throws IOException {
        git = new File("git");
        path = git.getAbsolutePath();
        objects = new File(path + File.separator + "objects");
        index = new File(path + File.separator + "index");
        HEAD = new File(path + File.separator + "HEAD");
        initialize();
    }

    public Git(String pathName) throws IOException {
        if (!new File(pathName).exists()) {
            System.out.println("The given path doesn't exist. Using default instead.");
            git = new File("git");
        } else {
            git = new File(pathName + File.separator + "git");
        }
        path = git.getAbsolutePath();
        objects = new File(path + File.separator + "objects");
        index = new File(path + File.separator + "index");
        HEAD = new File(path + File.separator + "HEAD");
        initialize();
    }

    public void initialize() throws IOException {
        if ((git.exists() && objects.exists()) && (index.exists() && HEAD.exists())) {
            System.out.println("Git Repository Already Exists");
        } else {
            if (!git.exists()) {
                git.mkdir();
            }
            if (!objects.exists()) {
                objects.mkdir();
            }
            if (!index.exists()) {
                index.createNewFile();
            }
            if (!HEAD.exists()) {
                HEAD.createNewFile();
            }
           System.out.println("Git Repository Created");
        }
    }

    public String getPath() {
        return path;
    }

    public static String genSHA1(String input) {
        try {
            // getInstance() method is called with algorithm SHA-1
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 40 digits long
            while (hashtext.length() < 40) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String genSHA1(File file) throws IOException {
        return genSHA1(Files.readString(file.toPath()));
    }

    public void genBLOB(File file) throws IOException {
            file.createNewFile(); // Joya added this 
            String hash = genSHA1(Files.readString(file.toPath()));
            File obj = new File(path + File.separator + "objects" + File.separator + hash);
            if (!obj.exists()) {
                obj.createNewFile();
            }
            Files.copy(file.toPath(), obj.toPath(), StandardCopyOption.REPLACE_EXISTING);
        
    }

    public void addIndex(File file) throws IOException {
            String input = file.getPath().toString();
            String toAdd = "";
            String contents = Files.readString(file.toPath());
            if (Files.readString(index.toPath()).equals("")) {
                toAdd = genSHA1(contents) + " " + input;
            } else {
                toAdd = "\n" + genSHA1(contents) + " " + input;
            }
            Files.write(index.toPath(), toAdd.getBytes(), StandardOpenOption.APPEND);
        
    }

    public void addFile(File file) throws IOException {
        if (file.exists() == false) { // Joya added this 
            genBLOB(file);
            addIndex(file);
        }
    }

    /*
     * Reads the current state of the index file and makes a tree file for any
     * directory within it
     */
    public String constructTreesFromIndex() throws IOException {
        File workingDirectory = createWorkingDirectory();
        File tempWorkingDirectory = new File("tempWD");

        while (Files.readAllLines(workingDirectory.toPath()).size() > 1) {
            BufferedReader br = new BufferedReader(new FileReader(workingDirectory));
            BufferedWriter bw = new BufferedWriter(new FileWriter(tempWorkingDirectory));
            ArrayList<String> initialEntry = genParsedEntry(br.readLine());
            ArrayList<ArrayList<String>> entriesAboutToBecomeATree = new ArrayList<>();
            entriesAboutToBecomeATree.add(initialEntry);

            while (br.ready()) {
                String currentEntry = br.readLine();
                ArrayList<String> parsedEntry = genParsedEntry(currentEntry);

                if (parsedEntry.size() == initialEntry.size()
                        && initialEntry.subList(2, initialEntry.size() - 1)
                                .equals(parsedEntry.subList(2, initialEntry.size() - 1))) {
                    entriesAboutToBecomeATree.add(parsedEntry);
                }

                else {
                    bw.write(currentEntry + "\n");
                    bw.flush();
                }

            }

            File temp = genTreeFile(entriesAboutToBecomeATree);
            String directoryName = "";
            if (initialEntry.subList(2, initialEntry.size() - 1).isEmpty()) {
                break;
            } else {
                for (String dir : initialEntry.subList(2, initialEntry.size() - 1)) {
                    directoryName += dir + File.separator;
                }
                directoryName = directoryName.substring(0, directoryName.length() - 1);
            }
            bw.write("tree " + genSHA1(temp) + " " + directoryName);

            br.close();
            bw.close();

            FileOutputStream fos = new FileOutputStream(workingDirectory);
            Files.copy(tempWorkingDirectory.toPath(), fos);
            fos.close();
            tempWorkingDirectory.delete();
        }

        alphabetizeLinesByFileName(workingDirectory);
        String hashOfRoot = genSHA1(workingDirectory);
        genBLOB(workingDirectory);
        workingDirectory.delete();
        tempWorkingDirectory.delete();

        return hashOfRoot;

    }

    private void alphabetizeLinesByFileName(File file) throws IOException {
        File temp = new File("tempAlphabetizer");
        ArrayList<ArrayList<String>> entries = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        BufferedWriter bw = new BufferedWriter(new FileWriter(temp));

        // This sorts all of the entries by alphabetical order of the filename
        while (br.ready()) {
            ArrayList<String> parsedEntry = genParsedEntry(br.readLine());

            if (entries.size() == 0) {
                entries.add(parsedEntry);
            } else {
                for (int i = 0; i < entries.size(); i++) {
                    if (parsedEntry.get(parsedEntry.size() - 1).compareTo(entries.get(i).get(entries.get(i).size() - 1)) < 0) {
                        entries.add(i, parsedEntry);
                        break;
                    } else if (i == entries.size() - 1) {
                        entries.add(parsedEntry);
                        break;
                    }
                }
            }
        }

        for (ArrayList<String> entry : entries) {

            // they should all have three parts here.
            // should find a better way to write this tbh
            // a for-loop would be good if it wasnt 1:47 am fri oct 3
            // [type, hash, filename]
            bw.write(entry.get(0) + " " + entry.get(1) + " " + entry.get(entry.size() - 1));

            if (entry != entries.get(entries.size() - 1)) {
                bw.newLine();
            }

            bw.flush();

        }

        FileOutputStream fos = new FileOutputStream(file);
        Files.copy(temp.toPath(), fos);
        fos.close();

        br.close();
        bw.close();
        temp.delete();
    }

    private File genTreeFile(ArrayList<ArrayList<String>> aboutToBecomeATree) throws IOException {
        String contents = "";
        for (ArrayList<String> line : aboutToBecomeATree) {
            contents += line.get(0) + " " + line.get(1) + " " + line.get(line.size() - 1) + "\n";
        }
        contents = contents.substring(0, contents.length() - 1);

        File temp = new File("temp");
        Files.write(temp.toPath(), contents.getBytes());

        alphabetizeLinesByFileName(temp);
        genBLOB(temp);

        String hash = genSHA1(Files.readString(temp.toPath()));

        temp.delete();

        // The path to the tree file in the objects directory
        return new File(path + File.separator + "objects" + File.separator + hash);
    }

    /*
     * Makes the working directory from the index
     * Returns the WD File so that it can be deleted later
     * 
     * Also sorts the working directory in alphabetical order so that a stack can be
     * used to collapse it
     */
    private File createWorkingDirectory() throws IOException {
        // All the entries that will be written to the WD
        ArrayList<ArrayList<String>> entries = new ArrayList<>();

        // The working directory
        File workingDirectory = new File("WD");
        workingDirectory.createNewFile();

        // Writer n reader
        BufferedReader br = new BufferedReader(new FileReader(index));
        BufferedWriter bw = new BufferedWriter(new FileWriter(workingDirectory, true));

        // This sorts all of the entries by length of path
        while (br.ready()) {
            ArrayList<String> parsedEntry = genParsedEntry(br.readLine());

            if (entries.size() == 0) {
                entries.add(parsedEntry);
            } else {
                for (int i = 0; i < entries.size(); i++) {
                    if (parsedEntry.size() > entries.get(i).size()) {
                        entries.add(i, parsedEntry);
                        break;
                    } else if (i == entries.size() - 1) {
                        entries.add(parsedEntry);
                        break;
                    }
                }
            }
        }

        // This writes all of the entries to the WD in the order given above
        // Currently, they look like [[hash, dir1, ..., dir(n), filename], ...]
        for (ArrayList<String> entry : entries) {
            String path = "";

            for (int i = 1; i < entry.size(); i++) {
                path += entry.get(i) + File.separator;
            }

            path = path.substring(0, path.length() - 1);

            bw.write("blob " + entry.get(0) + " " + path);

            if (entry != entries.get(entries.size() -1)) {
                bw.newLine();
            }

        }

        br.close();
        bw.close();
        return workingDirectory;
    }

    /*
     * Converts a line in the index in the form of
     * hash path -> [hash, dir1, dir2, ..., dir(n), filename]
     * 
     * Also converts a line in the WD in the form of
     * type hash path -> [type, hash, dir1, dir2, ..., dir(n), filename]
     * 
     * https://tinyurl.com/59dtacma
     */
    private ArrayList<String> genParsedEntry(String line) {
       // System.out.println(line);
        //System.out.println(Arrays.asList(line.split("[ \\\\]")));
        return new ArrayList<String>(Arrays.asList(line.split("[ \\\\]")));
    }

    // Staging a commit method 
    // returns a string of the commit's hash 
    // to-do: IMPORT DATE CLASS
    public static String commit(String author, String message)  throws IOException {
        Git obj = new Git();
        Date date = new Date();
        String HeadFileLoc = "git/HEAD";
        FileReader reader = new FileReader(HeadFileLoc);
        if (commits.size() == 0) {
            BufferedWriter writer0 = new BufferedWriter(new FileWriter(HeadFileLoc));
            writer0.write("");
            writer0.close();
        }
        if (reader.read() == -1) {
            // step 1: gather info for commit 
            String infoForCommit = "";
            String TreeHash = obj.constructTreesFromIndex();
            infoForCommit = infoForCommit + "tree hash:" + TreeHash + "\n" + "parent:" + "\n" + "author: "
                    + author + "\n" + "date: " + date.getMonth() + "/" + date.getDate() + "/"+ date.getYear() + "\n" + "message: " + message;
            // step 2: create hash of commit 
            String hashOfCommit = genSHA1(infoForCommit);
            commits.add(hashOfCommit);
            // step 3: write hash of commit into Head File  
            BufferedWriter writer = new BufferedWriter(new FileWriter(HeadFileLoc));
            writer.write(hashOfCommit);
            // step 5: creates a new file called hashOfCommit that has commit's content 
            File commitFile = new File("git/objects/" + hashOfCommit);
            commitFile.createNewFile();
            BufferedWriter writer2 = new BufferedWriter(new FileWriter("git/objects/" + hashOfCommit));
            writer2.write(infoForCommit);
            System.out.println("number of elements in linked list: " + commits.size());
            
            
            // step 6
            reader.close();
            writer.close();
            writer2.close();
            return hashOfCommit;
        }
        else {
            // step 1:
            System.out.println(commits.size());
            String HashOfPrevCommit = commits.getLast().toString();
            // step 2: 
            String infoForCommit = "";
            String TreeHash = obj.constructTreesFromIndex();
            infoForCommit = infoForCommit + "tree hash:" + TreeHash + "\n" + "parent: " + HashOfPrevCommit + "\n"
                    + "author: " + author + "\n" + "date: " + date.getMonth() + "/" + date.getDate() + "/" + date.getYear() +  "\n" + "message: " + message;
            // Step 3: 
            String hashOfThisCommit = genSHA1(infoForCommit);
            // step4: 
            commits.add(hashOfThisCommit);
            // step 5: 
            BufferedWriter writer = new BufferedWriter(new FileWriter(HeadFileLoc));
            writer.write(hashOfThisCommit);
             File commitFile = new File("git/objects/" + hashOfThisCommit);
            commitFile.createNewFile();
            BufferedWriter writer2 = new BufferedWriter(new FileWriter("git/objects/" + hashOfThisCommit));
            writer2.write(infoForCommit);
            // step 6: 
            writer.close();
            writer2.close();
            return hashOfThisCommit;
            
        }
    }
    


}