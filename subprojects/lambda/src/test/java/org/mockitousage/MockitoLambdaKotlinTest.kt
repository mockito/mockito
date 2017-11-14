/**
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage

import org.junit.Test
import org.mockito.MockitoLambda.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy

class MockitoLambdaKotlinTest {

    /**
     * Shows how #899 can be resolved.
     */
    @Test
    fun can_work_with_any_class() {
        val mock = mock(NonNullableClass::class.java)
        `when`(mock::nonNullableIMethods).invokedWith(any(IMethods::class.java)).thenAnswer { it }
        val methods = MethodsImpl()
        assertThat(mock.nonNullableIMethods(methods)).isSameAs(methods)
    }

    @Test
    fun throws_correct_exception_when_using_any() {
        val mock = mock(NonNullableClass::class.java)
        assertThatThrownBy { `when`(mock::nonNullableIMethods).invokedWith(any()).thenAnswer { it } }
            .hasMessageContaining("nullness check when the method was invoked.")
            .hasMessageContaining("concrete matchers such as")
    }

    /**
     * Shows how #1255 can be resolved.
     */
    @Test
    fun can_work_with_eq() {
        val mock = mock(NonNullableClass::class.java)
        `when`(mock::nonNullableString).invokedWith(eq("toto")).thenAnswer { it }
        assertThat(mock.nonNullableString("toto")).isEqualTo("toto")
    }

    open class NonNullableClass {
        open fun nonNullableIMethods(methods: IMethods): IMethods {
            return methods
        }
        open fun nonNullableString(string: String): String {
            return string
        }
    }
}
