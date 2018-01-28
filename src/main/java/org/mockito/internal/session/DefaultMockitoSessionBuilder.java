/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.session;

import org.mockito.MockitoSession;
import org.mockito.internal.framework.DefaultMockitoSession;
import org.mockito.internal.util.ConsoleMockitoLogger;
import org.mockito.quality.Strictness;
import org.mockito.session.MockitoSessionBuilder;

import java.util.ArrayList;
import java.util.List;

public class DefaultMockitoSessionBuilder implements MockitoSessionBuilder {

    private List<Object> testClassInstances = new ArrayList<Object>();
    private Strictness strictness;

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
    public MockitoSessionBuilder strictness(Strictness strictness) {
        this.strictness = strictness;
        return this;
    }

    @Override
    public MockitoSession startMocking() {
        //Configure default values
        List<Object> effectiveTestClassInstances = new ArrayList<Object>(testClassInstances);
        Strictness effectiveStrictness = this.strictness == null ? Strictness.STRICT_STUBS : this.strictness;
        return new DefaultMockitoSession(effectiveTestClassInstances, effectiveStrictness, new ConsoleMockitoLogger());
    }
}
