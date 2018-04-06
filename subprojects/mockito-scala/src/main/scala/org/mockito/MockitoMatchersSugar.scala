/*
 * Copyright © 2017 Morgan Stanley.  All rights reserved.
 *
 * THIS SOFTWARE IS SUBJECT TO THE TERMS OF THE MIT LICENSE.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * IN ADDITION, THE FOLLOWING DISCLAIMER APPLIES IN CONNECTION WITH THIS SOFTWARE:
 * THIS SOFTWARE IS LICENSED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE AND ANY WARRANTY OF NON-INFRINGEMENT, ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. THIS SOFTWARE MAY BE REDISTRIBUTED TO OTHERS ONLY BY EFFECTIVELY USING THIS OR ANOTHER EQUIVALENT DISCLAIMER IN ADDITION TO ANY OTHER REQUIRED LICENSE TERMS.
 */

package org.mockito

import scala.reflect.ClassTag

/**
  * Trait that provides some basic syntax sugar.
  *
  * It mostly forwards the calls to org.mockito.ArgumentMatchers, but with a few improvements to make it more scala-like
  * It also renames the "eq" matcher to "eqTo" as in Scala "eq" is a keyword used to do object identity equality
  *
  * @author Bruno Bonanno
  */
trait MockitoMatchersSugar {

    def same[T](value: T): T = ArgumentMatchers.same(value)

    def anyByte: Byte = ArgumentMatchers.anyByte

    def eqTo(value: Short): Short = ArgumentMatchers.eq(value)

    def anyList: List[_] = any[List[_]]

    def booleanThat(matcher: ArgumentMatcher[Boolean]): Boolean = argThat(matcher)

    def eqTo(value: Byte): Byte = ArgumentMatchers.eq(value)

    def matches(regex: String): String = ArgumentMatchers.matches(regex)

    def anyString: String = ArgumentMatchers.anyString

    def anyMapOf[K, V]: Map[K, V] = any[Map[K, V]]

    def eqTo(value: Boolean): Boolean = ArgumentMatchers.eq(value)

    def eqTo(value: Int): Int = ArgumentMatchers.eq(value)

    def notNull: AnyRef = ArgumentMatchers.notNull

    def startsWith(prefix: String): String = ArgumentMatchers.startsWith(prefix)

    def anyBoolean: Boolean = ArgumentMatchers.anyBoolean

    def anyListOf[T](implicit classTag: ClassTag[T]): List[T] = any[List[T]]

    def anyChar: Char = ArgumentMatchers.anyChar

    def anyDouble: Double = ArgumentMatchers.anyDouble

    def any[T]: T = ArgumentMatchers.any[T]

    def anyInt: Int = ArgumentMatchers.anyInt

    def anyFloat: Float = ArgumentMatchers.anyFloat

    def anySet: Set[_] = any[Set[_]]

    def eqTo(value: Long): Long = ArgumentMatchers.eq(value)

    def byteThat(matcher: ArgumentMatcher[Byte]): Byte = argThat(matcher)

    def anyIterable: Iterable[_] = any[Iterable[_]]

    def contains(substring: String): String = ArgumentMatchers.contains(substring)

    def argThat[T](matcher: ArgumentMatcher[T]): T = ArgumentMatchers.argThat(matcher)

    def eqTo(value: Char): Char = ArgumentMatchers.eq(value)

    def anyShort: Short = ArgumentMatchers.anyShort

    def anyIterableOf[T]: Iterable[T] = any[Iterable[T]]

    def longThat(matcher: ArgumentMatcher[Long]): Long = argThat(matcher)

    def shortThat(matcher: ArgumentMatcher[Short]): Short = argThat(matcher)

    def doubleThat(matcher: ArgumentMatcher[Double]): Double = argThat(matcher)

    def eqTo(value: Float): Float = ArgumentMatchers.eq(value)

    def anyLong: Long = ArgumentMatchers.anyLong

    def anyMap: Map[_, _] = any[Map[_, _]]

    def anySetOf[T]: Set[T] = any[Set[T]]

    def isA[T](implicit classTag: ClassTag[T]): T = ArgumentMatchers.isA(classTag.runtimeClass.asInstanceOf[Class[T]])

    def endsWith(suffix: String): String = ArgumentMatchers.endsWith(suffix)

    def refEq[T](value: T, excludeFields: String*): T = ArgumentMatchers.refEq(value, excludeFields: _*)

    def floatThat(matcher: ArgumentMatcher[Float]): Float = argThat(matcher)

    def charThat(matcher: ArgumentMatcher[Character]): Char = ArgumentMatchers.charThat(matcher)

    def eqTo[T](value: T): T = ArgumentMatchers.eq(value)

    def isNull[T]: T = ArgumentMatchers.isNull[T]

    def isNotNull[T]: T = ArgumentMatchers.isNotNull[T]

    def intThat(matcher: ArgumentMatcher[Integer]): Int = ArgumentMatchers.intThat(matcher)

    def eqTo(value: Double): Double = ArgumentMatchers.eq(value)
}

object MockitoMatchersSugar extends MockitoMatchersSugar
