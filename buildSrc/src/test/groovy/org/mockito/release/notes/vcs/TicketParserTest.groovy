package org.mockito.release.notes.vcs

import spock.lang.Specification

import static org.mockito.release.notes.vcs.TicketParser.parseTickets

class TicketParserTest extends Specification {

    def "no referenced tickets"() {
        expect:
        parseTickets("").isEmpty()
        parseTickets("asdfasf").isEmpty()
    }

    def "knows referenced tickets"() {
        expect:
        parseTickets("#0") == ['0'] as Set
        parseTickets("#12 #12 #13 #14.0 #15k #-1") == ['12', '13', '14', '15'] as Set
        parseTickets("stuff 12 #133 44") == ['133'] as Set
        parseTickets("line\n a #12 x \n b #13 z \n  ") == ['12', '13'] as Set
    }
}
