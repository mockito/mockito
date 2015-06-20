package org.mockito.internal.util.text;

import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;

/**
 * Temporary class, will be removed when refactorings are done.
 * The goal is that this class is the only 'link' to hamcrest description reporting.
 */
public class HamcrestPrinter {

    public static String print(SelfDescribing object) {
        return StringDescription.toString(object);
    }
}
