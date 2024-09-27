/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.Test;
import org.mockito.exceptions.misusing.WrongTypeOfReturnValue;
import org.mockito.internal.invocation.InvocationBuilder;

public class DefaultAnswerValidatorTest {

    @Test
    public void should_fail_if_returned_value_of_answer_is_incompatible_with_return_type()
            throws Throwable {
        // given
        class AWrongType {}
        try {
            // when
            DefaultAnswerValidator.validateReturnValueFor(
                    new InvocationBuilder().method("toString").toInvocation(), new AWrongType());
            fail("expected validation to fail");
        } catch (WrongTypeOfReturnValue e) {
            // then
            assertThat(e.getMessage())
                    .containsIgnoringCase("Default answer returned a result with the wrong type")
                    .containsIgnoringCase("AWrongType cannot be returned by toString()")
                    .containsIgnoringCase("toString() should return String");
        }
    }

    @Test
    public void should_not_fail_if_returned_value_of_answer_is_null() throws Throwable {
        DefaultAnswerValidator.validateReturnValueFor(
                new InvocationBuilder().method("toString").toInvocation(), null);
    }
}
