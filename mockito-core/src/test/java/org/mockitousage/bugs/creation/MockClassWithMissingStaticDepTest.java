/*
 * Copyright (c) 2024 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs.creation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;

// See Issue #3498
public class MockClassWithMissingStaticDepTest {

    @Test
    public void shouldNotSwallowClinitErrorOfClassToMock() {
        try {
            @SuppressWarnings("unused")
            var unused = Mockito.mock(ClassWithErrorInClassInit.class);

            fail();
        } catch (MockitoException e) {
            var cause = e.getCause();
            assertThat(cause).isInstanceOf(MockitoException.class);
            assertThat(cause.getMessage())
                    .isEqualTo(
                            "Cannot instrument class org.mockitousage.bugs.creation.MockClassWithMissingStaticDepTest$ClassWithErrorInClassInit because it or one of its supertypes could not be initialized");

            var cause2 = cause.getCause();
            assertThat(cause2).isInstanceOf(NoClassDefFoundError.class);
            assertThat(cause2.getMessage())
                    .isEqualTo("Simulate missing transitive dependency used in class init.");
        } catch (NoClassDefFoundError ex) {
            // Note: mock-maker-subclass will throw the NoClassDefFoundError as is
            assertThat(ex.getMessage())
                    .isEqualTo("Simulate missing transitive dependency used in class init.");
        }
    }

    private static class ClassWithErrorInClassInit {
        static {
            //noinspection ConstantValue
            if (true) {
                throw new NoClassDefFoundError(
                        "Simulate missing transitive dependency used in class init.");
            }
        }
    }

    @Test
    public void shouldNotSwallowClinitExceptionOfClassToMock() {
        try {
            @SuppressWarnings("unused")
            var unused = Mockito.mock(ClassWithExceptionInClassInit.class);

            fail();
        } catch (MockitoException e) {
            var cause = e.getCause();
            assertThat(cause).isInstanceOf(MockitoException.class);
            assertThat(cause.getMessage())
                    .isEqualTo(
                            "Cannot instrument class org.mockitousage.bugs.creation.MockClassWithMissingStaticDepTest$ClassWithExceptionInClassInit because it or one of its supertypes could not be initialized");

            var cause2 = cause.getCause();
            assertThat(cause2).isInstanceOf(IllegalStateException.class);
            assertThat(cause2.getMessage()).isEqualTo("Exception in class init.");
        } catch (ExceptionInInitializerError ex) {
            // Note: mock-maker-subclass will throw the ExceptionInInitializerError as is
            var cause = ex.getCause();
            assertThat(cause).isInstanceOf(IllegalStateException.class);
            assertThat(cause.getMessage()).isEqualTo("Exception in class init.");
        }
    }

    private static class ClassWithExceptionInClassInit {
        static {
            //noinspection ConstantValue
            if (true) {
                throw new IllegalStateException("Exception in class init.");
            }
        }
    }
}
