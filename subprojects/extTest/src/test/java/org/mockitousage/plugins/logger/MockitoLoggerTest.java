/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.plugins.logger;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.WARN)
@ExtendWith(MockitoExtension.class)
class MockitoLoggerTest {
    @BeforeAll
    static void setUp() {
        MyMockitoLogger.enable();
    }

    @Test
    void strictness_warn_logged_into_custom_logger() {
        when(mock(Foo.class).doIt()).thenReturn(123);
    }

    @AfterAll
    static void tearDown() {
        final List<Object> loggedItems = MyMockitoLogger.getLoggedItems();
        assertThat(loggedItems)
            .hasSize(1);
        assertThat(loggedItems.get(0).toString())
            .contains("[MockitoHint]")
            .contains("org.mockitousage.plugins.logger.MockitoLoggerTest")
            .contains("Unused");

        MyMockitoLogger.clear();
    }

    interface Foo {
        int doIt();
    }
}
