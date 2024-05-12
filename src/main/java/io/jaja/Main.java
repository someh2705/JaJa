package io.jaja;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Runtime runtime = new Runtime();

        do {
            System.out.printf("JaJa> ");
            String input = scanner.nextLine();
            String result = runtime.evaluate(input);
            System.out.println("JaJa> " + result);
        } while (true);
    }
}
