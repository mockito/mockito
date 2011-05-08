/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.invocation.MethodCallReport;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class VerboseMockInvocationLoggerTest {

    private VerboseMockInvocationLogger listener;
    private ByteArrayOutputStream output;
    private PrintableInvocation invocation;

    @Before public void init_Listener() throws Exception {
        output = new ByteArrayOutputStream();
        listener = new VerboseMockInvocationLogger(new PrintStream(output));
    }

    @Before public void init_Invocation() {
        invocation = mock(PrintableInvocation.class);
        given(invocation.getLocation()).willReturn(mock(Location.class));
    }

    @Test
	public void shouldPrintToSystemOut() {
		assertThat(new VerboseMockInvocationLogger().printStream).isSameAs(System.out);
	}

	@Test
	public void shouldPrintInvocationWithReturnValueToStream() {
        // when
		listener.reportInvocation(MethodCallReport.of(invocation, "return value", "location of stubbing"));

		// then
        assertThat(printed())
                .contains(invocation.toString())
                .contains(invocation.getLocation().toString())
                .contains("return value")
                .contains("location of stubbing");
	}

	@Test
	public void shouldPrintInvocationWithExceptionToStream() {
		// when
		listener.reportInvocation(MethodCallReport.of(invocation, new ThirdPartyException(), "location of stubbing"));

		// then
        assertThat(printed())
				.contains(invocation.toString())
				.contains(invocation.getLocation().toString())
				.contains(ThirdPartyException.class.getName())
				.contains("location of stubbing");
	}

	@Test
	public void shouldLogNumberOfInteractions() {
		// when & then
        listener.reportInvocation(MethodCallReport.of(invocation, new ThirdPartyException(), "location of stubbing"));
		assertThat(printed()).contains("#1");

        listener.reportInvocation(MethodCallReport.of(invocation, new ThirdPartyException(), "other location"));
		assertThat(printed()).contains("#2");

        listener.reportInvocation(MethodCallReport.of(invocation, new ThirdPartyException(), "location of stubbing"));
		assertThat(printed()).contains("#3");
	}

    private String printed() {
        return output.toString();
    }

	private static class ThirdPartyException extends Exception {
		private static final long serialVersionUID = 3022739107688491354L;
	}
}
