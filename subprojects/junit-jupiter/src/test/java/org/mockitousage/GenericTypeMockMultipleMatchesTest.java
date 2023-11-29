/*
 * Copyright (c) 2023 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Verify that a {@link MockitoException} is thrown when there are multiple {@link Mock} fields that
 * do match a candidate field by type, but cannot be matched by name.
 *
 * Uses a JUnit 5 extension to obtain the JUnit 5 {@link ExtensionContext} and
 * pass it to {@link MockitoExtension#beforeEach(ExtensionContext)}, as the exception
 * is thrown during {@link org.junit.jupiter.api.BeforeEach}.
 */
@ExtendWith(GenericTypeMockMultipleMatchesTest.ContextProvidingExtension.class)
public class GenericTypeMockMultipleMatchesTest {

    private static ExtensionContext currentExtensionContext;

    public static class ContextProvidingExtension implements BeforeEachCallback {
        @Override
        public void beforeEach(ExtensionContext context) throws Exception {
            currentExtensionContext = context;
        }
    }

    private void startMocking(Object testInstance) {
        MockitoExtension mockitoExtension = new MockitoExtension();
        mockitoExtension.beforeEach(currentExtensionContext);
    }

    @Nested
    public class MultipleCandidatesByTypeTest {
        public class UnderTestWithMultipleCandidatesByType {
            List<String> stringList;
        }

        @Mock List<String> stringList1;

        @Mock List<String> stringList2;

        @InjectMocks
        UnderTestWithMultipleCandidatesByType underTestWithMultipleCandidates =
                new UnderTestWithMultipleCandidatesByType();

        @Test
        void testMultipleCandidatesByTypes() {
            assertThrows(MockitoException.class, () -> startMocking(this));
        }
    }
}
