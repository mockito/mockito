/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.plugins;

/**
 * Mockito logger.
 *
 * <p>By default logs to console</p>
 *
 * <p>All mockito logging goes through this class and could be customized as usual Mockito plugin.</p>
 *
 * <h3>Using the extension point</h3>
 *
 * <p>Suppose you wrote an extension to customize logging, in order to tell Mockito to use it you need to put
 * in your <strong>classpath</strong>:
 * <ol style="list-style-type: lower-alpha">
 *     <li>
 *         The implementation itself, for example <code>org.awesome.mockito.AwesomeLogger</code> that
 *         extends the <code>MockitoLogger</code>.
 *     </li>
 *     <li>
 *         A file "<code>mockito-extensions/org.mockito.plugins.MockitoLogger</code>". The content of this file is
 *         exactly a <strong>one</strong> line with the qualified name:
 *         <code>org.awesome.mockito.AwesomeLogger</code>.
 *      </li>
 * </ol>
 * </p>
 *
 * <p>Note that if several <code>mockito-extensions/org.mockito.plugins.MockitoLogger</code> files exists in the
 * classpath Mockito will only use the first returned by the standard {@link ClassLoader#getResource} mechanism.
 *
 * @since 2.23.19
 */
public interface MockitoLogger {
    /**
     * Log specified object.
     *
     * @param what to be logged
     */
    void log(Object what);

    /**
     * Log specified object as a warning, if supported.
     *
     * @param what to be logged.
     */
    default void warn(Object what) {
        log(what);
    }
}
