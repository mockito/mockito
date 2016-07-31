package org.mockitousage.testng;

import org.mockito.Mock;
import org.testng.annotations.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class InitializeChildTestWhenParentHasListenerTest extends ParentTest {

    @Mock Map<?, ?> childMockField;

    @Test
    @SuppressWarnings("unchecked")
    public void verify_mocks_are_initialized() throws Exception {
        assertThat(childMockField).isNotNull();
        assertThat(parentMockField).isNotNull();
    }
}
