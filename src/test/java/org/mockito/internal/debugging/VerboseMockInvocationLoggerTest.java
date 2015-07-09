/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.internal.invocation.StubInfoImpl;
import org.mockito.internal.listeners.NotifiedMethodInvocationReport;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

public class VerboseMockInvocationLoggerTest {

    private VerboseMockInvocationLogger listener;

    private ByteArrayOutputStream output;
    private Invocation invocation = new InvocationBuilder().toInvocation();
    private DescribedInvocation stubbedInvocation = new InvocationBuilder().toInvocation();

    @Before
    public void init_Listener() throws Exception {
        output = new ByteArrayOutputStream();
        listener = new VerboseMockInvocationLogger(new PrintStream(output));
    }

    @After
    public void tearDown() throws Exception {
        System.out.println(output);
    }

    @Test
    public void should_print_to_system_out() {
        assertThat(new VerboseMockInvocationLogger().printStream).isSameAs(System.out);
    }

    @Test
    public void should_print_invocation_with_return_value() {
        // when
        listener.reportInvocation(new NotifiedMethodInvocationReport(invocation, "return value"));

        // then
        assertThat(printed())
                .contains(invocation.toString())
                .contains(invocation.getLocation().toString())
                .contains("return value");
    }

    @Test
    public void should_print_invocation_with_exception() {
        // when
        listener.reportInvocation(new NotifiedMethodInvocationReport(invocation, new ThirdPartyException()));

        // then
        assertThat(printed())
                .contains(invocation.toString())
                .contains(invocation.getLocation().toString())
                .contains(ThirdPartyException.class.getName());
    }

    @Test
    public void should_print_if_method_has_not_been_stubbed() throws Exception {
        listener.reportInvocation(new NotifiedMethodInvocationReport(invocation, "whatever"));

        assertThat(printed()).doesNotContain("stubbed");
    }

    @Test
    public void should_print_stubbed_info_if_availbable() throws Exception {
        invocation.markStubbed(new StubInfoImpl(stubbedInvocation));

        listener.reportInvocation(new NotifiedMethodInvocationReport(invocation, "whatever"));

        assertThat(printed())
                .contains("stubbed")
                .contains(stubbedInvocation.getLocation().toString());
    }

    @Test
    public void should_log_count_of_interactions() {
        // when & then
        listener.reportInvocation(new NotifiedMethodInvocationReport(invocation, new ThirdPartyException()));
        assertThat(printed()).contains("#1");

        listener.reportInvocation(new NotifiedMethodInvocationReport(invocation, new ThirdPartyException()));
        assertThat(printed()).contains("#2");

        listener.reportInvocation(new NotifiedMethodInvocationReport(invocation, new ThirdPartyException()));
        assertThat(printed()).contains("#3");
    }

    private String printed() {
        return output.toString();
    }

    private static class ThirdPartyException extends Exception {
        private static final long serialVersionUID = 3022739107688491354L;
    }
}
