package org.mockito.internal.junit;

import org.junit.Test;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.mockito.internal.util.SimpleMockitoLogger;
import org.mockitoutil.TestBase;

import java.util.List;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class UnusedStubbingsTest extends TestBase {

    SimpleMockitoLogger logger = new SimpleMockitoLogger();

    @Test
    public void no_unused_stubbings() throws Exception {
        //given
        UnusedStubbings stubbings = new UnusedStubbings((List) asList());

        //when
        stubbings.format("MyTest.myTestMethod", logger);

        //then
        assertEquals("", logger.getLoggedInfo());
    }

    @Test
    public void unused_stubbings() throws Exception {
        //given
        UnusedStubbings stubbings = new UnusedStubbings((List) asList(
            new StubbedInvocationMatcher(new InvocationBuilder().toInvocationMatcher(), new DoesNothing()),
            new StubbedInvocationMatcher(new InvocationBuilder().toInvocationMatcher(), new DoesNothing())
        ));


        //when
        stubbings.format("MyTest.myTestMethod", logger);

        //then
        String[] message = filterLineNo(logger.getLoggedInfo()).split("\n");
        assertThat(message[0]).isEqualTo("[MockitoHint] MyTest.myTestMethod (see javadoc for MockitoHint):");
        assertThat(message[1]).matches("\\[MockitoHint\\] 1\\. Unused \\-\\> at [\\w\\.]+\\.reflect\\.NativeMethodAccessorImpl\\.invoke0\\(.*Native Method\\)");
        assertThat(message[2]).matches("\\[MockitoHint\\] 2\\. Unused \\-\\> at [\\w\\.]+\\.reflect\\.NativeMethodAccessorImpl\\.invoke0\\(.*Native Method\\)");
    }
}
