/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.verification;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockitoutil.TestBase;

@Ignore
public class VerifyingRecorderTest extends TestBase {
    
    private VerifyingRecorder recorder;
    
    @Before
    public void setup() {
        recorder = new VerifyingRecorder();
    }
    
    @Test
    public void shouldNotReturnToStringMethod() throws Exception {
        Invocation toString = new InvocationBuilder().method("toString").toInvocation();
        Invocation simpleMethod = new InvocationBuilder().simpleMethod().toInvocation();
        
        recorder.recordInvocation(toString);
        recorder.recordInvocation(simpleMethod);
        
        assertTrue(recorder.getRegisteredInvocations().contains(simpleMethod));
        assertFalse(recorder.getRegisteredInvocations().contains(toString));
    }
}