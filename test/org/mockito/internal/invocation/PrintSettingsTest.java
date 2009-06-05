package org.mockito.internal.invocation;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.internal.matchers.Equals;

public class PrintSettingsTest {
    
    @Test
    public void shouldConfigureToPrintVerboselyASpecificMatcher() {
        //given
        Equals toPrintVerbosely = new Equals(1);
        PrintSettings settings = PrintSettings.verboseMatchers(toPrintVerbosely);
        //when
        boolean printsVerbosely = settings.printsVerbosely(toPrintVerbosely);
        //then
        assertTrue(printsVerbosely);
    }

    @Test
    public void shouldNotConfigureToPrintVerboselyMatcherThatIsNotTheSame() {
        //given
        PrintSettings settings = PrintSettings.verboseMatchers(new Equals(1));
        //when
        boolean printsVerbosely = settings.printsVerbosely(new Equals(1));
        //then
        assertFalse(printsVerbosely);
    }
}