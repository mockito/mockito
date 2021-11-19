/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.plugins.donotmockenforcer;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.DoNotMock;
import org.mockito.exceptions.misusing.DoNotMockException;

public class DoNotmockEnforcerTest {

    @Test
    public void uses_custom_enforcer_disallows_mocks_of_types_in_this_package() {
        assertThatThrownBy(() -> {
            NotMockable notMockable = mock(NotMockable.class);
        }).isInstanceOf(DoNotMockException.class);
    }

    @Test
    public void uses_custom_enforcer_allows_special_cased_class() {
        NotMockableButSpecialCased notMockable = mock(NotMockableButSpecialCased.class);
    }

    @Test
    public void uses_custom_enforcer_has_custom_message() {
        assertThatThrownBy(() -> {
            NotMockable notMockable = mock(NotMockable.class);
        }).isInstanceOf(DoNotMockException.class).hasMessage("Custom message!");
    }

    static class NotMockable {

    }

    @DoNotMock
    static class NotMockableButSpecialCased {

    }
}
