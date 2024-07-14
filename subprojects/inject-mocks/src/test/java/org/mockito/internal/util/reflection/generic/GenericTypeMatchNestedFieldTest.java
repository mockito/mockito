/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection.generic;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.internal.util.reflection.generic.DataTypes.Change;
import org.mockito.internal.util.reflection.generic.DataTypes.ChangeCollection;
import org.mockito.internal.util.reflection.generic.DataTypes.CollectionBox;
import org.mockito.internal.util.reflection.generic.DataTypes.ConcreteSubOfBox;
import org.mockito.internal.util.reflection.generic.DataTypes.SubOfBox;
import org.mockito.internal.util.reflection.generic.DataTypes.SubOfConcrete;
import org.mockito.internal.util.reflection.generic.DataTypes.SubOfSubOfBox;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("unused")
public class GenericTypeMatchNestedFieldTest {
    public static final String DATA_TYPES_CLASS_PREFIX = DataTypes.class.getName() + "$";

    private CollectionBox<Integer> integerCollectionBox;
    private CollectionBox<String> stringCollectionBox;
    private CollectionBox<?> wildcardCollectionBox;

    @SuppressWarnings("rawtypes")
    private CollectionBox rawCollectionBox;

    private SubOfBox<Integer> integerSubOfBox;
    private SubOfBox<String> stringSubOfBox;
    private SubOfBox<?> wildcardSubOfBox;

    private ConcreteSubOfBox concreteSubOfBox;
    private SubOfConcrete subOfConcrete;

    private SubOfSubOfBox<Integer> integerSubOfSubOfBox;
    private SubOfSubOfBox<String> stringSubOfSubOfBox;

    @SuppressWarnings("rawtypes")
    private ChangeCollection changesTargetRaw;

    private List<Integer> integerList;
    private List<String> stringList;
    private Collection<?> wildcardCollection;

    private List<Change> changeList;

    @ParameterizedTest
    @CsvSource({
        "integerList         ,integerCollectionBox   ,CollectionBox  ,collection  ,true",
        "integerList         ,stringCollectionBox    ,CollectionBox  ,collection  ,false",
        "integerList         ,wildcardCollectionBox  ,CollectionBox  ,collection  ,true",
        "wildcardCollection  ,wildcardCollectionBox  ,CollectionBox  ,collection  ,true",
        "integerList         ,integerSubOfBox        ,CollectionBox  ,collection  ,true",
        "integerList         ,stringSubOfBox         ,CollectionBox  ,collection  ,false",
        "integerList         ,wildcardSubOfBox       ,CollectionBox  ,collection  ,true",
        "wildcardCollection  ,wildcardSubOfBox       ,CollectionBox  ,collection  ,true",
        "integerList         ,concreteSubOfBox       ,CollectionBox  ,collection  ,true",
        "stringList          ,concreteSubOfBox       ,CollectionBox  ,collection  ,false",
        "integerList         ,subOfConcrete          ,CollectionBox  ,collection  ,true",
        "integerList         ,integerSubOfSubOfBox   ,CollectionBox  ,collection  ,true",
        "integerList         ,stringSubOfSubOfBox    ,CollectionBox  ,collection  ,false",
        "integerList         ,rawCollectionBox       ,CollectionBox  ,collection  ,true",
        "stringList          ,rawCollectionBox       ,CollectionBox  ,collection  ,true",
        "changeList          ,changesTargetRaw       ,ChangeCollection  ,changes  ,true",
        "integerList         ,changesTargetRaw       ,ChangeCollection  ,changes  ,false",
    })
    public void testNestedFields(
            String sourceFieldName,
            String containingFieldName,
            String targetClassName,
            String targetFieldName,
            boolean matches)
            throws NoSuchFieldException, ClassNotFoundException {
        Field sourceField = GenericTypeMatchNestedFieldTest.class.getDeclaredField(sourceFieldName);
        Field containingField =
                GenericTypeMatchNestedFieldTest.class.getDeclaredField(containingFieldName);
        String className = DATA_TYPES_CLASS_PREFIX + targetClassName;
        Class<?> targetClass = Class.forName(className);
        Field targetField = targetClass.getDeclaredField(targetFieldName);
        GenericTypeMatch sourceMatch = GenericTypeMatch.ofField(sourceField);
        GenericTypeMatch containingMatch = GenericTypeMatch.ofField(containingField);
        Optional<GenericTypeMatch> optTargetMatch = containingMatch.findDeclaredField(targetField);
        assertTrue(optTargetMatch.isPresent());
        assertEquals(matches, optTargetMatch.get().matches(sourceMatch));
    }
}
