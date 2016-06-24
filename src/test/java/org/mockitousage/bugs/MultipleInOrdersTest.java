/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs;

import org.junit.Test;
import org.mockito.InOrder;

import java.util.List;

import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

@SuppressWarnings("unchecked")
public class MultipleInOrdersTest {
    
    @Test
    public void inOrderTest(){
        List<String> list= mock(List.class);
        
        list.add("a");
        list.add("x");
        list.add("b");
        list.add("y");
        
        InOrder inOrder = inOrder(list);
        InOrder inAnotherOrder = inOrder(list);
        assertNotSame(inOrder, inAnotherOrder);
        
        inOrder.verify(list).add("a");
        inOrder.verify(list).add("b");
        
        inAnotherOrder.verify(list).add("x");
        inAnotherOrder.verify(list).add("y");
    }
}