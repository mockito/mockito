package testutil

import spock.lang.Specification

class OfflineCheckerTest extends Specification {

    def "knows when is offline"() {
        expect:
        OfflineChecker.isOffline("http://mockitoooooooooo.org")
    }
}
