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

import org.mockito.MockitoEnhancer.stubMock
import org.mockito.internal.util.MockUtil
import org.mockito.stubbing.{Answer, OngoingStubbing, Stubber}
import org.mockito.verification.{VerificationMode, VerificationWithTimeout}

import scala.collection.JavaConverters._
import scala.reflect.ClassTag

/**
  * Trait that provides some basic syntax sugar.
  *
  * The idea is based on org.scalatest.mockito.MockitoSugar but it adds 100% of the Mockito API
  *
  * It also solve problems like overloaded varargs calls to Java code and pre-stub the mocks so the default arguments
  * in the method parameters work as expected
  *
  * @author Bruno Bonanno
  */
trait MockitoSugar {

    /**
      * Invokes the <code>mock(classToMock: Class[T])</code> method on the <code>Mockito</code> companion object (<em>i.e.</em>, the
      * static <code>mock(java.lang.Class<T> classToMock)</code> method in Java class <code>org.mockito.Mockito</code>).
      *
      * <p>
      * Using the Mockito API directly, you create a mock with:
      * </p>
      *
      * <pre class="stHighlight">
      * val mockCollaborator = mock(classOf[Collaborator])
      * </pre>
      *
      * <p>
      * Using this method, you can shorten that to:
      * </p>
      *
      * <pre class="stHighlight">
      * val mockCollaborator = mock[Collaborator]
      * </pre>
      */
    def mock[T <: AnyRef](implicit classTag: ClassTag[T]): T = enhance(Mockito.mock(clazz))

    /**
      * Invokes the <code>mock(classToMock: Class[T], defaultAnswer: Answer[_])</code> method on the <code>Mockito</code> companion object (<em>i.e.</em>, the
      * static <code>mock(java.lang.Class<T> classToMock, org.mockito.stubbing.Answer defaultAnswer)</code> method in Java class <code>org.mockito.Mockito</code>).
      *
      * <p>
      * Using the Mockito API directly, you create a mock with:
      * </p>
      *
      * <pre class="stHighlight">
      * val mockCollaborator = mock(classOf[Collaborator], defaultAnswer)
      * </pre>
      *
      * <p>
      * Using this method, you can shorten that to:
      * </p>
      *
      * <pre class="stHighlight">
      * val mockCollaborator = mock[Collaborator](defaultAnswer)
      * </pre>
      */
    def mock[T <: AnyRef](defaultAnswer: Answer[_])(implicit classTag: ClassTag[T]): T =
        enhance(Mockito.mock(clazz, defaultAnswer))

    /**
      * Invokes the <code>mock(classToMock: Class[T], mockSettings: MockSettings)</code> method on the <code>Mockito</code> companion object (<em>i.e.</em>, the
      * static <code>mock(java.lang.Class<T> classToMock, org.mockito.MockSettings mockSettings)</code> method in Java class <code>org.mockito.Mockito</code>).
      *
      * <p>
      * Using the Mockito API directly, you create a mock with:
      * </p>
      *
      * <pre class="stHighlight">
      * val mockCollaborator = mock(classOf[Collaborator], mockSettings)
      * </pre>
      *
      * <p>
      * Using this method, you can shorten that to:
      * </p>
      *
      * <pre class="stHighlight">
      * val mockCollaborator = mock[Collaborator](mockSettings)
      * </pre>
      */
    def mock[T <: AnyRef](mockSettings: MockSettings)(implicit classTag: ClassTag[T]): T =
        enhance(Mockito.mock(clazz, mockSettings))

    /**
      * Invokes the <code>mock(classToMock: Class[T], name: String)</code> method on the <code>Mockito</code> companion object (<em>i.e.</em>, the
      * static <code>mock(java.lang.Class<T> classToMock, java.lang.String name)</code> method in Java class <code>org.mockito.Mockito</code>).
      *
      * <p>
      * Using the Mockito API directly, you create a mock with:
      * </p>
      *
      * <pre class="stHighlight">
      * val mockCollaborator = mock(classOf[Collaborator], name)
      * </pre>
      *
      * <p>
      * Using this method, you can shorten that to:
      * </p>
      *
      * <pre class="stHighlight">
      * val mockCollaborator = mock[Collaborator](name)
      * </pre>
      */
    def mock[T <: AnyRef](name: String)(implicit classTag: ClassTag[T]): T = enhance(Mockito.mock(clazz, name))


    /**
      * Works like {@link Mockito.reset}, but it restores the behaviour added for default arguments
      */
    def reset[T <: AnyRef](mock: T)(implicit classTag: ClassTag[T]): Unit = {
        Mockito.reset(mock)
        enhance(mock)
    }

    def spy[T <: AnyRef](mockObj: T)(implicit classTag: ClassTag[T]): T = Mockito.spy(mockObj)

    def argumentCaptor[T <: AnyRef](implicit classTag: ClassTag[T]): ArgumentCaptor[T] = ArgumentCaptor.forClass(clazz)

