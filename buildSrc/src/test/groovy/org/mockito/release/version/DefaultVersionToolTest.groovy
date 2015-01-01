package org.mockito.release.version

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Subject

class DefaultVersionToolTest extends Specification {

    @Rule TemporaryFolder dir = new TemporaryFolder()
    @Subject tool = new DefaultVersionTool(new VersionBumper())

    def "does not increment empty file"() {
        def f = dir.newFile()
        when: tool.incrementVersion("1.0.0", f)
        then: f.text == ""
    }

    def "does not increment file without 'version'"() {
        def f = dir.newFile() << "ala\nma"
        when: tool.incrementVersion("1.0.0", f)
        then: f.text == "ala\nma"
    }

    def "increments version in file"() {
        def f = dir.newFile() << """
ala
#version=x
version=y

foo=bar
"""
        when: tool.incrementVersion("1.0.0", f)
        then: f.text == """
ala
#version=1.0.1
version=1.0.1

foo=bar
"""
    }
}
