import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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

        testGit5.genBLOB(new File("testFile.txt")); // make BLOB file

        System.out.println(BLOBExists(testGit5, new File("testFile.txt"))); // test if BLOB file was made in correct
                                                                            // place with correct info

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

    public static boolean BLOBExists(Git gitToTest, File input) throws IOException {
        String path = gitToTest.getPath().toString() + File.separator + "objects" + File.separator + Git.genSHA1(input);
        File BLOB = new File(path);
        return BLOB.exists() && Files.readString(BLOB.toPath()).equals(Files.readString(input.toPath()));
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
}
