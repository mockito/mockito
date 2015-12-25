package org.mockito.release.comparison

import groovy.lang.Closure

import kotlin.text.replaceFirst
import kotlin.text.toRegex

internal class PomComparator(val left: Closure<String>, val right: Closure<String>) {

    fun areEqual(): Boolean {
        val leftValue = left.call()
        val rightValue = right.call()

        fun replaceVersion(pom: String): String {
            return pom.replaceFirst("<version>(.*)</version>".toRegex(), "<version>foobar</version>")
        }

        return replaceVersion(leftValue) == replaceVersion(rightValue)
    }
}
