package io.jaja;

public interface AST extends Iterable<AST> {

    AST lastChild();
}
