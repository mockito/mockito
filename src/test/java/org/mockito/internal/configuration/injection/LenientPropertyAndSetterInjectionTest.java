/*
 * Copyright (c) 2021 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

public class LenientPropertyAndSetterInjectionTest extends PropertyAndSetterInjectionTest {

    @Before
    @Override
    public void setup() {
        underTest = new LenientPropertyAndSetterInjection();
    }

    @Override
    public void should_fail_to_instantiate_one_arg_constructor() {
        // This test is not relevant for LenientPropertyAndSetterInjectionTest
    }

    @Test
    public void should_not_instantiate_one_arg_constructor() throws Exception {
        boolean result = underTest.process(field("oneArgConstructor"), this, new HashSet<>());

        assertThat(result).isFalse();
        assertThat(defaultConstructor).isNull();
    }

    @Override
    public void should_fail_to_instantiate_vararg_constructor() throws Exception {
        // This test is not relevant for LenientPropertyAndSetterInjectionTest
    }

    @Test
    public void should_not_instantiate_vararg_constructor() throws Exception {
        boolean result = underTest.process(field("varargConstructor"), this, new HashSet<>());

        assertThat(result).isFalse();
        assertThat(defaultConstructor).isNull();
    }
}
