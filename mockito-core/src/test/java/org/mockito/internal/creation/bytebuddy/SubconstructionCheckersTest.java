/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import org.junit.Test;
import org.mockitousage.subconstruction.SubconstructionFixtures;

/**
 * Verifies that both implementations deciding whether a construction mock applies reach the same
 * verdict for every shape a constructor chain can take.
 */
public class SubconstructionCheckersTest {

    private final List<Predicate<Class<?>>> checkers = checkers();

    private static List<Predicate<Class<?>>> checkers() {
        try {
            return Arrays.asList(new StackWalkerChecker(), new StackTraceChecker());
        } catch (Exception e) {
            // Without a usable StackWalker only the stack trace based fallback exists.
            return Arrays.asList(new StackTraceChecker());
        }
    }

    @Test
    public void accepts_directly_instantiated_mocked_type() {
        for (Predicate<Class<?>> checker : checkers) {
            Capture capture = new Capture(checker);
            new SubconstructionFixtures.DirectType(
                    capture, SubconstructionFixtures.DirectType.class);
            assertThat(capture.result).isFalse();
        }
    }

    @Test
    public void accepts_this_delegation_within_the_mocked_type() {
        for (Predicate<Class<?>> checker : checkers) {
            Capture capture = new Capture(checker);
            new SubconstructionFixtures.DelegatingType(
                    capture, SubconstructionFixtures.DelegatingType.class);
            assertThat(capture.result).isFalse();
        }
    }

    @Test
    public void rejects_construction_from_subclass_constructor() {
        for (Predicate<Class<?>> checker : checkers) {
            Capture capture = new Capture(checker);
            new SubconstructionFixtures.SubclassedType(
                    capture, SubconstructionFixtures.DirectType.class);
            assertThat(capture.result).isTrue();
        }
    }

    @Test
    public void accepts_instantiation_from_unrelated_constructor() {
        for (Predicate<Class<?>> checker : checkers) {
            Capture capture = new Capture(checker);
            new SubconstructionFixtures.UnrelatedCaller(
                    capture, SubconstructionFixtures.DirectType.class);
            assertThat(capture.result).isFalse();
        }
    }

    @Test
    public void rejects_construction_when_caller_class_is_not_visible_to_mocked_type() {
        for (Predicate<Class<?>> checker : checkers) {
            Capture capture = new Capture(checker);
            // The fixtures live on the application class path, which the mocked type's class
            // loader cannot see when the type is loaded by the bootstrap loader.
            new SubconstructionFixtures.SubclassedType(capture, Runnable.class);
            assertThat(capture.result).isFalse();
        }
    }

    @Test
    public void resolves_verdict_at_the_end_of_a_long_constructor_chain() {
        for (Predicate<Class<?>> checker : checkers) {
            Capture capture = new Capture(checker);
            new SubconstructionFixtures.DeepType(
                    capture, SubconstructionFixtures.DeepType.class, 1300);
            assertThat(capture.result).isFalse();
        }
    }

    private static final class Capture implements Predicate<Class<?>> {

        private final Predicate<Class<?>> checker;
        private Boolean result;

        private Capture(Predicate<Class<?>> checker) {
            this.checker = checker;
        }

        @Override
        public boolean test(Class<?> type) {
            return result = checker.test(type);
        }
    }
}
