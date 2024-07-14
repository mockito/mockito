/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection.generic;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.internal.util.reflection.generic.DataTypes.ArgToArray;
import org.mockito.internal.util.reflection.generic.DataTypes.ArgToArrayTwoDim;
import org.mockito.internal.util.reflection.generic.DataTypes.ArgToArrayWithArray;
import org.mockito.internal.util.reflection.generic.DataTypes.Box;
import org.mockito.internal.util.reflection.generic.DataTypes.WithArrayBox;
import org.mockito.internal.util.reflection.generic.DataTypes.WithBoxArray;
import org.mockito.internal.util.reflection.generic.DataTypes.WithBoxArrayTwoDim;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("unused")
public class GenericArrayTypesTest {
    public static final String DATA_TYPES_CLASS_PREFIX = DataTypes.class.getName() + "$";

    private WithBoxArray<String> withStringBoxArray;
    private WithBoxArrayTwoDim<String> withStringBoxArrayTwoDim;
    private WithArrayBox<String> withStringArrayBox;
    private ArgToArray<String> stringArgToArray;
    private ArgToArrayTwoDim<String> stringArgToArrayTwoDim;
    private ArgToArrayWithArray<String> stringArgToArrayWithArray;
    private ArgToArray<?> wildcardArgToArray;

    private Box<String>[] stringBoxArray;
    private Box<Integer>[] integerBoxArray;
    private Box<String>[][] stringBoxArrayTwoDim;
    private Box<Integer>[][] integerBoxArrayTwoDim;
    private Box<String[]> stringArrayBox;
    private Box<Integer[]> integerArrayBox;
    private Box<String[][]> stringArrayBoxTwoDim;
    private Box<String[]>[] stringArrayBoxArray;
    private Box<?> wildcardBox;

    @ParameterizedTest
    @CsvSource({
        "stringBoxArray         ,withStringBoxArray         ,WithBoxArray        ,boxArray        ,true",
        "integerBoxArray        ,withStringBoxArray         ,WithBoxArray        ,boxArray        ,false",
        "stringBoxArrayTwoDim   ,withStringBoxArray         ,WithBoxArray        ,boxArray        ,false",
        "stringBoxArrayTwoDim   ,withStringBoxArrayTwoDim   ,WithBoxArrayTwoDim  ,boxArrayTwoDim  ,true",
        "integerBoxArrayTwoDim  ,withStringBoxArrayTwoDim   ,WithBoxArrayTwoDim  ,boxArrayTwoDim  ,false",
        "stringBoxArray         ,withStringBoxArrayTwoDim   ,WithBoxArrayTwoDim  ,boxArrayTwoDim  ,false",
        "stringArrayBox         ,stringArgToArray           ,WithBox             ,box             ,true",
        "integerArrayBox        ,stringArgToArray           ,WithBox             ,box             ,false",
        "wildcardBox            ,stringArgToArray           ,WithBox             ,box             ,true",
        "stringArrayBox         ,wildcardArgToArray         ,WithBox             ,box             ,false",
        "integerArrayBox        ,wildcardArgToArray         ,WithBox             ,box             ,false",
        "wildcardBox            ,wildcardArgToArray         ,WithBox             ,box             ,true",
        "stringArrayBoxTwoDim   ,stringArgToArrayTwoDim     ,WithBox             ,box             ,true",
        "stringArrayBox         ,stringArgToArrayTwoDim     ,WithBox             ,box             ,false",
        "stringArrayBox         ,withStringArrayBox         ,WithArrayBox        ,arrayBox        ,true",
        "integerArrayBox        ,withStringArrayBox         ,WithArrayBox        ,arrayBox        ,false",
        "stringArrayBoxArray    ,stringArgToArrayWithArray  ,WithBoxArray        ,boxArray        ,true",
        "stringArrayBox         ,stringArgToArrayWithArray  ,WithBoxArray        ,boxArray        ,false",
        "stringBoxArray         ,stringArgToArrayWithArray  ,WithBoxArray        ,boxArray        ,false",
    })
    public void testGenericArrayTypes(
            String sourceFieldName,
            String containingFieldName,
            String targetClassName,
            String targetFieldName,
            boolean matches)
            throws NoSuchFieldException, ClassNotFoundException {
        Field sourceField = GenericArrayTypesTest.class.getDeclaredField(sourceFieldName);
        Field containingField = GenericArrayTypesTest.class.getDeclaredField(containingFieldName);
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
