package org.mockito.release.notes.vcs

import org.mockito.release.exec.ProcessRunner
import spock.lang.Specification
import spock.lang.Subject

class GitLogProviderTest extends Specification {

    def runner = Mock(ProcessRunner)
    @Subject provider = new GitLogProvider(runner)

    def "provides log"() {
        when:
        def log = provider.getLog("v1.10.10", "HEAD", "--pretty=foo")

        then:
        1 * runner.run("git", "fetch", "origin", "+refs/tags/v1.10.10:refs/tags/v1.10.10")
        1 * runner.run("git", "log", "--pretty=foo", "v1.10.10..HEAD") >> "some output"
        0 * _

        and:
        log == "some output"
    }
}
