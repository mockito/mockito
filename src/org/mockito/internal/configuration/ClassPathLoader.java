/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.ServiceConfigurationError;
import org.mockito.configuration.IMockitoConfiguration;
import org.mockito.exceptions.misusing.MockitoConfigurationException;
import org.mockito.internal.creation.CglibMockMaker;
import org.mockito.plugins.MockMaker;

public class ClassPathLoader {
    private static final MockMaker mockMaker = findPlatformMockMaker();

    /**
     * @return configuration loaded from classpath or null
     */
    @SuppressWarnings({"unchecked"})
    public IMockitoConfiguration loadConfiguration() {
        //Trying to get config from classpath
        Class configClass;
        try {
            configClass = (Class) Class.forName("org.mockito.configuration.MockitoConfiguration");
        } catch (ClassNotFoundException e) {
            //that's ok, it means there is no global config, using default one.
            return null;
        }

        try {
            return (IMockitoConfiguration) configClass.newInstance();
        } catch (ClassCastException e) {
            throw new MockitoConfigurationException("\n" +
                    "MockitoConfiguration class must implement org.mockito.configuration.IMockitoConfiguration interface.", e);
        } catch (Exception e) {
            throw new MockitoConfigurationException("\n" +
                    "Unable to instantiate org.mockito.configuration.MockitoConfiguration class. Does it have a safe, no-arg constructor?", e);
        }
    }

    /**
     * Returns the best mock maker available for the current runtime.
     */
    public static MockMaker getMockMaker() {
        return mockMaker;
    }

    /**
     * Scans the classpath to find a mock maker plugin if one is available,
     * allowing mockito to run on alternative platforms like Android.
     */
    private static MockMaker findPlatformMockMaker() {
        for (MockMaker mockMaker : loadImplementations(MockMaker.class)) {
            return mockMaker; // return the first one service loader finds (if any)
        }
        return new CglibMockMaker(); // default implementation
    }

    /**
     * Equivalent to {@link java.util.ServiceLoader#load} but without requiring
     * Java 6 / Android 2.3 (Gingerbread).
     */
    static <T> List<T> loadImplementations(Class<T> service) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = ClassLoader.getSystemClassLoader();
        }

        Enumeration<URL> resources;
        try {
            resources = loader.getResources("META-INF/services/" + service.getName());
        } catch (IOException e) {
            throw new ServiceConfigurationError("Failed to load " + service, e);
        }

        List<T> result = new ArrayList<T>();
        for (URL resource : Collections.list(resources)) {
            InputStream in = null;
            try {
                in = resource.openStream();
                for (String line : readerToLines(new InputStreamReader(in, "UTF-8"))) {
                    String name = stripCommentAndWhitespace(line);
                    if (!name.isEmpty()) {
                        result.add(service.cast(loader.loadClass(name).newInstance()));
                    }
                }
            } catch (Exception e) {
                throw new ServiceConfigurationError(
                        "Failed to load " + service + " using " + resource, e);
            } finally {
                closeQuietly(in);
            }
        }
        return result;
    }

    static List<String> readerToLines(Reader reader) throws IOException {
        List<String> result = new ArrayList<String>();
        BufferedReader lineReader = new BufferedReader(reader);
        String line;
        while ((line = lineReader.readLine()) != null) {
            result.add(line);
        }
        return result;
    }

    static String stripCommentAndWhitespace(String line) {
        int hash = line.indexOf('#');
        if (hash != -1) {
            line = line.substring(0, hash);
        }
        return line.trim();
    }

    static void closeQuietly(InputStream in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException ignored) {
            }
        }
    }
}