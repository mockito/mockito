package org.mockitousage.annotation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class InjectionOfInlinedMockDeclarationTest {

    @InjectMocks private Receiver receiver;
    @InjectMocks private Receiver spiedReceiver = spy(new Receiver());

    private Antena antena = mock(Antena.class);
    private Tuner tuner = spy(new Tuner());

    @Test
    public void mock_declared_fields_shall_be_injected_too() throws Exception {
        assertNotNull(receiver.antena);
        assertNotNull(receiver.tuner);
    }

    @Test
    public void inject_mocks_even_in_declared_spy() throws Exception {
        assertNotNull(spiedReceiver.antena);
        assertNotNull(spiedReceiver.tuner);
    }

    // note that static class is not private !!
    static class Receiver {
        Antena antena;
        Tuner tuner;

        public boolean tune() { return true; }
    }

    private static class Antena { }
    private static class Tuner { }

}
