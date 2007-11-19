package org.mockito.internal;

import static org.junit.Assert.*;

import org.junit.*;
import org.mockito.exceptions.InvalidUseOfMatchersException;
import org.mockito.internal.matchers.Equals;

@SuppressWarnings("unchecked")
public class MockitoControlTest {
    
    @Before
    @After
    public void resetState() {
        StateResetter.reset();
    }
    
    @Test
    public void shouldRemoveVerificationModeEvenWhenInvalidMatchers() throws Throwable {
        MockitoState state = MockitoState.instance(); 
        LastArguments lastArguments = LastArguments.instance();
        
        lastArguments.reportMatcher(new Equals("test"));
        state.verifyingStarted(VerifyingMode.anyTimes());
        
        MockitoControl control = new MockitoControl(state, lastArguments);

        try {
            control.invoke(null, String.class.getDeclaredMethod("toString"), new Object[]{});
            fail();
        } catch (InvalidUseOfMatchersException e) {
        }
        
        assertNull(state.pullVerifyingMode());
    }
}
