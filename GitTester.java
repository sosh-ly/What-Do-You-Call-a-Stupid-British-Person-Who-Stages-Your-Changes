import java.io.File;
import java.io.IOException;

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

        System.out.println(testGit5.genSHA1("hello"));

        testGit5.genBLOB(new File("testFile.txt"));

    }

    public static boolean repoExists(Git gitToTest) {
        File git = new File(gitToTest.getPath());
        String path = git.getAbsolutePath();
        File objects = new File(path + File.separator + "objects");
        File index = new File(path + File.separator + "index");
        File HEAD = new File(path + File.separator + "HEAD");
        return ((git.isDirectory() && objects.isDirectory()) && (index.isFile() && HEAD.isFile()));
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
