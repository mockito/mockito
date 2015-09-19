package org.mockitousage.bugs.creation;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.Random;

@RunWith(Enclosed.class)
public class ConstructorInvokingMethodShouldNotRaiseExceptionTest {

    public static class WithDumbMethod {
        @Spy
        HasConstructorInvokingMethod hasConstructorInvokingMethod;

        @Test
        public void should_be_able_to_create_spy() throws Exception {
            MockitoAnnotations.initMocks(this);
        }

        private static class HasConstructorInvokingMethod {
            public HasConstructorInvokingMethod() { someMethod(); }

            void someMethod() { }
        }
    }

    public static class UsingMethodObjectReferenceResult {
        @Spy
        HasConstructorInvokingMethod hasConstructorInvokingMethod;

        @Test
        public void should_be_able_to_create_spy() throws Exception {
            MockitoAnnotations.initMocks(this);
        }

        private static class HasConstructorInvokingMethod {
            private final boolean doesIt;
            public HasConstructorInvokingMethod() {
                doesIt = someMethod().contains("yup");
            }

            String someMethod() { return "tada!"; }
        }
    }

    public static class UsingMethodPrimitiveResult {
        @Spy
        HasConstructorInvokingMethod hasConstructorInvokingMethod;

        @Test
        public void should_be_able_to_create_spy() throws Exception {
            MockitoAnnotations.initMocks(this);
        }

        private static class HasConstructorInvokingMethod {
            private final boolean doesIt;
            public HasConstructorInvokingMethod() {
                doesIt = someMethod();
            }

            boolean someMethod() { return new Random().nextBoolean(); }
        }
    }
}
