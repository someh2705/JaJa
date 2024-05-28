package io.jaja.statement;

import io.jaja.AST;
import io.jaja.utils.IteratorUtils;

import java.util.Iterator;

public class VoidReturnStatement extends ReturnStatement {

    @Override
    public AST lastChild() {
        return this;
    }

    @Override
    public Iterator<AST> iterator() {
        return IteratorUtils.values();
    }
}
