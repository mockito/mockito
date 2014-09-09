package org.mockito.release.notes.internal

import spock.lang.Specification

import static org.mockito.release.notes.internal.ImprovementTestFixture.anImprovementWithIdAndLabels

class ImprovementsPrinterIntegrationTest extends Specification {

    public static final DEFAULT_MAPPINGS = ["bug": "Fixed bugs", "doc": "Documentation", "enhancement": "Enhancements"]

    def "should print formatted change list"() {
        given:
            def changes = [anImprovementWithIdAndLabels("1", ["bug"]),
                           anImprovementWithIdAndLabels("2", []),
                           anImprovementWithIdAndLabels("3", ["bug", "enhancement"]),
                           anImprovementWithIdAndLabels("4", ["ignored"])]
        and:
            def segregator = new ImprovementSetSegregator(DEFAULT_MAPPINGS.keySet(), ["ignored"])
            def sut = new ImprovementsPrinter(segregator, DEFAULT_MAPPINGS, "Other")
        when:
            def printed = sut.print(changes)
        then:
            printed == """* Improvements: 4
  * Fixed bugs: 2
    * bug 1 [(#1)](u1)
    * bug 3 [(#3)](u3)
  * Enhancements: 1
    * bug 3 [(#3)](u3)
  * Other: 1
    * bug 2 [(#2)](u2)"""
    }

    def "should print formatted change list for no changes"() {
        given:
            def changes = []
        and:
            def segregator = new ImprovementSetSegregator(DEFAULT_MAPPINGS.keySet(), ["ignored"])
            def sut = new ImprovementsPrinter(segregator, DEFAULT_MAPPINGS, "Other")
        when:
            def printed = sut.print(changes)
        then:
            printed == """* Improvements: 0"""
    }
}
