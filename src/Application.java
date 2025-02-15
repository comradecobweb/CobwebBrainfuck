import exceptions.ArgumentException;

public abstract class Application {
    protected String[] arguments;
    protected int position;

    public Application(String[] arguments) {
        this.arguments = arguments;
        this.position = 0;
    }

    protected String getNextArgument() throws ArgumentException {
        position++;
        if (position == arguments.length - 1)
            throw new ArgumentException("Value for argument " + arguments[position - 1] + " not specified!");

        return arguments[position];
    }

    protected int getNextInt() throws ArgumentException {
        int value;
        try {
            value = Integer.parseInt(getNextArgument());
        } catch (NumberFormatException e) {
            throw new ArgumentException("Value for argument " + arguments[position - 1] + " is not a number!");
        }

        return value;
    }

    public abstract void run();
}