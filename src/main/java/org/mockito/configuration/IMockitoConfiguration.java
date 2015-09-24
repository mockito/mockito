/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.configuration;

import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.stubbing.Answer;

/**
 * Use it to configure Mockito. For now there are not many configuration options but it may change in future.
 * <p>
 * In most cases you don't really need to configure Mockito. For example in case of working with legacy code, 
 * when you might want to have different 'mocking style' this interface might be helpful. 
 * A reason of configuring Mockito might be if you disagree with the {@link ReturnsEmptyValues} unstubbed mocks return.
 * <p>
 * To configure Mockito create exactly <b>org.mockito.configuration.MockitoConfiguration</b> class that implements this interface.
 * <p>
 * Configuring Mockito is completely <b>optional</b> - nothing happens if there isn't any <b>org.mockito.configuration.MockitoConfiguration</b> on the classpath. 
 * <p>
 * <b>org.mockito.configuration.MockitoConfiguration</b> must implement IMockitoConfiguration or extend {@link DefaultMockitoConfiguration}
 * <p>
 * Mockito will store single instance of org.mockito.configuration.MockitoConfiguration per thread (using ThreadLocal). 
 * For sanity of your tests, don't make the implementation stateful.
 * <p>
 * If you have comments on Mockito configuration feature don't hesitate to write to mockito@googlegroups.com
 */
public interface IMockitoConfiguration {

    /**
     * Allows configuring the default answers of unstubbed invocations
     * <p>
     * See javadoc for {@link IMockitoConfiguration}
     */    
    Answer<Object> getDefaultAnswer();

    /**
     * Configures annotations for mocks
     * <p>
     * See javadoc for {@link IMockitoConfiguration}
     */
    AnnotationEngine getAnnotationEngine();

    /**
     * This should be turned on unless you're a Mockito developer and you wish
     * to have verbose (read: messy) stack traces that only few understand (eg:
     * Mockito developers)
     * <p>
     * See javadoc for {@link IMockitoConfiguration}
     * 
     * @return if Mockito should clean stack traces
     */
    boolean cleansStackTrace();
    
    /**
     * Allow objenesis to cache classes. If you're in an environment where classes 
     * are dynamically reloaded, you can disable this to avoid classcast exceptions.
     */
    boolean enableClassCache();
}