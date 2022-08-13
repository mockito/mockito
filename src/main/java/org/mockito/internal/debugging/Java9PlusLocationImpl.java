/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.debugging;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.stacktrace.StackTraceCleaner;
import org.mockito.exceptions.stacktrace.StackTraceCleaner.StackFrameMetadata;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.exceptions.stacktrace.DefaultStackTraceCleaner;
import org.mockito.invocation.Location;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Java9PlusLocationImpl implements Location, Serializable {
    private static final long serialVersionUID = 2954388321980069195L;

    private static final String UNEXPECTED_ERROR_SUFFIX =
            "\nThis is unexpected and is likely due to a change in either Java's StackWalker or Reflection APIs."
                    + "\nIt's worth trying to upgrade to a newer version of Mockito, or otherwise to file a bug report.";

    private static final String STACK_WALKER = "java.lang.StackWalker";
    private static final String STACK_FRAME = STACK_WALKER + "$StackFrame";
    private static final String OPTION = STACK_WALKER + "$Option";
    private static final String SHOW_REFLECT_FRAMES = "SHOW_REFLECT_FRAMES";

    /**
     * This is an unfortunate buffer. Inside StackWalker, a buffer is created, which is resized by
     * doubling. The resizing also allocates a tonne of StackFrame elements. If we traverse more than
     * BUFFER_SIZE elements, the resulting resize can significantly affect the overall cost of the operation.
     * If we traverse fewer than this number, we are inefficient. Empirically, 16 is enough stack frames
     * for a simple stub+call operation to succeed without resizing, as measured on Java 11.
     */
    private static final int BUFFER_SIZE = 16;

    private static final Class<?> stackWalkerClazz = clazz(STACK_WALKER);
    private static final Class<?> stackFrameClazz = clazz(STACK_FRAME);
    private static final Class<?> optionClazz = clazz(OPTION);

    private static final Object stackWalker = stackWalker();
    private static final Method walk = walk();

    private static final String PREFIX = "-> at ";

    private static final StackTraceCleaner CLEANER =
            Plugins.getStackTraceCleanerProvider()
                    .getStackTraceCleaner(new DefaultStackTraceCleaner());

    /**
     * In Java, allocating lambdas is cheap, but not free. stream.map(this::doSomething)
     * will allocate a Function object each time the function is called (although not
     * per element). By assigning these Functions and Predicates to variables, we can
     * avoid the memory allocation.
     */
    private static final Function<Object, StackFrameMetadata> toStackFrameMetadata =
            MetadataShim::new;

    private static final Predicate<StackFrameMetadata> cleanerIsIn = CLEANER::isIn;

    private static final int FRAMES_TO_SKIP = framesToSkip();

    private final StackFrameMetadata sfm;
    private volatile String stackTraceLine;

    Java9PlusLocationImpl(boolean isInline) {
        this.sfm = getStackFrame(isInline);
    }

    @Override
    public String getSourceFile() {
        return sfm.getFileName();
    }

    @Override
    public String toString() {
        return stackTraceLine();
    }

    private String stackTraceLine() {
        if (stackTraceLine == null) {
            synchronized (this) {
                if (stackTraceLine == null) {
                    stackTraceLine = PREFIX + sfm.toString();
                }
            }
        }
        return stackTraceLine;
    }

    private static StackFrameMetadata getStackFrame(boolean isInline) {
        return stackWalk(
                stream ->
                        stream.map(toStackFrameMetadata)
                                .skip(FRAMES_TO_SKIP)
                                .filter(cleanerIsIn)
                                .skip(isInline ? 1 : 0)
                                .findFirst()
                                .orElseThrow(
                                        () -> new MockitoException(noStackTraceFailureMessage())));
    }

    private static boolean usingDefaultStackTraceCleaner() {
        return CLEANER instanceof DefaultStackTraceCleaner;
    }

    private static String noStackTraceFailureMessage() {
        if (usingDefaultStackTraceCleaner()) {
            return "Mockito could not find the first non-Mockito stack frame."
                    + UNEXPECTED_ERROR_SUFFIX;
        } else {
            String cleanerType = CLEANER.getClass().getName();
            String fmt =
                    "Mockito could not find the first non-Mockito stack frame. A custom stack frame cleaner \n"
                            + "(type %s) is in use and this has mostly likely filtered out all the relevant stack frames.";
            return String.format(fmt, cleanerType);
        }
    }

    /**
     * In order to trigger the stack walker, we create some reflective frames. These need to be skipped so as to
     * ensure there are no non-Mockito frames at the top of the stack trace.
     */
    private static int framesToSkip() {
        return stackWalk(
                stream -> {
                    List<String> metadata =
                            stream.map(toStackFrameMetadata)
                                    .map(StackFrameMetadata::getClassName)
                                    .collect(Collectors.toList());
                    return metadata.indexOf(Java9PlusLocationImpl.class.getName());
                });
    }

    @SuppressWarnings("unchecked")
    private static <T> T stackWalk(Function<Stream<Object>, T> function) {
        try {
            return (T) walk.invoke(stackWalker, function);
        } catch (IllegalAccessException e) {
            throw new MockitoException(
                    "Unexpected access exception while stack walking." + UNEXPECTED_ERROR_SUFFIX,
                    e);
        } catch (InvocationTargetException e) {
            throw new MockitoException(stackWalkFailureMessage());
        }
    }

    private static String stackWalkFailureMessage() {
        if (usingDefaultStackTraceCleaner()) {
            return "Caught an unexpected exception while stack walking." + UNEXPECTED_ERROR_SUFFIX;
        } else {
            String className = CLEANER.getClass().getName();
            String fmt =
                    "Caught an unexpected exception while stack walking."
                            + "\nThis is likely caused by the custom stack trace cleaner in use (class %s).";
            return String.format(fmt, className);
        }
    }

    private static Method walk() {
        try {
            return stackWalkerClazz.getMethod("walk", Function.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static Class<?> clazz(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Object stackWalker() {
        try {
            Set options =
                    Collections.singleton(Enum.valueOf((Class) optionClazz, SHOW_REFLECT_FRAMES));
            Method getInstance =
                    stackWalkerClazz.getDeclaredMethod("getInstance", Set.class, int.class);
            return getInstance.invoke(null, options, BUFFER_SIZE);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new MockitoException(
                    "Mockito received an exception while trying to acquire a StackWalker."
                            + UNEXPECTED_ERROR_SUFFIX);
        }
    }

    private static final class MetadataShim implements StackFrameMetadata, Serializable {
        private static final long serialVersionUID = 8491903719411428648L;
        private static final Method getClassName = getter("getClassName");
        private static final Method getMethodName = getter("getMethodName");
        private static final Method getFileName = getter("getFileName");
        private static final Method getLineNumber = getter("getLineNumber");
        private static final Method toString = getter(Object.class, "toString");

        private final Object stackFrame;

        private MetadataShim(Object stackFrame) {
            this.stackFrame = stackFrame;
        }

        @Override
        public String getClassName() {
            return (String) get(getClassName);
        }

        @Override
        public String getMethodName() {
            return (String) get(getMethodName);
        }

        @Override
        public String getFileName() {
            return (String) get(getFileName);
        }

        @Override
        public int getLineNumber() {
            return (int) get(getLineNumber);
        }

        @Override
        public String toString() {
            return (String) get(toString);
        }

        /**
         * Ensure that this type remains serializable.
         */
        private Object writeReplace() {
            return new SerializableShim(toStackTraceElement());
        }

        private StackTraceElement toStackTraceElement() {
            try {
                Method method = stackFrameClazz.getMethod("toStackTraceElement");
                return (StackTraceElement) method.invoke(stackFrame);
            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        private Object get(Method handle) {
            try {
                return handle.invoke(stackFrame);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        private static Method getter(String name) {
            return getter(stackFrameClazz, name);
        }

        private static Method getter(Class<?> clazz, String name) {
            try {
                return clazz.getDeclaredMethod(name);
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
    }

    private static final class SerializableShim implements StackFrameMetadata, Serializable {
        private static final long serialVersionUID = 7908320459080898690L;
        private final StackTraceElement ste;

        private SerializableShim(StackTraceElement ste) {
            this.ste = ste;
        }

        @Override
        public String getClassName() {
            return ste.getClassName();
        }

        @Override
        public String getMethodName() {
            return ste.getMethodName();
        }

        @Override
        public String getFileName() {
            return ste.getFileName();
        }

        @Override
        public int getLineNumber() {
            return ste.getLineNumber();
        }
    }
}
