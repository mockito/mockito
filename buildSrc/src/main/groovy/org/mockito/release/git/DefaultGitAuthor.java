package org.mockito.release.git;

import org.mockito.release.exec.ProcessRunner;

class DefaultGitAuthor implements GitAuthor {

  final String previousUser;
  final String previousEmail;
  private final ProcessRunner runner;

  DefaultGitAuthor(String previousUser, String previousEmail, ProcessRunner runner) {
    this.previousUser = previousUser;
    this.previousEmail = previousEmail;
    this.runner = runner;
  }

  public void restoreOriginal() {
    if (!previousUser.isEmpty()) {
      runner.run("git", "config", "--local", "user.name", previousUser);
    } else {
      runner.run("git", "config", "--local", "--unset", "user.name");
    }

    if (!previousEmail.isEmpty()) {
      runner.run("git", "config", "--local", "user.email", previousEmail);
    } else {
      runner.run("git", "config", "--local", "--unset", "user.email");
    }
  }
}
