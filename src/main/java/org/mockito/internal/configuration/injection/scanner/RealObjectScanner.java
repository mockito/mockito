package org.mockito.internal.configuration.injection.scanner;

import org.mockito.Real;
import org.mockito.internal.configuration.injection.RealObject;
import org.mockito.internal.util.reflection.FieldReader;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by tim on 1-8-15.
 */
public class RealObjectScanner {
    private final Object instance;
    private final Class<?> clazz;

    public RealObjectScanner(Object testClassInstance, Class<?> clazz) {
        this.instance = testClassInstance;
        this.clazz = clazz;
    }

    public void addRealObjects(Set<Object> realObjects) {realObjects.addAll(scan());}

    private Set<Object> scan() {
        Set<Object> realObjects = new HashSet<Object>();

        for (Field field : clazz.getDeclaredFields()) {
            FieldReader fieldReader = new FieldReader(instance, field);

            if (null != field.getAnnotation(Real.class)) {
                realObjects.add(new RealObject(fieldReader.read(), field.getName()));
            }
        }

        return realObjects;
    }
}
