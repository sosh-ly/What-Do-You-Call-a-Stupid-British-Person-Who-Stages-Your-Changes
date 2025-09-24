import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    public String genSHA1(String input) {
        try {
            // getInstance() method is called with algorithm SHA-1
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 40 digits long
            while (hashtext.length() < 40) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } 
    }
}