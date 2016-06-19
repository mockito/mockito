package org.mockito.internal.matchers;


import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

public class VarargCapturingMatcherTest {

    @Test
    @SuppressWarnings("unchecked")
    public void should_capture_simple_arguments() throws Exception {
        //given
        VarargCapturingMatcher<String> m = new VarargCapturingMatcher<String>();

        //when
        m.captureFrom("foo");
        m.captureFrom("bar");

        //then
        assertThat(m.getAllVarargs()).containsSequence(singletonList("foo"), singletonList("bar"));
    }

    @Test
    public void should_know_last_captured_vararg() throws Exception {
        //given
        VarargCapturingMatcher<String> m = new VarargCapturingMatcher<String>();

        //when
        m.captureFrom(new Object[] { "foo", "bar" });
        m.captureFrom(new Object[] { "fool", "bard" });

        //then
        assertThat(m.getLastVarargs()).containsSequence("fool", "bard");
    }

    @Test
    public void can_capture_primitive_varargs() throws Exception {
        //given
        VarargCapturingMatcher<Integer> m = new VarargCapturingMatcher<Integer>();

        //when
        m.captureFrom(new int[] { 1, 2, 3 });

        //then
        assertThat(m.getLastVarargs()).containsSequence(1, 2, 3);
    }

    @Test
    public void should_scream_when_nothing_yet_captured() throws Exception {
        //given
        VarargCapturingMatcher m = new VarargCapturingMatcher<Object>();

        try {
            //when
            m.getLastVarargs();
            //then
            fail();
        } catch (MockitoException e) {}
    }
}
