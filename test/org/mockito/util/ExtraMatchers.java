package org.mockito.util;

import java.util.*;

import org.hamcrest.*;

@SuppressWarnings("unchecked")
public class ExtraMatchers {

    public static <T> Matcher<Collection> collectionContaining(final T ... elements) {
        return new BaseMatcher<Collection>() {

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
