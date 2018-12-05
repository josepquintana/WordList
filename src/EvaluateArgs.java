import java.nio.file.Files;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;

public class EvaluateArgs
{
    public static String[] evaluateArgs(String[] args) throws Exception {
        String[] flags = new String[3]; // [n, /path/to/file, v]
        flags[0] = "0"; flags[1] = ""; flags[2] = "no";

        if (args.length == 1 && (args[0].equals("-h") || args[0].equals("--help"))) { printUsageAndExit(); }

        boolean n = false, f = false, v = false;
        for (int i = 0; i < args.length; i++)
        {
            if (args[i].equals("-n") || args[i].equals("--number")) {
                if(n) printErrorDuplicateFlagAndExit(args[i]);
                if(i + 1 >= args.length) printUsageAndExit();
                if(!isInteger(args[i+1])) printInvalidNumberAndExit(args[i], args[i+1]);
                flags[0] = args[i+1]; ++i;
                n = true;
            }
            else if (args[i].equals("-f") || args[i].equals("--file")) {
                if(f) printErrorDuplicateFlagAndExit(args[i]);
                if(i + 1 >= args.length) printUsageAndExit();
                if(!isValidPath(args[i+1])) printInvalidPathAndExit(args[i+1]);
                flags[1] = args[i+1]; ++i;
                if (!flags[1].endsWith(".txt")) flags[1] = flags[1] + ".txt";
                f = true;
            }
            else if (args[i].equals("-v") || args[i].equals("--verbose")) {
                if(v) printErrorDuplicateFlagAndExit(args[i]);
                flags[2] = "yes";
                v = true;
            }
            else {
                System.out.println("> Error: Unknown specified flag '" + args[i] + "'.");
                System.exit(0);
            }
        }

        if(!n) flags[0] = askNumericInput();
        if(!f) flags[1] = generateAutoPath();

        return flags;
    }

    private static String askNumericInput() {
        Scanner s = new Scanner(System.in);
        String num;
        do {
            System.out.print("Enter the maximum length of the words [e.g. 5]: > ");
            num = s.next();
        } while(!isInteger(num));
        System.out.println("");
        s.close();
        return num;
    }

    private static String generateAutoPath() {

        // create a folder to store the wordlist output if it does not exist
        Path dir = Paths.get("./wordlists").toAbsolutePath().normalize();
        if (!Files.exists(dir)) new File(dir.toString()).mkdirs();

        Random rand = new Random();
        int r = rand.nextInt(9999);
        Path path;
        do {
            String fn = "list" + String.valueOf(r) + ".txt";
            path = Paths.get((dir.toString() + "/" + fn)).toAbsolutePath().normalize();
        } while(Files.exists(path));

        return path.toString();
    }

    private static void printInvalidNumberAndExit(String currFlag, String NaN) {
        System.out.println("> Error: The succeeding element of '" + currFlag + "' should be an integer.");
        System.out.println(">> Error: The input is NaN: '" + NaN + "'.");
        System.exit(0);
    }

    private static void printInvalidPathAndExit(String currFlag) {
        System.out.println("> Error: The file path '" + currFlag + "' is not valid.");
        System.exit(0);
    }

    private static void printErrorDuplicateFlagAndExit(String currFlag) {
        System.out.println("> Error: The flag '" + currFlag + "' is duplicated.");
        System.exit(0);
    }

    private static void printUsageAndExit() throws Exception {
        System.out.println("Usage: java -jar WordList.jar ");
        System.out.println("");
        System.out.println("    -h, --help");
        System.out.println("    -n, --number 'NUM'");
        System.out.println("    -f, --file '/path/to/file'");
        System.out.println("    -v, --verbose");
        System.out.println("");
        System.exit(0);
    }

    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        }
        catch(NumberFormatException e) {
            return false;
        }
        return true;
    }

    private static boolean isValidPath(String s) {
        Path path = Paths.get(s).toAbsolutePath().normalize();
        // check that the parent file exists and is a directory
        return (Files.exists(path.getParent()) && Files.isDirectory(path.getParent()));
    }
}
