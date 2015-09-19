package org.mockito.release.comparison

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import testutil.ZipMaker

class ZipCompareTest extends Specification {

    @Rule TemporaryFolder tmp = new TemporaryFolder()

    def "compares zips"() {
        ZipMaker zip = new ZipMaker(tmp.newFolder())

        File zip1 =             zip.newZip("1.txt", "1", "x/2.txt", "2", "x/y/3.txt", "3", "x/y/4.txt", "4")
        File zip2 =             zip.newZip("1.txt", "1", "x/2.txt", "2", "x/y/3.txt", "3", "x/y/4.txt", "4")
        File differentContent = zip.newZip("1.txt", "1", "x/2.txt", "2", "x/y/3.txt", "3", "x/y/4.txt", "XX")
        File missingFile      = zip.newZip("1.txt", "1", "x/2.txt", "2", "x/y/3.txt", "3")
        File extraFile        = zip.newZip("1.txt", "1", "x/2.txt", "2", "x/y/3.txt", "3", "x/y/4.txt", "4", "x.txt", "")

        expect:
        eq zip1, zip2

        !eq(zip1, differentContent)
        !eq(zip1, missingFile)
        !eq(zip1, extraFile)
    }

    private static boolean eq(File z1, File z2) {
        new ZipCompare().compareZips(z1.absolutePath, z2.absolutePath) &&
        new ZipCompare().compareZips(z2.absolutePath, z1.absolutePath)
    }

    def "fails early when any of the zips cannot be opened"() {
        when: new ZipCompare().compareZips("foox", "bar")
        then:
        def ex = thrown(ZipCompare.ZipCompareException)
        ex.message.contains("foox")
    }
}
