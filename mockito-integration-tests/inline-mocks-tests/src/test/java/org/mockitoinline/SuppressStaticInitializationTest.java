/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoinline;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.plugins.MockMaker;

public final class SuppressStaticInitializationTest {

    @Test
    public void suppress_throws_when_premain_not_attached() {
        MockMaker mockMaker = Plugins.getMockMaker();

        assertThatThrownBy(
                        () ->
                                mockMaker.suppressStaticInitializationFor(
                                        Collections.singletonList(
                                                "org.mockitoinline.SomeNonExistentClass")))
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining("javaagent");
    }

    @Test
    public void suppress_throws_for_already_loaded_class_message() {
        MockMaker mockMaker = Plugins.getMockMaker();

        assertThatThrownBy(
                        () ->
                                mockMaker.suppressStaticInitializationFor(
                                        Arrays.asList("java.lang.String")))
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining("javaagent");
    }

    @Test
    public void restore_does_not_throw_for_any_class_name() {
        MockMaker mockMaker = Plugins.getMockMaker();

        mockMaker.restoreStaticInitializationFor(
                Collections.singletonList("org.mockitoinline.NonExistent"));
    }

    @Test
    public void restore_removes_from_suppression_set() throws Exception {
        // Verify restoreStaticInitializationFor actually delegates through
        // InlineByteBuddyMockMaker -> InlineDelegateByteBuddyMockMaker ->
        // TypeCachingBytecodeGenerator -> InlineBytecodeGenerator
        Set<String> suppressedClassNames = getSuppressedClassNamesSet();

        String testClass = "org.mockitoinline.RestoreDelegationTarget";
        suppressedClassNames.add(testClass);
        assertThat(suppressedClassNames).contains(testClass);

        Plugins.getMockMaker().restoreStaticInitializationFor(Collections.singletonList(testClass));

        assertThat(suppressedClassNames).doesNotContain(testClass);
    }

    @Test
    public void suppress_with_empty_list_throws_about_agent() {
        MockMaker mockMaker = Plugins.getMockMaker();

        assertThatThrownBy(() -> mockMaker.suppressStaticInitializationFor(Collections.emptyList()))
                .isInstanceOf(MockitoException.class)
                .hasMessageContaining("javaagent");
    }

    @Test
    public void annotation_has_correct_properties() {
        assertThat(
                        org.mockito.SuppressStaticInitializationFor.class
                                .getAnnotation(java.lang.annotation.Retention.class)
                                .value())
                .isEqualTo(java.lang.annotation.RetentionPolicy.RUNTIME);

        assertThat(
                        org.mockito.SuppressStaticInitializationFor.class
                                .getAnnotation(java.lang.annotation.Target.class)
                                .value())
                .containsExactly(java.lang.annotation.ElementType.TYPE);

        assertThat(
                        org.mockito.SuppressStaticInitializationFor.class.isAnnotationPresent(
                                java.lang.annotation.Inherited.class))
                .isTrue();

        assertThat(
                        org.mockito.SuppressStaticInitializationFor.class.isAnnotationPresent(
                                org.mockito.Incubating.class))
                .isTrue();
    }

    /**
     * Navigates the delegation chain to get the underlying suppressed class names set:
     * InlineByteBuddyMockMaker -> InlineDelegateByteBuddyMockMaker ->
     * TypeCachingBytecodeGenerator -> InlineBytecodeGenerator.suppressedClassNames
     *
     * Uses string-based reflection since the internal classes are package-private.
     */
    @SuppressWarnings("unchecked")
    private Set<String> getSuppressedClassNamesSet() throws Exception {
        MockMaker mockMaker = Plugins.getMockMaker();

        Field delegateField =
                mockMaker.getClass().getDeclaredField("inlineDelegateByteBuddyMockMaker");
        delegateField.setAccessible(true);
        Object delegate = delegateField.get(mockMaker);

        Field bytecodeGenField = delegate.getClass().getDeclaredField("bytecodeGenerator");
        bytecodeGenField.setAccessible(true);
        Object typeCaching = bytecodeGenField.get(delegate);

        Field innerGenField = typeCaching.getClass().getDeclaredField("bytecodeGenerator");
        innerGenField.setAccessible(true);
        Object inlineGen = innerGenField.get(typeCaching);

        Field suppressedField = inlineGen.getClass().getDeclaredField("suppressedClassNames");
        suppressedField.setAccessible(true);
        return (Set<String>) suppressedField.get(inlineGen);
    }
}
