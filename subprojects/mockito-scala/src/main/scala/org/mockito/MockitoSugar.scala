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

import org.mockito.MockitoEnhancerUtil.stubMock
import org.mockito.MockitoSugar.clazz
import org.mockito.stubbing.{Answer, OngoingStubbing, Stubber}
import org.mockito.verification.{VerificationMode, VerificationWithTimeout}

import scala.collection.JavaConverters._
import scala.reflect.ClassTag

private[mockito] trait DoSomething {
    /**
      * Delegates the call to <code>Mockito.doReturn(toBeReturned, toBeReturnedNext)</code>
      * but fixes the following compiler issue that happens because the overloaded vararg on the Java side
      *
      * Error:(33, 25) ambiguous reference to overloaded definition,
      * both method doReturn in class Mockito of type (x$1: Any, x$2: Object*)org.mockito.stubbing.Stubber
      * and  method doReturn in class Mockito of type (x$1: Any)org.mockito.stubbing.Stubber
      * match argument types (`Type`)
      *
      */
    def doReturn[T](toBeReturned: T, toBeReturnedNext: T*): Stubber =
        Mockito.doReturn(toBeReturned, toBeReturnedNext.map(_.asInstanceOf[Object]): _*)

    /**
      * Delegates to <code>Mockito.doThrow</code>, it's only here so we expose all the `ArgumentMatchers`
      * on a single place
      */
    def doThrow(toBeThrown: Throwable*): Stubber = Mockito.doThrow(toBeThrown: _*)

    /**
      * Delegates to <code>Mockito.doThrow(type: Class[T])</code>
      * It provides a nicer API as you can, for instance, do doThrow[Throwable] instead of doThrow(classOf[Throwable])
      *
      */
    def doThrow[T <: Throwable](implicit classTag: ClassTag[T]): Stubber = Mockito.doThrow(clazz)

    /**
      * Delegates to <code>Mockito.doNothing()</code>, it removes the parenthesis to have a cleaner API
      */
    def doNothing: Stubber = Mockito.doNothing()

    /**
      * Delegates to <code>Mockito.doCallRealMethod()</code>, it removes the parenthesis to have a cleaner API
      */
    def doCallRealMethod: Stubber = Mockito.doCallRealMethod()

    /**
      * Delegates to <code>Mockito.doAnswer()</code>, it's only here to expose the full Mockito API
      */
    def doAnswer(answer: Answer[_]): Stubber = Mockito.doAnswer(answer)
}

