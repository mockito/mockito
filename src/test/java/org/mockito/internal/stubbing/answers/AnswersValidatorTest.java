/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.mockito.exceptions.misusing.WrongTypeOfReturnValue;
import org.mockito.internal.invocation.InvocationBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@SuppressWarnings("unchecked")
public class AnswersValidatorTest {

    private AnswersValidator validator = new AnswersValidator();

    @Test
    public void should_fail_if_returned_value_of_answer_is_incompatible_with_return_type() throws Throwable {
        try {
            validator.validateDefaultAnswerReturnedValue(
                    new InvocationBuilder().method("toString").toInvocation(),
                    AWrongType.WRONG_TYPE
            );
            fail();
        } catch (WrongTypeOfReturnValue e) {
            assertThat(e.getMessage())
                    .containsIgnoringCase("Default answer returned a result with the wrong type")
                    .containsIgnoringCase("AWrongType cannot be returned by toString()")
                    .containsIgnoringCase("toString() should return String");
        }
    }

    @Test
    public void should_not_fail_if_returned_value_of_answer_is_null() throws Throwable {
        validator.validateDefaultAnswerReturnedValue(
                new InvocationBuilder().method("toString").toInvocation(),
                null
        );
    }

    private static class AWrongType {
        public static final AWrongType WRONG_TYPE = new AWrongType();
    }
}
