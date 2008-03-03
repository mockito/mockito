/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.exceptions.base;

import static org.junit.Assert.*;
import static org.mockito.util.ExtraMatchers.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.TestBase;

@SuppressWarnings("unchecked")
public class CommonStackTraceRemoverTest extends TestBase {

    private CommonStackTraceRemover remover;
    
    @Before
    public void setup() {
        remover = new CommonStackTraceRemover();
    }

    @Test
    public void testShouldNotRemoveWhenStackTracesDontHaveCommonPart() {
        HasStackTrace exception = new TraceBuilder().methods("intercept").toTrace();
        List<StackTraceElement> cause = new TraceBuilder().methods("foo").toTraceList();
        
        remover.remove(exception, cause);
        
        assertThat(exception, hasOnlyThoseMethodsInStackTrace("intercept"));
    }
    
    @Test
    public void testShouldRemoveCommonStackTracePart() {
        HasStackTrace exception = new TraceBuilder().methods("intercept", "handle", "foo", "bar").toTrace();
        List<StackTraceElement> cause = new TraceBuilder().methods("intercept", "handle", "hello", "world").toTraceList();
        
        remover.remove(exception, cause);
        
        assertThat(exception, hasOnlyThoseMethodsInStackTrace("bar", "foo"));
    }
}