package org.mockito.release.notes.internal

import spock.lang.Specification

import static ImprovementSetSegregator.NON_EXISTING_LABEL
import static org.mockito.release.notes.internal.ImprovementTestFixture.anImprovementWithIdAndLabels

class ImprovementSetSegregatorTest extends Specification {

    private ImprovementSetSegregator sut = new ImprovementSetSegregator(["bug", "improvement"], ["ignored"])

    def "should treat non labelled changes as Other"() {
        given:
            def bug = anImprovementWithIdAndLabels("1", ["bug"])
            def notLabelledChange = anImprovementWithIdAndLabels("2", [])
            def bugAndImprovement = anImprovementWithIdAndLabels("1", ["bug", "improvement"])
        when:
            def tokenizedChangesMap = sut.tokenize([bug, notLabelledChange, bugAndImprovement])
        then:
            tokenizedChangesMap.size() == 3
            tokenizedChangesMap["bug"].containsAll([bug, bugAndImprovement])
            tokenizedChangesMap["improvement"].containsAll([bugAndImprovement])
            tokenizedChangesMap[NON_EXISTING_LABEL].containsAll([notLabelledChange])
    }

    def "should treat not configured labels as Other"() {
        given:
            def wontfix = anImprovementWithIdAndLabels("4", ["wontfix"])
        when:
            def tokenizedChangesMap = sut.tokenize([wontfix])
        then:
            tokenizedChangesMap.size() == 3
            tokenizedChangesMap[NON_EXISTING_LABEL].contains(wontfix)
            tokenizedChangesMap["bug"].isEmpty()
            tokenizedChangesMap["improvement"].isEmpty()
    }

    def "should exclude ignored labels"() {
        given:
            def bug = anImprovementWithIdAndLabels("1", ["bug"])
            def ignored = anImprovementWithIdAndLabels("5", ["ignored"])
        when:
            def tokenizedChangesMap = sut.tokenize([bug, ignored])
        then:
            tokenizedChangesMap["bug"].contains(bug)
            tokenizedChangesMap[NON_EXISTING_LABEL].isEmpty()
    }

    def "should not fail on empty list"() {
        given:
            ImprovementSetSegregator sut = new ImprovementSetSegregator(["bug", "improvement"], ["ignored"])
        when:
            def tokenizedChangesMap = sut.tokenize([])
        then:
            tokenizedChangesMap.size() == 3
    }
}
