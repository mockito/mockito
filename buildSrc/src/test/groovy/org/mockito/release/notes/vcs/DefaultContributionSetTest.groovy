package org.mockito.release.notes.vcs

import spock.lang.Specification
import spock.lang.Subject

class DefaultContributionSetTest extends Specification {

    @Subject contributions = new DefaultContributionSet()

    def "empty contributions"() {
        expect:
        contributions.allCommits.isEmpty()
        contributions.toString() == "* Authors: 0\n* Commits: 0"
    }

    def "many contributions"() {
        contributions.add(new GitCommit("a@x", "A", "1"))
        contributions.add(new GitCommit("b@x", "B", "2"))
        contributions.add(new GitCommit("b@x", "B", "3"))

        expect:
        contributions.toString() == """* Authors: 2
* Commits: 3
  * 2: B
  * 1: A"""
    }
}
