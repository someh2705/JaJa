package io.jaja.statement;

import io.jaja.AST;

import java.util.ArrayList;
import java.util.Iterator;

public class BlockStatement implements Statement {
    private ArrayList<AST> asts;

    public BlockStatement(ArrayList<AST> asts) {
        this.asts = asts;
    }

    @Override
    public AST lastChild() {
        return asts.get(asts.size() - 1);
    }

    @Override
    public Iterator<AST> iterator() {
        return asts.iterator();
    }

    public ArrayList<AST> getAsts() {
        return asts;
    }
}
