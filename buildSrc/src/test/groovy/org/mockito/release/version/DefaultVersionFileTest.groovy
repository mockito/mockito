package org.mockito.release.version

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Subject

class DefaultVersionFileTest extends Specification {

    @Rule TemporaryFolder dir = new TemporaryFolder()

    def "does not support files without 'version' property"() {
        def f = dir.newFile() << "asdf"
        when: new DefaultVersionFile(f)
        then: thrown(IllegalArgumentException)
    }

    def "knows version stored in the file"() {
        def f = dir.newFile() << """
foo=bar
version=2.0.0
#version=3.0.0
x
"""
        expect:
        new DefaultVersionFile(f).version == "2.0.0"
    }

    def "increments version in file"() {
        def f = dir.newFile() << """
foo=bar
version=2.0.0
#version=3.0.0
x
"""

        when:
        def v = new DefaultVersionFile(f)
        v.incrementVersion()

        then:
        f.text == """
foo=bar
version=2.0.1
#version=3.0.0
x
"""
        v.version == "2.0.1"
    }
}
