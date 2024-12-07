/*
 * Copyright (c) 2024 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import org.mockito.MockMakers;
import org.mockito.internal.configuration.plugins.Plugins;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class MockitoInformationTest {

    @Test
    public void should_describe_mock_makers() {
        assertSoftly(softly -> {
            softly.assertThat(MockitoInformation.describe(null))
                  .containsPattern(
                      "mock-maker\\s+:\\s+" + Plugins.getMockMaker().getName());
            softly.assertThat(MockitoInformation.describe(MockMakers.SUBCLASS))
                  .containsPattern("mock-maker\\s+:\\s+mock-maker-subclass");
            softly.assertThat(MockitoInformation.describe(MockMakers.PROXY))
                  .containsPattern("mock-maker\\s+:\\s+mock-maker-proxy");
            softly.assertThat(MockitoInformation.describe(MockMakers.INLINE))
                  .containsPattern("mock-maker\\s+:\\s+mock-maker-inline");
        });
    }
}
