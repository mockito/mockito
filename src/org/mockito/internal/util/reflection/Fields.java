package org.mockito.internal.util.reflection;

import org.mockito.internal.util.Checks;
import org.mockito.internal.util.ListUtil;
import org.mockito.internal.util.ListUtil.Filter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Small fluent reflection tools to work with fields
 */
public abstract class Fields {


    public static InstanceFields allFieldsInHierarchy(Object instance) {
        List<InstanceField> instanceFields = new ArrayList<InstanceField>();
        for (Class<?> clazz = instance.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            instanceFields.addAll(instanceFieldsIn(instance, clazz));
        }
        return new InstanceFields(instance, instanceFields);
    }

    public static Filter<InstanceField> notAnnotatedBy(final Class<? extends Annotation>... annotations) {
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

    private static Filter<InstanceField> nullField() {
        return new Filter<InstanceField>() {
            public boolean isOut(InstanceField instanceField) {
                return instanceField.isNotNull();
            }
        };
    }

    private static List<InstanceField> instanceFieldsIn(Object instance, Class<?> clazz) {
        List<InstanceField> instanceDeclaredFields = new ArrayList<InstanceField>();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            InstanceField instanceField = new InstanceField(declaredField, instance);
            instanceDeclaredFields.add(instanceField);
        }
        return instanceDeclaredFields;
    }
    public static class InstanceFields {
        private final Object instance;

        private final List<InstanceField> instanceFields;

        public InstanceFields(Object instance, List<InstanceField> instanceFields) {
            this.instance = instance;
            this.instanceFields = instanceFields;
        }

        // TODO get Filter out of ListFilter
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

    }
}
