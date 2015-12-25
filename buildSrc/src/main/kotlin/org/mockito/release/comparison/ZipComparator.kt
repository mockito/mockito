package org.mockito.release.comparison

import groovy.lang.Closure

import java.io.File

internal class ZipComparator(private val file1: Closure<File>,
                             private val file2: Closure<File>,
                             private val zipCompare: ZipCompare = ZipCompare()) {

    fun compareFiles(): Result {
        val file1 = this.file1.call()
        val file2 = this.file2.call()
        val equals = zipCompare.compareZips(file1!!.absolutePath, file2!!.absolutePath)

        return object: Result {
            override val equal: Boolean = equals
            override val file1: File = file1
            override val file2: File = file2
        }
    }

    internal interface Result {
        val equal: Boolean
        val file1: File
        val file2: File
    }
}
