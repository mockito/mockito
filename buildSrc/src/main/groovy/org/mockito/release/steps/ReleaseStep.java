package org.mockito.release.steps;

import groovy.lang.Closure;
import org.mockito.release.util.operations.Operation;

public interface ReleaseStep extends Operation {

    String getDescription();

    void rollback(Closure closure); //TODO SF avoid leaking closure here as the release steps should be a java tool

    Operation getRollback();
}
