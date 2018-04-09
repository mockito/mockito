package org.mockito

import org.scalatest.{FlatSpec, Matchers => ScalaTestMatchers}


class MockitoSugarTest extends FlatSpec with MockitoSugar with ScalaTestMatchers with ArgumentMatchersSugar {

    class Foo {
        def bar = "not mocked"

        def returnDouble: Double = 10

        def returnFloat: Float = 10

        def returnLong: Long = 10

        def returnInt: Int = 10

        def returnShort: Short = 10

        def returnByte: Byte = 10

        def returnChar: Char = 'a'

        def returnBoolean: Boolean = true

        def iHaveSomeDefaultArguments(noDefault: String, default: String = "default value"): String = s"$noDefault - $default"
    }

    "mock[T]" should "create a valid mock" in {
        //        val aMock = Mockito.mock(classOf[Foo]) //this is how it looks without the sugar we add
        val aMock = mock[Foo]

        when(aMock.bar) thenReturn "mocked!"

        aMock.bar shouldBe "mocked!"
    }

    "doReturn" should "not fail with overloading issues" in {
        val aMock = mock[Foo]

        //        Mockito.doReturn("mocked!").when(aMock).bar //if you try this version it will not compile
        doReturn("mocked!").when(aMock).bar

        aMock.bar shouldBe "mocked!"
    }

    "doReturn" should "work with java primitives" in {
        val aMock = mock[Foo]

        doReturn(999d).when(aMock).returnDouble
        aMock.returnDouble shouldBe 999

        doReturn(999f).when(aMock).returnFloat
        aMock.returnFloat shouldBe 999

        doReturn(999l).when(aMock).returnLong
        aMock.returnLong shouldBe 999

        doReturn(999).when(aMock).returnInt
        aMock.returnInt shouldBe 999

        doReturn(255.toShort).when(aMock).returnShort
        aMock.returnShort shouldBe 255

        doReturn(128.toByte).when(aMock).returnByte
        aMock.returnByte shouldBe 128.toByte

        doReturn('c').when(aMock).returnChar
        aMock.returnChar shouldBe 'c'

        doReturn(false).when(aMock).returnBoolean
        aMock.returnBoolean shouldBe false
    }

    "mock[T]" should "pre-configure the mock so it works with default arguments" in {
        //        val aMock = Mockito.mock(classOf[Foo]) //if you try this version you'll get null instead of "default value" fof the first verification
        val aMock = mock[Foo]

        when(aMock.iHaveSomeDefaultArguments(any, any)) thenReturn "mocked!"

        aMock.iHaveSomeDefaultArguments("I'm not gonna pass the second argument") shouldBe "mocked!"
        aMock.iHaveSomeDefaultArguments("I'm gonna pass the second argument", "second argument") shouldBe "mocked!"

        verify(aMock).iHaveSomeDefaultArguments("I'm not gonna pass the second argument", "default value")
        verify(aMock).iHaveSomeDefaultArguments("I'm gonna pass the second argument", "second argument")
        verifyNoMoreInteractions(aMock)
        //        Mockito.verifyNoMoreInteractions(aMock) //if you use the standard verifyNoMoreInteractions it will fail as it will detect the call to the default arg provider
    }
}
