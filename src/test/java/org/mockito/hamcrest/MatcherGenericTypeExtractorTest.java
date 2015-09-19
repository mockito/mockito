package org.mockito.hamcrest;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockitoutil.TestBase;

import java.io.Serializable;
import java.util.HashMap;

import static org.mockito.hamcrest.MatcherGenericTypeExtractor.genericTypeOfMatcher;

public class MatcherGenericTypeExtractorTest extends TestBase {

    //traditional inner class for matcher
    private class IntMatcher extends BaseMatcher<Integer> {
        public boolean matches(Object o) {
            return true;
        }
        public void describeTo(Description description) {}
    }

    //static class with matcher
    private static class StaticIntMatcher extends BaseMatcher<Integer> {
        public boolean matches(Object o) {
            return true;
        }
        public void describeTo(Description description) {}
    }

    //static subclass
    private static class StaticIntMatcherSubclass extends StaticIntMatcher {
        public boolean matches(Object o) {
            return true;
        }
        public void describeTo(Description description) {}
    }

    //non-generic
    private static class NonGenericMatcher extends BaseMatcher {
        public boolean matches(Object o) {
            return true;
        }
        public void describeTo(Description description) {}
    }

    //Matcher interface implementation (instead of the BaseMatcher)
    private class IntMatcherFromInterface implements Matcher<Integer> {
        public boolean matches(Object o) {
            return true;
        }
        public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {}
        public void describeTo(Description description) {}
    }

    //Static Matcher interface implementation (instead of the BaseMatcher)
    private static class StaticIntMatcherFromInterface implements Matcher<Integer> {
        public boolean matches(Object o) {
            return true;
        }
        public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {}
        public void describeTo(Description description) {}
    }

    //non-generic matcher implementing the interface
    private static class NonGenericMatcherFromInterface implements Matcher {
        public boolean matches(Object o) {
            return true;
        }
        public void _dont_implement_Matcher___instead_extend_BaseMatcher_() { }
        public void describeTo(Description description) {}
    }

    private static interface IMatcher extends Matcher<Integer> {}

    //non-generic matcher implementing the interface
    private static class SubclassGenericMatcherFromInterface implements Serializable, Cloneable, IMatcher {
        public boolean matches(Object o) {
            return true;
        }
        public void _dont_implement_Matcher___instead_extend_BaseMatcher_() { }
        public void describeTo(Description description) {}
    }

    //I refuse to comment on the sanity of this case
    private static class InsaneEdgeCase extends SubclassGenericMatcherFromInterface {}

    @Test
    public void findsGenericType() {
        assertEquals(Integer.class, genericTypeOfMatcher(IntMatcher.class));
        assertEquals(Integer.class, genericTypeOfMatcher(StaticIntMatcher.class));
        assertEquals(Integer.class, genericTypeOfMatcher(IntMatcherFromInterface.class));
        assertEquals(Integer.class, genericTypeOfMatcher(StaticIntMatcherSubclass.class));
        assertEquals(Integer.class, genericTypeOfMatcher(IntMatcherFromInterface.class));
        assertEquals(Integer.class, genericTypeOfMatcher(StaticIntMatcherFromInterface.class));
        assertEquals(Integer.class, genericTypeOfMatcher(SubclassGenericMatcherFromInterface.class));
        assertEquals(Integer.class, genericTypeOfMatcher(InsaneEdgeCase.class));

        assertEquals(Integer.class, genericTypeOfMatcher(new BaseMatcher<Integer>() {
            public void describeTo(Description description) {
            }

            public boolean matches(Object o) {
                return false;
            }
        }.getClass()));
        assertEquals(Integer.class, genericTypeOfMatcher(new Matcher<Integer>() {
            public void describeTo(Description description) {
            }

            public boolean matches(Object o) {
                return false;
            }

            public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {
            }
        }.getClass()));

        assertEquals(Object.class, genericTypeOfMatcher(Object.class));
        assertEquals(Object.class, genericTypeOfMatcher(String.class));
        assertEquals(Object.class, genericTypeOfMatcher(new HashMap<String, String>().getClass()));
        assertEquals(Object.class, genericTypeOfMatcher(new HashMap<String, String>() {
        }.getClass()));
        assertEquals(Object.class, genericTypeOfMatcher(NonGenericMatcher.class));
        assertEquals(Object.class, genericTypeOfMatcher(NonGenericMatcherFromInterface.class));
    }
}