package org.mockito.matchers

import org.mockito.MockitoSugar
import org.scalatest.{FlatSpec, Matchers => ScalaTestMatchers}

class AnyMatchersTest extends FlatSpec with MockitoSugar with ScalaTestMatchers with AnyMatchers {

    class Foo {
        def bar[T](v: T): T = v

        def barTyped(v: String): String = v

        def barByte(v: Byte): Byte = v

        def barBoolean(v: Boolean): Boolean = v

        def barChar(v: Char): Char = v

        def barDouble(v: Double): Double = v

        def barInt(v: Int): Int = v

        def barFloat(v: Float): Float = v

        def barShort(v: Short): Short = v

        def barLong(v: Long): Long = v


        def barList[T](v: List[T]): List[T] = v

        def barSeq[T](v: Seq[T]): Seq[T] = v

        def barIterable[T](v: Iterable[T]): Iterable[T] = v

        def barMap[K, V](v: Map[K, V]): Map[K, V] = v

        def barSet[T](v: Set[T]): Set[T] = v
    }

    "any[Collection]" should "work with Scala types" in {
        val aMock = mock[Foo]

        when(aMock.barSeq(anySeq[String])) thenReturn Seq("mocked!")
        aMock.barSeq(Seq("meh")) shouldBe Seq("mocked!")
        verify(aMock).barSeq(Seq("meh"))

        when(aMock.barList(anyList[String])) thenReturn List("mocked!")
        aMock.barList(List("meh")) shouldBe List("mocked!")
        verify(aMock).barList(List("meh"))

        when(aMock.barIterable(anyIterable[String])) thenReturn Iterable("mocked!")
        aMock.barIterable(Iterable("meh")) shouldBe Iterable("mocked!")
        verify(aMock).barIterable(Iterable("meh"))

        when(aMock.barMap(anyMap[String, String])) thenReturn Map("I am" -> "mocked!")
        aMock.barMap(Map.empty) shouldBe Map("I am" -> "mocked!")
        verify(aMock).barMap(Map.empty)

        when(aMock.barSet(anySet[String])) thenReturn Set("mocked!")
        aMock.barSet(Set("meh")) shouldBe Set("mocked!")
        verify(aMock).barSet(Set("meh"))
    }

    "any" should "work with AnyRef" in {
        val aMock = mock[Foo]

        when(aMock.bar(any)) thenReturn "mocked!"
        aMock.bar("meh") shouldBe "mocked!"
        verify(aMock).bar("meh")

        when(aMock.barTyped(any)) thenReturn "mocked!"
        aMock.barTyped("meh") shouldBe "mocked!"
        verify(aMock).barTyped("meh")
    }

    "any" should "work with AnyVal" in {
        val aMock = mock[Foo]

        when(aMock.barByte(any)) thenReturn 10.toByte
        aMock.barByte(1) shouldBe 10
        verify(aMock).barByte(1)

        when(aMock.barBoolean(any)) thenReturn true
        aMock.barBoolean(false) shouldBe true
        verify(aMock).barBoolean(false)

        when(aMock.barChar(any)) thenReturn 'c'
        aMock.barChar('a') shouldBe 'c'
        verify(aMock).barChar('a')

        when(aMock.barDouble(any)) thenReturn 100d
        aMock.barDouble(1d) shouldBe 100d
        verify(aMock).barDouble(1d)

        when(aMock.barInt(any)) thenReturn 100
        aMock.barInt(1) shouldBe 100
        verify(aMock).barInt(1)

        when(aMock.barFloat(any)) thenReturn 100f
        aMock.barFloat(1) shouldBe 100f
        verify(aMock).barFloat(1)

        when(aMock.barShort(any)) thenReturn 100.toShort
        aMock.barShort(1) shouldBe 100
        verify(aMock).barShort(1)

        when(aMock.barLong(any)) thenReturn 100l
        aMock.barLong(1) shouldBe 100l
        verify(aMock).barLong(1l)
    }

    "anyPrimitive" should "work with AnyVal" in {
        val aMock = mock[Foo]

        when(aMock.barByte(anyByte)) thenReturn 10.toByte
        aMock.barByte(1) shouldBe 10
        verify(aMock).barByte(1)

        when(aMock.barBoolean(anyBoolean)) thenReturn true
        aMock.barBoolean(false) shouldBe true
        verify(aMock).barBoolean(false)

        when(aMock.barChar(anyChar)) thenReturn 'c'
        aMock.barChar('a') shouldBe 'c'
        verify(aMock).barChar('a')

        when(aMock.barDouble(anyDouble)) thenReturn 100d
        aMock.barDouble(1d) shouldBe 100d
        verify(aMock).barDouble(1d)

        when(aMock.barInt(anyInt)) thenReturn 100
        aMock.barInt(1) shouldBe 100
        verify(aMock).barInt(1)

        when(aMock.barFloat(anyFloat)) thenReturn 100f
        aMock.barFloat(1) shouldBe 100f
        verify(aMock).barFloat(1)

        when(aMock.barShort(anyShort)) thenReturn 100.toShort
        aMock.barShort(1) shouldBe 100
        verify(aMock).barShort(1)

        when(aMock.barLong(anyLong)) thenReturn 100l
        aMock.barLong(1) shouldBe 100l
        verify(aMock).barLong(1l)
    }
}
