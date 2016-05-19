package org.mockito.internal.configuration.injection.fieldscanner;

import org.mockito.exceptions.Reporter;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * A FieldScanner scans all fields in the declared {@link #testClassInstance} and adds the corresponding object
 * in {@link #getObjectToAdd(Field)} if it {@link #hasCorrectAnnotation(Field)}.
 *
 * @param <T> The type of objects we are adding for.
 */
public abstract class FieldScanner<T> {

    protected final Object testClassInstance;

    protected final Class<?> clazz;

    /**
     * Create a new FieldScanner for this {@link #testClassInstance}.
     *
     * @param testClassInstance The instance to scan the fields for.
     */
    public FieldScanner(Object testClassInstance) {
        this(testClassInstance, testClassInstance.getClass());
    }

    /**
     * Create a new FieldScanner for this {@link #testClassInstance} with the fields for the specified {@link #clazz}.
     *
     * @param testClassInstance The instance to scan the fields for.
     *
     * @param clazz The class in the hierarchy to get the fields from.
     */
    public FieldScanner(Object testClassInstance, Class<?> clazz) {
        this.testClassInstance = testClassInstance;
        this.clazz = clazz;
    }

    /**
     * Populate the collection if the corresponding field {@link #hasCorrectAnnotation(Field)}. It uses
     * {@link #getObjectToAdd(Field)} to determine what to add.
     *
     * @param collection The collection that is holding all objects.
     */
    public void addTo(Collection<T> collection) {
        for (Field field : clazz.getDeclaredFields()) {
            if (hasCorrectAnnotation(field)) {
                collection.add(getObjectToAdd(field));
            }
        }
    }

    /**
     * Assert that the clashingAnnotation is not present on the field where one of the annotation is also present.
     *
     * @param clashingAnnotation The annotation that should be on the field.
     * @param field The field that has the annotations.
     * @param annotations The annotations that should not be on the field.
     */
    protected void assertNoAnnotations(final Class clashingAnnotation, final Field field, final Class... annotations) {
        for (Class annotation : annotations) {
            if (field.isAnnotationPresent(annotation)) {
                new Reporter().unsupportedCombinationOfAnnotations(annotation.getSimpleName(), clashingAnnotation.getSimpleName());
            }
        }
    }

    /**
     * Check if the specified field has the specified annotation.
     *
     * @param field The field to check for.
     *
     * @return Whether it has the correct annotation or not.
     */
    protected abstract boolean hasCorrectAnnotation(Field field);

    /**
     * Get the object according to the field supplied.
     *
     * @param field The field to retrieve the object for.
     *
     * @return The object according to the field declaration.
     */
    protected abstract T getObjectToAdd(Field field);
}
