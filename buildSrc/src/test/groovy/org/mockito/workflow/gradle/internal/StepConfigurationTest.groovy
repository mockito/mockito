package org.mockito.workflow.gradle.internal

import org.gradle.api.GradleException
import org.gradle.api.Task
import spock.lang.Specification

class StepConfigurationTest extends Specification {

    def task = Mock(Task)

    def "provides rollback and cleanup"() {
        expect:
        new StepConfiguration([:]).cleanup == null
        new StepConfiguration([:]).rollback == null

        new StepConfiguration(cleanup: task).cleanup == task
        new StepConfiguration(cleanup: task).rollback == null

        new StepConfiguration(rollback: task).cleanup == null
        new StepConfiguration(rollback: task).rollback == task
    }

    def "validates configuration"() {
        when: new StepConfiguration([foo: "bar"])
        then: thrown(GradleException)

        when: new StepConfiguration([rollback: task, cleanup: task])
        then: thrown(GradleException)

        when: new StepConfiguration([rollback: "nonTask"])
        then: thrown(GradleException)
    }
}
