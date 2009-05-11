/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import java.util.ArrayList;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class MockUtilTest extends TestBase {
    
    public class CreationValidatorStub extends CreationValidator {
        private boolean extraInterfacesValidated;
        private boolean typeValidated;
        public void validateType(Class classToMock) {
            typeValidated = true;
        }
        public void validateExtraInterfaces(Class classToMock, Class ... interfaces) {
            extraInterfacesValidated = true;
        }
    }

    @Before
    public void setUp() {
        MockUtil.creationValidator = new CreationValidatorStub();
    }
    
    @After
    public void restoreValidator() {
        MockUtil.creationValidator = new CreationValidator();
    }
    
    @Test 
    public void shouldValidate() {
        //given
        assertFalse(((CreationValidatorStub) MockUtil.creationValidator).extraInterfacesValidated);
        assertFalse(((CreationValidatorStub) MockUtil.creationValidator).typeValidated);

        //when
        MockUtil.createMock(IMethods.class, new ThreadSafeMockingProgress(), new MockSettingsImpl());
        
        //then
        assertTrue(((CreationValidatorStub) MockUtil.creationValidator).extraInterfacesValidated);
        assertTrue(((CreationValidatorStub) MockUtil.creationValidator).typeValidated);
    }

    @Test 
    public void shouldGetHandler() {
        List mock = Mockito.mock(List.class);
        assertNotNull(MockUtil.getMockHandler(mock));
    }

    @Test 
    public void shouldScreamWhenEnhancedButNotAMockPassed() {
        Object o = Enhancer.create(ArrayList.class, NoOp.INSTANCE);
        try {
            MockUtil.getMockHandler(o);
            fail();
        } catch (NotAMockException e) {}
    }

    @Test (expected=NotAMockException.class)
    public void shouldScreamWhenNotAMockPassed() {
        MockUtil.getMockHandler("");
    }
    
    @Test (expected=MockitoException.class)
    public void shouldScreamWhenNullPassed() {
        MockUtil.getMockHandler(null);
    }
    
    @Test
    public void shouldValidateMock() {
        assertFalse(MockUtil.isMock("i mock a mock"));
        assertTrue(MockUtil.isMock(Mockito.mock(List.class)));
    }
}