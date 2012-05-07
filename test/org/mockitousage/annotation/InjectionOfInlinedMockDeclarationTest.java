/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.annotation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class InjectionOfInlinedMockDeclarationTest {

    @InjectMocks private Receiver receiver;
    @InjectMocks private Receiver spiedReceiver = spy(new Receiver());

    private Antenna oldAntenna = mock(Antenna.class);
    private Antenna satelliteAntenna = mock(Antenna.class);
    private Antenna antenna = mock(Antenna.class, "dvbtAntenna");
    private Tuner tuner = spy(new Tuner());

    @Test
    public void mock_declared_fields_shall_be_injected_too() throws Exception {
        assertNotNull(receiver.oldAntenna);
        assertNotNull(receiver.satelliteAntenna);
        assertNotNull(receiver.dvbtAntenna);
        assertNotNull(receiver.tuner);
    }

    @Test
    public void unnamed_mocks_should_be_resolved_withe_their_field_names() throws Exception {
        assertSame(oldAntenna, receiver.oldAntenna);
        assertSame(satelliteAntenna, receiver.satelliteAntenna);
    }

    @Test
    public void named_mocks_should_be_resolved_with_their_name() throws Exception {
        assertSame(antenna, receiver.dvbtAntenna);
    }


    @Test
    public void inject_mocks_even_in_declared_spy() throws Exception {
        assertNotNull(spiedReceiver.oldAntenna);
        assertNotNull(spiedReceiver.tuner);
    }

    // note that static class is not private !!
    static class Receiver {
        Antenna oldAntenna;
        Antenna satelliteAntenna;
        Antenna dvbtAntenna;
        Tuner tuner;

        public boolean tune() { return true; }
    }

    private static class Antenna { }
    private static class Tuner { }

}
