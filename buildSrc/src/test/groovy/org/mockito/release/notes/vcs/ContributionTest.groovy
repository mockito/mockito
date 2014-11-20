package org.mockito.release.notes.vcs

import spock.lang.Specification

class ContributionTest extends Specification {

    def "accumulates commits"() {
        def c = new Contribution(new GitCommit("a@b", "lad", "m1"))

        expect:
        c.author == "lad"
        c.authorId == "a@b"
        c.toText() == "1: lad"

        when: c.add(new GitCommit("a@b", "lad", "m2"))
        then: c.toText() == "2: lad"
    }

    def "can be sorted by number of commits"() {
        def c = new GitCommit("a", "a", "1")
        def c1 = new Contribution(c)
        def c2 = new Contribution(c).add(c)
        def c3 = new Contribution(c).add(c).add(c)

        def set = new TreeSet([c1, c3, c2])

        expect:
        c1.commits.size() == 1
        c2.commits.size() == 2
        c3.commits.size() == 3

        set as List == [c3, c2, c1]
    }

    def "has String representation"() {
        def c = new GitCommit("john.doe@gmail.com", "John Doe", "some message")
        expect:
        new Contribution(c).add(c).toText() == "2: John Doe"
    }
}
