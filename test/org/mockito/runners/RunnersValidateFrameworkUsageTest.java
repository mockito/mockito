/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.runners;

import static org.mockitoutil.ExtraMatchers.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.mockito.internal.runners.util.FrameworkUsageValidator;
import org.mockitoutil.TestBase;

@SuppressWarnings({"unchecked", "deprecation"})
public class RunnersValidateFrameworkUsageTest extends TestBase {
    
    private Runner runner;
    private RunNotifierStub notifier = new RunNotifierStub();
    
    public static class DummyTest extends TestBase {
        @Test public void dummy() throws Exception {}
    }

    public class RunNotifierStub extends RunNotifier {

        private List<RunListener> addedListeners = new LinkedList<RunListener>();

        @Override
        public void addListener(RunListener listener) {
            addedListeners.add(listener);
        }
    }

    @Test
    public void shouldValidateWithDefaultRunner() throws Exception {
        //given
        runner = new MockitoJUnitRunner(DummyTest.class);

        //when
        runner.run(notifier);
        
        //then
        assertThat(notifier.addedListeners, contains(clazz(FrameworkUsageValidator.class)));
    }
    
    @Test
    public void shouldValidateWithD44Runner() throws Exception {
        //given
        runner = new MockitoJUnit44Runner(DummyTest.class);

        //when
        runner.run(notifier);
        
        //then
        assertThat(notifier.addedListeners, contains(clazz(FrameworkUsageValidator.class)));
    }
    
    @Test
    public void shouldValidateWithVerboseRunner() throws Exception {
        //given
        runner = new ConsoleSpammingMockitoJUnitRunner(DummyTest.class);
        
        //when
        runner.run(notifier);
        
        //then
        assertEquals(2, notifier.addedListeners.size());
        assertThat(notifier.addedListeners, contains(clazz(FrameworkUsageValidator.class)));
    }
}