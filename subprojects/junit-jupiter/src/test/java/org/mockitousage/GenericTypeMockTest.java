/*
 * Copyright (c) 2023 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GenericTypeMockTest {

    public static class UnderTest {
        List<String> stringList;
        List<Integer> intList;
        Set<?> anySet;
    }

    @Mock
    private List<String> stringProviderMock;

    @Mock
    private List<Integer> intProviderMock;

    @Mock
    private TreeSet<String> treeSet = Mockito.mock(TreeSet.class);;

    @InjectMocks
    private UnderTest underTest;

    /**
     * Verify that InjectMocks will correctly match fields with same generic type but different type parameters,
     * without using the same field name.
     */
    @Test
    public void testInjectMock() {
        // this used to fail without any error message hinting at the problem, as soon as a class under test has
        // a second field of the same generic type, but with different type parameter. The programmer then
        // had to know that mock field names have to match field names in the class under test.
        assertNotNull(underTest.stringList);
        assertNotNull(underTest.intList);
        assertNotNull(underTest.anySet);

        assertEquals(stringProviderMock, underTest.stringList);
        assertEquals(intProviderMock, underTest.intList);
        assertEquals(treeSet, underTest.anySet);

    }

}
