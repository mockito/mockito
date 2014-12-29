package org.mockito.release.steps;

import groovy.lang.Closure;

public interface ConfigurableReleaseStep extends ReleaseStep {

  void rollback(Closure closure); //TODO SF avoid leaking closure here as the release steps should be a java tool
}
