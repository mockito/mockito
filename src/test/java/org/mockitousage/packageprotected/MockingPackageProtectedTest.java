/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.packageprotected;

import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockitoutil.TestBase;

public class MockingPackageProtectedTest extends TestBase {

    static class Foo {}

    class Bar {}

    @SuppressWarnings({"CheckReturnValue", "MockitoUsage"})
    @Test
    public void shouldMockPackageProtectedClasses() {
        mock(PackageProtected.class);
        mock(Foo.class);
        mock(Bar.class);
    }
}
