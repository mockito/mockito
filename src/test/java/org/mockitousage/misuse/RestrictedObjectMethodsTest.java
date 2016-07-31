/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.misuse;

import org.junit.After;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockitoutil.TestBase;

import java.util.List;

import static junit.framework.TestCase.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;

public class RestrictedObjectMethodsTest extends TestBase {

    @Mock List<?> mock;

    @After
    public void after() {
        this.resetState();
    }

    @Test
    public void shouldScreamWhenVerifyToString() {
        try {
            verify(mock).toString();
            fail();
        } catch (MockitoException e) {
            assertThat(e).hasMessageContaining("cannot verify");
        }
    }

    @Test
    public void shouldBeSilentWhenVerifyHashCode() {
        //because it leads to really wierd behavior sometimes
        //it's because cglib & my code can occasionelly call those methods
        // and when user has verification started at that time there will be a mess
        verify(mock).hashCode();
    }

    @Test
    public void shouldBeSilentWhenVerifyEquals() {
        //because it leads to really wierd behavior sometimes
        //it's because cglib & my code can occasionelly call those methods
        // and when user has verification started at that time there will be a mess
        verify(mock).equals(null);
    }

    @Test
    public void shouldBeSilentWhenVerifyEqualsInOrder() {
        //because it leads to really wierd behavior sometimes
        //it's because cglib & my code can occasionelly call those methods
        // and when user has verification started at that time there will be a mess
        InOrder inOrder = inOrder(mock);
        inOrder.verify(mock).equals(null);
    }       
}