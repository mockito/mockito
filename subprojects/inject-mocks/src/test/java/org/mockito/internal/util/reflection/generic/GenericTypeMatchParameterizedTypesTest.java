/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection.generic;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("unused")
public class GenericTypeMatchParameterizedTypesTest {

    private List<Integer> integerList;
    private Collection<Integer> integerCollection;
    private List<String> stringList;
    private List<?> wildcardList;
    private List<? extends Integer> wildcardListInteger;
    private Map<String, Integer> mapStringInteger;
    private Map<?, ?> wildcardMap;
    private Map<? extends String, ? extends Number> wildcardMapStringNumber;

    @ParameterizedTest
    @CsvSource({
        "integerList          ,integerList          ,true",
        "integerList          ,integerCollection    ,true",
        "integerCollection    ,integerList          ,false",
        "integerList          ,stringList           ,false",
        "integerList          ,wildcardListInteger  ,true",
        "stringList           ,integerList          ,false",
        "stringList           ,wildcardList         ,true",
        "wildcardList         ,stringList           ,true",
        "wildcardList         ,wildcardList         ,true",
        "wildcardListInteger  ,integerList          ,true",
        "wildcardListInteger  ,stringList           ,true",
        "wildcardListInteger  ,wildcardListInteger  ,true",
        "wildcardListInteger  ,wildcardList         ,true",
        "wildcardList         ,wildcardListInteger  ,false",
        "mapStringInteger         ,mapStringInteger         ,true",
        "wildcardMap              ,mapStringInteger         ,true",
        "wildcardMapStringNumber  ,mapStringInteger         ,true",
        "mapStringInteger         ,wildcardMap              ,true",
        "wildcardMap              ,wildcardMap              ,true",
        "wildcardMapStringNumber  ,wildcardMap              ,true",
        "mapStringInteger         ,wildcardMapStringNumber  ,true",
        "wildcardMapStringNumber  ,wildcardMapStringNumber  ,true",
        "wildcardMap              ,wildcardMapStringNumber  ,false",
    })
    public void testParameterizedTypes(
            String sourceFieldName, String targetFieldName, boolean matches)
            throws NoSuchFieldException {
        Field sourceField =
                GenericTypeMatchParameterizedTypesTest.class.getDeclaredField(sourceFieldName);
        Field targetField =
                GenericTypeMatchParameterizedTypesTest.class.getDeclaredField(targetFieldName);
        GenericTypeMatch sourceMatch = GenericTypeMatch.ofField(sourceField);
        GenericTypeMatch targetMatch = GenericTypeMatch.ofField(targetField);
        assertEquals(matches, targetMatch.matches(sourceMatch));
    }
}
