package org.mockitousage.androidtest

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class JUnit4BasicTests {

    private var closeable: AutoCloseable? = null
    private lateinit var sharedJUnitTests: SharedJUnitTests

    @Mock private lateinit var mockedViaAnnotationBasicOpenClass: BasicOpenClass
    @Mock private lateinit var mockedViaAnnotationBasicClosedClass: BasicClosedClass
    @Mock private lateinit var mockedViaAnnotationBasicInterface: BasicInterface

    @Before
    fun setup() {
        closeable = MockitoAnnotations.openMocks(this)
        sharedJUnitTests = SharedJUnitTests(
            mockedViaAnnotationBasicOpenClass,
            mockedViaAnnotationBasicClosedClass,
            mockedViaAnnotationBasicInterface)
    }

    @After
    @Throws(Exception::class)
    fun releaseMocks() {
        closeable?.close()
    }

//Open class

    @Test
    fun mockAndUseBasicOpenClassUsingAnnotatedMock() {
        sharedJUnitTests.mockAndUseBasicOpenClassUsingAnnotatedMock()
    }

    @Test
    fun mockAndUseBasicOpenClassUsingLocalMock() {
        sharedJUnitTests.mockAndUseBasicOpenClassUsingLocalMock()
    }

    @Test
    fun mockAndUseBasicOpenClassWithVerify() {
        sharedJUnitTests.mockAndUseBasicOpenClassWithVerify()
    }

//Closed class

    @Test
    fun mockAndUseBasicClosedClassUsingAnnotatedMock() {
        sharedJUnitTests.mockAndUseBasicClosedClassUsingAnnotatedMock()
    }

    @Test
    fun mockAndUseBasicClosedClassUsingLocalMock() {
        sharedJUnitTests.mockAndUseBasicClosedClassUsingLocalMock()
    }

    @Test
    fun mockAndUseBasicClosedClassWithVerify() {
        sharedJUnitTests.mockAndUseBasicClosedClassWithVerify()
    }

//Interface

    @Test
    fun mockAndUseBasicInterfaceUsingAnnotatedMock() {
        sharedJUnitTests.mockAndUseBasicInterfaceUsingAnnotatedMock()
    }

    @Test
    fun mockAndUseBasicInterfaceUsingLocalMock() {
        sharedJUnitTests.mockAndUseBasicInterfaceUsingLocalMock()
    }

    @Test
    fun mockAndUseBasicInterfaceAndVerify() {
        sharedJUnitTests.mockAndUseBasicInterfaceAndVerify()
    }
}
