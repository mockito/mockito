package org.mockito.release.git

import org.mockito.release.exec.ProcessRunner
import spock.lang.Specification

class DefaultGitAuthorTest extends Specification {

  def runner = Mock(ProcessRunner)

  def "restores specific author"() {
    def a = new DefaultGitAuthor("Mary", "m@mail.com", runner)

    when: a.restoreOriginal()

    then:
    1 * runner.run("git", "config", "--local", "user.name", "Mary")
    1 * runner.run("git", "config", "--local", "user.email", "m@mail.com")
    0 * _
  }

  def "unsets author if not configured previously"() {
    def a = new DefaultGitAuthor("", "", runner)

    when: a.restoreOriginal()

    then:
    1 * runner.run("git", "config", "--local", "--unset", "user.name")
    1 * runner.run("git", "config", "--local", "--unset", "user.email")
    0 * _
  }
}
