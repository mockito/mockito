/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.base;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.TestBase;

public class StackTraceFilterTest extends TestBase {
    
    private StackTraceFilter filter;
    
    @Before
    public void setup() {
        filter = new StackTraceFilter();
    }

    @Test
    public void testShouldFilterStackTrace() {
        StackTraceElement first = new StackTraceElement("MethodInterceptorFilter", "intercept", "MethodInterceptorFilter.java", 49);
        StackTraceElement second = new StackTraceElement("List$$EnhancerByCGLIB$$2c406024", "add", "<generated>", 0);
        StackTraceElement third = new StackTraceElement("MockitoSampleTest", "main", "MockitoSampleTest.java", 100);
        
        HasStackTraceStub trace = new HasStackTraceStub(first, second, third);
        
        filter.filterStackTrace(trace);
        
        assertEquals(1, trace.getStackTrace().length);
        assertEquals(third, trace.getStackTrace()[0]);
        //TODO even though this stuff is tested on functional level, I want some more tests here - the unit test should be complete 
    }
}