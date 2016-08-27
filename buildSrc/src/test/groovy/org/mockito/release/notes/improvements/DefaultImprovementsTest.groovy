package org.mockito.release.notes.improvements

import spock.lang.Specification

class DefaultImprovementsTest extends Specification {

    def "empty improvements"() {
        expect:
        new DefaultImprovements([:]).toText() == "* No notable improvements. See the commits for detailed changes."
    }

    def "set of improvements in order"() {
        def is = new DefaultImprovements([bug: "Bugfixes", enhancement: "Enhancements"])
            .add(new Improvement(100, "Fix bug x", "http://issues/100", ["bug"]))
            .add(new Improvement(122, "Javadoc update", "http://url/122", []))
            .add(new Improvement(125, "Some enh", "http://issues/125", ["java-8", "enhancement", "bug"]))
            .add(new Improvement(126, "Some other enh", "http://issues/126", ["enhancement"]))
            .add(new Improvement(130, "Refactoring", "http://issues/130", ["java-8", "refactoring"]))

        expect:
        is.toText() == """* Improvements: 5
  * Bugfixes: 2
    * Fix bug x [(#100)](http://issues/100)
    * Some enh [(#125)](http://issues/125)
  * Enhancements: 1
    * Some other enh [(#126)](http://issues/126)
  * Remaining changes: 2
    * Javadoc update [(#122)](http://url/122)
    * Refactoring [(#130)](http://issues/130)"""
    }

    def "no matching labels"() {
        given:
        def is = new DefaultImprovements([bug: "Bugfixes"])
            .add(new Improvement(10, "Issue 10", "10", []))

        expect: "the formatting is simplified"
        is.toText() == """* Improvements: 1
  * Issue 10 [(#10)](10)"""
    }

    def "no duplicated improvements"() {
        given:
        def is = new DefaultImprovements([bug: "Bugfixes", refactoring: "Refactorings"])
            .add(new Improvement(10, "Issue 10", "10", ["bug", "refactoring"]))
            .add(new Improvement(11, "Issue 11", "11", ["refactoring", "bug"]))

        expect: "no duplication even though labels are overused"
        is.toText() == """* Improvements: 2
  * Bugfixes: 2
    * Issue 10 [(#10)](10)
    * Issue 11 [(#11)](11)"""
    }

    def "the order of labels is determined"() {
        given:
        //input label captions determine the order of labels:
        def labels = [p0: "Priority 0", p1: "Priority 1"]
        def imp1 = new Improvement(10, "Issue 10", "10", ["p0"])
        def imp2 = new Improvement(11, "Issue 11", "11", ["p1"])

        when:
        def improvements = new DefaultImprovements(labels).addAll([imp1, imp2]).toText()
        def reordered = new DefaultImprovements(labels).addAll([imp2, imp1]).toText()

        then: "The order of labels is determined"
        improvements == reordered
    }
}
