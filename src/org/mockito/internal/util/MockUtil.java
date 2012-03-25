/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.InvocationNotifierHandler;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.configuration.ClassPathLoader;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.plugins.MockMaker;

import java.io.Serializable;

@SuppressWarnings("unchecked")
public class MockUtil {

    private static final MockMaker mockMaker = ClassPathLoader.getMockMaker();
    private final MockCreationValidator creationValidator;

    public MockUtil(MockCreationValidator creationValidator) {
        this.creationValidator = creationValidator;
    }

    public MockUtil() {
        this(new MockCreationValidator());
    }

    public <T> T createMock(Class<T> classToMock, MockSettingsImpl settings) {
        creationValidator.validateType(classToMock);
        creationValidator.validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
        creationValidator.validateMockedType(classToMock, settings.getSpiedInstance());
        creationValidator.validateDelegatedInstance(classToMock, settings.getDelegatedInstance()) ;
        creationValidator.validateMutualExclusionForSpyOrDelegate(settings) ;

        settings.initiateMockName(classToMock);

        InvocationNotifierHandler<T> mockHandler = new InvocationNotifierHandler<T>(
                new MockHandler<T>(settings), settings);
        Class<?>[] extraInterfaces = prepareAncillaryTypes(settings);
        T mock = mockMaker.createMock(classToMock, extraInterfaces, mockHandler, settings);

        Object spiedInstance = settings.getSpiedInstance();
        if (spiedInstance != null) {
            new LenientCopyTool().copyToMock(spiedInstance, mock);
        }

        return mock;
    }

    private Class<?>[] prepareAncillaryTypes(MockSettingsImpl settings) {
        Class<?>[] interfaces = settings.getExtraInterfaces();

        Class<?>[] ancillaryTypes;
        if (settings.isSerializable()) {
            ancillaryTypes = interfaces == null ? new Class<?>[] {Serializable.class} : new ArrayUtils().concat(interfaces, Serializable.class);
        } else {
            ancillaryTypes = interfaces == null ? new Class<?>[0] : interfaces;
        }
        if (settings.getSpiedInstance() != null) {
            ancillaryTypes = new ArrayUtils().concat(ancillaryTypes, MockitoSpy.class);
        }
        return ancillaryTypes;
    }

    public <T> void resetMock(T mock) {
        InvocationNotifierHandler oldHandler
                = (InvocationNotifierHandler) mockMaker.getHandler(mock);
        MockSettingsImpl settings = oldHandler.getMockSettings();
        InvocationNotifierHandler<T> newHandler = new InvocationNotifierHandler<T>(
                new MockHandler<T>(settings), settings);
        mockMaker.resetMock(mock, newHandler, settings);
    }

    public <T> MockHandlerInterface<T> getMockHandler(T mock) {
        if (mock == null) {
            throw new NotAMockException("Argument should be a mock, but is null!");
        }

        if (isMockitoMock(mock)) {
            return (MockHandlerInterface) mockMaker.getHandler(mock);
        } else {
            throw new NotAMockException("Argument should be a mock, but is: " + mock.getClass());
        }
    }

    public boolean isMock(Object mock) {
        return mock != null && isMockitoMock(mock);
    }

    public boolean isSpy(Object mock) {
        return mock instanceof MockitoSpy && isMock(mock);
    }

    private <T> boolean isMockitoMock(T mock) {
        return mockMaker.getHandler(mock) != null;
    }

    public MockName getMockName(Object mock) {
        return getMockHandler(mock).getMockSettings().getMockName();
    }

    public void redefineMockNameIfSurrogate(Object mock, String newName) {
        if (getMockName(mock).isSurrogate()) {
            getMockHandler(mock).getMockSettings().redefineMockName(newName);
        }
    }
}
