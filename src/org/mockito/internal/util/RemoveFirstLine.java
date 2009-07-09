package org.mockito.internal.util;

public class RemoveFirstLine {

    /**
     * @param string to have the first line removed
     * @return
     */
    public String of(String text) {
        return text.replaceFirst(".*?\n", "");
    }
}
