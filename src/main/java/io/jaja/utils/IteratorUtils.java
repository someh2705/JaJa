package io.jaja.utils;

import java.util.Arrays;
import java.util.Iterator;

public class IteratorUtils {

    public static <T> Iterator<T> values(T... args) {
        return Arrays.asList(args).iterator();
    }
}