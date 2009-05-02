/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.misuse;

import static org.mockito.Mockito.*;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class RestrictedObjectMethodsTest extends TestBase {

    @Mock IMethods mock;

    @Ignore
    @Test
    public void shouldNotAllowVerifyingRestrictedMethods() {
        //TODO: after 1.8 exception message should mention those methods are not verifiable
        verify(mock).toString();
        verify(mock).hashCode();
        verify(mock).equals(null);
    }
}