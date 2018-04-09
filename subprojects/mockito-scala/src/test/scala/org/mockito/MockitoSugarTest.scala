package org.mockito

import org.scalatest.{FlatSpec, Matchers => ScalaTestMatchers}

class MockitoSugarTest extends FlatSpec with MockitoSugar with ScalaTestMatchers with ArgumentMatchersSugar {

    class Foo {
        def bar = "not mocked"

        def iHaveSomeDefaultArguments(noDefault: String, default: String = "default value"): String = s"$noDefault - $default"
    }

    "mock[T]" should "create a valid mock" in {
        val aMock = mock[Foo]

        when(aMock.bar) thenReturn "mocked!"

        aMock.bar shouldBe "mocked!"
    }

    "mock[T]" should "pre-configure the mock so it works with default arguments" in {
        val aMock = mock[Foo]

        aMock.iHaveSomeDefaultArguments("I'm not gonna pass the second argument")
        aMock.iHaveSomeDefaultArguments("I'm gonna pass the second argument", "second argument")

        verify(aMock).iHaveSomeDefaultArguments("I'm not gonna pass the second argument", "default value")
        verify(aMock).iHaveSomeDefaultArguments("I'm gonna pass the second argument", "second argument")
    }

    "reset[T]" should "reset the mock and re-configure the mock so it works with default arguments" in {
        val aMock = mock[Foo]

        reset(aMock)

        aMock.iHaveSomeDefaultArguments("I'm not gonna pass the second argument")

        verify(aMock).iHaveSomeDefaultArguments("I'm not gonna pass the second argument", "default value")
    }

    "verifyNoMoreInteractions" should "ignore the calls to the methods that provide default arguments" in {
        val aMock = mock[Foo]

        reset(aMock)

        aMock.iHaveSomeDefaultArguments("I'm not gonna pass the second argument")

        verify(aMock).iHaveSomeDefaultArguments("I'm not gonna pass the second argument", "default value")
        verifyNoMoreInteractions(aMock)
    }

    "argumentCaptor[T]" should "deal with default arguments" in {
        val aMock = mock[Foo]

        reset(aMock)

        aMock.iHaveSomeDefaultArguments("I'm not gonna pass the second argument")

        val captor1 = argumentCaptor[String]
        val captor2 = argumentCaptor[String]
        verify(aMock).iHaveSomeDefaultArguments(captor1.capture(), captor2.capture())

        captor1.getValue shouldBe "I'm not gonna pass the second argument"
        captor2.getValue shouldBe "default value"
    }
}
