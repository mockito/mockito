/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.base;

import static org.mockitoutil.ExtraMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class StackTraceFilterTest extends TestBase {
    
    private StackTraceFilter filter;
    
    @Before
    public void setup() {
        filter = new StackTraceFilter();
    }

    @Test
    public void testShouldFilterOutCglibGarbage() {
        Throwable t = new TraceBuilder().classes(
            "MockitoExampleTest",
            "List$$EnhancerByMockitoWithCGLIB$$2c406024", 
            "MethodInterceptorFilter"
        ).toThrowable();
        
        filter.filterStackTrace(t);
        
        assertThat(t, hasOnlyThoseClassesInStackTrace("MockitoExampleTest"));
    }
    
    @Test
    public void testShouldFilterOutMockitoPackage() {
        Throwable t = new TraceBuilder().classes(
            "org.test.MockitoSampleTest",
            "org.test.TestSupport",
            "org.mockito.Mockito", 
            "org.test.TestSupport",
            "org.mockito.Mockito"
        ).toThrowable();
            
        filter.filterStackTrace(t);
        
        assertThat(t, hasOnlyThoseClassesInStackTrace("org.test.TestSupport", "org.test.MockitoSampleTest"));
    }
}