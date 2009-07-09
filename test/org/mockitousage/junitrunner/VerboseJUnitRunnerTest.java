/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.junitrunner;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.VerboseMockitoJUnitRunner;
import org.mockitoutil.TestBase;

@RunWith(VerboseMockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class VerboseJUnitRunnerTest extends TestBase {
    
    @Mock private List list;
    @Mock private List listTwo;
    
    @Test
    public void shouldInitMocksUsingRunner() {
        //given
//        when(list.get(0)).thenReturn("Igor");
        
//        assertEquals("Igor", list.get(1));
        
        //when
//        executeSystem(0);
        
        //then
//        verify(listTwo).add("Igor");
    }

    private void executeSystem(int idx) {
        Object first = list.get(idx + 1);
        listTwo.add(first);
    }
}