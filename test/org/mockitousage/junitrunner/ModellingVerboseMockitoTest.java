/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.junitrunner;

import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.ConsoleSpammingMockitoJUnitRunner;
import org.mockito.runners.VerboseMockitoJUnitRunner;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

//@RunWith(ConsoleSpammingMockitoJUnitRunner.class)
@RunWith(VerboseMockitoJUnitRunner.class)
//@Ignore
public class ModellingVerboseMockitoTest extends TestBase {
    
    @Mock private IMethods mock;
    
    public void cleanStackTraces() {
        super.makeStackTracesClean();
    }
    
    @Test
    public void shouldLogUnusedStubbingWarningWhenTestFails() throws Exception {
        when(mock.simpleMethod(1)).thenReturn("foo");
        when(mock.otherMethod()).thenReturn("foo");
        when(mock.booleanObjectReturningMethod()).thenReturn(false);

        String ret = mock.simpleMethod(2);

        assertEquals("foo", ret);
    }

    @Test
    public void shouldNotLogAnythingWhenNoWarnings() throws Exception {
        String ret = mock.simpleMethod(2);

        assertEquals("foo", ret);
    }

//    @After
//    public void checkStubs() {
////        stubsUsedIn(mock);
//    }
//
//    private void stubsUsed(Object ... mocks) {
//
//    }
//
//    private void stubsUsed(Object testCaseOrMock) {
//
//    }
}