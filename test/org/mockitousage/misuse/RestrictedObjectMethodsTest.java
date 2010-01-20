/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.misuse;

import static org.mockito.Mockito.*;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class RestrictedObjectMethodsTest extends TestBase {

    @Mock IMethods mock;

    @Test(expected= MockitoException.class)
    public void shouldNotVerifyToString() {
        verify(mock).toString();
    }

    @Ignore
    @Test(expected= MockitoException.class)
    public void shouldNotVerifyHashCode() {
        verify(mock).hashCode();
    }

    @Ignore
    @Test(expected= MockitoException.class)
    public void shouldNotVerifyEquals() {
        InOrder inOrder = inOrder(mock);
        inOrder.verify(mock).equals(null);
    }
}