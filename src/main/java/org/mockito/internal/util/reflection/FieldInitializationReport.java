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

    /**
     * Report field is not initialized.
     */
    public FieldInitializationReport() {
        this.fieldInstance = null;
        this.wasInitialized = false;
    }

    public FieldInitializationReport(Object fieldInstance, boolean wasInitialized) {
        this.fieldInstance = fieldInstance;
        this.wasInitialized = wasInitialized;
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
     * Indicate whether the field is initialized or not.
     *
     * @return <code>true</code> if field is initialized, <code>false</code> otherwise.
     */
    public boolean fieldIsInitialized() {
        return fieldInstance != null;
    }

    /**
     * Indicate whether the field was created during the process or not.
     *
     * @return <code>true</code> if created, <code>false</code> if the field did already hold an instance.
     */
    public boolean fieldWasInitialized() {
        return wasInitialized;
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

