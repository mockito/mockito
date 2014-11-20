package org.mockito.release.notes.improvements

import spock.lang.Ignore
import spock.lang.Specification
import spock.lang.Subject

class GitHubTicketFetcherTest extends Specification {

    @Subject fetcher = new GitHubTicketFetcher()
    def token = "secret"

    @Ignore //manual test, but we could make it working if we create a dummy repo with dummy auth commit. Not sure if it is worth it.
    def "fetches improvements from GitHub"() {
        def impr = new DefaultImprovements()
        when: fetcher.fetchTickets(token, ['109', '108', '99999', '112'], impr)
        then: impr.toText() == """* Improvements: 2
  * Clarify Spy vs Mock CALLS_REAL_METHODS [(#108)](https://github.com/mockito/mockito/issues/108)
  * Allow instances of other classes in AdditionalAnswers.delegatesTo [(#112)](https://github.com/mockito/mockito/issues/112)"""
    }
}
