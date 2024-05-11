package io.jaja.utils;

import io.jaja.expression.Expression;

public class Printer {

    public static void pretty(Expression expression) {
        pretty(expression, "", true);
    }

    /**
     * └───
     * ├───
     * │
     */
    private static void pretty(Expression node, String indent, boolean isLast) {
        String marker = isLast ? "└──" : "├──";

        System.out.print(indent);
        System.out.print(marker);
        System.out.print(node);

        System.out.println();

        indent += isLast ? "   " : "│  ";
        for (Expression child : node) {
            pretty(child, indent, child == node.lastChild());
        }
    }
}
