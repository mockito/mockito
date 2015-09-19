/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.runners;

import org.junit.runner.Description;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.notification.RunNotifier;

/**
 * I'm using this surrogate interface to hide internal Runner implementations.
 * Surrogate cannot be used with &#064;RunWith therefore it is less likely clients will use interal runners.
 */
public interface RunnerImpl extends Filterable{

    void run(RunNotifier notifier);

    Description getDescription();
    
}