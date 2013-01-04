/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage.serialization;

import org.mockito.Mockito;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Observable;

/**
 * These classes are here purely to show that mocks can be serialized in a different VM
 *
 * Just run as standalone app {@link SerializeMockToFile}, then {@link DeserializeMockFromFile}
 */

public class DeserializeMockFromFile {

    public static void main(String[] args) {
        Observable o = (Observable) deSerializeObject("mockito_mock.ser");

        // then verify
        Mockito.verify(o).addObserver(null);
        Mockito.verify(o).countObservers();
    }

    private static Object deSerializeObject(String filename) {
        InputStream is = null;
        ObjectInputStream ois = null;
        Object returnObject = null;
        try {

            is = new BufferedInputStream(new FileInputStream(filename));
            ois = new ObjectInputStream(is);
            returnObject = ois.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (ois != null)
                    ois.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return returnObject;
    }
   
}

