/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.junit;

import org.mockito.Incubating;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.internal.junit.JUnitRule;
import org.mockito.internal.junit.VerificationCollectorImpl;
import org.mockito.quality.Strictness;

/**
 * Mockito supports JUnit via:
 * <li>
 *     <ul>JUnit Rules - see {@link MockitoRule}</ul>
 *     <ul>JUnit runners - see {@link MockitoJUnitRunner}</ul>
 *     <ul><a href="http://javadoc.io/page/org.mockito/mockito-junit-jupiter/latest/org/mockito/junit/jupiter/MockitoExtension.html">JUnit Jupiter extension</a></ul>
 * </li>
 *
 * @since 1.10.17
 */
public class MockitoJUnit {

    /**
     * Creates rule instance that initiates &#064;Mocks
     * For more details and examples see {@link MockitoRule}.
     *
     * @return the rule instance
     * @since 1.10.17
     */
    public static MockitoRule rule() {
        return new JUnitRule(Plugins.getMockitoLogger(), Strictness.WARN);
    }

    /**
     * Creates a rule instance that can perform lazy verifications.
     *
     * @see VerificationCollector
     * @return the rule instance
     * @since 2.1.0
     */
    @Incubating
    public static VerificationCollector collector() {
        return new VerificationCollectorImpl();
    }
}
