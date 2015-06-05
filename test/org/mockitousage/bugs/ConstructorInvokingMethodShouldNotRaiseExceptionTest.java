package org.mockitousage.bugs;

import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class ConstructorInvokingMethodShouldNotRaiseExceptionTest {
    @Spy HasConstructorInvokingMethod hasConstructorInvokingMethod;

    @Test
    public void should_be_able_to_create_spy() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    private static class HasConstructorInvokingMethod {
        public HasConstructorInvokingMethod() {
            someMethod();
        }
        void someMethod() { }
    }
}
