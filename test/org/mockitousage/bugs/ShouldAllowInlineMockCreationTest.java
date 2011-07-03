/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.bugs;

import org.fest.assertions.Assertions;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockitoutil.TestBase;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;

//see issue 191
public class ShouldAllowInlineMockCreationTest extends TestBase {

    @Mock List list;

    @Test
    public void shouldAllowInlineMockCreation() {
        when(list.get(0)).thenReturn(mock(Set.class));
        assertTrue(list.get(0) instanceof Set);
    }
}