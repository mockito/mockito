package org.mockito.release.steps;

import org.mockito.release.util.operations.Operation;

public interface ReleaseSteps extends Operation {

  ConfigurableReleaseStep newStep(String description, Operation operation);

  ReleaseStep getStep(int stepNumber);

  void performRollback();
}