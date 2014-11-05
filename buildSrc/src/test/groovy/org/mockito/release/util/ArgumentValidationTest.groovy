package org.mockito.release.util

import spock.lang.Specification

import static org.mockito.release.util.ArgumentValidation.notNull

class ArgumentValidationTest extends Specification {

    def "validates arguments"() {
        expect:
        notNull()
        notNull("foo", "foo arg")
        notNull("foo", "foo arg", new Integer(21), "Integer arg")
    }

    def "throws exception when null found"() {
        when:
        notNull(null, "foo arg")
        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "foo arg cannot be null."

        when:
        notNull("foo", "foo arg", null, "Integer arg")
        then:
        ex = thrown(IllegalArgumentException)
        ex.message == "Integer arg cannot be null."
    }

    def "warns when incorrect input supplied"() {
        when: notNull("x")
        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "notNull method requires pairs of argument + message"

        when: notNull(null, new Integer(1234))
        then:
        ex = thrown(IllegalArgumentException)
        ex.message == "notNull method requires pairs of argument + message"
    }
}
