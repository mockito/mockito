/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.charset.CharacterCodingException;

import org.junit.Test;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.invocation.Invocation;

/**
 * by Szczepan Faber, created at: 3/31/12
 */
public class MethodInfoTest {

    @Test
    public void shouldKnowValidThrowables() throws Exception {
        //when
        final Invocation invocation = new InvocationBuilder().method("canThrowException").toInvocation();
        final MethodInfo info = new MethodInfo(invocation);

        //then
        assertFalse(info.isValidException(new Exception()));
        assertTrue(info.isValidException(new CharacterCodingException()));
    }
}
