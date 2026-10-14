/*
 * Copyright (c) 2021 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
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

    @Test
    public void preserves_interface_parameter_metadata_when_clearing_all_caches() throws Exception {
        Class<?> typeWithParameters =
                new ByteBuddy()
                        .makeInterface()
                        .defineMethod("foo", void.class, Visibility.PUBLIC)
                        .withParameter(String.class, "bar")
                        .withoutCode()
                        .make()
                        .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                        .getLoaded();

        var unused = Mockito.mock(typeWithParameters);

        Mockito.clearAllCaches();

        assertThat(typeWithParameters.getMethod("foo", String.class).getParameters()[0].getName())
                .isEqualTo("bar");
    }

    abstract static class Base {}
}
