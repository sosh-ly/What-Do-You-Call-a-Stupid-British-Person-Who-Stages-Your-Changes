import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Git {

    private File git;
    private File objects;
    private File index;
    private File HEAD;
    private String path;

    public Git() throws IOException {
        git = new File("git");
        path = git.getAbsolutePath();
        objects = new File(path + File.separator + "objects");
        index = new File(path + File.separator + "index");
        HEAD = new File(path + File.separator + "HEAD");
        initialize();
    }

    public Git(String pathName) throws IOException {
        if (!new File(pathName).exists()) {
            System.out.println("The given path doesn't exist. Using default instead.");
            git = new File("git");
        } else {
            git = new File(pathName + File.separator + "git");
        }
        path = git.getAbsolutePath();
        objects = new File(path + File.separator + "objects");
        index = new File(path + File.separator + "index");
        HEAD = new File(path + File.separator + "HEAD");
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
        return path;
    }

    public static String genSHA1(String input) {
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

    public static String genSHA1(File file) throws IOException {
        return genSHA1(Files.readString(file.toPath()));
    }

    public void genBLOB(File file) throws IOException {
        String hash = genSHA1(Files.readString(file.toPath()));
        File obj = new File(path + File.separator + "objects" + File.separator + hash);
        if (!obj.exists()) {
            obj.createNewFile();
        }
        Files.copy(file.toPath(), obj.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public void addIndex(File file) throws IOException {
        String input = file.getPath().toString();
        String toAdd = "";
        String contents = Files.readString(file.toPath());
        if (Files.readString(index.toPath()).equals("")) {
            toAdd = genSHA1(contents) + " " + input;
        } else {
            toAdd = "\n" + genSHA1(contents) + " " + input;
        }
        Files.write(index.toPath(), toAdd.getBytes(), StandardOpenOption.APPEND);
    }

    public void addFile(File file) throws IOException {
        genBLOB(file);
        addIndex(file);
    }
}