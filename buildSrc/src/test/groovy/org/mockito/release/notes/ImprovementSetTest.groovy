package org.mockito.release.notes

import spock.lang.Specification

class ImprovementSetTest extends Specification {

    def "renders improvements"() {
        def improvements = [
                new Improvement(id: "1", title: "feature 1", url: "f1"),
                new Improvement(id: "2", title: "refactoring 1", url: "f2"),
        ]
        def i = new ImprovementSet(improvements: improvements)
        expect:
        i.toString() == """* Improvements: 2
  * feature 1 [(#1)](f1)
  * refactoring 1 [(#2)](f2)"""
    }

    def "filters out improvements by pattern"() {
        def improvements = [
                new Improvement(id: "1", title: "feature 1", url: "f1"),
                new Improvement(id: "2", title: "Refactoring: refactoring 2", url: "f2"),
                new Improvement(id: "3", title: "feature 3", url: "f3"),
        ]
        def i = new ImprovementSet(improvements: improvements, ignorePattern: "^[Rr]efactoring.*")
        expect:
        i.toString() == """* Improvements: 2
  * feature 1 [(#1)](f1)
  * feature 3 [(#3)](f3)"""
    }
}
