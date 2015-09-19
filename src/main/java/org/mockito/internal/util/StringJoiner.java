/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.util;

public class StringJoiner {

    public static String join(Object ... linesToBreak) {
        StringBuilder out = new StringBuilder("\n");
        return join(out, linesToBreak);
    }

    private static String join(StringBuilder out, Object[] linesToBreak) {
        for (Object line : linesToBreak) {
            out.append(line.toString()).append("\n");
        }
        int lastBreak = out.lastIndexOf("\n");
        return out.replace(lastBreak, lastBreak+1, "").toString();
    }
}
