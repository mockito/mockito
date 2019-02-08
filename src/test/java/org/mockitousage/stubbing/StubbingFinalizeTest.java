package org.mockitousage.stubbing;

import static org.mockito.Mockito.mock;

import org.junit.Test;

public class StubbingFinalizeTest {

    interface FinalizeInterface {
        void finalize();
    }

    @Test
    public void should_not_throw_illegal_access_error_when_stubbing_finalize() {
        FinalizeInterface finalized = mock(FinalizeInterface.class);

        finalized.finalize();
    }

}
