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

package org.mockito.matchers

import org.mockito.{ArgumentMatchers => JavaMatchers}

private[mockito] trait AnyMatchers {
    /** List matcher that use Scala List to avoid compile errors like
      * Error:(40, 60) type mismatch;
      * found   : List[String] (in java.util)
      * required: List[?]      (in scala.collection.immutable)
      *
      * when trying to do something like ArgumentMatchers.anyList[String]()
      *
      */
    def anyList[T]: List[T] = any[List[T]]

    /** Seq matcher that use Scala Seq to avoid compile errors like
      * Error:(40, 60) type mismatch;
      * found   : List[String] (in java.util)
      * required: Seq[?]      (in scala.collection.immutable)
      *
      * when trying to do something like ArgumentMatchers.anyList[String]()
      *
      */
    def anySeq[T]: Seq[T] = any[Seq[T]]

    /** Iterable matcher that use Scala Iterable to avoid compile errors like
      * Error:(40, 60) type mismatch;
      * found   : Iterable[String] (in java.util)
      * required: Iterable[?]      (in scala.collection.immutable)
      *
      * when trying to do something like ArgumentMatchers.anyIterable[String]()
      *
      */
    def anyIterable[T]: Iterable[T] = any[Iterable[T]]

    /** Set matcher that use Scala Set to avoid compile errors like
      * Error:(40, 60) type mismatch;
      * found   : Set[String] (in java.util)
      * required: Set[?]      (in scala.collection.immutable)
      *
      * when trying to do something like ArgumentMatchers.anySet[String]()
      *
      */
    def anySet[T]: Set[T] = any[Set[T]]

    /** Map matcher that use Scala Map to avoid compile errors like
      * Error:(40, 60) type mismatch;
      * found   : Map[String, String] (in java.util)
      * required: Map[?]      (in scala.collection.immutable)
      *
      * when trying to do something like ArgumentMatchers.anyMap[String, String]()
      *
      */
    def anyMap[K, V]: Map[K, V] = any[Map[K, V]]

    /**
      * Delegates to <code>ArgumentMatchers.any()</code>, it's main purpose is to remove the () out of
      * the method call, if you try to do that directly on the test you get this error
      *
      * Error:(71, 46) polymorphic expression cannot be instantiated to expected type;
      * found   : [T]()T
      * required: String
      * when you try to something like ArgumentMatchers.any
      *
      */
    def any[T]: T = JavaMatchers.any[T]()

    /**
      * Delegates to <code>ArgumentMatchers.anyByte()</code>, it's only here so we expose all the `ArgumentMatchers`
      * on a single place as any[T] would do the job just fine
      *
      */
    def anyByte: Byte = JavaMatchers.anyByte

    /**
      * Delegates to <code>ArgumentMatchers.anyBoolean()</code>, it's only here so we expose all the `ArgumentMatchers`
      * on a single place as any[T] would do the job just fine
      *
      */
    def anyBoolean: Boolean = JavaMatchers.anyBoolean

    /**
      * Delegates to <code>ArgumentMatchers.anyChar()</code>, it's only here so we expose all the `ArgumentMatchers`
      * on a single place as any[T] would do the job just fine
      *
      */
    def anyChar: Char = JavaMatchers.anyChar

    /**
      * Delegates to <code>ArgumentMatchers.anyDouble()</code>, it's only here so we expose all the `ArgumentMatchers`
      * on a single place as any[T] would do the job just fine
      *
      */
    def anyDouble: Double = JavaMatchers.anyDouble

    /**
      * Delegates to <code>ArgumentMatchers.anyInt()</code>, it's only here so we expose all the `ArgumentMatchers`
      * on a single place as any[T] would do the job just fine
      *
      */
    def anyInt: Int = JavaMatchers.anyInt

    /**
      * Delegates to <code>ArgumentMatchers.anyFloat()</code>, it's only here so we expose all the `ArgumentMatchers`
      * on a single place as any[T] would do the job just fine
      *
      */
    def anyFloat: Float = JavaMatchers.anyFloat

    /**
      * Delegates to <code>ArgumentMatchers.anyShort()</code>, it's only here so we expose all the `ArgumentMatchers`
      * on a single place as any[T] would do the job just fine
      *
      */
    def anyShort: Short = JavaMatchers.anyShort

    /**
      * Delegates to <code>ArgumentMatchers.anyLong()</code>, it's only here so we expose all the `ArgumentMatchers`
      * on a single place as any[T] would do the job just fine
      *
      */
    def anyLong: Long = JavaMatchers.anyLong
}
