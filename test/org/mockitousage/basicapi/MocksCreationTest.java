/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.basicapi;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockitoutil.TestBase;

public class MocksCreationTest extends TestBase {

    private class HasPrivateConstructor {};
    
    @Test
    public void shouldCreateMockWhenConstructorIsPrivate() {
        assertNotNull(Mockito.mock(HasPrivateConstructor.class));
    }
}