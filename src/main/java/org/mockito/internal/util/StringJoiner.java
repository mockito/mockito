/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.util;

import static java.util.Arrays.asList;

/**
 * Joins Strings together producing yet another String
 */
public class StringJoiner {

    //TODO where's the unit test?

    /**
     * Joins Strings with EOL character
     */
    public static String join(Object ... linesToBreak) {
        return join(asList(linesToBreak));
    }

    /**
     * Joins Strings with EOL character
     */
    public static String join(Iterable<String> lines) {
        StringBuilder out = new StringBuilder("\n");
        for (Object line : lines) {
            out.append(line.toString()).append("\n");
        }
        int lastBreak = out.lastIndexOf("\n");
        return out.replace(lastBreak, lastBreak+1, "").toString();
    }
}
