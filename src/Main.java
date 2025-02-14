import exceptions.BracketException;
import exceptions.InterpretationException;

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
            Interpreter i = new Interpreter(path, length);
            i.execute();
        }catch (BracketException b) {
            System.err.println("Error: " + b.getMessage() + "\n\tat: ");
            System.exit(1);
        }catch (InterpretationException e){
            System.out.println("Error: " + e.getMessage() + "\n\tat: ");
        }
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