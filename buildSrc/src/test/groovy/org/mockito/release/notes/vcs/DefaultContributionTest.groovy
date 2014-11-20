package org.mockito.release.notes.vcs

import spock.lang.Specification

class DefaultContributionTest extends Specification {

    def "accumulates commits"() {
        def c = new DefaultContribution(new GitCommit("a@b", "lad", "m1"))

        expect:
        c.author == "lad"
        c.authorId == "a@b"
        c.toString() == "1: lad"

        when: c.add(new GitCommit("a@b", "lad", "m2"))
        then: c.toString() == "2: lad"
    }

    def "can be sorted by number of commits"() {
        def c = new GitCommit("a", "a", "1")
        def c1 = new DefaultContribution(c)
        def c2 = new DefaultContribution(c).add(c)
        def c3 = new DefaultContribution(c).add(c).add(c)

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
        new DefaultContribution(c).add(c).toString() == "2: John Doe"
    }
}
