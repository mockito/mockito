package org.mockitousage.annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class InitMocksReturnsMocksTest {

    @Mock
    List list;

    @Mock
    Set set;

    @Spy
    List list2 = new ArrayList();

    @Test
    public void shouldReturnMocks() {
        List mocks = MockitoAnnotations.initMocks(this);

        assertEquals(3, mocks.size());
        assertTrue(mocks.contains(set));
        assertTrue(mocks.contains(list));
        assertTrue(mocks.contains(list2));
    }

    @Test
    public void noMocksShouldReturnEmptyList() {
        List mocks = MockitoAnnotations.initMocks(new NoMocksClass());

        assertEquals(0, mocks.size());
    }

    @SuppressWarnings("unused")
    private class NoMocksClass {
        List list;

    }
}
