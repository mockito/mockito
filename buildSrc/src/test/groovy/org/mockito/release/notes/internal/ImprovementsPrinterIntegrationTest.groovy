package org.mockito.release.notes.internal

import spock.lang.Specification

import static org.mockito.release.notes.internal.ImprovementTestFixture.anImprovementWithIdAndLabels

class ImprovementsPrinterIntegrationTest extends Specification {

    def "should print formatted change list"() {
        given:
            def improvements = [anImprovementWithIdAndLabels("1", ["bug"]),
                                anImprovementWithIdAndLabels("2", []),
                                anImprovementWithIdAndLabels("3", ["bug", "enhancement"]),
                                anImprovementWithIdAndLabels("4", ["ignored"])]
            def mappings = ["bug": "Fixed bugs", "doc": "Documentation", "enhancement": "Improvements"]
        and:
            def segregator = new ChangeSetSegregator(mappings.keySet(), ["ignored"])
            def sut = new ImprovementsPrinter(segregator, mappings, "Other")
        when:
            def printed = sut.print(improvements)
        then:
            printed == """* Changes: 4
 * Fixed bugs: 2
  * bug 1 [(#1)](u1)
  * bug 3 [(#3)](u3)
 * Documentation: 0
 * Improvements: 1
  * bug 3 [(#3)](u3)
 * Other: 1
  * bug 2 [(#2)](u2)"""
    }
}
