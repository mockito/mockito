/*
 * Copyright (c) 2022 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.Matchers.not;
import static org.junit.Assume.assumeThat;

@RunWith(Enclosed.class)
public class MockTest {

    @RunWith(value = Parameterized.class)
    public static class StrictnessToMockStrictnessTest {

        public org.mockito.quality.Strictness strictness;

        public StrictnessToMockStrictnessTest(org.mockito.quality.Strictness strictness) {
            this.strictness = strictness;
        }

        @Test
        public void should_have_matching_enum_in_mock_strictness_enum() {
            Mock.Strictness.valueOf(strictness.name());
        }

        @Parameterized.Parameters(name = "{0}")
        public static org.mockito.quality.Strictness[] data() {
            return org.mockito.quality.Strictness.values();
        }
    }

    @RunWith(value = Parameterized.class)
    public static class MockStrictnessToStrictnessTest {

        public Mock.Strictness strictness;

        public MockStrictnessToStrictnessTest(Mock.Strictness strictness) {
            this.strictness = strictness;
        }

        @Test
        public void should_have_matching_enum_in_strictness_enum() {
            assumeThat("Ignore NOT_SET", strictness, not(Mock.Strictness.TEST_LEVEL_DEFAULT));
            org.mockito.quality.Strictness.valueOf(strictness.name());
        }

        @Parameterized.Parameters(name = "{0}")
        public static Mock.Strictness[] data() {
            return Mock.Strictness.values();
        }
    }
}
