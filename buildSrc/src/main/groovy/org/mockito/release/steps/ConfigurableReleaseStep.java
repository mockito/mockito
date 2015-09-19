package org.mockito.release.steps;

import groovy.lang.Closure;

public interface ConfigurableReleaseStep extends ReleaseStep {

  //TODO SF avoid leaking closures here as the release steps should be a java tool

  /**
   * Rollback operation is executed if the release process fails in some further step
   */
  void rollback(Closure closure);

  /**
   * Executed if release is successful. Executed if release is failed provided there is no rollback.
   */
  void cleanup(Closure closure);
}
