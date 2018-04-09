package org.mockito

import org.scalatest.{FlatSpec, Matchers => ScalaTestMatchers}


class DoSomethingTest extends FlatSpec with MockitoSugar with ScalaTestMatchers {

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

    "doCallRealMethod" should "work as normal" in {
        val aMock = mock[Foo]

        doCallRealMethod.when(aMock).bar

        aMock.bar shouldBe "not mocked"
    }

    "doAnswer" should "work as normal" in {
        val aMock = mock[Foo]

        doAnswer(_ => "mocked!").when(aMock).bar

        aMock.bar shouldBe "mocked!"
    }

    "doReturn(toBeReturned)" should "not fail with overloading issues" in {
        val aMock = mock[Foo]

        doReturn("mocked!").when(aMock).bar

        aMock.bar shouldBe "mocked!"
    }

    "doReturn(toBeReturned, toBeReturnedNext)" should "not fail with overloading issues" in {
        val aMock = mock[Foo]

        doReturn("mocked!", "mocked again!").when(aMock).bar

        aMock.bar shouldBe "mocked!"
        aMock.bar shouldBe "mocked again!"
    }

    "doReturn(toBeReturned)" should "work with AnyVals" in {
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

    "doReturn(toBeReturned, toBeReturnedNext)" should "work with AnyVals" in {
        val aMock = mock[Foo]

        doReturn(999d, 111d).when(aMock).returnDouble
        aMock.returnDouble shouldBe 999
        aMock.returnDouble shouldBe 111

        doReturn(999f, 111f).when(aMock).returnFloat
        aMock.returnFloat shouldBe 999
        aMock.returnFloat shouldBe 111

        doReturn(999l, 111l).when(aMock).returnLong
        aMock.returnLong shouldBe 999
        aMock.returnLong shouldBe 111

        doReturn(999, 111).when(aMock).returnInt
        aMock.returnInt shouldBe 999
        aMock.returnInt shouldBe 111

        doReturn(255.toShort, 111.toShort).when(aMock).returnShort
        aMock.returnShort shouldBe 255
        aMock.returnShort shouldBe 111

        doReturn(128.toByte, 111.toByte).when(aMock).returnByte
        aMock.returnByte shouldBe 128.toByte
        aMock.returnByte shouldBe 111.toByte

        doReturn('c', 'z').when(aMock).returnChar
        aMock.returnChar shouldBe 'c'
        aMock.returnChar shouldBe 'z'

        doReturn(false, true).when(aMock).returnBoolean
        aMock.returnBoolean shouldBe false
        aMock.returnBoolean shouldBe true
    }

    "doThrow" should "work as normal" in {
        val aMock = mock[Foo]

        doThrow(new IllegalArgumentException).when(aMock).bar

        a[IllegalArgumentException] shouldBe thrownBy(aMock.bar)
    }

    "doThrow[T]" should "work as normal" in {
        val aMock = mock[Foo]

        doThrow[IllegalArgumentException].when(aMock).bar

        a[IllegalArgumentException] shouldBe thrownBy(aMock.bar)
    }
}
