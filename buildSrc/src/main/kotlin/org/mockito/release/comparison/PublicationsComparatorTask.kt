package org.mockito.release.comparison

import groovy.lang.Closure
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import java.io.File

open class PublicationsComparatorTask : DefaultTask(), PublicationsComparator {

    private var zipComparator: ZipComparator? = null
    private var pomComparator: PomComparator? = null

    override fun compareBinaries(left: Closure<File>, right: Closure<File>) {
        zipComparator = ZipComparator(left, right)
    }

    override fun comparePoms(left: Closure<String>, right: Closure<String>) {
        pomComparator = PomComparator(left, right)
    }

    override var publicationsEqual: Boolean? = null
        get() {
            assert(publicationsEqual != null) { "Comparison task was not executed yet, the 'publicationsEqual' information not available." }
            return publicationsEqual
        }

    @TaskAction fun comparePublications() {
        logger.lifecycle("{} - about to compare publications", path)

        val poms = pomComparator!!.areEqual()
        logger.lifecycle("{} - pom files equal: {}", path, poms)

        val result = zipComparator!!.compareFiles()
        logger.info("{} - compared binaries: '{}' and '{}'", path, result.file1, result.file2)
        val jars = result.equal
        logger.lifecycle("{} - source jars equal: {}", path, jars)

        this.publicationsEqual = jars && poms
    }
}