package org.mockito.release.notes.vcs

import spock.lang.Specification
import spock.lang.Subject

class GitContributionsProviderTest extends Specification {

    def logProvider = Mock(GitLogProvider)
    @Subject provider = new GitContributionsProvider(logProvider, new IgnoreCiSkip())

    def log = """szczepiq@gmail.com@@info@@Szczepan Faber@@info@@Tidy-up in buildSrc
next line
@@commit@@
szczepiq@gmail.com@@info@@Szczepan Faber@@info@@Tidy-up in buildSrc - started using an interface where possible
@@commit@@
john@doe@@info@@John R. Doe@@info@@dummy commit
@@commit@@"""

    def "provides contributions"() {
        logProvider.getLog("v1.10.10", "HEAD", "--pretty=format:%ae@@info@@%an@@info@@%B%N@@commit@@") >> log

        when:
        def c = provider.getContributionsBetween("v1.10.10", "HEAD")

        then:
        c.toText() == """* Authors: 2
* Commits: 3
  * 2: Szczepan Faber
  * 1: John R. Doe"""

        and:
        def commits = c.allCommits as List
        commits[0].author == "Szczepan Faber"
        commits[0].authorId == "szczepiq@gmail.com"
        commits[0].message == "Tidy-up in buildSrc\nnext line"
    }

    def "has basic handling of garbage in log"() {
        logProvider.getLog(_, _, _) >> (log + " some garbage \n@@commit@@\n more garbage")

        when:
        def c = provider.getContributionsBetween("v1.10.10", "HEAD")

        then:
        c.allCommits.size() == 3
    }

    def "handles empty log"() {
        logProvider.getLog(_, _, _) >> ""

        when:
        def c = provider.getContributionsBetween("v1.10.10", "HEAD")

        then:
        c.allCommits.isEmpty()
    }
}
