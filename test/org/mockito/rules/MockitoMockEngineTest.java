package org.mockito.rules;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class MockitoMockEngineTest {

    interface Intf {

    }

    private final MockitoMockEngine mockEngine = MockitoMockEngine.getInstance();

    @Test
    public void shouldMock() {
        // given

        // when
        final Intf mock = mockEngine.mock(Intf.class);

        // then
        assertThat(mock).isNotNull(); // FIXME: better assertion?
    }

}
