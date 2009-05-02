/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.base;

import static org.mockitoutil.ExtraMatchers.*;

import org.junit.Test;
import org.mockitoutil.TestBase;

public class StackTraceFilterTest extends TestBase {
    
    private StackTraceFilter filter = new StackTraceFilter();
    
    @Test
    public void shouldFilterOutCglibGarbage() {
        StackTraceElement[] t = new TraceBuilder().classes(
            "MockitoExampleTest",
            "List$$EnhancerByMockitoWithCGLIB$$2c406024", 
            "MethodInterceptorFilter"
        ).toTraceArray();
        
        StackTraceElement[] filtered = filter.filter(t);
        
        assertThat(filtered, hasOnlyThoseClasses("MockitoExampleTest"));
    }
    
    @Test
    public void shouldFilterOutMockitoPackage() {
        StackTraceElement[] t = new TraceBuilder().classes(
            "org.test.MockitoSampleTest",
            "org.test.TestSupport",
            "org.mockito.Mockito", 
            "org.test.TestSupport",
            "org.mockito.Mockito"
        ).toTraceArray();
            
        StackTraceElement[] filtered = filter.filter(t);
        
        assertThat(filtered, hasOnlyThoseClasses("org.test.TestSupport", "org.test.MockitoSampleTest"));
    }
    
    @Test
    public void shouldKeepRunners() {
        StackTraceElement[] t = new TraceBuilder().classes(
                "org.mockito.runners.Runner",
                "junit.stuff",
                "org.test.MockitoSampleTest",
                "org.mockito.Mockito"
        ).toTraceArray();
        
        StackTraceElement[] filtered = filter.filter(t);
        
        assertThat(filtered, hasOnlyThoseClasses("org.test.MockitoSampleTest", "junit.stuff", "org.mockito.runners.Runner"));
    }
    
    @Test
    public void shouldKeepInternalRunners() {
        StackTraceElement[] t = new TraceBuilder().classes(
                "org.mockito.internal.runners.Runner",
                "org.test.MockitoSampleTest"
        ).toTraceArray();
        
        StackTraceElement[] filtered = filter.filter(t);
        
        assertThat(filtered, hasOnlyThoseClasses("org.test.MockitoSampleTest", "org.mockito.internal.runners.Runner"));
    }
}