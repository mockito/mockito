package org.mockitousage.stubbing;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoLambda;
import org.mockitousage.IMethods;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoLambda.anyInt;
import static org.mockito.MockitoLambda.anyString;

public class LambdaStubbingTest {

    @Test
    public void stubs_method_as_lambda() {
        IMethods mock = Mockito.mock(IMethods.class);
        MockitoLambda.when(mock::threeArgumentMethodWithStrings).isInvokedWith(anyInt(), anyString(), anyString()).thenReturn("foo");
        assertThat(mock.threeArgumentMethodWithStrings(5, "foo", "bar")).isEqualTo("foo");
    }

    @Test
    public void stubs_void_method_as_lambda() {
        IMethods mock = Mockito.mock(IMethods.class);
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        MockitoLambda.when(mock::twoArgumentMethod).isInvokedWith(anyInt(), anyInt()).thenAnswer((a, b) -> {
            atomicBoolean.set(true);
        });
        mock.twoArgumentMethod(0, 1);
        assertThat(atomicBoolean.get()).isEqualTo(true);
    }
}
