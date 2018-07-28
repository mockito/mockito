/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import org.junit.Test;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;
import static org.mockito.Mockito.*;

/**
 * This test is used to use vararg matcher instead of arrays
 */
public class VarargsAsArray extends TestBase {

    @Test
    public void shouldVerifyVarargsAsArray() throws Exception {
        IMethods mock = mock(IMethods.class);

        mock.mixedVarargs("1", "2", "3");
        verify(mock).mixedVarargs(any(), vararg("2", "3"));
    }
}
