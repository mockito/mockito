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
