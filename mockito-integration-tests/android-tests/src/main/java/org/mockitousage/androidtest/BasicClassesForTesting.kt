package org.mockitousage.androidtest

open class BasicOpenClass {
    open fun emptyMethod() {
        //do nothing
    }
}

class BasicClosedClass {
    fun emptyMethod() {
        //do nothing
    }
}

interface BasicInterface {
    fun interfaceMethod()
}

class BasicOpenClassReceiver(private val basicOpenClass: BasicOpenClass) {
    fun callDependencyMethod() {
        basicOpenClass.emptyMethod()
    }
}

class BasicClosedClassReceiver(private val basicClosedClass: BasicClosedClass) {
    fun callDependencyMethod() {
        basicClosedClass.emptyMethod()
    }
}

class BasicInterfaceReceiver(private val basicInterface: BasicInterface) {
    fun callInterfaceMethod() {
        basicInterface.interfaceMethod()
    }
}
