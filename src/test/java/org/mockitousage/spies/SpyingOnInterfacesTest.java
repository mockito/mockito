/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.spies;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockitoutil.TestBase;

import java.util.List;

import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings({"unchecked"})
public class SpyingOnInterfacesTest extends TestBase {

    @Test
    public void shouldFailFastWhenCallingRealMethodOnInterface() throws Exception {
        List<?> list = mock(List.class);
        try {
            //when
            when(list.get(0)).thenCallRealMethod();
            //then
            fail();
        } catch (MockitoException e) {
        }
    }

    @Test
    public void shouldFailInRuntimeWhenCallingRealMethodOnInterface() throws Exception {
        //given
        List<Object> list = mock(List.class);
        when(list.get(0)).thenAnswer(
                new Answer<Object>() {
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        return invocation.callRealMethod();
                    }
                }
        );
        try {
            //when
            list.get(0);
            //then
            fail();
        } catch (MockitoException e) {
        }
    }

    @Test
    public void shouldAllowDelegatingToDefaultMethod() throws Exception {
        assumeTrue("Test can only be executed on Java 8 capable VMs", ClassFileVersion.ofThisVm().isAtLeast(ClassFileVersion.JAVA_V8));

        Class<?> type = new ByteBuddy()
                .makeInterface()
                .defineMethod("foo", String.class, Visibility.PUBLIC)
                .intercept(FixedValue.value("bar"))
                .make()
                .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();

        Object object = mock(type);

        //when
        when(type.getMethod("foo").invoke(object)).thenCallRealMethod();
        //then
        Assertions.assertThat(type.getMethod("foo").invoke(object)).isEqualTo((Object) "bar");
        type.getMethod("foo").invoke(verify(object));
    }

    @Test
    public void shouldAllowSpyingOnDefaultMethod() throws Exception {
        assumeTrue("Test can only be executed on Java 8 capable VMs", ClassFileVersion.ofThisVm().isAtLeast(ClassFileVersion.JAVA_V8));

        Class<?> iFace = new ByteBuddy()
                .makeInterface()
                .defineMethod("foo", String.class, Visibility.PUBLIC)
                .intercept(FixedValue.value("bar"))
                .make()
                .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();

        Class<?> impl = new ByteBuddy()
                .subclass(iFace)
                .make()
                .load(iFace.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();

        Object object = spy(impl.newInstance());

        //when
        Assertions.assertThat(impl.getMethod("foo").invoke(object)).isEqualTo((Object) "bar");
        //then
        impl.getMethod("foo").invoke(verify(object));
    }
}