    def when[T](methodCall: T): OngoingStubbing[T] = Mockito.when(methodCall)

    def mockingDetails(toInspect: scala.Any): MockingDetails = Mockito.mockingDetails(toInspect)

    def times(wantedNumberOfInvocations: Int): VerificationMode = Mockito.times(wantedNumberOfInvocations)

    def timeout(millis: Int): VerificationWithTimeout = Mockito.timeout(millis)

    def doNothing: Stubber = Mockito.doNothing()

    def ignoreStubs(mocks: AnyRef*): Array[AnyRef] = Mockito.ignoreStubs(mocks: _*)

    def doThrow(toBeThrown: Class[_ <: Throwable]): Stubber = Mockito.doThrow(toBeThrown)

    def validateMockitoUsage(): Unit = Mockito.validateMockitoUsage()

    def atLeastOnce: VerificationMode = Mockito.atLeastOnce()

    def doCallRealMethod: Stubber = Mockito.doCallRealMethod()

    def verify[T](mock: T, mode: VerificationMode): T = Mockito.verify(mock, mode)

    def atLeast(minNumberOfInvocations: Int): VerificationMode = Mockito.atLeast(minNumberOfInvocations)

    def calls(wantedNumberOfInvocations: Int): VerificationMode = Mockito.calls(wantedNumberOfInvocations)

    def withSettings: MockSettings = Mockito.withSettings()

    def never: VerificationMode = Mockito.never()

    def atMost(maxNumberOfInvocations: Int): VerificationMode = Mockito.atMost(maxNumberOfInvocations)

    def doAnswer(answer: Answer[_]): Stubber = Mockito.doAnswer(answer)

    def verifyZeroInteractions(mocks: AnyRef*): Unit = Mockito.verifyZeroInteractions(mocks: _*)

    def verifyNoMoreInteractions(mocks: AnyRef*): Unit = {

        mocks.foreach { m =>
            MockUtil.getInvocationContainer(m)
                .getInvocations.asScala
                .filter(_.getMethod.getName.contains("$default$"))
                .foreach(_.ignoreForVerification())
        }

        Mockito.verifyNoMoreInteractions(mocks: _*)
    }

    def inOrder(mocks: AnyRef*): InOrder = Mockito.inOrder(mocks: _*)

    def only: VerificationMode = Mockito.only()

    //There are problems when you call an overloaded method in java where the overloading is made with a vararg,
    //to make the problem worse, the vararg is an Object... so the primitives do not know they need to box
    //If you can do this thing better, please, be my guest
    def doReturn(toBeReturned: Double, toBeReturnedNext: Double*): Stubber =
        Mockito.doReturn(toBeReturned, toBeReturnedNext.map(Double.box): _*)

    def doReturn(toBeReturned: Float, toBeReturnedNext: Float*): Stubber =
        Mockito.doReturn(toBeReturned, toBeReturnedNext.map(Float.box): _*)

    def doReturn(toBeReturned: Long, toBeReturnedNext: Long*): Stubber =
        Mockito.doReturn(toBeReturned, toBeReturnedNext.map(Long.box): _*)

    def doReturn(toBeReturned: Int, toBeReturnedNext: Int*): Stubber =
        Mockito.doReturn(toBeReturned, toBeReturnedNext.map(Int.box): _*)

    def doReturn(toBeReturned: Short, toBeReturnedNext: Short*): Stubber =
        Mockito.doReturn(toBeReturned, toBeReturnedNext.map(Short.box): _*)

    def doReturn(toBeReturned: Byte, toBeReturnedNext: Byte*): Stubber =
        Mockito.doReturn(toBeReturned, toBeReturnedNext.map(Byte.box): _*)

    def doReturn(toBeReturned: Char, toBeReturnedNext: Char*): Stubber =
        Mockito.doReturn(toBeReturned, toBeReturnedNext.map(Char.box): _*)

    def doReturn(toBeReturned: Boolean, toBeReturnedNext: Boolean*): Stubber =
        Mockito.doReturn(toBeReturned, toBeReturnedNext.map(Boolean.box): _*)

    def doReturn(toBeReturned: AnyRef, toBeReturnedNext: AnyRef*): Stubber =
        Mockito.doReturn(toBeReturned, toBeReturnedNext: _*)

    def doThrow(toBeThrown: Throwable): Stubber = Mockito.doThrow(toBeThrown)

    def verify[T](mock: T): T = Mockito.verify(mock)

    private def enhance[T <: AnyRef](m: T)(implicit classTag: ClassTag[T]): T = stubMock(m, clazz)

    private def clazz[T <: AnyRef](implicit classTag: ClassTag[T]) = classTag.runtimeClass.asInstanceOf[Class[T]]

}

/**
  * Simple object to allow the usage of the trait without mixing it in
  */
object MockitoSugar extends MockitoSugar
