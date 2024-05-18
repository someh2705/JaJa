package io.jaja;

import java.util.Scanner;

public class Shell {

    private static int line = 0;

    private static final String RED = "\u001b[31m";
    private static final String GREEN = "\u001b[32m";
    private static final String YELLOW = "\u001b[33m";
    private static final String RESET = "\u001b[0m";

    private static Scanner scanner = new Scanner(System.in);
    private static Runtime runtime = new Runtime();

    public static void main(String[] args) {
        do {
            try {
                scanBlock(0, new StringBuilder());
            } catch (Diagnostics diagnostics) {
                System.out.println(RED + "error: " + diagnostics.getMessage());
            }
        } while (true);
    }

    private static void scanBlock(int blockCount, StringBuilder builder) {
        String info = "JaJa(" + line++ + ")> ";

        if (blockCount == 0) {
            System.out.printf(RESET + info);
        } else {
            for (int i = 0; i < info.length() - 5; i++) {
                System.out.printf(RESET + " ");
            }
            System.out.printf(RESET + ".... ");
            for (int i = 0; i < blockCount - 1; i++) {
                System.out.printf(RESET + "    ");
            }
        }

        String input = scanner.nextLine();
        builder.append(input).append("\n");
        boolean hasLbrace = input.contains("{");
        boolean hasRbrace = input.contains("}");

        if (hasLbrace) blockCount++;
        if (hasRbrace) blockCount--;

        if (blockCount > 0) {
            scanBlock(blockCount, builder);
            return;
        }

        print(builder.toString());
    }

    private static void print(String inputs) {
        String result = runtime.evaluate(inputs);
        System.out.println(YELLOW + "output: " + result);
    }
}
