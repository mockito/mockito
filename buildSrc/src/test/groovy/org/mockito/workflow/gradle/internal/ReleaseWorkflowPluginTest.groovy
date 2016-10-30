package org.mockito.workflow.gradle.internal

import org.gradle.testkit.runner.BuildResult
import spock.lang.Specification
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder

/**
 * Created by sfaber on 10/29/16.
 */
class ReleaseWorkflowPluginTest extends Specification {

    //TODO:
//  - onlyIf - release prerequisite
//  - rollback and cleanup
//  - avoid no-op tasks
//
//  rw.dryRun (all rollbacks automatically enabled)
//  rw.singleStep (no dependencies between steps)

    @Rule final TemporaryFolder testProjectDir = new TemporaryFolder()
    File buildFile

    def setup() {
        buildFile = testProjectDir.newFile('build.gradle')
        buildFile << """
            buildscript {
              dependencies {
                classpath files('/Users/sfaber/mockito/src/out/production/buildSrc')
              }
            }

            task one << {}
            task two << {}
            task three << {}
            task four << {}

            task rollbackOne << {}
            task rollbackTwo << {}
            task rollbackThree << {}
            task rollbackFour << {}

            apply plugin: 'release-workflow'

            releaseWorkflow {
//                step init
//                abortWhen { !init.releaseNeeded }

                step one, [cleanup: rollbackOne]
                step two
                step three, [rollback: rollbackThree]
                step four, [rollback: rollbackFour]
            }
        """
    }

    def "executes release"() {
        when:
        def result = pass('release')

        then:
result.tasks.join("\n") == """:one=SUCCESS
:two=SUCCESS
:three=SUCCESS
:four=SUCCESS
:rollbackFour=SKIPPED
:rollbackThree=SKIPPED
:rollbackOne=SUCCESS
:release=SUCCESS"""
    }

    def "executes partial release"() {
        when:
        def result = pass('two')

        then:
        result.tasks.join("\n") == """:one=SUCCESS
:two=SUCCESS
:rollbackOne=SUCCESS"""
    }

    def "fails in the middle"() {
        buildFile << "three.doLast { assert false }"

        when:
        def result = fail('release')

        then:
        result.tasks.join("\n") == """:one=SUCCESS
:two=SUCCESS
:three=FAILED
:rollbackThree=SKIPPED
:rollbackOne=SUCCESS"""
    }

    def "dry run"() {
        when:
        def result = pass('release', '-PdryRun')

        then:
        result.tasks.join("\n") == """:one=SUCCESS
:two=SUCCESS
:three=SUCCESS
:four=SUCCESS
:rollbackFour=SUCCESS
:rollbackThree=SUCCESS
:rollbackOne=SUCCESS
:release=SUCCESS"""
    }

    def "failed rollback does not prevent other rollbacks"() {
        buildFile << """
            four.doLast { assert false }
            rollbackThree.doLast { assert false }"""

        when:
        def result = fail('release')

        then:
        result.tasks.join("\n") == """:one=SUCCESS
:two=SUCCESS
:three=SUCCESS
:four=FAILED
:rollbackFour=SKIPPED
:rollbackThree=FAILED
:rollbackOne=SUCCESS"""
    }

    private BuildResult pass(String ... args) {
        GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments(args)
                .build()
    }

    private BuildResult fail(String ... args) {
        GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments(args)
                .buildAndFail()
    }
}
