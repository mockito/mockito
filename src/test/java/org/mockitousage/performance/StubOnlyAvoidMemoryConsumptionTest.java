package org.mockitousage.performance;

import org.junit.Ignore;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class StubOnlyAvoidMemoryConsumptionTest {

    @Test
    public void using_stub_only_wont_thrown_an_OutOfMemoryError() {
        Object obj = mock(Object.class, withSettings().stubOnly());
        when(obj.toString()).thenReturn("asdf");

        for (int i = 0; i < 1000000; i++) {
            obj.toString();
        }
    }

    @Test
    @Ignore("ignored because it will detonate our test suite with an OOM for real")
    public void without_stub_only_mocks_will_store_invocations_leading_to_an_OutOfMemoryError() {
        Object obj = mock(Object.class, withSettings());
        when(obj.toString()).thenReturn("asdf");

        for (int i = 0; i < 1000000; i++) {
            obj.toString();
        }
    }
}
