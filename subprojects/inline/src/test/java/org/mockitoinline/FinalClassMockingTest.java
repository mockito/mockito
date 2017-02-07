package org.mockitoinline;

import org.junit.Test;
import org.mockito.Mockito;

public class FinalClassMockingTest {

    @Test
    public void no_exception_while_mocking_final_class() throws Exception {
        Mockito.mock(FinalClass.class);
    }

    private static final class FinalClass {

    }

}
