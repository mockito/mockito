/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.session;

import org.mockito.MockitoSession;
import org.mockito.internal.framework.DefaultMockitoSession;
import org.mockito.internal.framework.SamsMockitoSession;
import org.mockito.internal.junit.UniversalTestListener;
import org.mockito.internal.util.ConsoleMockitoLogger;
import org.mockito.internal.util.MockitoLogger;
import org.mockito.quality.Strictness;
import org.mockito.session.MockitoSessionBuilder;
import org.mockito.session.MockitoSessionLogger;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

public class DefaultMockitoSessionBuilder implements MockitoSessionBuilder {

    private List<Object> testClassInstances = new ArrayList<Object>();
    private String name;
    private Strictness strictness;
    private MockitoSessionLogger logger;
    private boolean samsSession;
    private UniversalTestListener listener;

    @Override
    public MockitoSessionBuilder initMocks(Object testClassInstance) {
        if (testClassInstance != null) {
            this.testClassInstances.add(testClassInstance);
        }
        return this;
    }

    @Override
    public MockitoSessionBuilder initMocks(Object... testClassInstances) {
        if (testClassInstances != null) {
            for (Object instance : testClassInstances) {
                initMocks(instance);
            }
        }
        return this;
    }

    @Override
    public MockitoSessionBuilder name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public MockitoSessionBuilder strictness(Strictness strictness) {
        this.strictness = strictness;
        return this;
    }

    @Override
    public MockitoSessionBuilder logger(MockitoSessionLogger logger) {
        this.logger = logger;
        return this;
    }

    @Override
    public MockitoSessionBuilder enableSamsSession(UniversalTestListener testListener) {
        this.samsSession = true;
        this.listener = testListener;

        return this;
    }

    @Override
    public MockitoSession startMocking() {
        //Configure default values
        List<Object> effectiveTestClassInstances;
        String effectiveName;
        if (testClassInstances.isEmpty()) {
            effectiveTestClassInstances = emptyList();
            effectiveName = this.name == null ? "<Unnamed Session>" : this.name;
        } else {
            effectiveTestClassInstances = new ArrayList<Object>(testClassInstances);
            Object lastTestClassInstance = testClassInstances.get(testClassInstances.size() - 1);
            effectiveName = this.name == null ? lastTestClassInstance.getClass().getName() : this.name;
        }
        Strictness effectiveStrictness = this.strictness == null ? Strictness.STRICT_STUBS : this.strictness;
        MockitoLogger logger = this.logger == null ? new ConsoleMockitoLogger() : new MockitoLoggerAdapter(this.logger);
        if (samsSession) {
            return new SamsMockitoSession(effectiveTestClassInstances, effectiveName, listener);
        }
        return new DefaultMockitoSession(effectiveTestClassInstances, effectiveName, effectiveStrictness, logger);
    }
}
