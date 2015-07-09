/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.configuration.injection;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * Injector strategy contract
 */
public abstract class MockInjectionStrategy {

    /**
     * NOP Strategy that will always try the next strategy.
     */
    public static final MockInjectionStrategy nop() {
        return new MockInjectionStrategy() {
            protected boolean processInjection(Field field, Object fieldOwner, Set<Object> mockCandidates) {
                return false;
            }
        };
    }


    private MockInjectionStrategy nextStrategy;

    /**
     * Enqueue next injection strategy.
     *
     * <p>
     * The implementation should take care of the actual calling if required.
     * </p>
     *
     * @param strategy Queued strategy.
     * @return The passed strategy instance to allow chaining.
     */
    public MockInjectionStrategy thenTry(MockInjectionStrategy strategy) {
        if(nextStrategy != null) {
            nextStrategy.thenTry(strategy);
        } else {
            nextStrategy = strategy;
        }
        return strategy;
    }

    /**
     * Actually inject mockCandidates on field.
     *
     * <p>
     * Actual algorithm is defined in the implementations of {@link #processInjection(Field, Object, Set)}.
     * However if injection occurred successfully, the process should return <code>true</code>,
     * and <code>false</code> otherwise.
     * </p>
     *
     * <p>
     * The code takes care of calling the next strategy if available and if of course if required
     * </p>
     *
     * @param onField Field needing injection.
     * @param fieldOwnedBy The owning instance of the field.
     * @param mockCandidates A set of mock candidate, that might be injected.
     * @return <code>true</code> if successful, <code>false</code> otherwise.
     */
    public boolean process(Field onField, Object fieldOwnedBy, Set<Object> mockCandidates) {
        if(processInjection(onField, fieldOwnedBy, mockCandidates)) {
            return true;
        }
        return relayProcessToNextStrategy(onField, fieldOwnedBy, mockCandidates);
    }

    /**
     * Process actual injection.
     *
     * <p>
     * Don't call this method directly, instead call {@link #process(Field, Object, Set)}
     * </p>
     *
     * @param field Field needing injection
     * @param fieldOwner Field owner instance.
     * @param mockCandidates Pool of mocks to inject.
     * @return <code>true</code> if injection occurred, <code>false</code> otherwise
     */
    protected abstract boolean processInjection(Field field, Object fieldOwner, Set<Object> mockCandidates);

    private boolean relayProcessToNextStrategy(Field field, Object fieldOwner, Set<Object> mockCandidates) {
        return nextStrategy != null && nextStrategy.process(field, fieldOwner, mockCandidates);
    }
}