private[mockito] trait MockitoEnhancer {
    /**
      * Delegates to <code>Mockito.mock(type: Class[T])</code>
      * It provides a nicer API as you can, for instance, do <code>mock[MyClass]</code>
      * instead of <code>mock(classOf[MyClass])</code>
      *
      * It also pre-stub the mock so the compiler-generated methods that provide the values for the default arguments
      * are called, ie:
      * given <code>def iHaveSomeDefaultArguments(noDefault: String, default: String = "default value")</code>
      *
      * without this fix, if you call it as <code>iHaveSomeDefaultArguments("I'm not gonna pass the second argument")</code>
      * then you could have not verified it like
      * <code>verify(aMock).iHaveSomeDefaultArguments("I'm not gonna pass the second argument", "default value")</code>
      * as the value for the second parameter would have been null...
      */
    def mock[T <: AnyRef](implicit classTag: ClassTag[T]): T = enhance(Mockito.mock(clazz))

    /**
      * Delegates to <code>Mockito.mock(type: Class[T], defaultAnswer: Answer[_])</code>
      * It provides a nicer API as you can, for instance, do <code>mock[MyClass](defaultAnswer)</code>
      * instead of <code>mock(classOf[MyClass], defaultAnswer)</code>
      *
      * It also pre-stub the mock so the compiler-generated methods that provide the values for the default arguments
      * are called, ie:
      * given <code>def iHaveSomeDefaultArguments(noDefault: String, default: String = "default value")</code>
      *
      * without this fix, if you call it as <code>iHaveSomeDefaultArguments("I'm not gonna pass the second argument")</code>
      * then you could have not verified it like
      * <code>verify(aMock).iHaveSomeDefaultArguments("I'm not gonna pass the second argument", "default value")</code>
      * as the value for the second parameter would have been null...
      */
    def mock[T <: AnyRef](defaultAnswer: Answer[_])(implicit classTag: ClassTag[T]): T =
        enhance(Mockito.mock(clazz, defaultAnswer))

    /**
      * Delegates to <code>Mockito.mock(type: Class[T], mockSettings: MockSettings)</code>
      * It provides a nicer API as you can, for instance, do <code>mock[MyClass](mockSettings)</code>
      * instead of <code>mock(classOf[MyClass], mockSettings)</code>
      *
      * It also pre-stub the mock so the compiler-generated methods that provide the values for the default arguments
      * are called, ie:
      * given <code>def iHaveSomeDefaultArguments(noDefault: String, default: String = "default value")</code>
      *
      * without this fix, if you call it as <code>iHaveSomeDefaultArguments("I'm not gonna pass the second argument")</code>
      * then you could have not verified it like
      * <code>verify(aMock).iHaveSomeDefaultArguments("I'm not gonna pass the second argument", "default value")</code>
      * as the value for the second parameter would have been null...
      */
    def mock[T <: AnyRef](mockSettings: MockSettings)(implicit classTag: ClassTag[T]): T =
        enhance(Mockito.mock(clazz, mockSettings))

    /**
      * Delegates to <code>Mockito.mock(type: Class[T], name: String)</code>
      * It provides a nicer API as you can, for instance, do <code>mock[MyClass](name)</code>
      * instead of <code>mock(classOf[MyClass], name)</code>
      *
      * It also pre-stub the mock so the compiler-generated methods that provide the values for the default arguments
      * are called, ie:
      * given <code>def iHaveSomeDefaultArguments(noDefault: String, default: String = "default value")</code>
      *
      * without this fix, if you call it as <code>iHaveSomeDefaultArguments("I'm not gonna pass the second argument")</code>
      * then you could have not verified it like
      * <code>verify(aMock).iHaveSomeDefaultArguments("I'm not gonna pass the second argument", "default value")</code>
      * as the value for the second parameter would have been null...
      */
    def mock[T <: AnyRef](name: String)(implicit classTag: ClassTag[T]): T = enhance(Mockito.mock(clazz, name))


    /**
      * Delegates to <code>Mockito.reset(T... mocks)</code>, but restores the default stubs that
      * deal with default argument values
      */
    def reset[T <: AnyRef](mocks: T*)(implicit classTag: ClassTag[T]): Unit = {
        Mockito.reset(mocks: _*)
        mocks.foreach(m => enhance(m))
    }


    def mockingDetails(toInspect: scala.Any): MockingDetails = Mockito.mockingDetails(toInspect)

    /**
      * Delegates to <code>Mockito.verifyNoMoreInteractions(Object... mocks)</code>, but ignores the default stubs that
      * deal with default argument values
      */
    def verifyNoMoreInteractions(mocks: AnyRef*): Unit = {

        mocks.foreach { m =>
            mockingDetails(m)
                .getInvocations.asScala
                .filter(_.getMethod.getName.contains("$default$"))
                .foreach(_.ignoreForVerification())
        }

        Mockito.verifyNoMoreInteractions(mocks: _*)
    }

    private def enhance[T <: AnyRef](m: T)(implicit classTag: ClassTag[T]): T = stubMock(m, clazz)
}

