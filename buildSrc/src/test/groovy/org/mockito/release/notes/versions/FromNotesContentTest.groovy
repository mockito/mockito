package org.mockito.release.notes.versions

import org.mockito.release.notes.util.ReleaseNotesException
import spock.lang.Specification

class FromNotesContentTest extends Specification {

    def "gets previous version from notes content"() {
        def notes = """### 1.10.12 (2014-11-17 00:09 UTC)
stuff
### 1.10.13 (2014-11-17 00:09 UTC)

"""
        expect:
        new FromNotesContent(notes).previousVersion == "1.10.12"
    }

    def "throws reasonable exception if version cannot be parsed"() {
        when: new FromNotesContent("foo").previousVersion
        then: thrown(ReleaseNotesException)
    }
}
