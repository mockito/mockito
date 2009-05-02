package org.mockito.internal.runners;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.runners.util.FrameworkUsageValidator;

//TODO: check out if this runner is still recommended in jUnit 4.6
public class MockitoJUnit45AndUpRunner extends BlockJUnit4ClassRunner {

    public MockitoJUnit45AndUpRunner(Class<?> klass)
            throws InitializationError {
        super(klass);
    }

    @Override
    protected Statement withBefores(FrameworkMethod method, Object target, Statement statement) {
        //init annotated mocks before tests
        MockitoAnnotations.initMocks(target);
        return super.withBefores(method, target, statement);
    }
    
    @Override
    public void run(final RunNotifier notifier) {
        //add listener that validates framework usage at the end of each test
        notifier.addListener(new FrameworkUsageValidator(notifier));
        
        super.run(notifier);
    }
}