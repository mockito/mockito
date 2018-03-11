package org.mockito.internal.creation.bytebuddy;

import org.assertj.core.api.Assertions;
import org.junit.Test;
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
