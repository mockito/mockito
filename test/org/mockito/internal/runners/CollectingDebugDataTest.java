/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.runners;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

public class CollectingDebugDataTest extends TestBase {

    @Mock IMethods mock;
    
    @Test
    public void shouldNotCollectWhenNoJUnitRunner() throws Throwable {
        //stubbing
        when(mock.simpleMethod()).thenReturn("foo");
        
        //calling unstubbed method
        mock.differentMethod();
        
        MockingProgress progress = new ThreadSafeMockingProgress();
        
        assertFalse(progress.getDebuggingInfo().hasData());
    }
}