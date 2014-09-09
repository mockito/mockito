package org.mockito.release.notes.internal

import spock.lang.Specification

import static org.mockito.release.notes.internal.ChangeSetSegregator.NON_EXISTING_LABEL
import static org.mockito.release.notes.internal.ImprovementTestFixture.anImprovementWithIdAndLabels

class ImprovementsPrinterIntegrationTest extends Specification {

    def "should print formatted change list"() {
        given:
            def improvements = [anImprovementWithIdAndLabels("1", ["bug", "other"]),
                                anImprovementWithIdAndLabels("2", []),
                                anImprovementWithIdAndLabels("3", ["bug", "enhancement"]),
                                anImprovementWithIdAndLabels("4", ["ignored"])]
            def mappings = ["bug": "Fixed bugs", "empty": "Empty", "enhancement": "Improvements", (NON_EXISTING_LABEL): "Other"]
        and:
            def segregator = new ChangeSetSegregator(mappings.keySet(), ["ignored"])
            def sut = new ImprovementsPrinter(segregator, mappings)
        when:
            def printed = sut.print(improvements)
        then:
            printed == """* Changes: 4
 * Fixed bugs: 2
  * bug 1 [(#1)](u1)
  * bug 3 [(#3)](u3)
 * Empty: 0
 * Improvements: 1
  * bug 3 [(#3)](u3)
 * Other: 1
  * bug 2 [(#2)](u2)"""
    }
}
