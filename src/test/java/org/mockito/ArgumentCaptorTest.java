/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.mockitousage.IMethods;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.validateMockitoUsage;
import static org.mockito.Mockito.verify;

public class ArgumentCaptorTest {
	@Captor private ArgumentCaptor<Integer> captorInteger;
	@Mock private IMethods methods;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		methods = mock(IMethods.class);
	}

	/**
	 * Clean up the internal Mockito-Stubbing state
	 */
	@After
	public void tearDown() {

		try {
			validateMockitoUsage();
		} catch (InvalidUseOfMatchersException ignore) {
		}
		
	}

	@Test
	public void tell_handy_return_values_to_return_value_for() throws Exception {

		ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
		assertThat(captor.capture()).isNull();

	}

	@Test
	public void getValue() {
		methods.forInteger(1);
		methods.forInteger(2);
		methods.forInteger(3);

		verify(methods, times(3)).forInteger(captorInteger.capture());

		assertThat(captorInteger.getValue()).isEqualTo(3);
	}

	@Test
	public void getLastValue() {
		methods.forInteger(1);
		methods.forInteger(2);
		methods.forInteger(3);

		verify(methods, times(3)).forInteger(captorInteger.capture());

		assertThat(captorInteger.getLastValue()).isEqualTo(3);
	}

	@Test
	public void getValueAt() {
		methods.forInteger(1);
		methods.forInteger(2);
		methods.forInteger(3);

		verify(methods, times(3)).forInteger(captorInteger.capture());

		assertThat(captorInteger.getValueAt(0)).isEqualTo(1);
		assertThat(captorInteger.getValueAt(1)).isEqualTo(2);
		assertThat(captorInteger.getValueAt(2)).isEqualTo(3);
	}

	@Test
	public void getFirstValue() {
		methods.forInteger(1);
		methods.forInteger(2);
		methods.forInteger(3);

		verify(methods, times(3)).forInteger(captorInteger.capture());

		assertThat(captorInteger.getFirstValue()).isEqualTo(1);
	}

	@Test
	public void getSecondValue() {
		methods.forInteger(1);
		methods.forInteger(2);
		methods.forInteger(3);

		verify(methods, times(3)).forInteger(captorInteger.capture());

		assertThat(captorInteger.getSecondValue()).isEqualTo(2);
	}

	@Test
	public void getThirdValue() {
		methods.forInteger(1);
		methods.forInteger(2);
		methods.forInteger(3);

		verify(methods, times(3)).forInteger(captorInteger.capture());

		assertThat(captorInteger.getThirdValue()).isEqualTo(3);
	}

	@Test
	public void getAllValues() {
		methods.forInteger(1);
		methods.forInteger(2);
		methods.forInteger(3);

		verify(methods, times(3)).forInteger(captorInteger.capture());

		assertThat(captorInteger.getAllValues()).containsExactly(1, 2, 3);
	}
}
