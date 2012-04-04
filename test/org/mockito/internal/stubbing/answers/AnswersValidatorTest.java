/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.answers.ReturnsIdentity;
import org.mockitoutil.TestBase;

import java.io.IOException;
import java.nio.charset.CharacterCodingException;
import java.util.ArrayList;

import static org.mockito.Mockito.mock;

@SuppressWarnings("unchecked")
public class AnswersValidatorTest extends TestBase {

    private AnswersValidator validator = new AnswersValidator();
    private Invocation invocation = new InvocationBuilder().method("canThrowException").toInvocation();

    @Test
    public void should_validate_null_throwable() throws Throwable {
        try {
            validator.validate(new ThrowsException(null), new InvocationBuilder().toInvocation());
            fail();
        } catch (MockitoException e) {}
    }

    @Test
    public void should_pass_proper_checked_exception() throws Throwable {
        validator.validate(new ThrowsException(new CharacterCodingException()), invocation);
    }

    @Test(expected = MockitoException.class)
    public void should_fail_invalid_checked_exception() throws Throwable {
        validator.validate(new ThrowsException(new IOException()), invocation);
    }
    
    @Test
    public void should_pass_RuntimeExceptions() throws Throwable {
        validator.validate(new ThrowsException(new Error()), invocation);
        validator.validate(new ThrowsException(new RuntimeException()), invocation);
    }
    
    @Test(expected = MockitoException.class)
    public void should_fail_when_return_Value_is_set_for_void_method() throws Throwable {
        validator.validate(new Returns("one"), new InvocationBuilder().method("voidMethod").toInvocation());
    }
    
    @Test(expected = MockitoException.class)
    public void should_fail_when_non_void_method_does_nothing() throws Throwable {
        validator.validate(new DoesNothing(), new InvocationBuilder().simpleMethod().toInvocation());
    }
    
    @Test
    public void should_allow_void_return_for_void_method() throws Throwable {
        validator.validate(new DoesNothing(), new InvocationBuilder().method("voidMethod").toInvocation());
    }
    
    @Test
    public void should_allow_correct_type_of_return_value() throws Throwable {
        validator.validate(new Returns("one"), new InvocationBuilder().simpleMethod().toInvocation());
        validator.validate(new Returns(false), new InvocationBuilder().method("booleanReturningMethod").toInvocation());
        validator.validate(new Returns(new Boolean(true)), new InvocationBuilder().method("booleanObjectReturningMethod").toInvocation());
        validator.validate(new Returns(1), new InvocationBuilder().method("integerReturningMethod").toInvocation());
        validator.validate(new Returns(1L), new InvocationBuilder().method("longReturningMethod").toInvocation());
        validator.validate(new Returns(1L), new InvocationBuilder().method("longObjectReturningMethod").toInvocation());
        validator.validate(new Returns(null), new InvocationBuilder().method("objectReturningMethodNoArgs").toInvocation());
        validator.validate(new Returns(1), new InvocationBuilder().method("objectReturningMethodNoArgs").toInvocation());
    }
    
    @Test(expected = MockitoException.class)
    public void should_fail_on_return_type_mismatch() throws Throwable {
        validator.validate(new Returns("String"), new InvocationBuilder().method("booleanReturningMethod").toInvocation());
    }
    
    @Test(expected = MockitoException.class)
    public void should_fail_on_wrong_primitive() throws Throwable {
        validator.validate(new Returns(1), new InvocationBuilder().method("doubleReturningMethod").toInvocation());
    }

    @Test(expected = MockitoException.class)
    public void should_fail_on_null_with_primitive() throws Throwable {
        validator.validate(new Returns(null), new InvocationBuilder().method("booleanReturningMethod").toInvocation());
    }
    
    @Test
    public void should_fail_when_calling_real_method_on_interface() throws Throwable {
        //given
        Invocation inovcationOnIterface = new InvocationBuilder().method("simpleMethod").toInvocation();
        try {
            //when
            validator.validate(new CallsRealMethods(), inovcationOnIterface);
            //then
            fail();
        } catch (MockitoException e) {}
    }
            
    @Test
    public void should_be_OK_when_calling_real_method_on_concrete_class() throws Throwable {
        //given
        ArrayList mock = mock(ArrayList.class);
        mock.clear();
        Invocation invocationOnClass = getLastInvocation();
        //when
        validator.validate(new CallsRealMethods(), invocationOnClass);
        //then no exception is thrown
    }

}