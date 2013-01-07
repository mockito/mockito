/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.serialization;

import org.mockito.Mockito;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Observable;

/**
 * These classes are here purely to show that mocks can be serialized in a different VM
 *
 * Just run as standalone app {@link SerializeMockToFile}, then {@link DeserializeMockFromFile}
 */
public class SerializeMockToFile {

    public static void main(String[] args) {
       
        Observable observable = Mockito.mock(
                Observable.class,
                Mockito.withSettings().serializable()
        );

        // play with mock
        observable.addObserver(null);
        observable.countObservers();

        // serialize to file
        serializeMock(observable, "mockito_mock.ser");
    }

    private static void serializeMock(Object obj, String filename) {
        OutputStream os = null;
        ObjectOutputStream oos = null;

        try {
            os = new BufferedOutputStream(new FileOutputStream(filename, false));
            oos = new ObjectOutputStream(os);

            oos.writeObject(obj);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (oos != null) oos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private static class DefaultMethodInterceptor implements MethodInterceptor, Serializable {
        private static final long serialVersionUID = 6606245777399406255L;

        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            return methodProxy.invokeSuper(o, objects);
        }
    }
}
