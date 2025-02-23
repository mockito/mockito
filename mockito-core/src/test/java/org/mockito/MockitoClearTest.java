/*
 * Copyright (c) 2021 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MockitoClearTest {

    @Test
    public void can_clear_mock() {
        Base mock = Mockito.mock(Base.class);
        assertThat(Mockito.mock(Base.class).getClass()).isEqualTo(mock.getClass());

        Mockito.clearAllCaches();

        assertThat(Mockito.mock(Base.class).getClass()).isNotEqualTo(mock.getClass());
    }

    abstract static class Base {}
}
