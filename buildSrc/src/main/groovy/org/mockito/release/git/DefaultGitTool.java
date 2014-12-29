package org.mockito.release.git;

class DefaultGitTool implements GitTool {
  public GitAuthor setAuthor(String name, String email) {
    return new GitAuthor() {
      public void restoreOriginal() {

      }
    };
  }
}
