package org.mockito.release.steps;

import org.mockito.release.util.operations.Operation;

public interface ReleaseSteps extends Operation {

    ReleaseStep newStep(String description, Operation operation);
}
