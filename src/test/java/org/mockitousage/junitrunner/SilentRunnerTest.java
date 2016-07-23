package org.mockitousage.junitrunner;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by sfaber on 4/22/16.
 */
public class SilentRunnerTest extends TestBase {

    @RunWith(MockitoJUnitRunner.Silent.class)
    /**
     * The test class itself is passing but it has some unnecessary stubs
     */
    public static class HasUnnecessaryStubs {
        IMethods mock1 = when(mock(IMethods.class).simpleMethod(1)).thenReturn("1").getMock();
        IMethods mock2 = when(mock(IMethods.class).simpleMethod(2)).thenReturn("2").getMock();
        IMethods mock3 = when(mock(IMethods.class).simpleMethod(3)).thenReturn("3").getMock();

        @Test
        public void usesStub() {
            assertEquals("1", mock1.simpleMethod(1));
        }

        @Test
        public void usesStubWithDifferentArg() {
            assertEquals(null, mock2.simpleMethod(200));
            assertEquals(null, mock3.simpleMethod(300));
        }
    }

    @Test
    public void ignores_unused_stubs() {
        JUnitCore runner = new JUnitCore();
        //when
        Result result = runner.run(HasUnnecessaryStubs.class);
        //then
        assertThat(result).isSuccessful();
    }
}
