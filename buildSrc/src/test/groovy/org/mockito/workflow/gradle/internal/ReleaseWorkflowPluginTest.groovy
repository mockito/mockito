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

            ext.releaseNeeded = true

            releaseWorkflow {
                step one, [cleanup: rollbackOne]
                onlyIf { project.releaseNeeded }
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

    def "does not run release if onlyIf predicate is negative"() {
        buildFile << "releaseNeeded = false"

        when:
        def result = pass('release')

        then:
        result.tasks.join("\n") == """:one=SUCCESS
:two=SKIPPED
:three=SKIPPED
:four=SKIPPED
:rollbackFour=SKIPPED
:rollbackThree=SKIPPED
:rollbackOne=SUCCESS
:release=SUCCESS"""
    }

    def "failing release workflow predicate"() {
        buildFile << """
            task newTask
            releaseWorkflow.onlyIf { assert false: 'big problem' }
            releaseWorkflow.step newTask
        """

        when:
        def result = fail('release', '-s')

        then:
        result.tasks.join("\n") == """:one=SUCCESS
:two=SUCCESS
:three=SUCCESS
:four=SUCCESS
:newTask=FAILED
:rollbackFour=SUCCESS
:rollbackThree=SUCCESS
:rollbackOne=SUCCESS"""
        result.output.contains('big problem')
    }

    def "executes single release step"() {
        when:
        def result = pass('three', '-PsingleStep')

        then:
        result.tasks.join("\n") == """:three=SUCCESS
:rollbackThree=SKIPPED"""
    }

    def "executes selected single release steps"() {
        when:
        //passing 'four' first on purpose, the command line sequence needs to be honored
        def result = pass('four', 'three', '-PsingleStep')

        then:
        result.tasks.join("\n") == """:four=SUCCESS
:three=SUCCESS
:rollbackFour=SKIPPED
:rollbackThree=SKIPPED"""
    }

    def "single failing step enables rollback"() {
        buildFile << "three.doLast { assert false }"

        when:
        def result = fail('four', 'three', '-PsingleStep')

        then:
        result.tasks.join("\n") == """:four=SUCCESS
:three=FAILED
:rollbackFour=SUCCESS
:rollbackThree=SKIPPED"""
    }

    def "executes selected single release steps in dry run"() {
        when:
        def result = pass('four', 'three', '-PsingleStep', '-PdryRun')

        then:
        result.tasks.join("\n") == """:four=SUCCESS
:three=SUCCESS
:rollbackFour=SUCCESS
:rollbackThree=SUCCESS"""
    }

    def "executes single rollback step"() {
        when:
        def result = pass('rollbackFour', '-PsingleStep')

        then:
        result.tasks.join("\n") == ":rollbackFour=SKIPPED"
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
