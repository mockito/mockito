/*
 * Copyright (c) 2022 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import java.util.function.Predicate;

class StackTraceChecker implements Predicate<Class<?>> {

    @Override
    public boolean test(Class<?> type) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        int index = 1;
        while (index < stackTrace.length
                && stackTrace[index].getClassName().startsWith("org.mockito.internal.")) {
            index++;
        }
        // The first frame that is not a Mockito frame is the currently executing constructor of
        // the mocked type. Its constructor chain is followed until the code that started the
        // construction is reached: a construction mock does not apply if the chain originated
        // from a subclass constructor, but applies for this(...) delegation within the type
        // itself and for direct instantiation.
        for (index++; index < stackTrace.length; index++) {
            StackTraceElement element = stackTrace[index];
            if (!element.getMethodName().equals("<init>")) {
                return false;
            }
            try {
                Class<?> declaringClass =
                        Class.forName(element.getClassName(), false, type.getClassLoader());
                if (type != declaringClass) {
                    return type.isAssignableFrom(declaringClass);
                }
            } catch (ClassNotFoundException ignored) {
                return false;
            }
        }
        return false;
    }
}
