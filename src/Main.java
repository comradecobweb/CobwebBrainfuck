import exceptions.*;

public class Main extends Application {
    public static void main(String[] args) {
        try {
            new Main(args).run();
        } catch (ArgumentException | InterpretationException | BracketException | CompilationException e) {
            System.err.println(e.getMessage());
        } catch (OutOfMemoryError e) {
            System.err.println("Out of memory!");
            System.out.println("To fix it run this program by:");
            System.out.println("java -jar CobwebBrainfuck.jar -Xmx?G ...");
            System.out.println("Replace the ? with the number of gigabytes of memory that the program can use (default 2).");
        } catch (ResourceException e) {
            System.err.println(e.getMessage());
            System.out.println("The application is corrupted. Download it from: https://github.com/comradecobweb/CobwebBrainfuck");
        }
    }

    Main(String[] args) {
        super(args);
    }

    @Override
    public void run() throws ArgumentException, BracketException, InterpretationException, CompilationException,
            ResourceException {
        String source = "";
        String destination = "";
        int length = 0;
        Modes mode = Modes.undefined;
        for (; position < arguments.length; position++)
            switch (arguments[position]) {
                case "memory", "-memory", "--memory", "mem", "-mem", "--mem":
                    if (mode == Modes.interpretation) length = getNextInt();
                    else throw new ArgumentException("Memory can only be adjusted in interpretation mode!");
                    break;
                case "source", "-source", "--source":
                    source = getNextArgument();
                    break;
                case "destination", "-destination", "--destination":
                    if (mode == Modes.compilation)
                        destination = getNextArgument();
                    else
                        throw new ArgumentException("You can only set destination in compile mode!");
                    break;
                case "i", "-i", "--i", "interpret", "-interpret", "--interpret":
                    mode = Modes.interpretation;
                    break;
                case "c", "-c", "--c", "compile", "-compile", "--compile":
                    mode = Modes.compilation;
                    break;
                default:
                    throw new ArgumentException("Unknown argument: " + arguments[position]);
            }

        switch (mode) {
            case interpretation -> new Interpreter(source, length).execute();
            case compilation -> {
                Compiler c = new Compiler(source);
                c.compile();
                c.save(destination);
            }
            case undefined -> throw new ArgumentException("Mode is not defined!");
        }
    }
}