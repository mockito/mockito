package org.mockito.internal.util;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.jmock.ClassImposterizer;

@SuppressWarnings("unchecked")
public class CreationValidator {

    public void validateType(Class classToMock) {
        if (!ClassImposterizer.INSTANCE.canImposterise(classToMock)) {
            new Reporter().cannotMockFinalClass(classToMock);
        }
    }
    
    public void validateExtraInterfaces(Class classToMock, Class ... extraInterfaces) {
        if (extraInterfaces == null) {
            return;
        }
        
        for (Class i : extraInterfaces) {
            if (classToMock == i) {
                new Reporter().extraInterfacesCannotContainMockedType(classToMock);
            }
        }
    }
}