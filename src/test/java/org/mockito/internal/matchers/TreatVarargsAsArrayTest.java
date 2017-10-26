package org.mockito.internal.matchers;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TreatVarargsAsArrayTest {

    @Test
    public void delegateDoesNotImplementCapturesArgument_captureFrom() {
        TreatVarargsAsArray<String[]> matcher = new TreatVarargsAsArray<String[]>(
            new Equals(new String[] {"1"}));

        // Should not do anything.
        matcher.captureFrom("2");
    }

    @Test
    public void delegateDoesImplementCapturesArgument_captureFrom() {
        CapturingMatcher<String[]> capturingMatcher = new CapturingMatcher<String[]>();
        TreatVarargsAsArray<String[]> matcher =
            new TreatVarargsAsArray<String[]>(capturingMatcher);

        // Should not do anything.
        matcher.captureFrom(new String[] {"2"});

        assertArrayEquals(new String[] {"2"}, capturingMatcher.getLastValue());
    }

    @Test
    public void delegateDoesNotImplementContainsExtraTypeInfo_toStringWithType() {
        TreatVarargsAsArray<String[]> matcher = new TreatVarargsAsArray<String[]>(NotNull.NOT_NULL);

        assertEquals("varargsAsArray(notNull())", matcher.toStringWithType());
    }

    @Test
    public void delegateDoesImplementContainsExtraTypeInfo_toStringWithType() {
        TreatVarargsAsArray<String[]> matcher = new TreatVarargsAsArray<String[]>(
            new Equals(new String[] {"1"}));

        assertEquals("varargsAsArray((String[]) [\"1\"])", matcher.toStringWithType());
    }

    @Test
    public void delegateDoesNotImplementContainsExtraTypeInfo_typeMatches() {
        TreatVarargsAsArray<String[]> matcher = new TreatVarargsAsArray<String[]>(NotNull.NOT_NULL);

        assertTrue(matcher.typeMatches(new String[] {}));
        assertTrue(matcher.typeMatches(1));
    }

    @Test
    public void delegateDoesImplementContainsExtraTypeInfo_typeMatches() {
        TreatVarargsAsArray<String[]> matcher = new TreatVarargsAsArray<String[]>(
            new Equals(new String[] {"1"}));

        assertTrue("String[]", matcher.typeMatches(new String[] {}));
        assertFalse("int", matcher.typeMatches(1));
    }
}
