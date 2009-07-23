/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
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
