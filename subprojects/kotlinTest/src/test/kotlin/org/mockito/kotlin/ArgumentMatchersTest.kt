package org.mockito.kotlin

import org.hamcrest.core.IsEqual
import org.junit.Assert.assertThat
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class ArgumentMatchersTest {

    @Test
    fun `test argument matchers`() {
        val mockDependency = mock(Dependency::class.java)
        `when`(mockDependency.foo(ArgumentMatchers.any(InputArgument::class.java))).thenReturn("mocked response")

        val target = Target(mockDependency)
        assertThat(target.doSomething(), IsEqual("mocked response"))
    }
}

class Target(val dependency: Dependency) {
    fun doSomething() = dependency.foo(InputArgument("blah"))
}

class Dependency {
    fun foo(input: InputArgument) = "do something with ${input.value}"
}

class InputArgument(val value: String)
