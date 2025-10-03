import java.io.File;
import java.io.IOException;

public class src {
    public static void main(String[] args) throws IOException {
        Git testGit = new Git();

        // GitTester.cleanup(testGit);

        testGit.addFile(new File("testTextFiles/a.txt"));
        testGit.constructTreesFromIndex();
        

        
    }
}
