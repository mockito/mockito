/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.subconstruction;

import java.util.function.Predicate;

/**
 * Fixtures that inspect the stack from within constructor chains of various shapes. They live
 * outside of Mockito's internal packages so that their frames are not skipped when the inspected
 * stack is walked.
 */
public final class SubconstructionFixtures {

    private SubconstructionFixtures() {}

    /** A type whose constructor performs the stack inspection. */
    public static class DirectType {

        public DirectType(Predicate<Class<?>> check, Class<?> type) {
            var unused = check.test(type);
        }
    }

    /** A type whose no-arg constructor delegates to another constructor of the same type. */
    public static class DelegatingType {

        public DelegatingType(Predicate<Class<?>> check, Class<?> type) {
            this(check, type, true);
        }

        public DelegatingType(Predicate<Class<?>> check, Class<?> type, boolean flag) {
            var unused = check.test(type);
        }
    }

    /** A subclass whose constructor invokes the constructor under inspection. */
    public static class SubclassedType extends DirectType {

        public SubclassedType(Predicate<Class<?>> check, Class<?> type) {
            super(check, type);
        }
    }

    /** An unrelated type whose constructor instantiates the inspected type. */
    public static class UnrelatedCaller {

        public UnrelatedCaller(Predicate<Class<?>> check, Class<?> type) {
            new DirectType(check, type);
        }
    }

    /**
     * A type that inspects the stack from deep within a chain of its own constructors, so that
     * the chain outlasts the retained stack trace depth.
     */
    public static class DeepType {

        public DeepType(Predicate<Class<?>> check, Class<?> type, int depth) {
            if (depth > 0) {
                new DeepType(check, type, depth - 1);
            } else {
                var unused = check.test(type);
            }
        }
    }
}
