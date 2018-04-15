package org.mockito.matchers

import org.mockito.{ArgumentMatcher, MockitoSugar}
import org.scalatest.{FlatSpec, Matchers => ScalaTestMatchers}

class ThatMatchersTest extends FlatSpec with MockitoSugar with ScalaTestMatchers with ThatMatchers {

    class EqTo[T](value: T) extends ArgumentMatcher[T] {
        override def matches(argument: T): Boolean = argument == value
    }


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

    "argThat[T]" should "work with AnyRef" in {
        val aMock = mock[Foo]

        aMock.bar("meh")
        verify(aMock).bar(argThat(new EqTo("meh")))

        aMock.barTyped("meh")
        verify(aMock).barTyped(argThat(new EqTo("meh")))

        aMock.bar(Seq("meh"))
        verify(aMock).bar(argThat(new EqTo(Seq("meh"))))

        aMock.baz(Baz("Hello", "World"))
        verify(aMock).baz(argThat(new EqTo(Baz("Hello", "World"))))
    }

    "argThat[T]" should "work with AnyVal" in {
        val aMock = mock[Foo]

        aMock.barByte(1)
        verify(aMock).barByte(argThat(new EqTo(1.toByte)))

        aMock.barBoolean(false)
        verify(aMock).barBoolean(argThat(new EqTo(false)))

        aMock.barChar('a')
        verify(aMock).barChar(argThat(new EqTo('a')))

        aMock.barDouble(1d)
        verify(aMock).barDouble(argThat(new EqTo(1d)))

        aMock.barInt(1)
        verify(aMock).barInt(argThat(new EqTo(1)))

        aMock.barFloat(1)
        verify(aMock).barFloat(argThat(new EqTo(1)))

        aMock.barShort(1)
        verify(aMock).barShort(argThat(new EqTo(1.toShort)))

        aMock.barLong(1)
        verify(aMock).barLong(argThat(new EqTo(1l)))
    }

    "primitiveThat[T]" should "work with AnyVal" in {
        val aMock = mock[Foo]

        aMock.barByte(1)
        verify(aMock).barByte(byteThat(new EqTo(1.toByte)))

        aMock.barBoolean(false)
        verify(aMock).barBoolean(booleanThat(new EqTo(false)))

        aMock.barChar('a')
        verify(aMock).barChar(charThat(new EqTo('a')))

        aMock.barDouble(1d)
        verify(aMock).barDouble(doubleThat(new EqTo(1d)))

        aMock.barInt(1)
        verify(aMock).barInt(intThat(new EqTo(1)))

        aMock.barFloat(1)
        verify(aMock).barFloat(floatThat(new EqTo(1)))

        aMock.barShort(1)
        verify(aMock).barShort(shortThat(new EqTo(1.toShort)))

        aMock.barLong(1)
        verify(aMock).barLong(longThat(new EqTo(1l)))
    }
}
