package org.mockito.release.notes.improvements

import spock.lang.Specification
import spock.lang.Subject

class DefaultImprovementsTest extends Specification {

    @Subject improvements = new DefaultImprovements()

    def "empty improvements"() {
        expect:
        improvements.toText() == "* No notable improvements. See the commits for detailed changes."
    }

    def "with improvements"() {
        improvements.add(new Improvement(100, "Fix bug x", "http://issues/100", ["bug"]))
        improvements.add(new Improvement(122, "Javadoc update", "http://url/122", []))
        improvements.add(new Improvement(125, "Some enh", "http://issues/125", ["enhancement", "custom"]))
        improvements.add(new Improvement(126, "Some other enh", "http://issues/126", ["enhancement"]))

        expect:
        improvements.toText() == """* Improvements: 4
  * Fix bug x [(#100)](http://issues/100)
  * Javadoc update [(#122)](http://url/122)
  * Some enh [(#125)](http://issues/125)
  * Some other enh [(#126)](http://issues/126)"""
    }
}
