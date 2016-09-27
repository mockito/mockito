package org.mockito.internal.creation.bytebuddy;

import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.MockMaker;

public interface ClassCreatingMockMaker extends MockMaker {

    <T> Class<? extends T> createMockType(MockCreationSettings<T> settings);
}
