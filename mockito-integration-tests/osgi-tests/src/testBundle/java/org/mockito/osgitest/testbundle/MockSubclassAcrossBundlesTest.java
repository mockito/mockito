/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.osgitest.testbundle;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

import org.junit.Test;
import org.mockito.MockMakers;

public class MockSubclassAcrossBundlesTest {

    // Regression test for issue #2694. The mocked subclass lives in this bundle, but its superclass
    // lives in another bundle and exposes a type from a third bundle that this bundle does not
    // import. The subclass mock maker must define the generated mock with a class loader that can
    // also reach the superclass' bundle; otherwise resolving the mock's members fails with
    // NoClassDefFoundError. The regression only affects the subclass mock maker, so it is selected
    // explicitly here.
    @Test
    public void can_mock_subclass_whose_superclass_lives_in_another_bundle() {
        SubClassInTestBundle mock =
                mock(SubClassInTestBundle.class, withSettings().mockMaker(MockMakers.SUBCLASS));

        // Resolving the inherited foreignType() method loads its return type through the mock's
        // class loader. Reflection-based frameworks do this routinely; before the fix it threw
        // NoClassDefFoundError: org/mockito/osgitest/depbundle/ForeignType.
        assertNotNull(mock.getClass().getMethods());
    }
}
