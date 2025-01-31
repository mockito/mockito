/*
 * Copyright (c) 2024 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.sealed;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class SealedClassUnsupportedTest {
    @Test
    public void properly_report_reason_for_abstract_sealed_classes() {
        Assertions.assertThatThrownBy(() -> mock(SealedClass.class))
                .hasMessageContaining(
                        "Mockito cannot mock/spy '" + SealedClass.class + "' because :")
                .hasMessageContaining(
                        "Sealed interfaces or abstract classes can't be mocked. Interfaces cannot be instantiated and cannot be subclassed for mocking purposes. Instead of mocking a sealed interface or an abstract class, a non-abstract class can be mocked and used to represent the interface.");
    }
}

abstract sealed class SealedClass permits SealedClassA, SealedClassB {}

final class SealedClassA extends SealedClass {}

final class SealedClassB extends SealedClass {}
