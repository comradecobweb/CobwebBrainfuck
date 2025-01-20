import java.io.IOException;
import java.util.Stack;

public class Interpreter {
    private final char[] memory;
    private final char[] instructions;
    private int index;
    private final Stack<Integer> leftBracketPositions = new Stack<>();

    public Interpreter(int memoryLength, char[] instructions) {
        if (memoryLength < 2) memoryLength = 1000;
        memory = new char[memoryLength];
        index = memoryLength / 2;
        this.instructions = instructions;
    }

    private void comma() throws IOException {
        memory[index] = (char) System.in.read();
    }

    private void dot() {
        System.out.print(memory[index]);
    }

    private void rightArrow() {
        if (index == memory.length - 1)
            index = 0;
        else
            index++;
    }

    private void leftArrow() {
        if (index == 0)
            index = memory.length - 1;
        else
            index--;
    }

    private void plus() {
        if (memory[index] == Character.MAX_VALUE)
            memory[index] = 0;
        else
            memory[index]++;
    }

    private void minus() {
        if (memory[index] == Character.MIN_VALUE)
            memory[index] = Character.MAX_VALUE;
        else
            memory[index]--;
    }

    private int getNextRightBracket(int position) throws BracketException {
        for (int i = position; i < instructions.length; i++)
            if (instructions[i] == ']') return i;
        throw new BracketException("No right bracket found to end loop!", instructions.length);
    }

    public void execute() throws BracketException, IOException {
        for (int i = 0; i < instructions.length; i++)
            switch (instructions[i]) {
                case '+':
                    plus();
                    break;
                case '-':
                    minus();
                    break;
                case '.':
                    dot();
                    break;
                case ',':
                    comma();
                    break;
                case '>':
                    rightArrow();
                    break;
                case '<':
                    leftArrow();
                    break;
                case '[':
                    if (memory[index] == 0)
                        i = getNextRightBracket(i);
                    else
                        leftBracketPositions.push(i - 1); // - 1 because of i++
                    break;
                case ']':
                    if (leftBracketPositions.isEmpty())
                        throw new BracketException("No left bracket found to end loop!", i);
                    i = leftBracketPositions.pop();
                    break;
            }
    }
}