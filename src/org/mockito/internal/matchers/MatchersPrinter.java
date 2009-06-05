package org.mockito.internal.matchers;

import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;
import org.mockito.internal.invocation.PrintSettings;

@SuppressWarnings("unchecked")
public class MatchersPrinter {
    
    public String getArgumentsLine(List<Matcher> matchers, PrintSettings printSettings) {
        Description result = new StringDescription();
        result.appendList("(", ", ", ");", applyPrintSettings(matchers, printSettings));
        return result.toString();
    }

    public String getArgumentsBlock(List<Matcher> matchers, PrintSettings printSettings) {
        Description result = new StringDescription();
        result.appendList("(\n    ", ",\n    ", "\n);", applyPrintSettings(matchers, printSettings));
        return result.toString();
    }

    private List<SelfDescribing> applyPrintSettings(List<Matcher> matchers, PrintSettings printSettings) {
        List<SelfDescribing> withPrintSettings = new LinkedList<SelfDescribing>();
        for (final Matcher matcher : matchers) {
            if (matcher instanceof HasVerboseVariant && printSettings.printsVerbosely(matcher)) {
                withPrintSettings.add(((HasVerboseVariant) matcher).getVerboseVariant());
            } else {
                withPrintSettings.add(matcher);
            }
        }
        return withPrintSettings;
    }
}