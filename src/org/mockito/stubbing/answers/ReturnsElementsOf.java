/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.stubbing.answers;

import java.util.Collection;
import java.util.LinkedList;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Returns elements of the collection. Keeps returning the last element forever.
 * Might be useful on occasion when you have a collection of elements to return.
 * <p>
 * <pre>
 *   //this:
 *   when(mock.foo()).thenReturn(1, 2, 3);
 *   //is equivalent to:
 *   when(mock.foo()).thenReturn(new ReturnsElementsOf(Arrays.asList(1, 2, 3)));
 * <pre>
 */
@SuppressWarnings("unchecked")
public class ReturnsElementsOf implements Answer {

    final LinkedList elements;

    public ReturnsElementsOf(Collection elements) {
        if (elements == null) {
            throw new MockitoException("ReturnsElementsOf does not accept null as constructor argument.\n" +
            		"Please pass a collection instance");
        }
        this.elements = new LinkedList(elements);
    }

    public Object answer(InvocationOnMock invocation) throws Throwable {
        if (elements.size() == 1)
            return elements.get(0);
        else 
            return elements.poll();
    }
}