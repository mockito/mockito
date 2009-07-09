/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.reporting;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class PrintSettings {

    private boolean multiline;
    private List<Integer> withTypeInfo = new LinkedList<Integer>();

    public void setMultiline(boolean multiline) {
        this.multiline = multiline;
    }

    public boolean isMultiline() {
        return multiline;
    }

    public static PrintSettings verboseMatchers(Integer ... indexesOfMatchers) {
        PrintSettings settings = new PrintSettings();
        settings.setMatchersToBeDescribedWithExtraTypeInfo(indexesOfMatchers);
        return settings;
    }

    public boolean extraTypeInfoFor(int argumentIndex) {
        return withTypeInfo.contains(argumentIndex);
    }

    public void setMatchersToBeDescribedWithExtraTypeInfo(Integer[] indexesOfMatchers) {
        this.withTypeInfo = Arrays.asList(indexesOfMatchers);
    }
}