package org.mockito.release.exec

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.IgnoreIf
import spock.lang.Specification

import static org.mockito.release.exec.TestUtil.commandAvailable

//ignore the test when there is no 'ls' utility
@IgnoreIf({!commandAvailable("ls")})
class DefaultProcessRunnerTest extends Specification {

    @Rule TemporaryFolder tmp = new TemporaryFolder()

    def "runs processes"() {
        File dir = tmp.newFolder()
        new File(dir, "xyz.txt").createNewFile()
        new File(dir, "hey joe.jar").createNewFile()

        when:
        String output = new DefaultProcessRunner(dir).run("ls")

        then:
        output.contains("xyz.txt")
        output.contains("hey joe.jar")
    }
}
