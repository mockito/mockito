/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockitousage.IMethods;

@SuppressWarnings("unchecked")
public class MockCreationValidatorTest {

    MockCreationValidator validator = new MockCreationValidator();

    @Test
    public void should_not_allow_extra_interface_that_is_the_same_as_the_mocked_type() {
        try {
            // when
            validator.validateExtraInterfaces(IMethods.class, (Collection) asList(IMethods.class));
            fail();
        } catch (MockitoException e) {
            // then
            assertThat(e.getMessage()).contains("You mocked following type: IMethods");
        }
    }

    @Test
    public void should_not_allow_inconsistent_types() {
        // when / then
        assertThatThrownBy(
                        () -> {
                            validator.validateMockedType(List.class, new ArrayList());
                        })
                .isInstanceOf(MockitoException.class)
                .hasMessageContainingAll(
                        "Mocked type must be the same as the type of your spied instance.",
                        "Mocked type must be: ArrayList, but is: List");
    }

    @Test
    public void should_allow_only_consistent_types() {
        // when
        validator.validateMockedType(ArrayList.class, new ArrayList());
        // then no exception is thrown
    }

    @Test
    public void should_validation_be_safe_when_nulls_passed() {
        // when
        validator.validateMockedType(null, new ArrayList());
        // or
        validator.validateMockedType(ArrayList.class, null);
        // then no exception is thrown
    }

    @Test
    public void should_fail_when_type_not_mockable() {
        try {
            validator.validateType(long.class, null);
        } catch (MockitoException ex) {
            assertThat(ex.getMessage()).contains("primitive");
        }
    }
}
