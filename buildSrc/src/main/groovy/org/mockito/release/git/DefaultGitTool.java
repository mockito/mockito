package org.mockito.release.git;

import org.mockito.release.exec.ProcessRunner;

class DefaultGitTool implements GitTool {
  private final ProcessRunner runner;

  public DefaultGitTool(ProcessRunner runner) {
    this.runner = runner;
  }

  public DefaultGitAuthor setAuthor(String name, String email) {
    String currentLocalUser = runner.run("git", "config", "--local", "user.name").trim();
    String currentLocalEmail = runner.run("git", "config", "--local", "user.email").trim();

    runner.run("git", "config", "user.name", name);
    runner.run("git", "config", "user.email", email);

    return new DefaultGitAuthor(currentLocalUser, currentLocalEmail, runner);
  }
}
