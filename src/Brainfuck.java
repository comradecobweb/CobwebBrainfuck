import exceptions.*;

public class Brainfuck extends Application {
    public static void main(String[] args) {
        try {
            new Brainfuck(args).run();
        } catch (ArgumentException | InterpretationException | BracketException | CompilationException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (OutOfMemoryError e) {
            System.err.println("Out of memory!");
            System.out.println("To fix it run this program by:");
            System.out.println("java -jar CobwebBrainfuck.jar -Xmx?G ...");
            System.out.println("Replace the ? with the number of gigabytes of memory that the program can use (default 2).");
            System.exit(1);
        } catch (ResourceException e) {
            System.err.println(e.getMessage());
            System.out.println("The application is corrupted. Download it from: https://github.com/comradecobweb/CobwebBrainfuck");
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            System.exit(1);
        }
    }

    Brainfuck(String[] args) {
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
                case "h", "-h", "--h", "help", "-help", "--help":
                    System.out.println("Cobweb Brainfuck");
                    System.out.println("For more info see: https://github.com/comradecobweb/CobwebBrainfuck");
                    System.exit(0);
                    break;
                default:
                    throw new ArgumentException("Unknown argument: " + arguments[position]);
            }
        switch (mode) {
            case interpretation -> new Interpreter(source, length).execute();
            case compilation -> {
                Compiler compiler;
                String extension = Compiler.getExtension(destination);
                switch (extension) {
                    case ".c" -> compiler = new C(source, "    ", "\n");
                    default -> throw new ArgumentException("Unsupported file extension: " + extension);
                }
                compiler.compile();
                compiler.save(destination);
            }
            case undefined -> throw new ArgumentException("Mode is not defined!");
        }
    }
}