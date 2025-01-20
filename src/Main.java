import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String path = "";
        int length = 0;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "memory", "-memory", "--memory", "mem", "-mem", "--mem":
                    length = getNextInt(args, i);
                    if (i != args.length - 1) i++;
                    break;
                case "path", "-path", "--path":
                    path = getNextArg(args, i);
                    if (i != args.length - 1) i++;
                    break;
                default:
                    System.err.println("Error: Unknown argument: " + args[i] + "\n");
                    System.exit(1);
            }
        }
        run(path, length);
    }

    public static void run(String path, int length) {
        if (path.isEmpty()) {
            System.err.println("Error: Input file is not specified!");
            System.exit(1);
        }
        try {
            String text = read(path);
            Interpreter i = new Interpreter(length, text.toCharArray());
            try {
                i.execute();
            } catch (IOException io) {
                System.err.println("Error: Cannot get user input!");
                System.exit(1);
            } catch (BracketException b) {
                System.err.println("Error: " + b.getMessage() + "\n\tat: " + b.getPosition());
                System.exit(1);
            }
        } catch (IOException e) {
            System.err.println("Error: Cannot read the file: " + path);
            System.exit(1);
        }
    }

    private static String read(String path) throws IOException {
        File file = new File(path);
        if (!file.canRead()) throw new IOException();
        Scanner scanner = new Scanner(file);

        return scanner.next();
    }

    private static String getNextArg(String[] args, int i) {
        if (i == args.length - 1) {
            System.err.println("Error: Value for argument " + args[i] + " not specified!");
            System.exit(1);
        }

        return args[i + 1];
    }

    private static int getNextInt(String[] args, int i) {
        int value = 0;
        try {
            value = Integer.parseInt(getNextArg(args, i));
        } catch (NumberFormatException e) {
            System.err.println("Error: Value for argument " + args[i] + " is not a number!");
            System.exit(1);
        }
        if (value < 2) {
            System.err.println("Error: Value for argument " + args[i] + " is too low!");
            System.exit(1);
        }

        return -1;
    }
}