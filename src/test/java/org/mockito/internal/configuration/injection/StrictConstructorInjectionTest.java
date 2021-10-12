/*
 * Copyright (c) 2021 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StrictConstructorInjectionTest extends ConstructorInjectionTest {

    @Before
    public void initialize_dependencies() {
        underTest = new StrictConstructorInjection();
    }

    @Override
    public void should_instantiate_with_null_arg() {
        // This test is not relevant for StrictConstructorInjection
    }

    @Test
    public void should_not_instantiate_with_null_arg() throws Exception {
        boolean result = underTest.process(field("whatever"), this, new HashSet<>());

        assertThat(result).isFalse();
        assertThat(whatever).isNull();
    }
}
