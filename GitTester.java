import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class GitTester {

    public static void main(String args[]) throws IOException {

        /* Your tester code goes here */
        GitWrapper gw = new GitWrapper();
        gw.init();
        gw.add("git/hello.txt");
        gw.add("testFileSystem/JustAnotherFile.txt");
        gw.add("git/hello.txt"); // should do nothing 
        gw.commit("John Doe", "Initial commit");
        gw.commit("Tristen", "This is Tristen's message");
        //gw.checkout("1234567890");

    }
    
}
