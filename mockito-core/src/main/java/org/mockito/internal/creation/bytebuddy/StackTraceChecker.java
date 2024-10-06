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
        for (int index = 1; index < stackTrace.length - 1; index++) {
            if (!stackTrace[index].getClassName().startsWith("org.mockito.internal.")) {
                if (stackTrace[index + 1].getMethodName().startsWith("<init>")) {
                    try {
                        if (!stackTrace[index + 1].getClassName().equals(type.getName())
                                && type.isAssignableFrom(
                                        Class.forName(
                                                stackTrace[index + 1].getClassName(),
                                                false,
                                                type.getClassLoader()))) {
                            return true;
                        } else {
                            break;
                        }
                    } catch (ClassNotFoundException ignored) {
                        break;
                    }
                }
            }
        }
        return false;
    }
}
