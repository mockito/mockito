/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.junitrunner;

import static org.mockito.Mockito.*;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.VerboseMockitoJUnitRunner;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

@Ignore
@RunWith(VerboseMockitoJUnitRunner.class)
public class ModellingVerboseMockitoTest extends TestBase {
    
    @Mock private IMethods mock;
    
    public void cleanStackTraces() {
        super.makeStackTracesClean();
    }
    
    @Test
    public void shouldLogUnusedStubbingWarningWhenTestFails() throws Exception {
        when(mock.simpleMethod(1)).thenReturn("foo");
        
        String ret = mock.simpleMethod(2);
        
        assertEquals("foo", ret);
    }
}