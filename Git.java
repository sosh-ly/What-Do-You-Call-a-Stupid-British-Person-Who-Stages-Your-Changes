import java.io.*;

public class Git {

    private File git;
    private File objects;
    private File index;
    private File HEAD;

    public Git() throws IOException {
        git = new File("git");
        objects = new File(git.getAbsolutePath() + File.separator + "objects");
        index = new File(git.getAbsolutePath() + File.separator + "index");
        HEAD = new File(git.getAbsolutePath() + File.separator + "HEAD");
        initialize();
    }

    public Git(String pathName) throws IOException {
        if (!new File(pathName).exists()) {
            System.out.println("The given path doesn't exist. Using default instead.");
            git = new File("git");
        } else {
            git = new File(pathName + File.separator + "git");
        }
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