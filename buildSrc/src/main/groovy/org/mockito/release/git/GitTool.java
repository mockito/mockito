package org.mockito.release.git;

/**
 * Git operations
 */
public interface GitTool {

  /**
   * Configures local git author by name and email.
   * Returns an object that can be used to restore the author to original value.
   */
  GitAuthor setAuthor(String name, String email);
}
