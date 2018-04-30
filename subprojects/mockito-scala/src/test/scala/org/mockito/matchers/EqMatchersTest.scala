package org.mockito.matchers

import org.mockito.MockitoSugar
import org.scalatest.{FlatSpec, Matchers => ScalaTestMatchers}

class EqMatchersTest extends FlatSpec with MockitoSugar with ScalaTestMatchers with EqMatchers {

    case class Baz(param1: String, param2: String)

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


        def baz(v: Baz): Baz = v
    }

    "eqTo[T]" should "work with AnyRef" in {
        val aMock = mock[Foo]

        aMock.bar("meh")
        verify(aMock).bar(eqTo("meh"))

        aMock.barTyped("meh")
        verify(aMock).barTyped(eqTo("meh"))

        aMock.bar(Seq("meh"))
        verify(aMock).bar(eqTo(Seq("meh")))

        aMock.baz(Baz("Hello", "World"))
        verify(aMock).baz(eqTo(Baz("Hello", "World")))
    }

    "eqTo[T]" should "work with AnyVal" in {
        val aMock = mock[Foo]

        aMock.barByte(1)
        verify(aMock).barByte(eqTo(1))

        aMock.barBoolean(false)
        verify(aMock).barBoolean(eqTo(false))

        aMock.barChar('a')
        verify(aMock).barChar(eqTo('a'))

        aMock.barDouble(1d)
        verify(aMock).barDouble(eqTo(1d))

        aMock.barInt(1)
        verify(aMock).barInt(eqTo(1))

        aMock.barFloat(1)
        verify(aMock).barFloat(eqTo(1))

        aMock.barShort(1)
        verify(aMock).barShort(eqTo(1))

        aMock.barLong(1)
        verify(aMock).barLong(eqTo(1l))
    }

    "same[T]" should "work with AnyRef" in {
        val aMock = mock[Foo]

        aMock.bar("meh")
        verify(aMock).bar(same("meh"))

        aMock.barTyped("meh")
        verify(aMock).barTyped(same("meh"))

        val seq = Seq("meh")
        aMock.bar(seq)
        verify(aMock).bar(same(seq))
    }

    "isA[T]" should "work with AnyRef" in {
        val aMock = mock[Foo]

        aMock.bar("meh")
        verify(aMock).bar(isA[String])

        aMock.barTyped("meh")
        verify(aMock).barTyped(isA[String])

        aMock.bar(Seq("meh"))
        verify(aMock).bar(isA[Seq[String]])
    }

    "isA[T]" should "work with AnyVal" in {
        val aMock = mock[Foo]

        aMock.barByte(1)
        verify(aMock).barByte(isA[Byte])

        aMock.barBoolean(false)
        verify(aMock).barBoolean(isA[Boolean])

        aMock.barChar('a')
        verify(aMock).barChar(isA[Char])

        aMock.barDouble(1d)
        verify(aMock).barDouble(isA[Double])

        aMock.barInt(1)
        verify(aMock).barInt(isA[Int])

        aMock.barFloat(1)
        verify(aMock).barFloat(isA[Float])

        aMock.barShort(1)
        verify(aMock).barShort(isA[Short])

        aMock.barLong(1)
        verify(aMock).barLong(isA[Long])
    }

    "refEq[T]" should "work on scala types" in {
        val aMock = mock[Foo]

        aMock.baz(Baz("Hello", "World"))
        verify(aMock).baz(refEq(Baz("Hello", "World")))
        verify(aMock).baz(refEq(Baz("Hello", "Mars"), "param2"))
    }
}
