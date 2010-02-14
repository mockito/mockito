/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.cglib.proxy.Enhancer;
import org.mockito.cglib.proxy.NoOp;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

@SuppressWarnings("unchecked")
public class MockUtilTest extends TestBase {
    
    public class CreationValidatorStub extends MockCreationValidator {
        private boolean extraInterfacesValidated;
        private boolean typeValidated;
        public void validateType(Class classToMock) {
            typeValidated = true;
        }
        public void validateExtraInterfaces(Class classToMock, Class ... interfaces) {
            extraInterfacesValidated = true;
        }
    }

    private CreationValidatorStub creationValidator = new CreationValidatorStub();
    private MockUtil mockUtil = new MockUtil(creationValidator);

    @Test 
    public void shouldValidate() {
        //given
        assertFalse(creationValidator.extraInterfacesValidated);
        assertFalse(creationValidator.typeValidated);

        //when
        mockUtil.createMock(IMethods.class, new MockSettingsImpl());
        
        //then
        assertTrue(creationValidator.extraInterfacesValidated);
        assertTrue(creationValidator.typeValidated);
    }

    @Test 
    public void shouldGetHandler() {
        List mock = Mockito.mock(List.class);
        assertNotNull(mockUtil.getMockHandler(mock));
    }

    @Test 
    public void shouldScreamWhenEnhancedButNotAMockPassed() {
        Object o = Enhancer.create(ArrayList.class, NoOp.INSTANCE);
        try {
            mockUtil.getMockHandler(o);
            fail();
        } catch (NotAMockException e) {}
    }

    @Test (expected=NotAMockException.class)
    public void shouldScreamWhenNotAMockPassed() {
        mockUtil.getMockHandler("");
    }
    
    @Test (expected=MockitoException.class)
    public void shouldScreamWhenNullPassed() {
        mockUtil.getMockHandler(null);
    }
    
    @Test
    public void shouldValidateMock() {
        assertFalse(mockUtil.isMock("i mock a mock"));
        assertTrue(mockUtil.isMock(Mockito.mock(List.class)));
    }
}