package org.mockito.release.util

import spock.lang.Specification

class InputOutputTest extends Specification {

    def "closes streams"() {
        def s = new ByteArrayOutputStream()
        expect:
        InputOutput.closeStream(s)
        InputOutput.closeStream(null)
    }
}
