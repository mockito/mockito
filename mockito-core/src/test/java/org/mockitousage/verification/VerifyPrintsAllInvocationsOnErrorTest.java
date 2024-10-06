/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.verification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.verification.opentest4j.ArgumentsAreDifferent;

public class VerifyPrintsAllInvocationsOnErrorTest {

    @Test
    public void shouldPrintAllInvocationsOnError() {
        ExampleBuilder mockBuilder = Mockito.mock(ExampleBuilder.class);
        mockBuilder.with("key1", "val1");
        mockBuilder.with("key2", "val2");
        try {
            Mockito.verify(mockBuilder).with("key1", "wrongValue");
            fail();
        } catch (ArgumentsAreDifferent e) {
            assertThat(e).hasMessageContaining("exampleBuilder.with(\"key1\", \"val1\")");
            assertThat(e).hasMessageContaining("exampleBuilder.with(\"key2\", \"val2\"");
        }
    }

    private static class ExampleBuilder {
        public ExampleBuilder with(String key, String val) {
            return this;
        }
    }
}
