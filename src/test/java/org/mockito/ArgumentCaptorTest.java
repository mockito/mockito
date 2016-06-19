/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.junit.After;
import org.junit.Test;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.validateMockitoUsage;

public class ArgumentCaptorTest {

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

}