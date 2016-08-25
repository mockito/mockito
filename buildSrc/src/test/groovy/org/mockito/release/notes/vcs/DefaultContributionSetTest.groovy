package org.mockito.release.notes.vcs

import org.mockito.release.notes.util.Predicate
import spock.lang.Specification
import spock.lang.Subject

class DefaultContributionSetTest extends Specification {

    @Subject contributions = new DefaultContributionSet({false} as Predicate)

    def "empty contributions"() {
        expect:
        contributions.allCommits.isEmpty()
        contributions.allTickets.isEmpty()
        contributions.toText() == "* Authors: 0\n* Commits: 0"
    }

    def "contains referenced tickets"() {
        contributions.add(new GitCommit("a@x", "A", "fixes issue #123"))
        contributions.add(new GitCommit("a@x", "A", "fixes issue 250 and #123"))
        contributions.add(new GitCommit("b@x", "B", """adds new feature
#100
"""))

        expect:
        contributions.allTickets == ["123", "100"] as Set
    }

    def "many contributions"() {
        contributions.add(new GitCommit("a@x", "A", "1"))
        contributions.add(new GitCommit("b@x", "B", "2"))
        contributions.add(new GitCommit("b@x", "B", "3"))

        expect:
        contributions.toText() == """* Authors: 2
* Commits: 3
  * 2: B
  * 1: A"""
    }

    def "ignores specific commits"() {
        contributions = new DefaultContributionSet(new IgnoreCiSkip())

        when:
        contributions.add(new GitCommit("a@b", "A", "foo [ci skip] bar"))
        contributions.add(new GitCommit("a@b", "A", "good one"))
        def c = contributions.allCommits as List

        then:
        c.size() == 1
        c[0].message == "good one"
        c[0].authorName == "A"
    }

    def "contributions by same author with different email"() {
        contributions.add(new GitCommit("john@x", "john", ""))
        contributions.add(new GitCommit("john@x", "john", ""))
        contributions.add(new GitCommit("john@y", "john", "")) //same person, different email
        contributions.add(new GitCommit("x@y", "x", "")) //different person

        expect:
        contributions.toText() == """* Authors: 2
* Commits: 4
  * 3: john
  * 1: x"""
    }

    def "contributions sorted by name if number of commits the same"() {
        contributions.add(new GitCommit("d@d", "d", ""))
        contributions.add(new GitCommit("d@d", "d", ""))
        contributions.add(new GitCommit("c@c", "c", ""))
        contributions.add(new GitCommit("B@B", "B", ""))
        contributions.add(new GitCommit("a@a", "a", ""))

        expect:
        contributions.toText() == """* Authors: 4
* Commits: 5
  * 2: d
  * 1: a
  * 1: B
  * 1: c"""
    }
}
