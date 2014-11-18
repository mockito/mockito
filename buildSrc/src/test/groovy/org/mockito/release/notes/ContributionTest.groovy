package org.mockito.release.notes

import spock.lang.Specification

class ContributionTest extends Specification {

    def "accumulates commits"() {
        def c = new Contribution(new GitCommit(email: "a@b", author: "lad", message: "m1"))

        expect:
        c.author == "lad"
        c.email == "a@b"
        c.toString() == "lad:1"

        when: c.add(new GitCommit(email: "a@b", author: "lad", message: "m2"))
        then: c.toString() == "lad:2"
    }
}
