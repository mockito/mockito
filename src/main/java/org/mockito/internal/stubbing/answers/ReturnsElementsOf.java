/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import java.util.Collection;
import java.util.LinkedList;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Returns elements of the collection. Keeps returning the last element forever.
 * Might be useful on occasion when you have a collection of elements to return.
 * <p>
 * <pre class="code"><code class="java">
 *   //this:
 *   when(mock.foo()).thenReturn(1, 2, 3);
 *   //is equivalent to:
 *   when(mock.foo()).thenAnswer(new ReturnsElementsOf(Arrays.asList(1, 2, 3)));
 * </code></pre>
 * </p>
 *
 * <p>
 * Also you might better want to use the static factory there
 * {@link org.mockito.AdditionalAnswers#returnsElementsOf(java.util.Collection)}
 * </p>
 *
 * @see org.mockito.AdditionalAnswers
 */
public class ReturnsElementsOf implements Answer<Object> {

    private final LinkedList<Object> elements;

    public ReturnsElementsOf(Collection<?> elements) {
        if (elements == null) {
            throw new MockitoException("ReturnsElementsOf does not accept null as constructor argument.\n" +
                    "Please pass a collection instance");
        }
        this.elements = new LinkedList<Object>(elements);
    }

    public Object answer(InvocationOnMock invocation) throws Throwable {
        if (elements.size() == 1)
            return elements.get(0);
        else 
            return elements.poll();
    }
}
