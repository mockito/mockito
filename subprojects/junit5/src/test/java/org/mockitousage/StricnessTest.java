package org.mockitousage;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit5.Strictness;
import org.mockito.junit5.WithMockito;

import java.util.function.Predicate;

import static org.mockito.Mockito.when;
import static org.mockito.quality.Strictness.*;

@WithMockito
class StricnessTest {

    @Mock
    Predicate mock;

    @Test
    void strictnessFromTestRoot_defaultIsWarn_shouldLogUnnecessaryStubbingException() {
        when(mock.test(1)).thenReturn(true);

        //FIXME how to trigger MockitoSession.finishMocking() here and check that unneccesssay stubbing was logged

    }

    @Test
    @Strictness(LENIENT)
    void strictnessFromTestMethod_lenient_shouldIgnoreUnnecessaryStubbing() {
        when(mock.test(1)).thenReturn(true);


        //FIXME how to trigger MockitoSession.finishMocking() here and check that unneccesssay stubbing was ignored
    }

    @Nested
    @Strictness(WARN)
    class NestedTest{

        @Mock
        Predicate mock;

        @Test
        void strictnessFromNestedTest_warn_shouldLogUnnecessaryStubbing() {
            when(mock.test(1)).thenReturn(true);

            //FIXME how to trigger MockitoSession.finishMocking() here and check that unneccesssay stubbing was logged
        }

        @Nested
        @Strictness(STRICT_STUBS)
        class NestedTest2{
            @Mock
            Predicate mock;

            @Test
            @Disabled
            void strictnessFromNestedTest_strict_shouldThrowUnnecessaryStubbing() {
                when(mock.test(1)).thenReturn(true);

                //FIXME how to trigger MockitoSession.finishMocking() here and check that unneccesssay stubbing was thrown
            }
        }
    }
}
