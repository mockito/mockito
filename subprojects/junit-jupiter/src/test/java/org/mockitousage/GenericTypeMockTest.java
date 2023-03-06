/*
 * Copyright (c) 2023 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockitousage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.Time;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests that verify Mockito can discern mocks by generic types, so if there are multiple mock candidates
 * with the same generic type but different type parameters available for injection into a given field,
 * Mockito won't fail to inject (even if mock field name doesn't match under test's field name).
 */
@ExtendWith(MockitoExtension.class)
public class GenericTypeMockTest {


    @Nested
    public class SingleTypeParamTest {
        public class UnderTestWithSingleTypeParam {
            List<String> stringList;
            List<Integer> intList;
        }

        @Mock
        private List<String> stringListMock;

        @Mock
        private List<Integer> intListMock;

        // must construct non-static inner class ourselves here
        // (making it public static classes doesn't work either)
        @InjectMocks
        private UnderTestWithSingleTypeParam underTestWithSingleTypeParam = new UnderTestWithSingleTypeParam();

        @Test
        void testSingleTypeParam() {
            // testing for not null first before testing for equals,
            // because assertEquals(null, null) == true,
            // so test would succeed if both @Mock and @InjectMocks
            // don't work at all
            assertNotNull(stringListMock);
            assertNotNull(intListMock);

            assertEquals(stringListMock, underTestWithSingleTypeParam.stringList);
            assertEquals(intListMock, underTestWithSingleTypeParam.intList);
        }
    }

    @Nested
    public class WildcardTest {
        class UnderTestWithWildcard {
            Set<? extends Date> dateSet;
            Set<? extends Number> numberSet;
        }

        @Mock
        Set<Time> timeSetMock; // java.sql.Time extends Date

        @Mock
        Set<Integer> integerSetMock;

        @InjectMocks
        UnderTestWithWildcard underTestWithWildcard = new UnderTestWithWildcard();

        @Test
        void testWildcard() {
            assertNotNull(timeSetMock);
            assertNotNull(integerSetMock);

            // this also tests whether WildcardType.upperBounds() is evaluated,
            // i.e. that we match <? extends Date> to <Time> type parameter
            assertEquals(timeSetMock, underTestWithWildcard.dateSet);
            assertEquals(integerSetMock, underTestWithWildcard.numberSet);
        }
    }


    @Nested
    public class NestedTypeParametersTest {
        public class UnderTestWithNestedTypeParameters {
            Map<Integer, Collection<String>> intToStringCollectionMap;
            Map<Integer, Collection<Integer>> intoToIntCollectionMap;
        }

        @Mock
        Map<Integer, Collection<String>> intToStringCollectionMapMock;

        @Mock
        Map<Integer, Collection<Integer>> intToIntCollectionMapMock;

        @InjectMocks
        UnderTestWithNestedTypeParameters underTestWithNestedTypeParameters = new UnderTestWithNestedTypeParameters();

        @Test
        void testNestedTypeParameters() {
            assertNotNull(intToStringCollectionMapMock);
            assertNotNull(intToIntCollectionMapMock);

            assertEquals(intToStringCollectionMapMock, underTestWithNestedTypeParameters.intToStringCollectionMap);
            assertEquals(intToIntCollectionMapMock, underTestWithNestedTypeParameters.intoToIntCollectionMap);
        }
    }

    @Nested
    public class GenericSubclassTest {
        public class UnderTestWithGenericSubclass {
            Set<String> stringSet;
            Set<Integer> intSet;
        }

        @Mock
        TreeSet<String> stringTreeSetMock;

        @Mock
        HashSet<Integer> intHashSetMock;

        @InjectMocks
        UnderTestWithGenericSubclass underTestWithGenericSubclass = new UnderTestWithGenericSubclass();

        @Test
        void testGenericSubclass() {
            assertNotNull(stringTreeSetMock);
            assertNotNull(intHashSetMock);

            assertEquals(stringTreeSetMock, underTestWithGenericSubclass.stringSet);
            assertEquals(intHashSetMock, underTestWithGenericSubclass.intSet);
        }
    }

    @Nested
    public class MultipleCandidatesByTypeTest {
        public class UnderTestWithMultipleCandidatesByType {
            List<String> stringList;
        }

        @Mock
        List<String> stringList1;

        @Mock
        List<String> stringList2;

        @InjectMocks
        UnderTestWithMultipleCandidatesByType underTestWithMultipleCandidates = new UnderTestWithMultipleCandidatesByType();

        @Test
        void testMultipleCandidatesByTypes() {
            assertNotNull(stringList1);
            assertNotNull(stringList2);

            // verify that when mutiple mock candidates exist with same type (but not matching by field names), none will be injected
            assertNull(underTestWithMultipleCandidates.stringList);
        }
    }

    @Nested
    public class MultipleCandidatesOneByNameTest {
        public class UnderTestWithMultipleCandidatesOneByName {
            List<String> stringList;
        }

        @Mock
        List<String> stringList;

        @Mock
        List<String> stringListMock;

        @InjectMocks
        UnderTestWithMultipleCandidatesOneByName underTestWithMultipleCandidatesOneByName = new UnderTestWithMultipleCandidatesOneByName();

        @Test
        void testMultipleCandidatesOneByName() {
            // verify that when multiple mock candidates exist by type, and one of them matches by field name, that one is injected
            assertNotNull(underTestWithMultipleCandidatesOneByName.stringList);
            assertEquals(stringList, underTestWithMultipleCandidatesOneByName.stringList);
        }
    }

}
