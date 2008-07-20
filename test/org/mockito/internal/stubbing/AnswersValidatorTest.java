/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing;

import java.io.IOException;
import java.nio.charset.CharacterCodingException;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockitoutil.TestBase;

public class AnswersValidatorTest extends TestBase {

    private AnswersValidator validator = new AnswersValidator();
    private Invocation invocation = new InvocationBuilder().method("canThrowException").toInvocation();

    @Test
    public void shouldValidateNullThrowable() throws Throwable {
        try {
            validator.validate(new ThrowsException(null), null);
            fail();
        } catch (MockitoException e) {}
    }

    @Test
    public void shouldPassProperCheckedException() throws Throwable {
        validator.validate(new ThrowsException(new CharacterCodingException()), invocation);
    }

    @Test(expected = MockitoException.class)
    public void shouldFailInvalidCheckedException() throws Throwable {
        validator.validate(new ThrowsException(new IOException()), invocation);
    }
    
    @Test
    public void shouldPassRuntimeExceptions() throws Throwable {
        validator.validate(new ThrowsException(new Error()), invocation);
        validator.validate(new ThrowsException(new RuntimeException()), invocation);
    }
    
    @Test
    public void shouldValidateReturnValue() throws Throwable {
        validator.validate(new Returns("one"), new InvocationBuilder().simpleMethod().toInvocation());
    }
    
    @Test(expected = MockitoException.class)
    public void shouldFailWhenReturnValueSetForVoidMethod() throws Throwable {
        validator.validate(new Returns("one"), new InvocationBuilder().method("voidMethod").toInvocation());
    }
    
    @Test(expected = MockitoException.class)
    public void shouldFailWhenNonVoidMethodDoesNothing() throws Throwable {
        validator.validate(new DoesNothing(), new InvocationBuilder().simpleMethod().toInvocation());
    }
    
    @Test
    public void shouldAllowVoidReturnForVoidMethod() throws Throwable {
        validator.validate(new DoesNothing(), new InvocationBuilder().method("voidMethod").toInvocation());
    }
}