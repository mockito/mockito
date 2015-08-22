package org.mockitousage.bugs;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class InjectMocksAndSpyTest {

    // Inject here
    static class A {
        private B b;
    }

    // Inject here also
    static class B {
        private C c;
    }

    // Some dependency
    static class C {
    }

    @InjectMocks
    private final A a = new A();

    @Spy
    @InjectMocks
    private final B b = new B();

    @Mock
    private C c;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testArgumentShouldBeAMockButIsNonMock() {
        b.toString();
    }
}
