import java.io.File;
import java.io.IOException;

public class TreeTester {
    public static void main(String[] args) throws IOException {
        Git cleaner = new Git();
        GitTester.cleanup(cleaner);
        Git testGit = new Git();

        testGit.addFile(new File("GitTester.java"));
        testGit.addFile(new File("testFileSystem/a.txt"));
        testGit.addFile(new File("testFileSystem/b.txt"));
        testGit.addFile(new File("testFileSystem/d/d.txt"));
        testGit.addFile(new File("testFileSystem/c/c1.txt"));
        testGit.addFile(new File("testFileSystem/c/c2.txt"));
        testGit.addFile(new File("testFileSystem/d/e.txt"));
        testGit.addFile(new File("testFileSystem/d/f.txt"));
        testGit.addFile(new File(".vscode/settings.json"));
        testGit.addFile(new File("testFileSystem/d/g.txt"));
        testGit.addFile(new File("testFileSystem/d/h.txt"));
        testGit.addFile(new File("testFileSystem/d/e/bye.txt"));
        testGit.addFile(new File("testFileSystem/d/e/hello.txt"));
        testGit.addFile(new File("TreeTester.java"));
        testGit.addFile(new File("README.md"));
        testGit.addFile(new File("SECURITY.md"));

        testGit.constructTreesFromIndex();

        System.out.println(GitTester.BLOBSExist(testGit, new File("testFileSystem/d/e")));
        
    }
}
