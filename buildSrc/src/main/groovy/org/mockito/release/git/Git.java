package org.mockito.release.git;

import org.mockito.release.exec.ProcessRunner;

public class Git {

  public static GitTool gitTool(ProcessRunner runner) {
    return new DefaultGitTool(runner);
  }
}
