import exceptions.ArgumentException;
import exceptions.BracketException;
import exceptions.InterpretationException;

public class Main extends Application{
    public static void main(String[] args) {
        try {
            new Main(args).run();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    Main(String[] args) {
        super(args);
    }

    @Override
    public void run() throws ArgumentException {
        String path = "";
        int length = 0;
        for (; position < arguments.length; position++)
             switch (arguments[position]) {
                case "memory", "-memory", "--memory", "mem", "-mem", "--mem":
                    length = getNextInt();
                    break;
                case "path", "-path", "--path":
                    path = getNextArgument();
                    break;
                default:
                    System.exit(1);
            }
        run(path, length);
    }

    private static void run(String path, int length) {
        if (path.isEmpty()) {
            System.err.println("Error: Input file is not specified!");
            System.exit(1);
        }
        try {
            Interpreter i = new Interpreter(path, length);
            i.execute();
        }catch (BracketException | InterpretationException b) {
            System.err.println("Error: " + b.getMessage());
            System.exit(1);
        }
    }
}