package org.mockito.internal.listeners;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.listeners.VerificationStartedListener;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

public class VerificationStartedNotifierTest {

    @Test
    public void does_not_do_anything_when_list_is_empty() throws Exception {
        //when
        VerificationStartedNotifier.notifyVerificationStarted((List) emptyList(), null);

        //then nothing happens even though we passed illegal null value
    }

    @Test
    public void detects_illegal_null_argument() throws Exception {
        try {
            //when
            VerificationStartedNotifier.notifyVerificationStarted(asList(mock(VerificationStartedListener.class)), null);
            fail();
        } catch (MockitoException e) {
            //then
            assertEquals("Null passed to VerificationStartedEvent.setMock() method.\n" +
                "Null is not acceptable, see Javadoc for VerificationStartedListener for API information.", e.getMessage());
        }
    }

}
