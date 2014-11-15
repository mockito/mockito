package org.mockito.release.comparison

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Ignore
import spock.lang.Specification

class ZipComparatorTest extends Specification {

    @Rule TemporaryFolder tmp = new TemporaryFolder()
    def compare = Mock(ZipCompare)

    def "compares files"() {
        def f1 = tmp.newFile()
        def f2 = tmp.newFile()

        when: def result = new ZipComparator(compare).setPair({ f1 }, { f2 }).compareFiles()

        then:
        1 * compare.compareZips(f1.absolutePath, f2.absolutePath) >> true
        0 * _

        and:
        result.areEqual()
    }

    def "detects not equal zips"() {
        def f1 = tmp.newFile() << "asdf"
        def f2 = tmp.newFile() << "asdf\n"

        when:
        def result = new ZipComparator(compare).setPair({ f1 }, { f2 }).compareFiles()

        then:
        result.file1.absolutePath == f1.absolutePath
        result.file2.absolutePath == f2.absolutePath

        and:
        1 * compare.compareZips(f1.absolutePath, f2.absolutePath) >> false
        !result.areEqual()
    }
}
