import java.io.*;
import java.util.Scanner;

public class MainClass {
    static ReverseLogSearcher logSearcher = null;
    static String pattern = null;
    static PatternMap patternMap = null;
    static File file = null;

    public static void main (String[] args) {
        //file = "C:\\Users\\inc-611\\Documents\\nodejs\\logs.txt"
        //pattern = "Bradley Jaskolski"
        if (args.length != 2) {
            System.out.println("Wrong number of arguments");
            System.out.println("Usage: LogsExplorer [File Path] [String Pattern]");
            return;
        }

        file = new File(args[0]);
        pattern = args[1];

        if (!file.exists()) {
            System.out.println("File doesn't exist!");
            return;
        }

        if (pattern.length() < 3) {
            System.out.println("Pattern should contain at least 3 characters");
            return;
        }

        System.out.println("=======================================");
        System.out.println("Searching file: \"" + file.getName() + "\" for pattern: \"" + pattern + "\"");
        System.out.println("=======================================");
        System.out.println("Use commands: ");
        System.out.println("=======================================");
        System.out.println("UP - To move to the previous occurrence of the pattern");
        System.out.println("DOWN - To move to the next occurrence of the pattern");
        System.out.println("EXIT - Exit the application");
        System.out.println("=======================================");

        logSearcher = new ReverseLogSearcher(file);
        patternMap = logSearcher.search(pattern);

        iterate();
    }

    static void iterate () {
        boolean terminate = false;
        while (!terminate) {
            Scanner sc = new Scanner(System.in);
            System.out.print("\n> ");
            String command = sc.next();
            System.out.println("=======================================");
            switch (command.toLowerCase()) {
                case "up":
                    try {
                        Long pointer = patternMap.prev();
                        System.out.println("=======================================");
                        System.out.println("Found at: " + getPercentage(pointer, file.length()) + "%");
                        System.out.println("=======================================");
                        Utils.displayLine(file, pointer, 2);
                        System.out.println("=======================================");
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("No occurrences found!");
                    }
                    break;
                case "down":
                    try {
                        Long pointer = patternMap.next();
                        System.out.println("=======================================");
                        System.out.println("Found at: " + getPercentage(pointer, file.length()) + "%");
                        System.out.println("=======================================");
                        Utils.displayLine(file, pointer, 2);
                        System.out.println("=======================================");
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("No occurrences found!");
                    }
                    break;
                case "exit":
                    terminate = true;
                    break;
                default:
                    System.out.println("WRONG INPUT");
                    System.out.println("=======================================");
                    System.out.println("Use commands: ");
                    System.out.println("=======================================");
                    System.out.println("UP - To move to the previous occurrence of the pattern");
                    System.out.println("DOWN - To move to the next occurrence of the pattern");
                    System.out.println("EXIT - Exit the application");
                    System.out.println("=======================================");
            }

            double loadStatus = logSearcher.getLoadStatus();
            System.out.println("File searched: " + String.format("%.2f", loadStatus) + "%");
            System.out.println("=======================================");
        }
    }

    static String getPercentage (double pointer, double fileSize) {
        return String.format("%.2f", ((pointer / fileSize) * 100));
    }
}