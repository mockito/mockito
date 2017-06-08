/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.stubbing.answers;

import org.mockito.internal.exceptions.stacktrace.ConditionalStackTraceFilter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisBase;
import org.objenesis.ObjenesisStd;
import org.objenesis.strategy.StdInstantiatorStrategy;

import static org.mockito.internal.exceptions.Reporter.notAnException;

import java.io.Serializable;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Constructor;

public class ThrowsExceptionClass implements Answer<Object>, Serializable {

    private static final Objenesis objenesis;
    
    static {
        final MethodHandle newConstructorForSerialization;
        final Constructor<Throwable> throwableInit;
        try {
            newConstructorForSerialization = MethodHandles.lookup().unreflect(
                Class.forName("org.objenesis.instantiator.sun.SunReflectionFactoryInstantiator")
                             .getDeclaredMethod("newConstructorForSerialization"));
        } catch (Exception e) {
            // TODO: Log a warning
            objenesis = new ObjenesisStd();
            return;
        }
        try {
            throwableInit = Throwable.class.getDeclaredConstructor();
        } catch (Exception e) {
            // TODO: Log a warning
            objenesis = new ObjenesisStd();
            return;
        }
        objenesis = new ObjenesisBase(new StdInstantiatorStrategy() {
            @Override
            public <T> ObjectInstantiator<T> newInstantiatorOf(Class<T> type) {
                if (newConstructorForSerialization != null
                        && throwableInit != null
                        && Throwable.class.isAssignableFrom(type)) {
                   final Constructor<T> ctor = newConstructorForSerialization.invokeExact(type, throwableInit);
                   return new ObjectInstantiator<T>() {
                       @Override
                       public T newInstance() {
                           return ctor.newInstance();
                       }
                   };
                } else {
                    return super.newInstantiatorOf(type);
                }
            }
        });
    }
    
    private final Class<? extends Throwable> throwableClass;
    private final ConditionalStackTraceFilter filter = new ConditionalStackTraceFilter();

    public ThrowsExceptionClass(Class<? extends Throwable> throwableClass) {
        this.throwableClass = checkNonNullThrowable(throwableClass);
    }

    private Class<? extends Throwable> checkNonNullThrowable(Class<? extends Throwable> throwableClass) {
        if(throwableClass == null || !Throwable.class.isAssignableFrom(throwableClass)) {
            throw notAnException();
        }
        return throwableClass;
    }

    public Object answer(InvocationOnMock invocation) throws Throwable {
        //TODO centralize the use of Objenesis. Why do we use ObjenesisHelper?
        Throwable throwable = objenesis.newInstance(throwableClass);
        throwable.fillInStackTrace();
        filter.filter(throwable);
        throw throwable;
    }

    public Class<? extends Throwable> getThrowableClass() {
        return throwableClass;
    }
}
