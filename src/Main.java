
import java.nio.file.Paths;

public class Main
{
    private static int n = 0;
    private static String pathToFile;
    private static boolean v = false;
    public static int howManyValues = 26;

    public static void main(String[] args) throws Exception
    {
        printHelloMessage();

        String[] flags = new String[3];
        flags = EvaluateArgs.evaluateArgs(args);
        int n = Integer.parseInt(flags[0]);
        String pathToFile = flags[1];
        boolean v = false;
        if(flags[2].equals("yes")) v = true;

        GenerateWordList.generateWordList(n, pathToFile, v, 1000);    // last param is the interval for the Timer to print the status

        System.out.println("Processing... 100.00%\t################################################## !!!");
        System.out.println("\nOutput written to: " + Paths.get(pathToFile).toAbsolutePath().normalize().toString());
    }

    private static void printHelloMessage() {
        System.out.println("");
        System.out.println("####################################################################################");
        System.out.println("############################## GENERATE WORD LIST ##################################");
        System.out.println("####################################################################################");
        System.out.println("");
    }
}
