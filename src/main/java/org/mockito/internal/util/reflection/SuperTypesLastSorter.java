/*
 * Copyright (c) 2015 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.util.reflection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Sort fields in an order suitable for injection, by name with superclasses
 * moved after their subclasses.
 */
public class SuperTypesLastSorter {
    /**
     * Return a new collection with the fields sorted first by name,
     * then with any fields moved after their supertypes.
     */
    public List<Field> sort(Collection<? extends Field> unsortedFields) {
        List<Field> fields = new ArrayList<Field>(unsortedFields);

        Collections.sort(fields, compareFieldsByName);

        int i = 0;

        while (i < fields.size() - 1) {
            Field f = fields.get(i);
            Class<?> ft = f.getType();
            int newPos = i;
            for (int j = i + 1; j < fields.size(); j++) {
                Class<?> t = fields.get(j).getType();

                if (ft != t && ft.isAssignableFrom(t)) {
                    newPos = j;
                }
            }

            if (newPos == i) {
                i++;
            } else {
                fields.remove(i);
                fields.add(newPos, f);
            }
        }

        return fields;
    }


    private static final Comparator<Field> compareFieldsByName = new Comparator<Field>() {
        public int compare(Field o1, Field o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };
}
