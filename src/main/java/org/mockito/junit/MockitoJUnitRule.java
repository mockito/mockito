package org.mockito.junit;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.mockito.internal.junit.JUnitRule;


/**
 * Please use {@link MockitoJUnit#rule()} instead of direct use.
 * The reason of the deprecation is that we want to avoid concrete classes in the public api.
 *
 * @see MockitoJUnit
 * @since 1.10.6
 */
@Deprecated
public class MockitoJUnitRule implements MethodRule, MockitoRule {
    //MethodRule is undeprecated in latest JUnit
    private final JUnitRule jUnitRule;

    /**
     * Please use {@link MockitoJUnit#rule()}.
     * The reason of the deprecation is that we want to avoid concrete classes in the public api.
     */
    @Deprecated
    public MockitoJUnitRule() {
        this.jUnitRule = new JUnitRule();
    }

    /**
     * Please use {@link MockitoJUnit#rule()}.
     * The reason of the deprecation is that we want to avoid concrete classes in the public api.
     */
    @Deprecated
    public MockitoJUnitRule(Object targetTest) {
        this();
    }

    public Statement apply(Statement base, FrameworkMethod method, Object target) {
        return jUnitRule.apply(base, target);
    }
}
