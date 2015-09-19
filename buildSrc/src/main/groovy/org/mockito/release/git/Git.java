package org.mockito.release.git;

import org.mockito.release.exec.ProcessRunner;

/**
 * Git utilities
 */
public class Git {

  /**
   * Provides git tool that uses given process runner
   */
  public static GitTool gitTool(ProcessRunner runner) {
    return new DefaultGitTool(runner);
  }
}
