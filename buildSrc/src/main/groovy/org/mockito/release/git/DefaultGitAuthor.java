package org.mockito.release.git;

class DefaultGitAuthor implements GitAuthor {

  final String previousUser;
  final String previousEmail;

  DefaultGitAuthor(String previousUser, String previousEmail) {
    this.previousUser = previousUser;
    this.previousEmail = previousEmail;
  }

  public void restoreOriginal() {

  }
}
