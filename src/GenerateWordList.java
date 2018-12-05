import java.io.*;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;

public class GenerateWordList
{
    private static char[] word;
    private static int n;
    private static boolean v;
    private static Timer printStatusTimer;
    private static boolean stopTimer = false;
    private static long counter;
    private static long n_words;

    public static void generateWordList(int number, String pathToFile, boolean verbose, int timerInterval) throws IOException {
        WriteToFile.open(Paths.get(pathToFile));
        n = number;
        v = verbose;
        counter = 0;
        n_words = getNumberOfWords();
        if (!v) iniTimer(timerInterval);
        generateWL();   // generate all possible combinations
        stopTimer = true;
        WriteToFile.close();
    }

    private static void generateWL() throws IOException {
        // each call to recursive method "generate(int, int, char[])" writes to the
        // output file all the possible combinations of characters with length 'n'
        for (int i = 1; i <= n; i++) {
            word = new char[i];
            generate(i,0, word);
        }
    }

    private static void generate(int limit, int k, char[] word) throws IOException {
        if (limit == k) {
            WriteToFile.write(new String(word));
            if(v) System.out.println(new String(word)); // bottleneck!
            ++counter;
            return;
        }
        for (word[k] = 'a'; word[k] <= 'z'; ++word[k]) {
            generate(limit, k + 1, word);
        }
    }

    // method never used!
    // maybe JavaOutOfIndexArray bc of ASCII '{' (97)
    private static int getCurrentWordNumber(char[] curr) {
        int h = Main.howManyValues;
        int id = 0;
        for (int i = n-1; i >= 0; i--) {
            int c_value = (int) (curr[i]-96);
            double pwr = Math.pow(h, i);
            id = id + (c_value * (int) pwr);
        }
        return id;
    }

    private static int getNumberOfWords() {
        int h = Main.howManyValues;
        double pwr = Math.pow(h, n-1);
        return (((int) pwr) * h+1);
    }

    private static void iniTimer(int timerInterval) {
        printStatusTimer = new Timer();
        printStatusTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                double progress = ((float) counter/(float) n_words)*100;
                if (!stopTimer) printProgress(progress);
                else printStatusTimer.cancel();
            }
        }, timerInterval, timerInterval);
    }

    private static void printProgress(double progess) {
        if (progess >= 100) progess = 99.99;
        String p = String.format("%.2f", progess);
        System.out.print("Processing... " + p + "% \t");
        if      (progess < 10) System.out.println("##### ...");
        else if (progess < 20) System.out.println("########## ...");
        else if (progess < 30) System.out.println("############### ...");
        else if (progess < 40) System.out.println("#################### ...");
        else if (progess < 50) System.out.println("######################### ...");
        else if (progess < 60) System.out.println("############################## ...");
        else if (progess < 70) System.out.println("################################### ...");
        else if (progess < 80) System.out.println("######################################## ...");
        else if (progess < 90) System.out.println("############################################# ...");
        else                   System.out.println("################################################## ...");
    }

}
