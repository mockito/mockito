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
     * Joins Strings with line break character. It adds line break in front, too.
     * This makes it something like 'format' no really 'join'.
     */
    @SuppressWarnings("unchecked")
    public static String join(Object ... linesToBreak) {
        return join("\n", asList(linesToBreak));
    }

    /**
     * Joins Strings with EOL character
     */
    public static String join(String start, Iterable<?> lines) {
        StringBuilder out = new StringBuilder(start);
        for (Object line : lines) {
            out.append(line.toString()).append("\n");
        }
        int lastBreak = out.lastIndexOf("\n");
        return out.replace(lastBreak, lastBreak+1, "").toString();
    }
}
