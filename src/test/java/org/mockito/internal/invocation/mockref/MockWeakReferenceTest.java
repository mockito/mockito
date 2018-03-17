/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation.mockref;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.internal.invocation.mockref.MockWeakReference;
import org.mockitoutil.TestBase;

import static org.junit.Assert.fail;

public class MockWeakReferenceTest extends TestBase {

    @Test
    public void descriptive_exception_when_mock_was_collected() {
        try {
            //when
            new MockWeakReference(null).get();
            //then
            fail();
        } catch (Exception e) {
            Assertions.assertThat(e).hasMessageContaining("The mock object was garbage collected");
        }
    }
}
