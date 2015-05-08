package org.mockito.verification;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.FriendlyReminderException;

public class NegativeDurationTest {

    @Test
    public void should_throw_exception_when_duration_is_negative_for_timeout_method() {
        try {
            Mockito.timeout(-1);
            Assert.fail("It is forbidden to invoke Mockito.timeout() with negative value.");
        } catch (FriendlyReminderException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void should_throw_exception_when_duration_is_negative_for_after_method() {
        try {
            Mockito.after(-1);
            Assert.fail("It is forbidden to invoke Mockito.after() with negative value.");
        } catch (FriendlyReminderException e) {
            Assert.assertTrue(true);
        }
    }
}
