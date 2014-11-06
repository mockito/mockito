package org.mockito.release.comparison

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Subject

class FileHasherTest extends Specification {

    @Subject FileHasher hasher = new FileHasher()
    @Rule TemporaryFolder tmp = new TemporaryFolder()

    def "hashes files"() {
        def file = tmp.newFile() << "asdf"
        def same = tmp.newFile() << "asdf"
        def diff = tmp.newFile() << "asdf\n"

        expect:
        hasher.hash(file) == hasher.hash(same)
        hasher.hash(same) == hasher.hash(file)

        hasher.hash(file) != hasher.hash(diff)
        hasher.hash(diff) != hasher.hash(file)
    }
}
