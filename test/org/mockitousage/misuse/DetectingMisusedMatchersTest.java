package org.mockitousage.misuse;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.StateMaster;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockitousage.IMethods;
import org.mockitoutil.ExtraMatchers;
import org.mockitoutil.TestBase;

public class DetectingMisusedMatchersTest extends TestBase {

    class WithFinal {
        final Object finalMethod(Object object) {
            return null;
        }
    }

    @Mock private WithFinal withFinal;
    
    @Before
    @After
    public void resetState() {
        StateMaster.reset();
    }

    private void misplacedArgumentMatcher() {
        anyObject();
    }

    @Ignore
    @Test
    public void shouldFailFastWhenArgumentMatchersAbused() {
        misplacedArgumentMatcher();
        try {
            mock(IMethods.class);
            fail();
        } catch (InvalidUseOfMatchersException e) {
            assertThat(e, messageContains("Misplaced argument matcher"));
            assertThat(e.getCause(), ExtraMatchers.hasFirstMethodInStackTrace("misplacedArgumentMatcher"));
        }
    }
}