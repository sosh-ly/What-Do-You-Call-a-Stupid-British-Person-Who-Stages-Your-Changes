import java.io.*;
import java.nio.file.Files;

public class Git {

    private File git;
    private File objects;
    private File index;
    private File HEAD;

    public Git() throws IOException {
        git = new File(".aGit");
        objects = new File("/.aGit/objects");
        index = new File("/.aGit/index");
        HEAD = new File("/.aGit/HEAD");
        initialize();
    }

    public void initialize() throws IOException {
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
        if ((git.exists()&&objects.exists())&&(index.exists()&&HEAD.exists())) {
            System.out.println("Git Repository Already Exists");
        } else {
            System.out.println("Git Repository Created");
        }
    }
}