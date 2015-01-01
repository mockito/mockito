package org.mockito.release.version

import spock.lang.Specification
import spock.lang.Subject

class DefaultVersionToolTest extends Specification {

    @Subject tool = new DefaultVersionTool()

    def "increments version"() {
        expect:
        tool.incrementVersion("1.0.0") == "1.0.1"
        tool.incrementVersion("0.0.0") == "0.0.1"
        tool.incrementVersion("1.10.15") == "1.10.16"

        tool.incrementVersion("1.0.0-beta") == "1.0.1-beta"
        tool.incrementVersion("1.10.15-beta") == "1.10.16-beta"
    }

    def "increments only 3 numbered versions"() {
        when:
        tool.incrementVersion(unsupported)

        then:
        thrown(IllegalArgumentException)

        where:
        unsupported << ["1.0", "2", "1.0.0.0", "1.0.1-beta.2"]
    }
}
