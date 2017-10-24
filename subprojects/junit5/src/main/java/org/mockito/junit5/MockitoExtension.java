package org.mockito.junit5;


import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.MockitoSession;
import org.mockito.quality.Strictness;


import java.util.Optional;

import static org.mockito.Mockito.mockitoSession;

public class MockitoExtension implements BeforeEachCallback, AfterEachCallback{
    private MockitoSession session;

    /**
     * Callback that is invoked <em>before</em> each test is invoked.
     *
     * @param context the current extension context; never {@code null}
     */
    @Override
    public void beforeEach(ExtensionContext context)  {
        Optional<?> testInstance=context.getTestInstance();
        if (!testInstance.isPresent()){
            //for some reason junit don't give us the test instance
            //there is nothing we can do in this case
            return;

        }

        session = mockitoSession()
            .initMocks(testInstance.get())
            .strictness(Strictness.WARN)
            .startMocking();

    }

    /**
     * Callback that is invoked <em>after</em> each test has been invoked.
     *
     * @param context the current extension context; never {@code null}
     */
    @Override
    public void afterEach(ExtensionContext context) {
        session.finishMocking();
    }
}
