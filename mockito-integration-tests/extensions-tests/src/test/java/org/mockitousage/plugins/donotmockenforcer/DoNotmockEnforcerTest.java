/*
 * Copyright (c) 2019 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.plugins.donotmockenforcer;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import org.junit.Test;
import org.mockito.DoNotMock;
import org.mockito.MockedStatic;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.DoNotMockException;

public class DoNotmockEnforcerTest {

    @Test
    public void uses_custom_enforcer_disallows_mocks_of_types_in_this_package() {
        assertThatThrownBy(
                        () -> {
                            NotMockable notMockable = mock(NotMockable.class);
                        })
                .isInstanceOf(DoNotMockException.class);
    }

    @Test
    public void uses_custom_enforcer_allows_special_cased_class() {
        NotMockableButSpecialCased notMockable = mock(NotMockableButSpecialCased.class);
    }

    @Test
    public void uses_custom_enforcer_allows_statically_non_mockable() {
        StaticallyNotMockable notMockable = mock(StaticallyNotMockable.class);
    }

    @Test
    public void uses_custom_enforcer_disallows_static_mocks_of_type_with_specific_name() {
        assertThatThrownBy(
                        () -> {
                            MockedStatic<StaticallyNotMockable> notMockable =
                                    mockStatic(StaticallyNotMockable.class);
                        })
                .isInstanceOf(DoNotMockException.class)
                .hasMessage("Cannot mockStatic!");
    }

    @Test
    public void
            uses_custom_enforcer_disallows_static_mocks_of_type_that_inherits_from_non_statically_mockable() {
        assertThatThrownBy(
                        () -> {
                            MockedStatic<StaticallyNotMockableChild> notMockable =
                                    mockStatic(StaticallyNotMockableChild.class);
                        })
                .isInstanceOf(DoNotMockException.class)
                .hasMessage("Cannot mockStatic!");
    }

    @Test
    public void uses_custom_enforcer_allows_static_mocks_of_type_with_specific_name() {
        /*
         Current MockMaker does not support static mocks, so asserting we get its exception rather than
         a DoNotMockException is enough to assert the DoNotMockEnforcer let it through.
        */
        assertThatThrownBy(
                        () -> {
                            MockedStatic<StaticallyMockable> notMockable =
                                    mockStatic(StaticallyMockable.class);
                        })
                .isInstanceOf(MockitoException.class)
                .isNotInstanceOf(DoNotMockException.class)
                .hasMessageContaining("does not support the creation of static mocks");
    }

    @Test
    public void uses_custom_enforcer_has_custom_message() {
        assertThatThrownBy(
                        () -> {
                            NotMockable notMockable = mock(NotMockable.class);
                        })
                .isInstanceOf(DoNotMockException.class)
                .hasMessage("Custom message!");
    }

    static class NotMockable {}

    @DoNotMock
    static class NotMockableButSpecialCased {}

    static class StaticallyNotMockable {}

    static class StaticallyNotMockableChild extends StaticallyNotMockable {}

    static class StaticallyMockable {}
}
