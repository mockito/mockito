package org.mockito.internal.listeners;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.MockitoMock;
import org.mockitoutil.TestBase;

import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

public class VerificationStartedNotifierTest extends TestBase {

    MockitoMock mockitoMock = MockUtil.getMockitoMock(Mockito.mock(List.class));

    @Test
    public void does_not_do_anything_when_list_is_empty() throws Exception {
        //expect nothing to happen
        VerificationStartedNotifier.notifyVerificationStarted((List) emptyList(), mockitoMock);
    }

    @Test
    public void decent_exception_when_setting_non_mock() throws Exception {
        VerificationStartedNotifier.Event event = new VerificationStartedNotifier.Event(mockitoMock);

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
        VerificationStartedNotifier.Event event = new VerificationStartedNotifier.Event(mockitoMock);

        try {
            //when
            event.setMock(null);
            fail();
        } catch (Exception e) {
            //then
            assertEquals("VerificationStartedEvent.setMock() does not accept null parameter. See the Javadoc.", e.getMessage());
        }
    }

    @Test
    public void decent_exception_when_setting_mock_of_wrong_type() throws Exception {
        final Set differentTypeMock = mock(Set.class);
        VerificationStartedNotifier.Event event = new VerificationStartedNotifier.Event(mockitoMock);

        try {
            //when
            event.setMock(differentTypeMock);
            fail();
        } catch (Exception e) {
            //then
            assertEquals("VerificationStartedEvent.setMock() does not accept parameter which does not implement/extend the original mock type: java.util.List. See the Javadoc.", e.getMessage());
        }
    }

    @Test
    @Ignore //TODO! not yet implemented
    public void decent_exception_when_setting_mock_that_does_not_implement_all_desired_interfaces() throws Exception {
        final Set mock = mock(Set.class, withSettings().extraInterfaces(List.class));
        final Set missingExtraInterface = mock(Set.class);
        VerificationStartedNotifier.Event event = new VerificationStartedNotifier.Event(mockitoMock);

        try {
            //when
            event.setMock(missingExtraInterface);
            fail();
        } catch (Exception e) {
            //then
            assertEquals("???", e.getMessage());
        }
    }

    interface Foo {}
}
