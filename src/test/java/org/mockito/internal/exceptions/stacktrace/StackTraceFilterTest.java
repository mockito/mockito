/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.exceptions.stacktrace;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.exceptions.base.TraceBuilder;
import org.mockitoutil.TestBase;

import static junit.framework.TestCase.assertEquals;
import static org.mockitoutil.Conditions.onlyThoseClasses;

public class StackTraceFilterTest extends TestBase {
    
    private final StackTraceFilter filter = new StackTraceFilter();
    
    @Test
    public void shouldFilterOutCglibGarbage() {
        StackTraceElement[] t = new TraceBuilder().classes(
            "MockitoExampleTest",
            "List$$EnhancerByMockitoWithCGLIB$$2c406024"
        ).toTraceArray();
        
        StackTraceElement[] filtered = filter.filter(t, false);
        
        Assertions.assertThat(filtered).has(onlyThoseClasses("MockitoExampleTest"));
    }

    @Test
    public void shouldFilterOutByteBuddyGarbage() {
        StackTraceElement[] t = new TraceBuilder().classes(
                "MockitoExampleTest",
                "org.testcase.MockedClass$MockitoMock$1882975947.doSomething(Unknown Source)"
        ).toTraceArray();

        StackTraceElement[] filtered = filter.filter(t, false);

        Assertions.assertThat(filtered).has(onlyThoseClasses("MockitoExampleTest"));
    }


    @Test
    public void shouldFilterOutMockitoPackage() {
        StackTraceElement[] t = new TraceBuilder().classes(
            "org.test.MockitoSampleTest",
            "org.mockito.Mockito"
        ).toTraceArray();
            
        StackTraceElement[] filtered = filter.filter(t, false);

        Assertions.assertThat(filtered).has(onlyThoseClasses("org.test.MockitoSampleTest"));
    }
    
    @Test
    public void shouldNotFilterOutTracesMiddleGoodTraces() {
        StackTraceElement[] t = new TraceBuilder().classes(
                "org.test.MockitoSampleTest",
                "org.test.TestSupport",
                "org.mockito.Mockito", 
                "org.test.TestSupport",
                "org.mockito.Mockito"
        ).toTraceArray();
        
        StackTraceElement[] filtered = filter.filter(t, false);

        Assertions.assertThat(filtered).has(onlyThoseClasses("org.test.TestSupport", "org.test.TestSupport", "org.test.MockitoSampleTest"));
    }
    
    @Test
    public void shouldKeepRunners() {
        StackTraceElement[] t = new TraceBuilder().classes(
                "org.mockito.runners.Runner",
                "junit.stuff",
                "org.test.MockitoSampleTest",
                "org.mockito.Mockito"
        ).toTraceArray();
        
        StackTraceElement[] filtered = filter.filter(t, false);

        Assertions.assertThat(filtered).has(onlyThoseClasses("org.test.MockitoSampleTest", "junit.stuff", "org.mockito.runners.Runner"));
    }

    @Test
    public void shouldNotFilterElementsAboveMockitoJUnitRule() {
        StackTraceElement[] t = new TraceBuilder().classes(
                "org.mockito.internal.junit.JUnitRule$1.evaluate(JUnitRule.java:16)",
                "org.mockito.runners.Runner",
                "junit.stuff",
                "org.test.MockitoSampleTest",
                "org.mockito.internal.MockitoCore.verifyNoMoreInteractions",
                "org.mockito.internal.debugging.LocationImpl"
        ).toTraceArray();

        StackTraceElement[] filtered = filter.filter(t, false);

        Assertions.assertThat(filtered).has(onlyThoseClasses("org.test.MockitoSampleTest", "junit.stuff", "org.mockito.runners.Runner","org.mockito.internal.junit.JUnitRule$1.evaluate(JUnitRule.java:16)"));
    }
    
    @Test
    public void shouldKeepInternalRunners() {
        StackTraceElement[] t = new TraceBuilder().classes(
                "org.mockito.internal.runners.Runner",
                "org.test.MockitoSampleTest"
        ).toTraceArray();
        
        StackTraceElement[] filtered = filter.filter(t, false);

        Assertions.assertThat(filtered).has(onlyThoseClasses("org.test.MockitoSampleTest", "org.mockito.internal.runners.Runner"));
    }
    
    @Test
    public void shouldStartFilteringAndKeepTop() {
        //given
        StackTraceElement[] t = new TraceBuilder().classes(
                "org.test.Good",
                "org.mockito.internal.Bad",
                "org.test.MockitoSampleTest"
        ).toTraceArray();
        
        //when
        StackTraceElement[] filtered = filter.filter(t, true);
        
        //then
        Assertions.assertThat(filtered).has(onlyThoseClasses("org.test.MockitoSampleTest", "org.test.Good"));
    }

    @Test
    public void shouldKeepGoodTraceFromTheTopBecauseRealImplementationsOfSpiesSometimesThrowExceptions() {
        StackTraceElement[] t = new TraceBuilder().classes(
                "org.good.Trace",
                "org.yet.another.good.Trace",
                "org.mockito.internal.to.be.Filtered",
                "org.test.MockitoSampleTest"
        ).toTraceArray();
        
        StackTraceElement[] filtered = filter.filter(t, true);

        Assertions.assertThat(filtered).has(onlyThoseClasses(
                "org.test.MockitoSampleTest",
                "org.yet.another.good.Trace",
                "org.good.Trace"
                ));
    }
    
    @Test
    public void shouldReturnEmptyArrayWhenInputIsEmpty() throws Exception {
        //when
        StackTraceElement[] filtered = filter.filter(new StackTraceElement[0], false);
        //then
        assertEquals(0, filtered.length);
    }
}