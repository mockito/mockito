package org.mockitousage.testng;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class DontResetMocksIfNoListenerTest {

    @Mock private Map map;

    @BeforeMethod
    public void init_mocks() {
        MockitoAnnotations.initMocks(this);
        when(map.get("the answer to ...")).thenReturn(42);
    }

    @Test
    public void mock_behavior_not_resetted_1() throws IOException {
        assertThat(map.get("the answer to ...")).isEqualTo(42);
    }

    @Test
    public void mock_behavior_not_resetted_2() throws IOException {
        assertThat(map.get("the answer to ...")).isEqualTo(42);
    }

}