private[mockito] trait Verifications {

    /**
      * Delegates to <code>Mockito.atLeastOnce()</code>, it removes the parenthesis to have a cleaner API
      */
    def atLeastOnce: VerificationMode = Mockito.atLeastOnce()

    /**
      * Delegates to <code>Mockito.never()</code>, it removes the parenthesis to have a cleaner API
      */
    def never: VerificationMode = Mockito.never()

    /**
      * Delegates to <code>Mockito.only()</code>, it removes the parenthesis to have a cleaner API
      */
    def only: VerificationMode = Mockito.only()

    /**
      * Delegates to <code>Mockito.timeout()</code>, it's only here to expose the full Mockito API
      */
    def timeout(millis: Int): VerificationWithTimeout = Mockito.timeout(millis)

    /**
      * Delegates to <code>Mockito.times()</code>, it's only here to expose the full Mockito API
      */
    def times(wantedNumberOfInvocations: Int): VerificationMode = Mockito.times(wantedNumberOfInvocations)

    /**
      * Delegates to <code>Mockito.calls()</code>, it's only here to expose the full Mockito API
      */
    def calls(wantedNumberOfInvocations: Int): VerificationMode = Mockito.calls(wantedNumberOfInvocations)

    /**
      * Delegates to <code>Mockito.atMost()</code>, it's only here to expose the full Mockito API
      */
    def atMost(maxNumberOfInvocations: Int): VerificationMode = Mockito.atMost(maxNumberOfInvocations)

    /**
      * Delegates to <code>Mockito.atLeast()</code>, it's only here to expose the full Mockito API
      */
    def atLeast(minNumberOfInvocations: Int): VerificationMode = Mockito.atLeast(minNumberOfInvocations)
}

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
trait MockitoSugar extends MockitoEnhancer with DoSomething with Verifications {

    /**
      * Delegates to <code>ArgumentCaptor.forClass(type: Class[T])</code>
      * It provides a nicer API as you can, for instance, do <code>argumentCaptor[SomeClass]</code>
      * instead of <code>ArgumentCaptor.forClass(classOf[SomeClass])</code>
      */
    def argumentCaptor[T <: AnyRef](implicit classTag: ClassTag[T]): ArgumentCaptor[T] = ArgumentCaptor.forClass(clazz)

    /**
      * Delegates to <code>Mockito.spy()</code>, it's only here to expose the full Mockito API
      */
    def spy[T](mockObj: T): T = Mockito.spy(mockObj)

    /**
      * Delegates to <code>Mockito.when()</code>, it's only here to expose the full Mockito API
      */
    def when[T](methodCall: T): OngoingStubbing[T] = Mockito.when(methodCall)

    /**
      * Delegates to <code>Mockito.ignoreStubs()</code>, it's only here to expose the full Mockito API
      */
    def ignoreStubs(mocks: AnyRef*): Array[AnyRef] = Mockito.ignoreStubs(mocks: _*)

    /**
      * Delegates to <code>Mockito.validateMockitoUsage()</code>, it's only here to expose the full Mockito API
      */
    def validateMockitoUsage(): Unit = Mockito.validateMockitoUsage()

    /**
      * Delegates to <code>Mockito.withSettings()</code>, it's only here to expose the full Mockito API
      */
    def withSettings: MockSettings = Mockito.withSettings()

    /**
      * Delegates to <code>Mockito.verifyZeroInteractions()</code>, it's only here to expose the full Mockito API
      */
    def verifyZeroInteractions(mocks: AnyRef*): Unit = Mockito.verifyZeroInteractions(mocks: _*)

    /**
      * Delegates to <code>Mockito.inOrder()</code>, it's only here to expose the full Mockito API
      */
    def inOrder(mocks: AnyRef*): InOrder = Mockito.inOrder(mocks: _*)

    /**
      * Delegates to <code>Mockito.verify()</code>, it's only here to expose the full Mockito API
      */
    def verify[T](mock: T): T = Mockito.verify(mock)

    /**
      * Delegates to <code>Mockito.verify()</code>, it's only here to expose the full Mockito API
      */
    def verify[T](mock: T, mode: VerificationMode): T = Mockito.verify(mock, mode)

}

/**
  * Simple object to allow the usage of the trait without mixing it in
  */
object MockitoSugar extends MockitoSugar {
    private[mockito] def clazz[T <: AnyRef](implicit classTag: ClassTag[T]) = classTag.runtimeClass.asInstanceOf[Class[T]]
}
