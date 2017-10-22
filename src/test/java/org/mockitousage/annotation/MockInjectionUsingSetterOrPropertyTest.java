/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.annotation;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.MockUtil;
import org.mockitousage.IMethods;
import org.mockitoutil.TestBase;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.*;

@SuppressWarnings({"unchecked", "unused"})
public class MockInjectionUsingSetterOrPropertyTest extends TestBase {

    private SuperUnderTesting superUnderTestWithoutInjection = new SuperUnderTesting();
    @InjectMocks private SuperUnderTesting superUnderTest = new SuperUnderTesting();
    @InjectMocks private BaseUnderTesting baseUnderTest = new BaseUnderTesting();
    @InjectMocks private SubUnderTesting subUnderTest = new SubUnderTesting();
    @InjectMocks private OtherBaseUnderTesting otherBaseUnderTest = new OtherBaseUnderTesting();
    @InjectMocks private HasTwoFieldsWithSameType hasTwoFieldsWithSameType = new HasTwoFieldsWithSameType();

    private BaseUnderTesting baseUnderTestingInstance = new BaseUnderTesting();
    @InjectMocks private BaseUnderTesting initializedBase = baseUnderTestingInstance;
    @InjectMocks private BaseUnderTesting notInitializedBase;

    @Spy @InjectMocks private SuperUnderTesting initializedSpy = new SuperUnderTesting();
    @Spy @InjectMocks private SuperUnderTesting notInitializedSpy;

    @Mock private Map<?, ?> map;
    @Mock private List<?> list;
    @Mock private Set<?> histogram1;
    @Mock private Set<?> histogram2;
    @Mock private IMethods candidate2;

    @Spy private TreeSet<String> searchTree = new TreeSet<String>();

    @Before
    public void enforces_new_instances() {
        // initMocks called in TestBase Before method, so instances are not the same
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_keep_same_instance_if_field_initialized() {
        assertSame(baseUnderTestingInstance, initializedBase);
    }

    @Test
    public void should_initialize_annotated_field_if_null() {
        assertNotNull(notInitializedBase);
    }

    @Test
    public void should_inject_mocks_in_spy() {
        assertNotNull(initializedSpy.getAList());
        assertTrue(MockUtil.isMock(initializedSpy));
    }

    @Test
    public void should_initialize_spy_if_null_and_inject_mocks() {
        assertNotNull(notInitializedSpy);
        assertNotNull(notInitializedSpy.getAList());
        assertTrue(MockUtil.isMock(notInitializedSpy));
    }

    @Test
    public void should_inject_mocks_if_annotated() {
        MockitoAnnotations.initMocks(this);
        assertSame(list, superUnderTest.getAList());
    }

    @Test
    public void should_not_inject_if_not_annotated() {
        MockitoAnnotations.initMocks(this);
        assertNull(superUnderTestWithoutInjection.getAList());
    }

    @Test
    public void should_inject_mocks_for_class_hierarchy_if_annotated() {
        MockitoAnnotations.initMocks(this);
        assertSame(list, baseUnderTest.getAList());
        assertSame(map, baseUnderTest.getAMap());
    }

    @Test
    public void should_inject_mocks_by_name() {
        MockitoAnnotations.initMocks(this);
        assertSame(histogram1, subUnderTest.getHistogram1());
        assertSame(histogram2, subUnderTest.getHistogram2());
    }

    @Test
    public void should_inject_spies() {
        MockitoAnnotations.initMocks(this);
        assertSame(searchTree, otherBaseUnderTest.getSearchTree());
    }

    @Test
    public void should_insert_into_field_with_matching_name_when_multiple_fields_of_same_type_exists_in_injectee() {
        MockitoAnnotations.initMocks(this);
        assertNull("not injected, no mock named 'candidate1'", hasTwoFieldsWithSameType.candidate1);
        assertNotNull("injected, there's a mock named 'candidate2'", hasTwoFieldsWithSameType.candidate2);
    }

    @Test
    public void should_instantiate_inject_mock_field_if_possible() throws Exception {
        assertNotNull(notInitializedBase);
    }

    @Test
    public void should_keep_instance_on_inject_mock_field_if_present() throws Exception {
        assertSame(baseUnderTestingInstance, initializedBase);
    }

    @Test
    public void should_report_nicely() throws Exception {
        Object failing = new Object() {
            @InjectMocks ThrowingConstructor failingConstructor;
        };
        try {
            MockitoAnnotations.initMocks(failing);
            fail();
        } catch (MockitoException e) {
            Assertions.assertThat(e.getMessage()).contains("failingConstructor").contains("constructor").contains("threw an exception");
            Assertions.assertThat(e.getCause()).isInstanceOf(RuntimeException.class);
        }
    }

    static class ThrowingConstructor {
        ThrowingConstructor() { throw new RuntimeException("aha"); }
    }

    static class SuperUnderTesting {
        private List<?> aList;

        public List<?> getAList() { return aList; }
    }

    static class BaseUnderTesting extends SuperUnderTesting {
        private Map<?, ?> aMap;

        public Map<?, ?> getAMap() { return aMap; }
    }

    static class OtherBaseUnderTesting extends SuperUnderTesting {
        private TreeSet<?> searchTree;

        public TreeSet<?> getSearchTree() { return searchTree; }
    }

    static class SubUnderTesting extends BaseUnderTesting {
        private Set<?> histogram1;
        private Set<?> histogram2;

        public Set<?> getHistogram1() { return histogram1; }
        public Set<?> getHistogram2() { return histogram2; }
    }

    static class HasTwoFieldsWithSameType {
        private IMethods candidate1;
        private IMethods candidate2;
    }
}
