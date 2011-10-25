/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.annotation;

import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;
import org.mockitoutil.TestBase;

@SuppressWarnings({"unchecked", "unused"})
public class WrongSetOfAnnotationsTest extends TestBase {
    
    @Test(expected=MockitoException.class)
    public void shouldNotAllowMockAndSpy() throws Exception {
        MockitoAnnotations.initMocks(new Object() {
            @Mock @Spy List mock;
        });
    }
    
    @Test(expected=MockitoException.class)
    public void shouldNotAllowSpyAndInjectMock() throws Exception {
        MockitoAnnotations.initMocks(new Object() {
            @InjectMocks @Spy List mock;
        });
    }
    
    @Test(expected=MockitoException.class)
    public void shouldNotAllowMockAndInjectMock() throws Exception {
        MockitoAnnotations.initMocks(new Object() {
            @InjectMocks @Mock List mock;
        });
    }
    
    @Test(expected=MockitoException.class)
    public void shouldNotAllowCaptorAndMock() throws Exception {
        MockitoAnnotations.initMocks(new Object() {
            @Mock @Captor ArgumentCaptor captor;
        });
    }
    
    @Test(expected=MockitoException.class)
    public void shouldNotAllowCaptorAndSpy() throws Exception {
        MockitoAnnotations.initMocks(new Object() {
            @Spy @Captor ArgumentCaptor captor;
        });
    }
    
    @Test(expected=MockitoException.class)
    public void shouldNotAllowCaptorAndInjectMock() throws Exception {
        MockitoAnnotations.initMocks(new Object() {
            @InjectMocks @Captor ArgumentCaptor captor;
        });
    }
    
    
}
