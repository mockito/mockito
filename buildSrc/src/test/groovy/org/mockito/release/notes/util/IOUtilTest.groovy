package org.mockito.release.notes.util

import spock.lang.Specification

import static org.mockito.release.notes.util.IOUtil.readFully

class IOUtilTest extends Specification {

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
}
