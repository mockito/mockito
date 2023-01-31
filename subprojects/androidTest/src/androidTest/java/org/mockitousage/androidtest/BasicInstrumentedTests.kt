package org.mockitousage.androidtest

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class BasicInstrumentedTests {

    private var closeable: AutoCloseable? = null

    @Mock private lateinit var mockedViaAnnotationBasicOpenClass: BasicOpenClass
    @Mock private lateinit var mockedViaAnnotationBasicInterface: BasicInterface

    @Before
    fun setup() {
        closeable = MockitoAnnotations.openMocks(this)
    }

    @After
    @Throws(Exception::class)
    fun releaseMocks() {
        closeable?.close()
    }

//Open class

    @Test
    fun mockAndUseBasicClassUsingAnnotatedMock() {
        val basicClass = BasicOpenClassReceiver(mockedViaAnnotationBasicOpenClass)
        basicClass.callDependencyMethod()
    }

    @Test
    fun mockAndUseBasicClassUsingLocalMock() {
        val basicOpenClass = mock(BasicOpenClass::class.java)
        val basicReceiver = BasicOpenClassReceiver(basicOpenClass)
        basicReceiver.callDependencyMethod()
    }

    @Test
    fun mockAndUseBasicClassWithVerify() {
        val basicClass = BasicOpenClassReceiver(mockedViaAnnotationBasicOpenClass)
        basicClass.callDependencyMethod()
        verify(mockedViaAnnotationBasicOpenClass).emptyMethod()
    }

//Interface

    @Test
    fun mockAndUseBasicInterfaceUsingAnnotatedMock() {
        val receiver = BasicInterfaceReceiver(mockedViaAnnotationBasicInterface)
        receiver.callInterfaceMethod()
        verify(mockedViaAnnotationBasicInterface).interfaceMethod()
    }

    @Test
    fun mockAndUseBasicInterfaceUsingLocalMock() {
        val basicInterface = mock(BasicInterface::class.java)
        val receiver = BasicInterfaceReceiver(basicInterface)
        receiver.callInterfaceMethod()
    }

    @Test
    fun mockAndUseBasicInterfaceAndVerify() {
        val basicInterface = mock(BasicInterface::class.java)
        val receiver = BasicInterfaceReceiver(basicInterface)
        receiver.callInterfaceMethod()
        verify(basicInterface).interfaceMethod()
    }
}
