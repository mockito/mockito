/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.util.reflection;

/**
 * Report on field initialization
 */
public class FieldInitializationReport {
    private final Object fieldInstance;
    private final boolean wasInitialized;
    private final boolean wasInitializedUsingConstructorArgs;

    public FieldInitializationReport(Object fieldInstance, boolean wasInitialized, boolean wasInitializedUsingConstructorArgs) {
        this.fieldInstance = fieldInstance;
        this.wasInitialized = wasInitialized;
        this.wasInitializedUsingConstructorArgs = wasInitializedUsingConstructorArgs;
    }

    /**
     * Returns the actual field instance.
     *
     * @return the actual instance
     */
    public Object fieldInstance() {
        return fieldInstance;
    }

    /**
     * Indicate wether the field was created during the process or not.
     *
     * @return <code>true</code> if created, <code>false</code> if the field did already hold an instance.
     */
    public boolean fieldWasInitialized() {
        return wasInitialized;
    }

    /**
     * Indicate wether the field was created using constructor args.
     *
     * @return <code>true</code> if field was created using constructor parameters.
     */
    public boolean fieldWasInitializedUsingContructorArgs() {
        return wasInitializedUsingConstructorArgs;
    }

    /**
     * Returns the class of the actual instance in the field.
     *
     * @return Class of the instance
     */
    public Class<?> fieldClass() {
        return fieldInstance != null ? fieldInstance.getClass() : null;
    }
}

