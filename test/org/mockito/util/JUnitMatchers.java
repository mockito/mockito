package org.mockito.util;

import java.util.Arrays;
import java.util.Collection;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class JUnitMatchers {

    public static <T> Matcher<Collection<T>> contains(final T ... elements) {
        return new BaseMatcher<Collection<T>>() {

            public boolean matches(Object collection) {
                for (T element : elements) {
                    if (((Collection)collection).contains(element) == false) {
                        return false;
                    }
                }
                return true;
            }

            public void describeTo(Description desc) {
                desc.appendText("collection doesn't containg one of: " + Arrays.toString(elements));
            }
        };
    }
}
