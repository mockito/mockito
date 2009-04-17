/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.junitrunner;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.VerboseMockitoJUnitRunner;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

@RunWith(VerboseMockitoJUnitRunner.class)
public class ModellingVerboseMockitoTest extends TestBase {
    
    @Mock private IMethods mock;
    
    @Before
    public void cleanStackTraces() {
        super.makeStackTracesClean();
    }
    
    @Ignore
    @Test
    public void shouldLogUnusedStubbingWarningWhenTestFails() throws Exception {
        when(mock.simpleMethod()).thenReturn("foo");
        
        fail();
    }
}