package org.mockito.release.notes.util

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import static org.mockito.release.notes.util.IOUtil.readFully
import static org.mockito.release.notes.util.IOUtil.writeFile

class IOUtilTest extends Specification {

    @Rule TemporaryFolder tmp = new TemporaryFolder()

    def "reads stream"() {
        expect:
        readFully(new ByteArrayInputStream("hey\njoe!".bytes)) == "hey\njoe!"
        readFully(new ByteArrayInputStream("\n".bytes)) == "\n"
        readFully(new ByteArrayInputStream("\n\n".bytes)) == "\n\n"
        readFully(new ByteArrayInputStream("".bytes)) == ""
    }

    def "closes streams"() {
        expect:
        IOUtil.close(null)
        IOUtil.close(new ByteArrayInputStream())
    }

    def "writes file"() {
        def f = tmp.newFile()
        writeFile(f, "ala\nma")

        expect:
        readFully(f) == "ala\nma"
    }
}
