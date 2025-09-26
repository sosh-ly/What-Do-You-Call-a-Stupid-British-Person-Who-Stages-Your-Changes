import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class GitTester {
    public static void main(String[] args) throws IOException {
        Git testGit = new Git(); // create new Git to test
        System.out.println(repoExists(testGit));
        cleanup(testGit); // remove Git
        System.out.println(repoExists(testGit)); // see if it still exists
        cleanup(testGit); // attempt to clean up non-existent Git

        Git testGit2 = new Git(); // create new Git to test
        System.out.println(repoExists(testGit2));
        Git testGit3 = new Git(); // attempt to create duplicate Git
        System.out.println(repoExists(testGit3)); // see if duplicate Git exists

        cleanup(testGit2);
        cleanup(testGit3);

        Git testGit4 = new Git("notrealfolder"); // try to create git in non-existent path
        System.out.println(repoExists(testGit4));

        cleanup(testGit4);

        Git testGit5 = new Git("testFolder"); // try to create git in testFolder
        System.out.println(repoExists(testGit5));

        System.out.println(Git.genSHA1("hello")); // test SHA1 function

        File fileFolder = new File("testFiles"); // make folder for test files
        fileFolder.mkdir();

        testFile("testFiles" + File.separator + "testFile1.txt", "sample text woah!!"); // make sample files
        testFile("testFiles" + File.separator + "testFile2.txt",
                "heres some other text, and this time im going to make it long. lets make sure nothing gets weird if i do that");
        testFile("testFiles" + File.separator + "testFile3.txt", ""); // make an empty file and see if anything breaks

        testGit5.addFile(new File("testFiles" + File.separator + "testFile1.txt")); // make BLOB file + index
        testGit5.addFile(new File("testFiles" + File.separator + "testFile2.txt"));
        testGit5.addFile(new File("testFiles" + File.separator + "testFile3.txt"));

        System.out.println(BLOBSExist(testGit5, new File("testFiles"))); // test if BLOB files were made correctly

        System.out.println(checkIndicies(testGit5, fileFolder));

        cleanupGit(testGit5); // clean up BLOB files in objects and index
        cleanup(fileFolder); // delete folder of test files
        
        cleanup(testGit5);

    }

    public static boolean repoExists(Git gitToTest) {
        File git = new File(gitToTest.getPath());
        String path = git.getAbsolutePath();
        File objects = new File(path + File.separator + "objects");
        File index = new File(path + File.separator + "index");
        File HEAD = new File(path + File.separator + "HEAD");
        return ((git.isDirectory() && objects.isDirectory()) && (index.isFile() && HEAD.isFile()));
    }

    public static boolean BLOBSExist(Git gitToTest, File folder) throws IOException {
        for (File f : folder.listFiles()) {
            if (!BLOBExists(gitToTest, f)) {
                return false;
            }
        }
        return true;
    }

    public static boolean BLOBExists(Git gitToTest, File input) throws IOException {
        String path = gitToTest.getPath().toString() + File.separator + "objects" + File.separator + Git.genSHA1(input);
        File BLOB = new File(path);
        return BLOB.exists() && Files.readString(BLOB.toPath()).equals(Files.readString(input.toPath()));
    }

    public static boolean checkIndicies(Git gitToTest, File folder) throws IOException {
        for (File f : folder.listFiles()) {
            if (!checkIndex(gitToTest, f)) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkIndex(Git gitToTest, File file) throws IOException {
        String path = gitToTest.getPath().toString() + File.separator + "index";
        Boolean status = false;
        List<String> lines = Files.readAllLines(Paths.get(path));
        for (String line : lines) {
            if ((Git.genSHA1(Files.readString(file.toPath())) + " " + file.getName()).equals(line)) {
                status = true;
                break;
            } else {
                status = false;
            }
        }
        if (!status) {
            System.out.println("No index found for " + file.getName());
            return false;
        }
        return true;
    }

    public static void cleanup(Git gitToClean) {
        cleanup(new File(gitToClean.getPath()));
    }

    public static void cleanup(File file) {
        File[] insides = file.listFiles();
        if (insides != null) {
            for (File f : insides) {
                cleanup(f);
            }
        }
        file.delete();
    }

    public static void testFile(String name, String contents) throws IOException {
        File file = new File(name);
        file.createNewFile();
        Files.write(file.toPath(), contents.getBytes());
    }

    public static void cleanupGit(Git gitToClean) throws IOException {
        for (File blob : (new File(gitToClean.getPath() + File.separator + "objects")).listFiles()) {
            blob.delete();
        }
        File index = new File(gitToClean.getPath() + File.separator + "index");
        index.delete();
        index.createNewFile();

    }
}
