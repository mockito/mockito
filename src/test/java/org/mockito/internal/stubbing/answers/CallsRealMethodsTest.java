/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import java.util.ArrayList;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.invocation.Invocation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.RETURNS_DEFAULTS;
import static org.mockito.Mockito.mock;

public class CallsRealMethodsTest {
    @Test
    public void should_invoke_real_method() throws Throwable {
        class Concrete {
            void concrete() {
                throw new RuntimeException("real code");
            }
        }
        Concrete mock = mock(Concrete.class);

        Invocation concrete = new InvocationBuilder().mock(mock).method(Concrete.class.getDeclaredMethod("concrete")).toInvocation();
        try {
            new CallsRealMethods().answer(concrete);
        } catch (RuntimeException throwable) {
            throwable.printStackTrace();
            assertThat(throwable).hasMessage("real code");
        }
    }

    @Test
    public void should_delegate_to_returns_default_when_abstract_method() throws Throwable {
        Invocation abstractMethod = new InvocationBuilder().method("booleanReturningMethod").toInvocation();
        assertThat(new CallsRealMethods().answer(abstractMethod)).isEqualTo(RETURNS_DEFAULTS.answer(abstractMethod));
    }

    @Test
    public void should_fail_when_calling_real_method_on_interface() throws Throwable {
        //given
        Invocation invocationOnInterface = new InvocationBuilder().method("simpleMethod").toInvocation();
        try {
            //when
            new CallsRealMethods().validateFor(invocationOnInterface);
            //then
            Assertions.fail("can not invoke interface");
        } catch (MockitoException expected) {}
    }

    @Test
    public void should_be_OK_when_calling_real_method_on_concrete_class() throws Throwable {
        //given
        ArrayList<?> mock = mock(ArrayList.class);
        mock.clear();
        Invocation invocationOnClass = new MockitoCore().getLastInvocation();
        //when
        new CallsRealMethods().validateFor(invocationOnClass);
        //then no exception is thrown
    }
}
