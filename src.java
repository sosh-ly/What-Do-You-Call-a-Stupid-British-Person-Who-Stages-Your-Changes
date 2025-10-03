import java.io.File;
import java.io.IOException;

public class src {
    public static void main(String[] args) throws IOException {
        Git cleaner = new Git();
        GitTester.cleanup(cleaner);
        Git testGit = new Git();

        testGit.addFile(new File("testFileSystem/a.txt"));
        testGit.addFile(new File("testFileSystem/b.txt"));
        testGit.addFile(new File("testFileSystem/c/c1.txt"));
        testGit.addFile(new File("testFileSystem/c/c2.txt"));
        testGit.addFile(new File("testFileSystem/d/d.txt"));
        testGit.addFile(new File("testFileSystem/d/e.txt"));
        testGit.addFile(new File("testFileSystem/d/f.txt"));
        testGit.addFile(new File("testFileSystem/d/g.txt"));
        testGit.addFile(new File("testFileSystem/d/h.txt"));
        testGit.addFile(new File("testFileSystem/d/e/bye.txt"));
        testGit.addFile(new File("testFileSystem/d/e/hello.txt"));

        testGit.constructTreesFromIndex();

    }
}
