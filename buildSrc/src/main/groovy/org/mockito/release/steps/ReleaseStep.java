package org.mockito.release.steps;

import org.mockito.release.util.operations.Operation;

public interface ReleaseStep extends Operation {

  String getDescription();

  void performRollback();

  void performCleanup();
}
