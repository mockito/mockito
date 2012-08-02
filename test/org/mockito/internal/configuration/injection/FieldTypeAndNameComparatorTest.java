package org.mockito.internal.configuration.injection;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

@SuppressWarnings("unused")
public class FieldTypeAndNameComparatorTest {

    private Object objectA;
    private Object objectB;

    private Number numberA;
    private Number numberB;

    private Integer integerA;
    private Integer integerB;

    @Test
    public void when_same_type_the_order_is_based_on_field_name() throws Exception {
        assertThat(new PropertyAndSetterInjection.FieldTypeAndNameComparator().compare(field("objectA"), field("objectB"))).isEqualTo(-1);
        assertThat(new PropertyAndSetterInjection.FieldTypeAndNameComparator().compare(field("objectB"), field("objectA"))).isEqualTo(1);
        assertThat(new PropertyAndSetterInjection.FieldTypeAndNameComparator().compare(field("objectB"), field("objectB"))).isEqualTo(0);
    }

    @Test
    public void when_type_is_different_the_supertype_comes_last() throws Exception {
        assertThat(new PropertyAndSetterInjection.FieldTypeAndNameComparator().compare(field("numberA"), field("objectB"))).isEqualTo(-1);
        assertThat(new PropertyAndSetterInjection.FieldTypeAndNameComparator().compare(field("objectB"), field("numberA"))).isEqualTo(1);
    }

    @Test
    public void using_Collections_dot_sort() throws Exception {
        List<Field> unsortedFields = Arrays.asList(
                field("objectB"),
                field("integerB"),
                field("numberA"),
                field("numberB"),
                field("objectA"),
                field("integerA")
        );

        Collections.sort(unsortedFields, new PropertyAndSetterInjection.FieldTypeAndNameComparator());

        assertThat(unsortedFields).containsSequence(
                field("integerA"),
                field("integerB"),
                field("numberA"),
                field("numberB"),
                field("objectA"),
                field("objectB")
        );
    }


    @Test
    public void issue_352_order_was_different_between_JDK6_and_JDK7() throws Exception {
        List<Field> unsortedFields = Arrays.asList(
                field("objectB"),
                field("objectA")
        );

        Collections.sort(unsortedFields, new PropertyAndSetterInjection.FieldTypeAndNameComparator());

        assertThat(unsortedFields).containsSequence(
                field("objectA"),
                field("objectB")
        );
    }

    private Field field(String field) throws NoSuchFieldException {
        return getClass().getDeclaredField(field);
    }
}
