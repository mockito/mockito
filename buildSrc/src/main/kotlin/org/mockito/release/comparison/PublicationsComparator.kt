package org.mockito.release.comparison

import groovy.lang.Closure

import java.io.File

/**
 * Compares publications
 */
interface PublicationsComparator {

    /**
     * Sets files for comparison
     */
    fun compareBinaries(left: Closure<File>, right: Closure<File>)

    /**
     * Sets poms for comparison
     */
    fun comparePoms(left: Closure<String>, right: Closure<String>)

    /**
     * Gives information if publications are equal
     */
    var publicationsEqual: Boolean?
}
