/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.configuration;

import org.mockito.stubbing.Answer;

public class MockitoConfiguration extends DefaultMockitoConfiguration
        implements IMockitoConfiguration {

    private Answer<Object> overriddenDefaultAnswer = null;

    private boolean cleansStackTrace;

    private org.mockito.plugins.AnnotationEngine overriddenEngine;

    private boolean enableClassCache = true;

    // for testing purposes, allow to override the configuration
    public void overrideDefaultAnswer(Answer<Object> defaultAnswer) {
        this.overriddenDefaultAnswer = defaultAnswer;
    }

    // for testing purposes, allow to override the configuration
    public void overrideCleansStackTrace(boolean cleansStackTrace) {
        this.cleansStackTrace = cleansStackTrace;
    }

    // for testing purposes, allow to override the annotation engine
    public void overrideAnnotationEngine(org.mockito.plugins.AnnotationEngine engine) {
        this.overriddenEngine = engine;
    }

    // for testing purposes, allow to override the annotation engine
    public void overrideEnableClassCache(boolean enableClassCache) {
        this.enableClassCache = enableClassCache;
    }

    @Override
    public Answer<Object> getDefaultAnswer() {
        if (overriddenDefaultAnswer == null) {
            return super.getDefaultAnswer();
        } else {
            return overriddenDefaultAnswer;
        }
    }

    @Override
    public boolean cleansStackTrace() {
        return cleansStackTrace;
    }

    @Override
    public boolean enableClassCache() {
        return enableClassCache;
    }
}
