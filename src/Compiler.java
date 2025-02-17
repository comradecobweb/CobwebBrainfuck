import exceptions.ArgumentException;
import exceptions.BracketException;
import exceptions.CompilationException;
import exceptions.ResourceException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class Compiler {
    private String code;
    private final String tab;
    private final String newLine;
    private int inBracket = 0;
    private static final String templateName = "template";
    private final String templateFileName;
    private String output;
    private final Instructions instructions;
    private final String extension;

    public Compiler(String path, String tab, String newLine, String extension, Instructions instructions) throws BracketException, CompilationException,
            ArgumentException, OutOfMemoryError {
        if (path.isEmpty()) throw new ArgumentException("Input path is not defined!");
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
        templateFileName = templateName + extension;
        this.extension = extension;
        this.instructions = instructions;
    }

    public static String getExtension(String path) throws ArgumentException {
        if (path.isEmpty()) throw new ArgumentException("Output path is not defined!");
        if (!path.contains(".")) throw new ArgumentException("Output path is not defined!");
        String extension = path.substring(path.lastIndexOf("."));
        if (extension.isEmpty() || extension.equals("."))
            throw new ArgumentException("Extension of file " + path + " is not defined!");
        return extension;
    }

    private String readTemplate() throws ResourceException, OutOfMemoryError {
        StringBuilder template = new StringBuilder();
        String line;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream(templateFileName))));
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

    public void save(String path) throws ResourceException, ArgumentException, CompilationException {
        if (!output.isEmpty()) compile();
        if (path.isEmpty()) throw new ArgumentException("Output path is not defined!");
        if (!path.endsWith(extension)) path += extension;

        File file = new File(path);

        try {
            if (!file.exists()) file.createNewFile();
        } catch (IOException | SecurityException e) {
            throw new CompilationException("Cannot create file: " + path);
        }

        String name = path.substring(0, path.lastIndexOf(extension));
        if (path.contains("/")) name = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf(extension));
        if (path.contains("\\")) name = path.substring(path.lastIndexOf("\\") + 1, path.lastIndexOf(extension));
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

    private String addLine(String rawBody, String line) {
        StringBuilder result = new StringBuilder(rawBody + newLine + tab);
        if (inBracket != 0)
            result.append(String.valueOf(tab).repeat(inBracket));
        result.append(line);

        return result.toString();
    }

    private String getBody() {
        String body = "";
        for (char c : code.toCharArray())
            switch (c) {
                case '+' -> body = addLine(body, instructions.getPlus());

                case '-' -> body = addLine(body, instructions.getMinus());

                case '<' -> body = addLine(body, instructions.getLeftArrow());

                case '>' -> body = addLine(body, instructions.getRightArrow());

                case '.' -> body = addLine(body, instructions.getDot());

                case ',' -> body = addLine(body, instructions.getComma());

                case '[' -> {
                    body = addLine(body, instructions.getLeftBracket());
                    inBracket++;
                }
                case ']' -> {
                    inBracket--;
                    body = addLine(body, instructions.getRightBracket());
                }
            }
        inBracket = 0;
        body = addLine(body, "");

        return body;
    }
}