package org.mockito.release.comparison

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class SourceJarComparatorTest extends Specification {

    @Rule TemporaryFolder tmp = new TemporaryFolder()

    def "compares files"() {
        def file = tmp.newFile() << "asdf"
        def same = tmp.newFile() << "asdf"
        def diff = tmp.newFile() << "asdf\n"

        expect:
        new SourceJarComparator().setPair({file}, {same}).areEqual()
        new SourceJarComparator().setPair({same}, {file}).areEqual()

        !new SourceJarComparator().setPair({file}, {diff}).areEqual()
        !new SourceJarComparator().setPair({diff}, {file}).areEqual()
    }
}
