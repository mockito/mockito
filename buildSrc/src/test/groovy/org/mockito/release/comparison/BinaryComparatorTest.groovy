package org.mockito.release.comparison

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Ignore
import spock.lang.Specification

@Ignore
class BinaryComparatorTest extends Specification {

    @Rule TemporaryFolder tmp = new TemporaryFolder()

    def "compares files"() {
        def file = tmp.newFile() << "asdf"
        def same = tmp.newFile() << "asdf"
        def diff = tmp.newFile() << "asdf\n"

        expect:
        new BinaryComparator().setPair({file}, {same}).compareFiles().areEqual()
        new BinaryComparator().setPair({same}, {file}).compareFiles().areEqual()

        !new BinaryComparator().setPair({file}, {diff}).compareFiles().areEqual()
        !new BinaryComparator().setPair({diff}, {file}).compareFiles().areEqual()
    }

    def "provides file information"() {
        def f1 = tmp.newFile() << "asdf"
        def f2 = tmp.newFile() << "asdf\n"
        def result = new BinaryComparator().setPair({ f1 }, { f2 }).compareFiles()

        expect:
        result.file1.absolutePath == f1.absolutePath
        result.file2.absolutePath == f2.absolutePath
    }
}
