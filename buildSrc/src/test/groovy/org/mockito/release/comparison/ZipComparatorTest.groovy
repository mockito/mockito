package org.mockito.release.comparison

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Ignore
import spock.lang.Specification

@Ignore
class ZipComparatorTest extends Specification {

    @Rule TemporaryFolder tmp = new TemporaryFolder()

    def "compares files"() {
        def file = tmp.newFile() << "asdf"
        def same = tmp.newFile() << "asdf"
        def diff = tmp.newFile() << "asdf\n"

        expect:
        new ZipComparator().setPair({file}, {same}).compareFiles().areEqual()
        new ZipComparator().setPair({same}, {file}).compareFiles().areEqual()

        !new ZipComparator().setPair({file}, {diff}).compareFiles().areEqual()
        !new ZipComparator().setPair({diff}, {file}).compareFiles().areEqual()
    }

    def "provides file information"() {
        def f1 = tmp.newFile() << "asdf"
        def f2 = tmp.newFile() << "asdf\n"
        def result = new ZipComparator().setPair({ f1 }, { f2 }).compareFiles()

        expect:
        result.file1.absolutePath == f1.absolutePath
        result.file2.absolutePath == f2.absolutePath
    }
}
