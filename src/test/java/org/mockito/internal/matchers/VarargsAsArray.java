/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.util.Primitives;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationFactory;
import org.mockito.invocation.MockHandler;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
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
