/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

public class RemoveFirstLine {

    private RemoveFirstLine(){}
    
    /**
     * @param text to have the first line removed
     * @return less first line
     */
    public static String removeFirstLine(String text) {
        return text.replaceFirst(".*?\n", "");
    }
}
