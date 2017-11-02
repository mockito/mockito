/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.listeners;

import org.junit.Test;
import org.mockito.MockingDetails;
import org.mockitoutil.TestBase;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.withSettings;

public class VerificationStartedNotifierTest extends TestBase {

    MockingDetails mockingDetails = mockingDetails(mock(List.class));

    @Test
    public void does_not_do_anything_when_list_is_empty() throws Exception {
        //expect nothing to happen
        VerificationStartedNotifier.notifyVerificationStarted((List) emptyList(), mockingDetails);
    }

    @Test
    public void decent_exception_when_setting_non_mock() throws Exception {
        VerificationStartedNotifier.Event event = new VerificationStartedNotifier.Event(mockingDetails);

        try {
            //when
            event.setMock("not a mock");
            fail();
        } catch (Exception e) {
            //then
            assertEquals("VerificationStartedEvent.setMock() does not accept parameter which is not a Mockito mock.\n" +
                "  Received parameter: \"not a mock\".\n" +
                "  See the Javadoc.", e.getMessage());
        }
    }

    @Test
    public void shows_clean_exception_message_when_illegal_null_arg_is_used() throws Exception {
        VerificationStartedNotifier.Event event = new VerificationStartedNotifier.Event(mockingDetails);

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
        VerificationStartedNotifier.Event event = new VerificationStartedNotifier.Event(mockingDetails);

        try {
            //when
            event.setMock(differentTypeMock);
            fail();
        } catch (Exception e) {
            //then
            assertEquals(filterHashCode("VerificationStartedEvent.setMock() does not accept parameter which is not the same type as the original mock.\n" +
                "  Required type: java.util.List\n" +
                "  Received parameter: Mock for Set, hashCode: xxx.\n" +
                "  See the Javadoc."), filterHashCode(e.getMessage()));
        }
    }

    @Test
    public void decent_exception_when_setting_mock_that_does_not_implement_all_desired_interfaces() throws Exception {
        final Set mock = mock(Set.class, withSettings().extraInterfaces(List.class));
        final Set missingExtraInterface = mock(Set.class);
        VerificationStartedNotifier.Event event = new VerificationStartedNotifier.Event(mockingDetails(mock));

        try {
            //when setting mock that does not have all necessary interfaces
            event.setMock(missingExtraInterface);
            fail();
        } catch (Exception e) {
            //then
            assertEquals(filterHashCode("VerificationStartedEvent.setMock() does not accept parameter which does not implement all extra interfaces of the original mock.\n" +
                "  Required type: java.util.Set\n" +
                "  Required extra interface: java.util.List\n" +
                "  Received parameter: Mock for Set, hashCode: xxx.\n" +
                "  See the Javadoc."), filterHashCode(e.getMessage()));
        }
    }

    @Test
    public void accepts_replacement_mock_if_all_types_are_compatible() throws Exception {
        final Set mock = mock(Set.class, withSettings().extraInterfaces(List.class, Map.class));
        final Set compatibleMock = mock(Set.class, withSettings().extraInterfaces(List.class, Map.class));
        VerificationStartedNotifier.Event event = new VerificationStartedNotifier.Event(mockingDetails(mock));

        //when
        event.setMock(compatibleMock);

        //then
        assertEquals(compatibleMock, event.getMock());
    }
}
