/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import org.mockito.internal.util.Checks;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.collections.ListUtil.Filter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Small fluent reflection tools to work with fields.
 *
 * Code is very new and might need rework.
 */
public abstract class Fields {

    /**
     * Instance fields declared in the class and superclasses of the given instance.
     *
     * @param instance Instance from which declared fields will be retrieved.
     * @return InstanceFields of this object instance.
     */
    public static InstanceFields allDeclaredFieldsOf(Object instance) {
        List<InstanceField> instanceFields = new ArrayList<InstanceField>();
        for (Class<?> clazz = instance.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            instanceFields.addAll(instanceFieldsIn(instance, clazz.getDeclaredFields()));
        }
        return new InstanceFields(instance, instanceFields);
    }

    /**
     * Instance fields declared in the class of the given instance.
     *
     * @param instance Instance from which declared fields will be retrieved.
     * @return InstanceFields of this object instance.
     */
    public static InstanceFields declaredFieldsOf(Object instance) {
        List<InstanceField> instanceFields = new ArrayList<InstanceField>();
        instanceFields.addAll(instanceFieldsIn(instance, instance.getClass().getDeclaredFields()));
        return new InstanceFields(instance, instanceFields);
    }

    private static List<InstanceField> instanceFieldsIn(Object instance, Field[] fields) {
        List<InstanceField> instanceDeclaredFields = new ArrayList<InstanceField>();
        for (Field field : fields) {
            InstanceField instanceField = new InstanceField(field, instance);
            instanceDeclaredFields.add(instanceField);
        }
        return instanceDeclaredFields;
    }

    /**
     * Accept fields annotated by the given annotations.
     *
     * @param annotations Annotation types to check.
     * @return The filter.
     */
    @SuppressWarnings({"unchecked", "vararg"})
    public static Filter<InstanceField> annotatedBy(final Class<? extends Annotation>... annotations) {
        return new Filter<InstanceField>() {
            public boolean isOut(InstanceField instanceField) {
                Checks.checkNotNull(annotations, "Provide at least one annotation class");

                for (Class<? extends Annotation> annotation : annotations) {
                    if(instanceField.isAnnotatedBy(annotation)) {
                        return false;
                    }
                }
                return true;
            }
        };
    }

    /**
     * Accept fields with non null value.
     *
     * @return The filter.
     */
    private static Filter<InstanceField> nullField() {
        return new Filter<InstanceField>() {
            public boolean isOut(InstanceField instanceField) {
                return instanceField.isNull();
            }
        };
    }

    /**
     * Accept fields with non null value.
     *
     * @return The filter.
     */
    public static Filter<InstanceField> syntheticField() {
        return new Filter<InstanceField>() {
            public boolean isOut(InstanceField instanceField) {
                return instanceField.isSynthetic();
            }
        };
    }

    public static class InstanceFields {
        private final Object instance;

        private final List<InstanceField> instanceFields;

        public InstanceFields(Object instance, List<InstanceField> instanceFields) {
            this.instance = instance;
            this.instanceFields = instanceFields;
        }

        public InstanceFields filter(Filter<InstanceField> withFilter) {
            return new InstanceFields(instance, ListUtil.filter(instanceFields, withFilter));
        }

        public InstanceFields notNull() {
            return filter(nullField());
        }

        public List<InstanceField> instanceFields() {
            return new ArrayList<InstanceField>(instanceFields);
        }

        public List<Object> assignedValues() {
            List<Object> values = new ArrayList<Object>(instanceFields.size());
            for (InstanceField instanceField : instanceFields) {
                values.add(instanceField.read());
            }
            return values;
        }

        public List<String> names() {
            List<String> fieldNames = new ArrayList<String>(instanceFields.size());
            for (InstanceField instanceField : instanceFields) {
                fieldNames.add(instanceField.name());
            }
            return fieldNames;
        }
    }
}
