import exceptions.BracketException;
import exceptions.CompilationException;
import exceptions.ResourceException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Compiler {
    private String code;
    private final String tab;
    private final String newLine;
    private int inBracket = 0;
    private static final String templateName = "template";
    private static final String templateFileName = templateName + ".c";
    private String output;

    public Compiler(String path) throws BracketException, CompilationException, OutOfMemoryError {
        try {
            code = Files.readString(Paths.get(path));
        } catch (IOException | SecurityException e) {
            throw new CompilationException("Cannot read file: " + path);
        }
        if (!Utils.areBracketsValid(code)) throw new BracketException(code);
        code = Utils.simplify(code);
        tab = "\t";
        newLine = "\n";
        output = "";
    }

    public Compiler(String path, String tab, String newLine) throws BracketException, CompilationException,
            OutOfMemoryError {
        try {
            code = Files.readString(Paths.get(path));
        } catch (IOException | SecurityException e) {
            throw new CompilationException("Cannot read file: " + path);
        }
        if (!Utils.areBracketsValid(code)) throw new BracketException(code);
        code = Utils.simplify(code);
        this.tab = tab;
        this.newLine = newLine;
        output = "";
    }

    private String readTemplate() throws ResourceException, OutOfMemoryError {
        StringBuilder template = new StringBuilder();
        String line;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(templateFileName)));
            while ((line = reader.readLine()) != null) {
                template.append(line).append(newLine);
            }
        } catch (IOException | SecurityException | NullPointerException e) {
            throw new ResourceException("Cannot open " + templateFileName + " file!");
        }

        return template.toString();
    }

    public void compile() throws ResourceException {
        String rawTemplate = readTemplate();
        if (!tab.equals("\t"))
            rawTemplate = rawTemplate.replace("\t", tab);
        String[] template = rawTemplate.split("//here");
        if (template.length != 2) throw new ResourceException("Invalid " + templateFileName + " file!");
        output = template[0] + getBody() + template[1];
    }

    public void save(String path) throws ResourceException, CompilationException {
        if (!output.isEmpty()) compile();
        if (path.isEmpty()) throw new CompilationException("Output path is empty!");
        if (!path.endsWith(".c")) path += ".c";

        File file = new File(path);

        try {
            if (!file.exists()) file.createNewFile();
        } catch (IOException | SecurityException e) {
            throw new CompilationException("Cannot create file: " + path);
        }

        String name = path.substring(0, path.lastIndexOf(".c"));
        if (path.contains("/")) name = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf(".c"));
        if (path.contains("\\")) name = path.substring(path.lastIndexOf("\\") + 1, path.lastIndexOf(".c"));
        output = output.replace(templateName, name);

        try {
            FileWriter writer = new FileWriter(path);
            writer.write(output);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new CompilationException("Cannot write to file: " + path);
        }
    }

    public void println() throws ResourceException {
        if (!output.isEmpty()) compile();
        System.out.println(output);
    }

    private String addLine(String text, String line) {
        StringBuilder result = new StringBuilder(text + newLine + tab);
        if (inBracket != 0)
            result.append(String.valueOf(tab).repeat(inBracket));
        result.append(line);

        return result.toString();
    }

    private String getBody() {
        String body = "";
        String plus = "plus();";
        String minus = "minus();";
        String leftArrow = "left_arrow();";
        String rightArrow = "right_arrow();";
        String dot = "dot();";
        String comma = "comma();";
        String leftBracket = "while(memory[memory_pointer]) {";
        String rightBracket = "};";

        for (char c : code.toCharArray())
            switch (c) {
                case '+':
                    body = addLine(body, plus);
                    break;
                case '-':
                    body = addLine(body, minus);
                    break;
                case '<':
                    body = addLine(body, leftArrow);
                    break;
                case '>':
                    body = addLine(body, rightArrow);
                    break;
                case '.':
                    body = addLine(body, dot);
                    break;
                case ',':
                    body = addLine(body, comma);
                    break;
                case '[':
                    body = addLine(body, leftBracket);
                    inBracket++;
                    break;
                case ']':
                    inBracket--;
                    body = addLine(body, rightBracket);
                    break;
            }
        inBracket = 0;
        body = addLine(body, "");

        return body;
    }
}