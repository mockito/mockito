package org.mockito.listeners;

import org.mockito.mock.MockCreationSettings;

/**
 * Created by sfaber on 8/5/16.
 * TODO 384 javadoc
 */
public interface MockCreationListener extends MockitoListener {

    void mockCreated(Object mock, MockCreationSettings settings);
}
