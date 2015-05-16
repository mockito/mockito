package org.mockito.internal.util;

import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;

import java.util.List;

public class PrettyPrinter {

    public static String toStringLine(List<SelfDescribing> items) {
        return makeList("(", ", ", ");", items);
    }

    public static String toStringBlock(List<SelfDescribing> items) {
        return makeList("(\n    ", ",\n    ", "\n);", items);
    }

    private static String makeList(String start, String separator, String end, List<SelfDescribing> items) {
        Description result = new StringDescription();
        result.appendList(start, separator, end, items);
        return result.toString();
    }

}
