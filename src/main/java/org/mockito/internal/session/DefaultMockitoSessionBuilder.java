package org.mockito.internal.session;

import org.mockito.MockitoSession;
import org.mockito.internal.framework.DefaultMockitoSession;
import org.mockito.internal.util.ConsoleMockitoLogger;
import org.mockito.quality.Strictness;
import org.mockito.session.MockitoSessionBuilder;

public class DefaultMockitoSessionBuilder implements MockitoSessionBuilder {

    private Object testClassInstance;
    private Strictness strictness;

    @Override
    public MockitoSessionBuilder initMocks(Object testClassInstance) {
        this.testClassInstance = testClassInstance;
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
        Object effectiveTest = this.testClassInstance == null ? new Object() : this.testClassInstance;
        Strictness effectiveStrictness = this.strictness == null ? Strictness.STRICT_STUBS : this.strictness;
        return new DefaultMockitoSession(effectiveTest, effectiveStrictness, new ConsoleMockitoLogger());
    }
}
