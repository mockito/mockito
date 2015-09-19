package org.mockito.release.notes.improvements

import spock.lang.IgnoreIf
import spock.lang.Specification
import spock.lang.Subject
import testutil.OfflineChecker

@IgnoreIf({ OfflineChecker.isOffline()})
class GitHubTicketFetcherTest extends Specification {

    @Subject fetcher = new GitHubTicketFetcher()

    //TODO SF tidy up this and the test subject
    def "fetches improvements from GitHub"() {
        def impr = new DefaultImprovements()
        def readOnlyToken = "a0a4c0f41c200f7c653323014d6a72a127764e17"
        when: fetcher.fetchTickets(readOnlyToken, ['109', '108', '99999', '112'], impr)
        then:
        impr.toText() == """* Improvements: 2
  * Allow instances of other classes in AdditionalAnswers.delegatesTo [(#112)](https://github.com/mockito/mockito/issues/112)
  * Clarify Spy vs Mock CALLS_REAL_METHODS [(#108)](https://github.com/mockito/mockito/issues/108)"""
    }
}
