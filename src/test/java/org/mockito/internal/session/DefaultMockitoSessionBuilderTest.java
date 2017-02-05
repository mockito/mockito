package org.mockito.internal.session;

import org.junit.After;
import org.junit.Test;
import org.mockito.StateMaster;
import org.mockito.exceptions.misusing.UnfinishedMockingSessionException;
import org.mockito.quality.Strictness;
import org.mockitoutil.ThrowableAssert;

public class DefaultMockitoSessionBuilderTest {

    @After public void after() {
        new StateMaster().clearMockitoListeners();
    }

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

    @Test public void requires_finish_mocking() throws Exception {
        new DefaultMockitoSessionBuilder().startMocking();

        ThrowableAssert.assertThat(new Runnable() {
            public void run() {
                new DefaultMockitoSessionBuilder().startMocking();
            }
        }).throwsException(UnfinishedMockingSessionException.class);
    }
}
