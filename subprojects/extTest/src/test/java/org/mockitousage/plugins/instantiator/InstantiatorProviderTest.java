/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.plugins.instantiator;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

public class InstantiatorProviderTest {

    @SuppressWarnings("CheckReturnValue")
    @Test
    public void uses_custom_instantiator_provider() {
        //given mocking works:
        mock(List.class);

        //when
        MyInstantiatorProvider2.explosive.set(true);

        //then
        try {
            mock(List.class);
            fail();
        } catch (Exception e) {
            assertEquals(MyInstantiatorProvider2.class.getName(), e.getMessage());
        } finally {
            MyInstantiatorProvider2.explosive.remove();
        }
    }
}
