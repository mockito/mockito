package org.mockitousage.testng.failuretests;

import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockito.testng.MockitoTestNGListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.PrintStream;

import static org.mockito.Matchers.anySet;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;

/**
 * Should fail.
 *
 * @see TestNGShouldFailWhenMockitoListenerFailsTest
 */
@Listeners(MockitoTestNGListener.class)
@Test(description = "Always failing, shouldn't be listed in 'mockito-testng.xml'")
public class FailingOnPurposeBecauseIncorrectStubbingSyntax {

    @Test(expectedExceptions = InvalidUseOfMatchersException.class)
    public void incorrect_stubbing_syntax_in_test() throws Exception {
        mock(PrintStream.class);
        anyString();
        anySet();
    }

}
