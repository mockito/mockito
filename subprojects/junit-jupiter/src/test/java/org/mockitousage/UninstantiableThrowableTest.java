/*
 * Copyright (c) 2018 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.mockito.Mockito;

import java.util.List;

//issue 1514
class UninstantiableThrowableTest {

    @RepeatedTest(2)
    void should_behave_consistently(RepetitionInfo i) {
        List mock = Mockito.mock(List.class);
        if (i.getCurrentRepetition() == 1) {
            Assertions.assertThatThrownBy(
                () -> Mockito.doThrow(UninstantiableException.class).when(mock).clear())
                .isInstanceOf(InstantiationError.class);
        }

        // The following operation results in "UnfinishedStubbing"
        Mockito.doThrow(RuntimeException.class).when(mock).clear();
    }

    abstract static class UninstantiableException extends RuntimeException { }
}
