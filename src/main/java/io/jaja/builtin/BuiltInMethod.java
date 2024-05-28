package io.jaja.builtin;

import io.jaja.Diagnostics;
import io.jaja.Parser;
import io.jaja.Program;
import io.jaja.bind.BindObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class BuiltInMethod {

    public static Program compileFile(String filename) {
        File file = new File(filename);
        try (Scanner scanner = new Scanner(file)) {
            StringBuilder builder = new StringBuilder();
            while (scanner.hasNextLine()) {
                builder.append(scanner.nextLine());
            }
            Parser parser = new Parser(builder.toString());

            return parser.parse();
        } catch (FileNotFoundException e) {
            throw new Diagnostics("File not found: " + filename);
        }
    }

    public static void println(BindObject<?> evaluate) {
        System.out.println(evaluate.getValue());
    }
}
