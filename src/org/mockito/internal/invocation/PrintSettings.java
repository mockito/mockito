package org.mockito.internal.invocation;

public class PrintSettings {

    private boolean multiline;
    private boolean verboseArguments;

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

    public static PrintSettings verboseArgs() {
        PrintSettings settings = new PrintSettings();
        settings.verboseArguments = true;
        return settings;
    }
}
