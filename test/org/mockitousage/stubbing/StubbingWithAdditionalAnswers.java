/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.stubbing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockitousage.IMethods;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsArgAt;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.AdditionalAnswers.returnsLastArg;
import static org.mockito.AdditionalAnswers.returnsSecondArg;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Matchers.eq;

@RunWith(MockitoJUnitRunner.class)
public class StubbingWithAdditionalAnswers {

    @Mock IMethods iMethods;

    @Test
    public void can_return_arguments_of_invocation() throws Exception {
        given(iMethods.objectArgMethod(anyObject())).will(returnsFirstArg());
        given(iMethods.threeArgumentMethod(eq(0), anyObject(), anyString())).will(returnsSecondArg());
        given(iMethods.threeArgumentMethod(eq(1), anyObject(), anyString())).will(returnsLastArg());

        assertThat(iMethods.objectArgMethod("first")).isEqualTo("first");
        assertThat(iMethods.threeArgumentMethod(0, "second", "whatever")).isEqualTo("second");
        assertThat(iMethods.threeArgumentMethod(1, "whatever", "last")).isEqualTo("last");
    }

    @Test
    public void can_return_expanded_arguments_of_invocation() throws Exception {
        given(iMethods.varargsObject(eq(1), anyVararg())).will(returnsArgAt(3));

        assertThat(iMethods.varargsObject(1, "bob", "alexander", "alice", "carl")).isEqualTo("alice");
    }

    @Test
    public void can_return_primitives_or_wrappers() throws Exception {
        given(iMethods.toIntPrimitive(anyInt())).will(returnsFirstArg());
        given(iMethods.toIntWrapper(anyInt())).will(returnsFirstArg());

        assertThat(iMethods.toIntPrimitive(1)).isEqualTo(1);
        assertThat(iMethods.toIntWrapper(1)).isEqualTo(1);
    }
}
