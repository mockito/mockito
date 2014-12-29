package org.mockito.release.git;

public class Git {

  public static GitTool gitTool() {
    return new DefaultGitTool();
  }
}
