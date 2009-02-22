/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.stacktrace;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

@Ignore
public class ModellingDescriptiveMessagesTest extends TestBase {
    
    private IMethods mock;

    @Before
    public void setup() {
        mock = Mockito.mock(IMethods.class);
    }
    
    @Test
    public void shouldShowActualAndExpected() {
        mock.simpleMethod("blah");
        verify(mock).simpleMethod();
    }
    
    @Test
    public void shouldTooLittleInvocations() {
        mock.simpleMethod();
        verify(mock, times(2)).simpleMethod();
    }
    
    @Test
    public void shouldTooManyInvocations() {
        mock.simpleMethod();
        mock.simpleMethod();
        verify(mock, times(1)).simpleMethod();
    }
}