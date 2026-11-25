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
    private final Method stackWalkerStackFrameGetMethodName;
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
        stackWalkerStackFrameGetMethodName = stackWalkerStackFrame.getMethod("getMethodName");
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
                                                // The first frame that is not a Mockito frame is
                                                // the currently executing constructor of the
                                                // mocked type. Its constructor chain is followed
                                                // until the code that started the construction is
                                                // reached: a construction mock does not apply if
                                                // the chain originated from a subclass constructor,
                                                // but applies for this(...) delegation within the
                                                // type itself and for direct instantiation.
                                                while (iterator.hasNext()) {
                                                    Object caller = iterator.next();
                                                    if (!stackWalkerStackFrameGetMethodName
                                                            .invoke(caller)
                                                            .equals("<init>")) {
                                                        return false;
                                                    }
                                                    Class<?> declaringClass =
                                                            (Class<?>)
                                                                    stackWalkerStackFrameGetDeclaringClass
                                                                            .invoke(caller);
                                                    if (type != declaringClass) {
                                                        return type.isAssignableFrom(
                                                                declaringClass);
                                                    }
                                                }
                                                return false;
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
