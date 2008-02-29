/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.base;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.TestBase;

public class CommonStackTraceRemoverTest extends TestBase {

    private CommonStackTraceRemover remover;
    
    @Before
    public void setup() {
        remover = new CommonStackTraceRemover();
    }

    @Test
    public void testShouldNotRemoveWhenStackTracesDontHaveCommonPart() {
        StackTraceElement elementOne = new StackTraceElement("MethodInterceptorFilter", "intercept", "MethodInterceptorFilter.java", 49);
        HasStackTrace trace = new HasStackTraceStub(elementOne);
        
        StackTraceElement elementTwo = new StackTraceElement("Mockito", "other", "Mockito.java", 90);
        List<StackTraceElement> cause = Arrays.asList(elementTwo);
        
        remover.remove(trace, cause);
        
        assertEquals(1, trace.getStackTrace().length);
        assertEquals(elementOne, trace.getStackTrace()[0]);
        //TODO decent arrays equal please, the same in STFT
    }
    
    //TODO even though this stuff is tested on functional level, I want some more tests here - the unit test should be complete
}