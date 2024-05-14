package io.jaja.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtils {

    public static String startMatch(String string, Pattern pattern) {
        Matcher matcher = pattern.matcher(string);

        if (matcher.find()) {
            if (matcher.start() == 0) {
                return matcher.group();
            }
        }

        return null;
    }

    public static boolean startsWith(String string, Pattern pattern) {
        Matcher matcher = pattern.matcher(string);

        if (matcher.find()) {
            if (matcher.start() == 0) {
                return true;
            }
        }

        return false;
    }
}
