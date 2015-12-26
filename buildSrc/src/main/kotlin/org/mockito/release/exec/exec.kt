package org.mockito.release.exec

import java.io.File

/**
 * Process execution services
 */
object Exec {

    /**
     * Provides process runner for given working dir
     */
    fun getProcessRunner(workDir: File): ProcessRunner {
        return DefaultProcessRunner(workDir)
    }
}