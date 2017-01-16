package org.mockito.internal.session;

import org.junit.Test;
import org.mockito.quality.Strictness;

public class DefaultMockitoSessionBuilderTest {

    @Test public void creates_sessions() throws Exception {
        //no configuration is legal
        new DefaultMockitoSessionBuilder().startMocking().finishMocking();

        //passing null to configuration is legal, default value will be used
        new DefaultMockitoSessionBuilder().initMocks(null).startMocking().finishMocking();
        new DefaultMockitoSessionBuilder().initMocks(null).strictness(null).startMocking().finishMocking();
        new DefaultMockitoSessionBuilder().strictness(null).startMocking().finishMocking();

        //happy path
        new DefaultMockitoSessionBuilder().initMocks(this).startMocking().finishMocking();
        new DefaultMockitoSessionBuilder().initMocks(new Object()).startMocking().finishMocking();
        new DefaultMockitoSessionBuilder().strictness(Strictness.LENIENT).startMocking().finishMocking();
    }
}