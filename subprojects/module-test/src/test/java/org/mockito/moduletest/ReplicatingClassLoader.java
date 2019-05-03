/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.moduletest;

import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

public class ReplicatingClassLoader extends URLClassLoader {

    public ReplicatingClassLoader(Path path) throws MalformedURLException {
        super(new URL[]{path.toUri().toURL()}, null);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        try {
            return super.loadClass(name);
        } catch (ClassNotFoundException e) {
            InputStream in = Mockito.class.getClassLoader().getResourceAsStream(name.replace('.', '/') + ".class");
            if (in == null) {
                throw e;
            } else {
                try {
                    try {
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = (in.read(buffer))) != -1) {
                            out.write(buffer, 0, length);
                        }
                        byte[] b = out.toByteArray();
                        return defineClass(name, b, 0, b.length);
                    } finally {
                        in.close();
                    }
                } catch (IOException e1) {
                    throw new RuntimeException(e1);
                }
            }
        }
    }
}
