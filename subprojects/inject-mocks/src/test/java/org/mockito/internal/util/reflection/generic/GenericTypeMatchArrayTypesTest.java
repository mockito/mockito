/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection.generic;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("unused")
public class GenericTypeMatchArrayTypesTest {

    private Integer[] integerArray;
    private String[] stringArray;
    private Object[] objectArray;

    @ParameterizedTest
    @CsvSource({
        "integerArray  ,integerArray  ,true",
        "stringArray   ,integerArray  ,false",
        "objectArray   ,integerArray  ,false",
        "stringArray   ,stringArray   ,true",
        "integerArray  ,stringArray   ,false",
        "objectArray   ,stringArray   ,false",
        "objectArray   ,objectArray   ,true",
        "stringArray   ,objectArray   ,true",
        "integerArray  ,objectArray   ,true",
    })
    public void testArrayTypes(String sourceFieldName, String targetFieldName, boolean matches)
            throws NoSuchFieldException {
        Field sourceField = GenericTypeMatchArrayTypesTest.class.getDeclaredField(sourceFieldName);
        Field targetField = GenericTypeMatchArrayTypesTest.class.getDeclaredField(targetFieldName);
        GenericTypeMatch sourceMatch = GenericTypeMatch.ofField(sourceField);
        GenericTypeMatch targetMatch = GenericTypeMatch.ofField(targetField);
        assertEquals(matches, targetMatch.matches(sourceMatch));
    }
}
