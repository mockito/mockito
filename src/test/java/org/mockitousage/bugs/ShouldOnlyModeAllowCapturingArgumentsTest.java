/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.bugs;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

//bug 197
public class ShouldOnlyModeAllowCapturingArgumentsTest extends TestBase {
    
    @Mock IMethods mock;
    
    @Test
    public void shouldAllowCapturingArguments() {
        //given
        mock.simpleMethod("o");
        ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
        
        //when
        verify(mock, only()).simpleMethod(arg.capture());

        //then
        assertEquals("o", arg.getValue());
    }
}