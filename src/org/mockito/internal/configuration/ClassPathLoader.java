/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import org.mockito.configuration.IMockitoConfiguration;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.misusing.MockitoConfigurationException;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.StackTraceCleanerProvider;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Loads configuration or extension points available in the classpath.
 *
 * <p>
 * <ul>
 *     <li>
 *         Can load the mockito configuration. The user who want to provide his own mockito configuration
 *         should write the class <code>org.mockito.configuration.MockitoConfiguration</code> that implements
 *         {@link IMockitoConfiguration}. For example :
 *         <pre class="code"><code class="java">
 * package org.mockito.configuration;
 *
 * //...
 *
 * public class MockitoConfiguration implements IMockitoConfiguration {
 *     boolean enableClassCache() { return false; }
 *
 *     // ...
 * }
 *     </code></pre>
 *     </li>
 *     <li>
 *         Can load available mockito extensions. Currently Mockito only have one extension point the
 *         {@link MockMaker}. This extension point allows a user to provide his own bytecode engine to build mocks.
 *         <br>Suppose you wrote an extension to create mocks with some <em>Awesome</em> library, in order to tell
 *         Mockito to use it you need to put in your classpath
 *         <ol style="list-style-type: lower-alpha">
 *             <li>The implementation itself, for example <code>org.awesome.mockito.AwesomeMockMaker</code>.</li>
 *             <li>A file named <code>org.mockito.plugins.MockMaker</code> in a folder named
 *             <code>mockito-extensions</code>, the content of this file need to have <strong>one</strong> line with
 *             the qualified name <code>org.awesome.mockito.AwesomeMockMaker</code>.</li>
 *         </ol>
 *     </li>
 * </ul>
 * </p>
 */
public class ClassPathLoader {
    private static final String DEFAULT_MOCK_MAKER_CLASS =
            "org.mockito.internal.creation.CglibMockMaker";
    private static final String DEFAULT_STACK_TRACE_CLEANER_PROVIDER_CLASS =
            "org.mockito.internal.exceptions.stacktrace.DefaultStackTraceCleanerProvider";
    public static final String MOCKITO_CONFIGURATION_CLASS_NAME = "org.mockito.configuration.MockitoConfiguration";

    private static final MockMaker mockMaker = findPlatformMockMaker();
    private static final StackTraceCleanerProvider stackTraceCleanerProvider =
            findPlatformStackTraceCleanerProvider();

    /**
     * @return configuration loaded from classpath or null
     */
    @SuppressWarnings({"unchecked"})
    public IMockitoConfiguration loadConfiguration() {
        //Trying to get config from classpath
        Class configClass;
        try {
            configClass = (Class) Class.forName(MOCKITO_CONFIGURATION_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            //that's ok, it means there is no global config, using default one.
            return null;
        }

        try {
            return (IMockitoConfiguration) configClass.newInstance();
        } catch (ClassCastException e) {
            throw new MockitoConfigurationException("MockitoConfiguration class must implement " + IMockitoConfiguration.class.getName() + " interface.", e);
        } catch (Exception e) {
            throw new MockitoConfigurationException("Unable to instantiate " + MOCKITO_CONFIGURATION_CLASS_NAME +" class. Does it have a safe, no-arg constructor?", e);
        }
    }

    /**
     * Returns the implementation of the mock maker available for the current runtime.
     *
     * <p>Returns {@link org.mockito.internal.creation.CglibMockMaker} if no
     * {@link MockMaker} extension exists or is visible in the current classpath.</p>
     */
    public static MockMaker getMockMaker() {
        return mockMaker;
    }

    public static StackTraceCleanerProvider getStackTraceCleanerProvider() {
        //TODO we should throw some sensible exception if this is null.
        return stackTraceCleanerProvider;
    }

    /**
     * Scans the classpath to find a mock maker plugin if one is available,
     * allowing mockito to run on alternative platforms like Android.
     */
    static MockMaker findPlatformMockMaker() {
        return findPluginImplementation(MockMaker.class, DEFAULT_MOCK_MAKER_CLASS);
    }

    static StackTraceCleanerProvider findPlatformStackTraceCleanerProvider() {
        return findPluginImplementation(
                StackTraceCleanerProvider.class, DEFAULT_STACK_TRACE_CLEANER_PROVIDER_CLASS);
    }

    static <T> T findPluginImplementation(Class<T> pluginType, String defaultPluginClassName) {
        for (T plugin : loadImplementations(pluginType)) {
            return plugin; // return the first one service loader finds (if any)
        }

        try {
            // Default implementation. Use our own ClassLoader instead of the context
            // ClassLoader, as the default implementation is assumed to be part of
            // Mockito and may not be available via the context ClassLoader.
            return pluginType.cast(Class.forName(defaultPluginClassName).newInstance());
        } catch (Exception e) {
            throw new MockitoException("Internal problem occurred, please report it. " +
                    "Mockito is unable to load the default implementation of class that is a part of Mockito distribution. " +
                    "Failed to load " + pluginType, e);
        }
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
            resources = loader.getResources("mockito-extensions/" + service.getName());
        } catch (IOException e) {
            throw new MockitoException("Failed to load " + service, e);
        }

        List<T> result = new ArrayList<T>();
        for (URL resource : Collections.list(resources)) {
            InputStream in = null;
            try {
                in = resource.openStream();
                for (String line : readerToLines(new InputStreamReader(in, "UTF-8"))) {
                    String name = stripCommentAndWhitespace(line);
                    if (name.length() != 0) {
                        result.add(service.cast(loader.loadClass(name).newInstance()));
                    }
                }
            } catch (Exception e) {
                throw new MockitoConfigurationException(
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

    private static void closeQuietly(InputStream in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException ignored) {
            }
        }
    }
}