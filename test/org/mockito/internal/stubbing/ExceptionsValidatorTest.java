/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import java.io.IOException;
import java.nio.charset.CharacterCodingException;

import org.junit.Test;
import org.mockito.TestBase;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationBuilder;

public class ExceptionsValidatorTest extends TestBase {

    private ExceptionsValidator validator = new ExceptionsValidator();
    private Invocation invocation = new InvocationBuilder().method("canThrowException").toInvocation();

    @Test
    public void shouldValidateNullThrowable() throws Throwable {
        try {
            validator.validate(null, null);
            fail();
        } catch (MockitoException e) {
        }
    }

    @Test
    public void shouldPassProperCheckedException() throws Throwable {
        validator.validate(new CharacterCodingException(), invocation);
    }

    @Test(expected = MockitoException.class)
    public void shouldFailInvalidCheckedException() throws Throwable {
        validator.validate(new IOException(), invocation);
    }
    
    @Test
    public void shouldPassRuntimeExceptions() throws Throwable {
        validator.validate(new Error(), invocation);
        validator.validate(new RuntimeException(), invocation);
    }
}
