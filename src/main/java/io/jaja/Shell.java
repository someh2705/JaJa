package io.jaja;

import java.util.Scanner;

public class Shell {

    private static int line = 0;

    private static final String RED = "\u001b[31m";
    private static final String GREEN = "\u001b[32m";
    private static final String YELLOW = "\u001b[33m";
    private static final String RESET = "\u001b[0m";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Runtime runtime = new Runtime();

        do {
            try {
                System.out.printf(RESET + "JaJa(" + line++ + ")> ");
                String input = scanner.nextLine();
                String result = runtime.evaluate(input);
                System.out.println(YELLOW + "output: " + result);
            } catch (Diagnostics diagnostics) {
                System.out.println(RED + "error: " + diagnostics.getMessage());
            }
        } while (true);
    }
}
