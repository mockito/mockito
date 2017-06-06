package org.mockito.rules;

public interface ObjectCreationEngine {

    <T> T create(Class<T> type);
}
