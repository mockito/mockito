package org.mockitousage.junitrunner;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by sfaber on 4/22/16.
 */
public class UnusedStubsExceptionMessageTest extends TestBase {

    //Moving the code around this class is tricky and may cause the test to fail
    //We're asserting on full exception message which contains line numbers
    //Let's leave it for now, updating the test is cheap and if it turns out hindrance we can make the assertion smarter.
    @RunWith(MockitoJUnitRunner.class)
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
    public void lists_all_unused_stubs_cleanly() {
        JUnitCore runner = new JUnitCore();
        //when
        Result result = runner.run(HasUnnecessaryStubs.class);
        //then
        Failure failure = result.getFailures().get(0);
        assertEquals("\n" +
                        "Unnecessary stubbings detected in test class: HasUnnecessaryStubs\n" +
                        "To keep the tests clean it is important to remove unnecessary code.\n" +
                        "Following stubbings are declared in test but not realized during test execution:\n" +
                        "  1. -> at org.mockitousage.junitrunner.UnusedStubsExceptionMessageTest$HasUnnecessaryStubs.<init>(UnusedStubsExceptionMessageTest.java:0)\n" +
                        "  2. -> at org.mockitousage.junitrunner.UnusedStubsExceptionMessageTest$HasUnnecessaryStubs.<init>(UnusedStubsExceptionMessageTest.java:0)\n" +
                        "Please remove unnecessary stubbings. More info: javadoc for UnnecessaryStubbingException class.",
                filterLineNo(failure.getException().getMessage()));
    }
}
