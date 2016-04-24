package org.mockito.internal.junit;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoRule;

/**
 * Internal implementation.
 */
public class JUnitRule implements MockitoRule {
	@Override
	public Statement apply(final Statement base, FrameworkMethod method, final Object target) {
		return new Statement() {
		    @Override
		    public void evaluate() throws Throwable {
		        MockitoAnnotations.initMocks(target);
		        base.evaluate();
		        Mockito.validateMockitoUsage();
		    }
		};
	}
}
