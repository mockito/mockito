package org.mockito.release.notes.improvements

import spock.lang.IgnoreIf
import spock.lang.Specification
import spock.lang.Subject
import testutil.OfflineChecker

@IgnoreIf({ OfflineChecker.isOffline()})
class GitHubTicketFetcherTest extends Specification {

    @Subject fetcher = new GitHubTicketFetcher()

    //This is an integration test
    //It's not ideal but it gives us a good smoke test
    //So far it is not problematic to maintain :)
    def "fetches improvements from GitHub"() {
        def impr = new DefaultImprovements([:])
        def readOnlyToken = "a0a4c0f41c200f7c653323014d6a72a127764e17"
        when: fetcher.fetchTickets(readOnlyToken, ['109', '108', '99999', '112'], impr)
        then:
        impr.improvements.get(0).labels == ["enhancement"] as Set
        impr.toText() == """* Improvements: 3
  * Allow instances of other classes in AdditionalAnswers.delegatesTo [(#112)](https://github.com/mockito/mockito/issues/112)
  * Improve automated release notes look [(#109)](https://github.com/mockito/mockito/issues/109)
  * Clarify Spy vs Mock CALLS_REAL_METHODS [(#108)](https://github.com/mockito/mockito/issues/108)"""
    }
}
