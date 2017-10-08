package org.mockito.internal.listeners;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.listeners.VerificationStartedListener;

import java.util.List;
import java.util.Set;

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
            assertEquals("VerificationStartedEvent.setMock() does not accept null parameter. See the Javadoc.", e.getMessage());
        }
    }

    @Test
    public void decent_exception_when_setting_non_mock() throws Exception {
        VerificationStartedNotifier.Event event = new VerificationStartedNotifier.Event();

        try {
            //when
            event.setMock("not a mock");
            fail();
        } catch (Exception e) {
            //then
            assertEquals("VerificationStartedEvent.setMock() does not accept parameter which is not a Mockito mock. See the Javadoc.", e.getMessage());
        }
    }

    @Test
    public void shows_clean_exception_message_when_illegal_null_arg_is_used() throws Exception {
        VerificationStartedNotifier.Event event = new VerificationStartedNotifier.Event();

        try {
            //when
            event.setMock(null);
            fail();
        } catch (Exception e) {
            //then
            assertEquals("VerificationStartedEvent.setMock() does not accept null parameter. See the Javadoc.", e.getMessage());
        }
    }

    @Ignore //TODO! not implemented yet
    @Test
    public void decent_exception_when_setting_mock_of_wrong_type() throws Exception {
        final Set differentTypeMock = mock(Set.class);
        VerificationStartedNotifier.Event event = new VerificationStartedNotifier.Event();

        try {
            //when
            event.setMock(differentTypeMock);
            fail();
        } catch (Exception e) {
            //then
            assertEquals("???", e.getMessage());
        }
    }
}
