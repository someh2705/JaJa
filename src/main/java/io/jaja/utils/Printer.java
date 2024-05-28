package io.jaja.utils;

import io.jaja.AST;

public class Printer {

    public static void pretty(AST ast) {
        pretty(ast, "", true);
    }

    /**
     * └───
     * ├───
     * │
     */
    private static void pretty(AST node, String indent, boolean isLast) {
        String marker = isLast ? "\u2524\u2500\u2500\u2500" : "\u251C\u2500\u2500";

        System.out.print(indent);
        System.out.print(marker);
        System.out.print(node);

        System.out.println();

        indent += isLast ? "   " : "\u2502  ";
        for (AST child : node) {
            pretty(child, indent, child == node.lastChild());
        }
    }
}
