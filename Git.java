import java.io.*;
import java.nio.file.*;

public class Git {

    private File git;
    private File objects;
    private File index;
    private File HEAD;

    public Git() throws IOException {
        git = new File(".aGit");
        objects = new File(git.getAbsolutePath() + File.separator + "objects");
        index = new File(git.getAbsolutePath() + File.separator + "index");
        HEAD = new File(git.getAbsolutePath() + File.separator + "HEAD");
        initialize();
    }

    public Git(String folderName) throws IOException {
        if (folderName.equals("git")) {
            System.out.println("\".git\" folder already exists, using \".aGit\" instead.");
            folderName = "aGit";
        }
        git = new File("."+folderName);
        objects = new File(git.getAbsolutePath() + File.separator + "objects");
        index = new File(git.getAbsolutePath() + File.separator + "index");
        HEAD = new File(git.getAbsolutePath() + File.separator + "HEAD");
        initialize();
    }

    public void initialize() throws IOException {
        if ((git.exists() && objects.exists()) && (index.exists() && HEAD.exists())) {
            System.out.println("Git Repository Already Exists");
        } else {
            if (!git.exists()) {
                git.mkdir();
            }
            Path path = git.toPath();
            Files.setAttribute(path, "dos:hidden", true);
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
        return git.getAbsolutePath();
    }
}