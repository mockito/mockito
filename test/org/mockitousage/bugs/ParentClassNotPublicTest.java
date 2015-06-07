/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.fest.assertions.Assertions;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.misusing.CannotStubVoidMethodWithReturnValue;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockito.exceptions.misusing.MissingMethodInvocationException;
import org.mockito.exceptions.misusing.UnfinishedVerificationException;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.exceptions.MockitoLimitations;
import org.mockitoutil.TestBase;

/**
 * See bug 212
 *
 * Mocking methods that are declared on a non-public parent is not supported. We
 * cannot really fail fast during mock creation because one might mock a method
 * that is not declared on a parent - this would be valid.
 */
public class ParentClassNotPublicTest extends TestBase {

    class SuperClass {
        public boolean isValid() {
            return false;
        }

        public int arg(final Object o) {
            return 0;
        }
    }

    public class ClassForMocking extends SuperClass {
    }

    @Before
    public void only_if_CglibMockMaker() {
        // CGLIB issue ; not reproducible with ByteBuddy
        Assume.assumeTrue("CglibMockMaker".equals(Plugins.getMockMaker().getClass().getSimpleName()));
    }

    @Test
    public void hints_that_parent_not_public_during_stubbing() throws Exception {
        final ClassForMocking clazzMock = mock(ClassForMocking.class);
        try {
            when(clazzMock.isValid()).thenReturn(true);
            fail();
        } catch (final MissingMethodInvocationException e) {
            Assertions.assertThat(e.getMessage())
                    .contains(MockitoLimitations.NON_PUBLIC_PARENT);
        }
    }

    @Test
    public void hints_that_parent_not_public_during_stubbing_start() throws Exception {
        final ClassForMocking clazzMock = mock(ClassForMocking.class);
        mock(List.class).clear();
        try {
            // Mockito thinks that we're stubbing void 'clear' method here and
            // reports that boolean value cannot stub void method
            when(clazzMock.isValid()).thenReturn(true);
            fail();
        } catch (final CannotStubVoidMethodWithReturnValue e) {
            Assertions.assertThat(e.getMessage())
                    .contains(MockitoLimitations.NON_PUBLIC_PARENT);
        }
    }

    @Test
    public void hints_that_parent_not_public_during_verify() throws Exception {
        final ClassForMocking clazzMock = mock(ClassForMocking.class);
        verify(clazzMock).isValid();
        try {
            // Since Mockito did not see 'isValid()' method, we will report
            // unfinished verification
            verify(clazzMock);
            fail();
        } catch (final UnfinishedVerificationException e) {
            Assertions.assertThat(e.getMessage())
                    .contains(MockitoLimitations.NON_PUBLIC_PARENT);
        }
    }

    @Test
    public void hints_that_parent_not_public_when_misplaced_matchers_detected() throws Exception {
        final ClassForMocking clazzMock = mock(ClassForMocking.class);
        try {
            // Mockito does not see 'arg()' method so the anyObject() matcher is
            // reported as misplaced
            when(clazzMock.arg(anyObject())).thenReturn(0);
            fail();
        } catch (final InvalidUseOfMatchersException e) {
            Assertions.assertThat(e.getMessage())
                    .contains(MockitoLimitations.NON_PUBLIC_PARENT);
        }
    }
}
