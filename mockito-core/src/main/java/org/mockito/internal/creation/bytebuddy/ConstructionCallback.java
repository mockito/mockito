/*
 * Copyright (c) 2020 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
public interface ConstructionCallback {

    Object apply(Class<?> type, Object[] arguments, String method) throws Throwable;


    default boolean shouldThrowConstructorException() {
        return false;
    }

    default Throwable getConstructorException() {
        return null;
    }
}
