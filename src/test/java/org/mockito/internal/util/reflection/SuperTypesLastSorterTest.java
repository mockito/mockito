package org.mockito.internal.util.reflection;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("unused")
public class SuperTypesLastSorterTest {
    /**
     * A Comparator that behaves like the old one, so the existing tests
     * continue to work.
     */
    private static Comparator<Field> cmp = new Comparator<Field>() {
        public int compare(Field o1, Field o2) {
            if (o1.equals(o2)) {
                return 0;
            }

            List<Field> l = new SuperTypesLastSorter().sort(Arrays.asList(o1, o2));

            if (l.get(0) == o1) {
                return -1;
            } else {
                return 1;
            }
        }
    };

    private Object objectA;
    private Object objectB;

    private Number numberA;
    private Number numberB;

    private Integer integerA;
    private Integer integerB;

    private Iterable<?> iterableA;

    private Number xNumber;
    private Iterable<?> yIterable;
    private Integer zInteger;


    @Test
    public void when_same_type_the_order_is_based_on_field_name() throws Exception {
        assertThat(cmp.compare(field("objectA"), field("objectB"))).isEqualTo(-1);
        assertThat(cmp.compare(field("objectB"), field("objectA"))).isEqualTo(1);
        assertThat(cmp.compare(field("objectB"), field("objectB"))).isEqualTo(0);
    }

    @Test
    public void when_type_is_different_the_supertype_comes_last() throws Exception {
        assertThat(cmp.compare(field("numberA"), field("objectB"))).isEqualTo(-1);
        assertThat(cmp.compare(field("objectB"), field("numberA"))).isEqualTo(1);
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

        List<Field> sortedFields = new SuperTypesLastSorter().sort(unsortedFields);

        assertThat(sortedFields).containsSequence(
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

        Collections.sort(unsortedFields, cmp);

        assertThat(unsortedFields).containsSequence(
                field("objectA"),
                field("objectB")
        );
    }

    @Test
    public void fields_sort_consistently_when_interfaces_are_included() throws NoSuchFieldException {
        assertSortConsistently(field("iterableA"), field("numberA"), field("integerA"));
    }

    @Test
    public void fields_sort_consistently_when_names_and_type_indicate_different_order() throws NoSuchFieldException {
        assertSortConsistently(field("xNumber"), field("yIterable"), field("zInteger"));
    }

    /**
     * Assert that these fields sort in the same order no matter which order
     * they start in.
     */
    private static void assertSortConsistently(Field a, Field b, Field c) {
        Field[][] initialOrderings = {
                {a, b, c},
                {a, c, b},
                {b, a, c},
                {b, c, a},
                {c, a, b},
                {c, b, a}
        };

        Set<List<Field>> results = new HashSet<List<Field>>();

        for (Field[] o : initialOrderings) {
            results.add(new SuperTypesLastSorter().sort(Arrays.asList(o)));
        }

        assertThat(results).hasSize(1);
    }

    private Field field(String field) throws NoSuchFieldException {
        return getClass().getDeclaredField(field);
    }
}
