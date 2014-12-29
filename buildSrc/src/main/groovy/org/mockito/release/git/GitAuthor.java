package org.mockito.release.git;

/**
 * Operations related to git author
 */
public interface GitAuthor {

  /**
   * Restores the local author / email to the original values
   */
  void restoreOriginal();
}
