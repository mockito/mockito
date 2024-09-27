/*
 * Copyright (c) 2022 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

class StackWalkerChecker implements Predicate<Class<?>> {

    private final Method stackWalkerGetInstance;
    private final Method stackWalkerWalk;
    private final Method stackWalkerStackFrameGetDeclaringClass;
    private final Enum<?> stackWalkerOptionRetainClassReference;

    StackWalkerChecker() throws Exception {
        Class<?> stackWalker = Class.forName("java.lang.StackWalker");
        @SuppressWarnings({"unchecked", "rawtypes"})
        Class<? extends Enum> stackWalkerOption =
                (Class<? extends Enum<?>>) Class.forName("java.lang.StackWalker$Option");
        stackWalkerGetInstance = stackWalker.getMethod("getInstance", stackWalkerOption);
        stackWalkerWalk = stackWalker.getMethod("walk", Function.class);
        Class<?> stackWalkerStackFrame = Class.forName("java.lang.StackWalker$StackFrame");
        stackWalkerStackFrameGetDeclaringClass =
                stackWalkerStackFrame.getMethod("getDeclaringClass");
        @SuppressWarnings("unchecked")
        Enum<?> stackWalkerOptionRetainClassReference =
                Enum.valueOf(stackWalkerOption, "RETAIN_CLASS_REFERENCE");
        this.stackWalkerOptionRetainClassReference = stackWalkerOptionRetainClassReference;
    }

    static Predicate<Class<?>> orFallback() {
        try {
            return new StackWalkerChecker();
        } catch (Exception e) {
            return new StackTraceChecker();
        }
    }

    @Override
    public boolean test(Class<?> type) {
        try {
            Object walker =
                    stackWalkerGetInstance.invoke(null, stackWalkerOptionRetainClassReference);
            return (Boolean)
                    stackWalkerWalk.invoke(
                            walker,
                            (Function<?, ?>)
                                    stream -> {
                                        Iterator<?> iterator = ((Stream<?>) stream).iterator();
                                        while (iterator.hasNext()) {
                                            try {
                                                Object frame = iterator.next();
                                                if (((Class<?>)
                                                                stackWalkerStackFrameGetDeclaringClass
                                                                        .invoke(frame))
                                                        .getName()
                                                        .startsWith("org.mockito.internal.")) {
                                                    continue;
                                                }
                                                if (iterator.hasNext()) {
                                                    Object next = iterator.next();
                                                    Class<?> declaringClass =
                                                            (Class<?>)
                                                                    stackWalkerStackFrameGetDeclaringClass
                                                                            .invoke(next);
                                                    if (type != declaringClass
                                                            && type.isAssignableFrom(
                                                                    declaringClass)) {
                                                        return true;
                                                    } else {
                                                        break;
                                                    }
                                                } else {
                                                    break;
                                                }
                                            } catch (Exception ignored) {
                                                return false;
                                            }
                                        }
                                        return false;
                                    });
        } catch (Exception ignored) {
            return false;
        }
    }
}
