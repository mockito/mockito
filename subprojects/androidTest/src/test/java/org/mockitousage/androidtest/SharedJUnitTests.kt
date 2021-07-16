package org.mockitousage.androidtest

import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class SharedJUnitTests(
    private val mockedViaAnnotationBasicOpenClass: BasicOpenClass,
    private val mockedViaAnnotationBasicClosedClass: BasicClosedClass,
    private val mockedViaAnnotationBasicInterface: BasicInterface,
)
{
    fun mockAndUseBasicOpenClassUsingAnnotatedMock() {
        val basicClass = BasicOpenClassReceiver(mockedViaAnnotationBasicOpenClass)
        basicClass.callDependencyMethod()
    }

    fun mockAndUseBasicOpenClassUsingLocalMock() {
        val basicOpenClass = mock(BasicOpenClass::class.java)
        val basicReceiver = BasicOpenClassReceiver(basicOpenClass)
        basicReceiver.callDependencyMethod()
    }

    fun mockAndUseBasicOpenClassWithVerify() {
        val basicClass = BasicOpenClassReceiver(mockedViaAnnotationBasicOpenClass)
        basicClass.callDependencyMethod()
        verify(mockedViaAnnotationBasicOpenClass).emptyMethod()
    }

//Closed class

    fun mockAndUseBasicClosedClassUsingAnnotatedMock() {
        val basicReceiver = BasicClosedClassReceiver(mockedViaAnnotationBasicClosedClass)
        basicReceiver.callDependencyMethod()
    }

    fun mockAndUseBasicClosedClassUsingLocalMock() {
        val basicClosedClass = mock(BasicClosedClass::class.java)
        val basicReceiver = BasicClosedClassReceiver(basicClosedClass)
        basicReceiver.callDependencyMethod()
    }

    fun mockAndUseBasicClosedClassWithVerify() {
        val basicReceiver = BasicClosedClassReceiver(mockedViaAnnotationBasicClosedClass)
        basicReceiver.callDependencyMethod()
        verify(mockedViaAnnotationBasicClosedClass).emptyMethod()
    }

//Interface

    fun mockAndUseBasicInterfaceUsingAnnotatedMock() {
        val receiver = BasicInterfaceReceiver(mockedViaAnnotationBasicInterface)
        receiver.callInterfaceMethod()
        verify(mockedViaAnnotationBasicInterface).interfaceMethod()
    }

    fun mockAndUseBasicInterfaceUsingLocalMock() {
        val basicInterface = mock(BasicInterface::class.java)
        val receiver = BasicInterfaceReceiver(basicInterface)
        receiver.callInterfaceMethod()
    }

    fun mockAndUseBasicInterfaceAndVerify() {
        val basicInterface = mock(BasicInterface::class.java)
        val receiver = BasicInterfaceReceiver(basicInterface)
        receiver.callInterfaceMethod()
        verify(basicInterface).interfaceMethod()
    }
}
