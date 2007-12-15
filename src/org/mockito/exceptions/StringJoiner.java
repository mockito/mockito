/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions;

class StringJoiner {

    static String join(String ... linesToBreak) {
        StringBuilder out = new StringBuilder("\n");
        for (String line : linesToBreak) {
            out.append(line).append("\n");
        }
        int lastBreak = out.lastIndexOf("\n");
        return out.replace(lastBreak, lastBreak+1, "").toString();
    }
}