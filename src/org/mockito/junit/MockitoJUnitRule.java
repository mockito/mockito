package org.mockito.junit;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.mockito.internal.rules.JUnitRule;
import org.mockito.rules.MockitoJUnit;


/**
 * Please use {@link MockitoJUnit#rule()} instead of direct use.
 *
 * @see MockitoJUnit
 */
@Deprecated
public class MockitoJUnitRule implements MethodRule {

    private final JUnitRule jUnitRule;

    /**
     * Please use {@link MockitoJUnit#rule()}.
     */
    @Deprecated
    public MockitoJUnitRule() {
        this.jUnitRule = new JUnitRule();
    }

    /**
     * Please use {@link MockitoJUnit#rule()}.
     */
    @Deprecated
    public MockitoJUnitRule(Object targetTest) {
        this();
    }

    public Statement apply(Statement base, FrameworkMethod method, Object target) {
        return jUnitRule.apply(base, target);
    }
}
