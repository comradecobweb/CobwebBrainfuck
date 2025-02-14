import exceptions.BracketException;
import exceptions.InterpretationException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Stack;

public class Interpreter {
    private final char[] memory;
    private final char[] instructions;
    private int index;
    private final Stack<Integer> leftBracketPositions = new Stack<>();

    public Interpreter(String path, int memoryLength) throws BracketException, InterpretationException {
        String code;
        try {
            code = Files.readString(Paths.get(path));
        } catch (IOException e) {
            throw new InterpretationException("Cannot read file: " + path);
        }
        if (!Utils.areBracketsValid(code)) throw new BracketException(code);
        if (memoryLength < 2) memoryLength = 30000;
        memory = new char[memoryLength];
        index = 0;
        instructions = Utils.simplify(code).toCharArray();
    }

    private void comma() throws InterpretationException {
        try {
            memory[index] = (char) System.in.read();
        } catch (IOException e) {
            throw new InterpretationException("Cannot get input from user!");
        }
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
        if (memory[index] == 0)
            memory[index] = Character.MAX_VALUE;
        else
            memory[index]--;
    }

    private int getNextRightBracket(int position) {
        for (int i = position; i < instructions.length; i++)
            if (instructions[i] == ']') return i;
        return -1;
    }

    public void execute() throws BracketException, InterpretationException {
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
                    i = leftBracketPositions.pop();
                    break;
            }
    }
}