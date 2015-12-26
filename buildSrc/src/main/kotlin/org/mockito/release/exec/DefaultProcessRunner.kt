package org.mockito.release.exec

import org.slf4j.LoggerFactory

import java.io.*
import java.nio.charset.Charset.defaultCharset
import java.util.Arrays
import kotlin.collections.toString

internal class DefaultProcessRunner(private val workDir: File) : ProcessRunner {

    private companion object {
        val LOG = LoggerFactory.getLogger(DefaultProcessRunner::class.java)
    }

    override fun run(vararg commandLine: String): String {
        LOG.info("Executing command: {}", commandLine as Any)

        try {
            val process = ProcessBuilder(*commandLine).directory(workDir).redirectErrorStream(true).start()
            return process.inputStream.readBytes().toString(defaultCharset())
        } catch (e: Exception) {
            throw RuntimeException("Problems executing command: " + Arrays.toString(commandLine), e)
        }
    }
}
