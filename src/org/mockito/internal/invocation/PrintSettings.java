package org.mockito.internal.invocation;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;

@SuppressWarnings("unchecked")
public class PrintSettings {

    private boolean multiline;
    private boolean verboseArguments;
    private List<Matcher> verboseMatchers = new LinkedList<Matcher>();

    public void setMultiline(boolean multiline) {
        this.multiline = multiline;
    }

    public void setVerboseArguments(boolean verboseArguments) {
        this.verboseArguments = verboseArguments;
    }

    public boolean isMultiline() {
        return multiline;
    }

    public boolean isVerboseArguments() {
        return verboseArguments;
    }

    public static PrintSettings verboseMatchers(Matcher ... verboselyPrinted) {
        PrintSettings settings = new PrintSettings();
        settings.verboseMatchers.addAll((List) Arrays.asList(verboselyPrinted));
        return settings;
    }

    public boolean printsVerbosely(Matcher matcher) {
        if (isVerboseArguments()) {
            return true;
        }
        for (Matcher m : verboseMatchers) {
            if (m == matcher) {
                return true;
            }
        }
        return false;
    }
}