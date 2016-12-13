package org.mockitousage.stubbing;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoLambda;
import org.mockitousage.IMethods;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

public class LambdaStubbingTest {

    @Test
    public void stubs_method_as_lambda() {
        IMethods mock = Mockito.mock(IMethods.class);
        MockitoLambda.when(mock::threeArgumentMethodWithStrings).isInvokedWith(anyInt(), anyString(), anyString()).thenReturn("foo");
    }

    @Test
    public void stubs_void_method_as_lambda() {
        IMethods mock = Mockito.mock(IMethods.class);
        MockitoLambda.when(mock::twoArgumentMethod).isInvokedWith(anyInt(), anyInt()).thenAnswer((a, b) -> {
            System.out.println("Answered " + (a * b) + " is type-safe!");
        });
    }
}
