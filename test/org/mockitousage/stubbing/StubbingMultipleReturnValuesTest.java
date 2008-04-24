/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.stubbing;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.TestBase;
import org.mockito.MockitoAnnotations.Mock;
import org.mockitousage.IMethods;

@SuppressWarnings("unchecked")
public class StubbingMultipleReturnValuesTest extends TestBase {

    @Mock private IMethods mock;
   
    @Test
    public void shouldReturnMultipleValues() throws Exception {
        stub(mock.simpleMethod())
            .toReturn("one")
            .toReturn("two")
            .toReturn("three");
        
        assertEquals("one", mock.simpleMethod());
        assertEquals("two", mock.simpleMethod());
        assertEquals("three", mock.simpleMethod());
        assertEquals("three", mock.simpleMethod());
    }
}