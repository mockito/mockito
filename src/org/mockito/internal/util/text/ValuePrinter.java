package org.mockito.internal.util.text;

import org.hamcrest.SelfDescribing;

import java.util.Iterator;

import static java.lang.String.valueOf;

/**
 * Prints a Java object value in a way humans can read it neatly.
 * Inspired on hamcrest. The implementation needs to safely depend on hamcrest.
 *
 * TODO add specific unit tests instead of relying on higher level unit tests.
 */
public class ValuePrinter {

    private final StringBuilder content = new StringBuilder();

    public String toString() {
        return content.toString();
    }

    public ValuePrinter appendText(String text) {
        append(text);
        return this;
    }
    
    public ValuePrinter appendValue(Object value) {
        if (value == null) {
            append("null");
        } else if (value instanceof String) {
            toJavaSyntax((String) value);
        } else if (value instanceof Character) {
            append('"');
            toJavaSyntax((Character) value);
            append('"');
        } else if (value instanceof Short) {
            append('<');
            append(descriptionOf(value));
            append("s>");
        } else if (value instanceof Long) {
            append('<');
            append(descriptionOf(value));
            append("L>");
        } else if (value instanceof Float) {
            append('<');
            append(descriptionOf(value));
            append("F>");
        } else if (value.getClass().isArray()) {
            appendList("[", ", ", "]", new org.mockito.internal.util.text.ArrayIterator(value));
        } else if (value instanceof SelfDescribing) {
            append(HamcrestPrinter.print((SelfDescribing) value));
        } else {
            append('<');
            append(descriptionOf(value));
            append('>');
        }
        return this;
    }

    private String descriptionOf(Object value) {
        try {
            return valueOf(value);
        }
        catch (Exception e) {
            return value.getClass().getName() + "@" + Integer.toHexString(value.hashCode());
        }
    }

    public ValuePrinter appendList(String start, String separator, String end, Iterator i) {
        boolean separate = false;
        
        append(start);
        while (i.hasNext()) {
            if (separate) append(separator);
            appendValue(i.next());
            separate = true;
        }
        append(end);
        
        return this;
    }

    /**
     * Append the String <var>str</var> to the content.
     */
    protected void append(String str) {
        for (int i = 0; i < str.length(); i++) {
            append(str.charAt(i));
        }
    }
    
    /**
     * Append the char <var>c</var> to the content.
     */
    protected void append(char c) {
        content.append(c);
    }

    private void toJavaSyntax(String unformatted) {
        append('"');
        for (int i = 0; i < unformatted.length(); i++) {
            toJavaSyntax(unformatted.charAt(i));
        }
        append('"');
    }

    private void toJavaSyntax(char ch) {
        switch (ch) {
            case '"':
                append("\\\"");
                break;
            case '\n':
                append("\\n");
                break;
            case '\r':
                append("\\r");
                break;
            case '\t':
                append("\\t");
                break;
            default:
                append(ch);
        }
    }

}
