package org.mockito.internal.reporting;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unchecked")
public class PrintSettings {

    private boolean multiline;
    private List<Integer> verboseMatchers = new LinkedList<Integer>();

    public void setMultiline(boolean multiline) {
        this.multiline = multiline;
    }

    public boolean isMultiline() {
        return multiline;
    }

    public static PrintSettings verboseMatchers(Integer ... verboselyPrinted) {
        PrintSettings settings = new PrintSettings();
        settings.setMatchersToBePrintedVerbosely(verboselyPrinted);
        return settings;
    }

    public boolean printsVerbosely(int argumentIndex) {
        return verboseMatchers.contains(argumentIndex);
    }

    public void setMatchersToBePrintedVerbosely(Integer[] toBePrintedVerbosely) {
        this.verboseMatchers = Arrays.asList(toBePrintedVerbosely);
    }
}