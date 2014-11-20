package org.mockito.release.notes.vcs

import spock.lang.Specification

class DefaultContributionTest extends Specification {

    def "accumulates commits"() {
        def c = new DefaultContribution(new GitCommit("a@b", "lad", "m1"))

        expect:
        c.author == "lad"
        c.email == "a@b"
        c.toString() == "lad:1"

        when: c.add(new GitCommit("a@b", "lad", "m2"))
        then: c.toString() == "lad:2"
    }
}
