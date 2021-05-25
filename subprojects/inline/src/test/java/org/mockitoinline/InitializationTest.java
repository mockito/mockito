/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoinline;

import org.junit.Test;
import org.mockito.Mockito;

import static junit.framework.TestCase.assertEquals;

public class InitializationTest {

    @Test
    public void assure_initialization_prior_to_instrumentation() {
        @SuppressWarnings("unused")
        SampleEnum mock = Mockito.mock(SampleEnum.class);
        SampleEnum[] values = SampleEnum.values();
        assertEquals("VALUE", values[0].name());
    }

    public enum SampleEnum {
        VALUE
    }
}
