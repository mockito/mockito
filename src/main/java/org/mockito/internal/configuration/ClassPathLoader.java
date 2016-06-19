/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import org.mockito.configuration.IMockitoConfiguration;
import org.mockito.exceptions.misusing.MockitoConfigurationException;
import org.mockito.plugins.MockMaker;


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

    public static final String MOCKITO_CONFIGURATION_CLASS_NAME = "org.mockito.configuration.MockitoConfiguration";

    /**
     * @return configuration loaded from classpath or null
     */
    @SuppressWarnings({"unchecked"})
    public IMockitoConfiguration loadConfiguration() {
        // Trying to get config from classpath
        Class<?> configClass;
        try {
            configClass = Class.forName(MOCKITO_CONFIGURATION_CLASS_NAME);
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
}
