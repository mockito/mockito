/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.packageprotected;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockitoutil.TestBase;

public class MockingPackageProtectedTest extends TestBase {

    static class Foo {};
    
    class Bar {};
    
    @Test
    public void shouldMockPackageProtectedClasses() {
        mock(PackageProtected.class);
        mock(Foo.class);
        mock(Bar.class);
    }
    
    @Test
    public void should_verify_package_protected_methods() {
        PackageProtected packageProtected = mock(PackageProtected.class);
        verifyDoStuff(packageProtected);
    }

    @Test
    public void can_spy_and_verify_package_protected_methods() {
        PackageProtected packageProtected = spy(new PackageProtected());
        verifyDoStuff(packageProtected);
    }

	private void verifyDoStuff(PackageProtected packageProtected) {
		packageProtected.doStuff();
        verify(packageProtected).doStuff();
	}
}