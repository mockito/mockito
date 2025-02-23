/*
 * Copyright (c) 2022 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockitoutil.TestBase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class OrTest extends TestBase {

    @Mock private ArgumentMatcher<?> m1;
    @Mock private ArgumentMatcher<?> m2;
    private Or or;

    @Before
    public void setUp() throws Exception {
        or = new Or(m1, m2);
    }

    @Test
    public void shouldReturnMatchingTypes() {
        given(m1.type()).will(inv -> String.class);
        given(m2.type()).will(inv -> String.class);

        assertThat(or.type()).isEqualTo(String.class);
    }

    @Test
    public void shouldDefaultMismatchingTypes() {
        given(m1.type()).will(inv -> String.class);
        given(m2.type()).will(inv -> Integer.class);

        assertThat(or.type()).isEqualTo(Void.class);
    }

    @Test
    public void shouldReturnLeftBaseType() {
        given(m1.type()).will(inv -> BaseType.class);
        given(m2.type()).will(inv -> SubType.class);

        assertThat(or.type()).isEqualTo(BaseType.class);
    }

    @Test
    public void shouldReturnRightBaseType() {
        given(m1.type()).will(inv -> SubType.class);
        given(m2.type()).will(inv -> BaseType.class);

        assertThat(or.type()).isEqualTo(BaseType.class);
    }

    private interface BaseType {}

    private interface SubType extends BaseType {}
}
