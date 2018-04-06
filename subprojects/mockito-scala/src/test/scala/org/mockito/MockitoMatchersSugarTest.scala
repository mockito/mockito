package org.mockito

import org.scalatest.{FlatSpec, Matchers => ScalaTestMatchers}

class MockitoMatchersSugarTest extends FlatSpec with MockitoSugar with MockitoMatchersSugar with ScalaTestMatchers {

    class Foo {
        def bar(arg1: String) = arg1
    }

    "eq" should "become eqTo to avoid clashes with scala equality" in {
        val aMock = mock[Foo]

        when(aMock.bar(any)) thenReturn "mocked!"

        aMock.bar("meh") shouldBe "mocked!"

        verify(aMock).bar(eqTo("meh"))
    }
}
