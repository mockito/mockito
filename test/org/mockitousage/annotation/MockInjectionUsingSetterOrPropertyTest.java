/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.annotation;

import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.MockUtil;
import org.mockitoutil.TestBase;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@SuppressWarnings({"unchecked", "unused"})
public class MockInjectionUsingSetterOrPropertyTest extends TestBase {

	private SuperUnderTesting superUnderTestWithoutInjection = new SuperUnderTesting();
	@InjectMocks private SuperUnderTesting superUnderTest = new SuperUnderTesting();
	@InjectMocks private BaseUnderTesting baseUnderTest = new BaseUnderTesting();
	@InjectMocks private SubUnderTesting subUnderTest = new SubUnderTesting();
	@InjectMocks private OtherBaseUnderTesting otherBaseUnderTest = new OtherBaseUnderTesting();

    private BaseUnderTesting baseUnderTestingInstance = new BaseUnderTesting();
    @InjectMocks private BaseUnderTesting initializedBase = baseUnderTestingInstance;
    @InjectMocks private BaseUnderTesting notInitializedBase;

    @Spy @InjectMocks private SuperUnderTesting initializedSpy = new SuperUnderTesting();
    @Spy @InjectMocks private SuperUnderTesting notInitializedSpy;

    @Mock private Map map;
    @Mock private List list;
	@Mock private Set histogram1;
	@Mock private Set histogram2;
	@Spy private TreeSet searchTree = new TreeSet();
    private MockUtil mockUtil = new MockUtil();

    @Before
	public void init() {
		// initMocks called in TestBase Before method, so instances ar not the same
		MockitoAnnotations.initMocks(this);
	}

    @Test
    public void shouldKeepSameInstanceIfFieldInitialized() {
        assertSame(baseUnderTestingInstance, initializedBase);
    }

    @Test
    public void shouldInitializeAnnotatedFieldIfNull() {
        assertNotNull(notInitializedBase);
    }                                          

    @Test
    public void shouldIInjectMocksInSpy() {
        assertNotNull(initializedSpy.getAList());
        assertTrue(mockUtil.isMock(initializedSpy));
    }
    @Test
    public void shouldInitializeSpyIfNullAndInjectMocks() {
        assertNotNull(notInitializedSpy);
        assertNotNull(notInitializedSpy.getAList());
        assertTrue(mockUtil.isMock(notInitializedSpy));
    }

	@Test
	public void shouldInjectMocksIfAnnotated() {
		MockitoAnnotations.initMocks(this);
		assertSame(list, superUnderTest.getAList());
	}

	@Test
	public void shouldNotInjectIfNotAnnotated() {
		MockitoAnnotations.initMocks(this);
		assertNull(superUnderTestWithoutInjection.getAList());
	}

	@Test
	public void shouldInjectMocksForClassHierarchyIfAnnotated() {
		MockitoAnnotations.initMocks(this);
		assertSame(list, baseUnderTest.getAList());
		assertSame(map, baseUnderTest.getAMap());
	}

	@Test
	public void shouldInjectMocksByName() {
		MockitoAnnotations.initMocks(this);
		assertSame(histogram1, subUnderTest.getHistogram1());
		assertSame(histogram2, subUnderTest.getHistogram2());
	}

	@Test
	public void shouldInjectSpies() {
		MockitoAnnotations.initMocks(this);
		assertSame(searchTree, otherBaseUnderTest.getSearchTree());
	}
	
    @Test
    public void shouldInstantiateInjectMockFieldIfPossible() throws Exception {
        assertNotNull(notInitializedBase);
    }

    @Test
    public void shouldKeepInstanceOnInjectMockFieldIfPresent() throws Exception {
        assertSame(baseUnderTestingInstance, initializedBase);
    }

    @Test
    public void shouldReportNicely() throws Exception {
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
        ThrowingConstructor() { throw new RuntimeException("aha"); };
    }

    static class SuperUnderTesting {

		private List aList;

		public List getAList() {
			return aList;
		}
	}

	static class BaseUnderTesting extends SuperUnderTesting {
		private Map aMap;

		public Map getAMap() {
			return aMap;
		}
	}

	static class OtherBaseUnderTesting extends SuperUnderTesting {
		private TreeSet searchTree;

		public TreeSet getSearchTree() {
			return searchTree;
		}
	}

	static class SubUnderTesting extends BaseUnderTesting {
		private Set histogram1;
		private Set histogram2;

		public Set getHistogram1() {
			return histogram1;
		}

		public Set getHistogram2() {
			return histogram2;
		}
	}
}
