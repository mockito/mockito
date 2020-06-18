/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation.mockref;

import static org.junit.Assert.fail;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockitoutil.TestBase;

public class MockWeakReferenceTest extends TestBase {

    @Test
    public void descriptive_exception_when_mock_was_collected() {
        try {
            // when
            new MockWeakReference(null).get();
            // then
            fail();
        } catch (Exception e) {
            Assertions.assertThat(e).hasMessageContaining("The mock object was garbage collected");
        }
    }
}
