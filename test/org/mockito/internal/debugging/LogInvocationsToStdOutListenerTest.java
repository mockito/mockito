/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import static org.mockito.Mockito.*;

import java.io.*;

import org.junit.*;
import org.mockito.invocation.*;
import org.mockitoutil.*;

public class LogInvocationsToStdOutListenerTest extends TestBase {

    private static final InvocationOnMock SOME_INVOCATION = mock(InvocationOnMock.class);

	@Test
    public void presetStreamIsStdOut() {
    	assertSame(System.out, new LogInvocationsToStdOutListener().printStream);
    }
    
	@Test
	public void shouldPrintInvocationToStream() {
		//given
		LogInvocationsToStdOutListener listener = new LogInvocationsToStdOutListener();
		listener.printStream = mock(PrintStream.class);
		
		//when
		listener.invoking(SOME_INVOCATION);
	
	    //then
		verify(listener.printStream).println(SOME_INVOCATION.toString());
	}
    
    
}