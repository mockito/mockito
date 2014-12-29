package org.mockito.release.git

import org.mockito.release.exec.ProcessRunner
import spock.lang.Specification
import spock.lang.Subject

class DefaultGitToolTest extends Specification {

  def runner = Mock(ProcessRunner)
  @Subject tool = new DefaultGitTool(runner)

  def "sets author"() {
    runner.run("git", "config", "--local", "user.name") >> "John"
    runner.run("git", "config", "--local", "user.email") >> "j@gmail.com"

    when:
    def author = tool.setAuthor("foo", "bar")

    then:
    author.previousUser == "John"
    author.previousEmail == "j@gmail.com"
  }
}
