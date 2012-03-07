package org.mockito.testng;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;
import org.testng.IInvokedMethod;
import org.testng.ITestResult;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class MockitoAfterTestNGMethod {

    public void applyFor(IInvokedMethod method, ITestResult testResult) {
        Mockito.validateMockitoUsage();

        if (method.isTestMethod()) {
            resetMocks(testResult.getInstance());
        }
    }


    private void resetMocks(Object instance) {
        Mockito.reset(instanceMocksOf(instance).toArray());
    }

    private Set<Object> instanceMocksOf(Object instance) {
        Class<?> testClass = instance.getClass();
        Set<Object> instanceMocks = new HashSet<Object>();

        for (Class<?> clazz = testClass; clazz != Object.class; clazz = clazz.getSuperclass()) {
            instanceMocks.addAll(instanceMocksIn(instance, clazz));
        }
        return instanceMocks;
    }

    private Set<Object> instanceMocksIn(Object instance, Class<?> clazz) {
        Set<Object> instanceMocks = new HashSet<Object>();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (declaredField.isAnnotationPresent(Mock.class) || declaredField.isAnnotationPresent(Spy.class)) {
                declaredField.setAccessible(true);
                try {
                    Object fieldValue = declaredField.get(instance);
                    if (fieldValue != null) {
                        instanceMocks.add(fieldValue);
                    }
                } catch (IllegalAccessException e) {
                    throw new MockitoException("Could not access field " + declaredField.getName());
                }
            }
        }
        return instanceMocks;
    }


}
