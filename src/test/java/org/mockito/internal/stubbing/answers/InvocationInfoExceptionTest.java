/*
 * Copyright (c) 2022 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.invocation.Invocation;

import java.nio.charset.CharacterCodingException;
import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class InvocationInfoExceptionTest {

    private final String methodName;

    public InvocationInfoExceptionTest(final String methodName) {
        this.methodName = methodName;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(
                new Object[][] {
                    {"throwException"},
                    {"parentThrowsException"},
                    {"grandParentThrowsException"},
                    {"interfaceThrowsException"},
                    {"grandInterfaceThrowsException"},
                    {"interfaceOfParentThrowsException"}
                });
    }

    @Test
    public void should_know_valid_throwables() throws Exception {
        // when
        final Invocation invocation =
                new InvocationBuilder()
                        .method(methodName)
                        .mockClass(CurrentClass.class)
                        .toInvocation();
        final InvocationInfo info = new InvocationInfo(invocation);

        // then
        assertThat(info.isValidException(new Exception())).isFalse();
        assertThat(info.isValidException(new CharacterCodingException())).isTrue();
    }

    private abstract static class GrandParent {
        public abstract void grandParentThrowsException() throws CharacterCodingException;
    }

    private interface InterfaceOfParent {
        abstract void interfaceOfParentThrowsException() throws CharacterCodingException;
    }

    private abstract static class Parent extends GrandParent implements InterfaceOfParent {
        public abstract void parentThrowsException() throws CharacterCodingException;
    }

    private interface GrandInterface {
        void grandInterfaceThrowsException() throws CharacterCodingException;
    }

    private interface Interface extends GrandInterface {
        void interfaceThrowsException() throws CharacterCodingException;
    }

    private static class CurrentClass extends Parent implements Interface {

        public void throwException() throws CharacterCodingException {}

        @Override
        public void grandParentThrowsException() {}

        @Override
        public void parentThrowsException() {}

        @Override
        public void grandInterfaceThrowsException() {}

        @Override
        public void interfaceThrowsException() {}

        @Override
        public void interfaceOfParentThrowsException() {}
    }
}
