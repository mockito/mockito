package org.mockito.release.comparison

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class BinaryComparatorTest extends Specification {

    @Rule TemporaryFolder tmp = new TemporaryFolder()

    def "compares files"() {
        def file = tmp.newFile() << "asdf"
        def same = tmp.newFile() << "asdf"
        def diff = tmp.newFile() << "asdf\n"

        expect:
        new BinaryComparator().setPair({file}, {same}).areEqual()
        new BinaryComparator().setPair({same}, {file}).areEqual()

        !new BinaryComparator().setPair({file}, {diff}).areEqual()
        !new BinaryComparator().setPair({diff}, {file}).areEqual()
    }
}
