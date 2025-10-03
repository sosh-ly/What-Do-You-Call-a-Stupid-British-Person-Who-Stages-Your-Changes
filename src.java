import java.io.File;
import java.io.IOException;

public class src {
    public static void main(String[] args) throws IOException {
        Git cleaner = new Git();
        GitTester.cleanup(cleaner);
        Git testGit = new Git();

        testGit.addFile(new File("testTextFiles/a.txt"));
        testGit.addFile(new File("testTextFiles/b.txt"));
        testGit.constructTreesFromIndex();
        
    }
}
