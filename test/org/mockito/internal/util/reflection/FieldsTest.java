/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import org.junit.Test;

import java.lang.reflect.Field;

import static org.fest.assertions.Assertions.assertThat;

public class FieldsTest {

    @Test
    public void fields_should_return_all_declared_fields_in_hierarchy() throws Exception {
        assertThat(Fields.allDeclaredFieldsOf(new HierarchyOfClasses()).names())
                .containsOnly("a", "b", "static_a", "static_b");
    }

    @Test
    public void fields_should_return_declared_fields() throws Exception {
        assertThat(Fields.declaredFieldsOf(new HierarchyOfClasses()).names())
                .containsOnly("b", "static_b");
    }

    @Test
    public void can_filter_not_null_fields() throws Exception {
        assertThat(Fields.declaredFieldsOf(new NullOrNotNullFields()).notNull().names())
                .containsOnly("c");
    }

    @Test
    public void can_get_values_of_instance_fields() throws Exception {
        assertThat(Fields.declaredFieldsOf(new ValuedFields()).assignedValues())
                .containsOnly("a", "b");
    }


    @Test
    public void can_get_list_of_InstanceField() throws Exception {
        ValuedFields instance = new ValuedFields();

        assertThat(Fields.declaredFieldsOf(instance).instanceFields())
                .containsOnly(new InstanceField(field("a", instance), instance),
                              new InstanceField(field("b", instance), instance)
                );
    }

    private Field field(String name, Object instance) throws NoSuchFieldException {
        return instance.getClass().getDeclaredField(name);
    }


    interface AnInterface {
        int someStaticInInterface = 0;

    }
    public static class ParentClass implements AnInterface {
        static int static_a;
        int a;

    }
    public static class HierarchyOfClasses extends ParentClass {
        static int static_b;
        int b = 1;

    }
    public static class NullOrNotNullFields {
        static Object static_b;
        Object b;
        Object c = new Object();
    }

    public static class ValuedFields {
        String a = "a";
        String b = "b";
    }
}
