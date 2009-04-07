/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import static org.mockito.Mockito.*;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
@Ignore
public class PlaygroundTest extends TestBase {

    @Mock IMethods mock;
    @Mock DummyException mock2;
    
    class DummyException extends RuntimeException {
        public String otherMethod() {
            return "";
        }
    }

    @Test
    public void testGetLastUpdates() {
        when(mock.simpleMethod()).thenThrow(mock2);
        mock.simpleMethod();
    }
}