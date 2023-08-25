/*
 * Copyright (c) 2023 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

import org.junit.Test;

public class ReturnsOptionalMocksTest {
    private interface Type {
        Optional<String> getOptString();

        OptionalLong getOptLong();

        OptionalDouble getOptDouble();

        OptionalInt getOptInt();
    }

    @Test
    public void deepStubs_Optional_should_return_normal_optional_empty_Issue2865() {
        final Type type = mock(Type.class, RETURNS_DEEP_STUBS);
        assertThat(type.getOptString()).isEqualTo(Optional.empty());
        assertIsNoMock(type.getOptString());
    }

    @Test
    public void deepStubs_OptionalLong_should_return_normal_optional_empty() {
        final Type type = mock(Type.class, RETURNS_DEEP_STUBS);
        assertThat(type.getOptLong()).isEqualTo(OptionalLong.empty());
        assertIsNoMock(type.getOptLong());
    }

    @Test
    public void deepStubs_OptionalDouble_should_normal_optional_empty() {
        final Type type = mock(Type.class, RETURNS_DEEP_STUBS);
        assertThat(type.getOptDouble()).isEqualTo(OptionalDouble.empty());
        assertIsNoMock(type.getOptDouble());
    }

    @Test
    public void deepStubs_OptionalInt_should_normal_optional_empty() {
        final Type type = mock(Type.class, RETURNS_DEEP_STUBS);
        assertThat(type.getOptInt()).isEqualTo(OptionalInt.empty());
        assertIsNoMock(type.getOptInt());
    }

    @Test
    public void normal_mock_Optional_should_return_normal_optional_empty() {
        final Type type = mock(Type.class);
        assertThat(type.getOptString()).isEqualTo(Optional.empty());
        assertIsNoMock(type.getOptString());
    }

    @Test
    public void normal_mock_OptionalLong_should_return_normal_optional_empty() {
        final Type type = mock(Type.class);
        assertThat(type.getOptLong()).isEqualTo(OptionalLong.empty());
    }

    @Test
    public void normal_mock_OptionalDouble_should_return_normal_optional_empty() {
        final Type type = mock(Type.class);
        assertThat(type.getOptDouble()).isEqualTo(OptionalDouble.empty());
    }

    @Test
    public void normal_mock_OptionalInt_should_return_normal_optional_empty() {
        final Type type = mock(Type.class);
        assertThat(type.getOptInt()).isEqualTo(OptionalInt.empty());
    }

    @Test
    public void deepStubs_Optional_isPresent_isEmpty_Issue2865() {
        final Type type = mock(Type.class, RETURNS_DEEP_STUBS);
        Optional<String> opt = type.getOptString();
        assertThat(opt.isPresent()).isEqualTo(false);
        assertThat(opt.isEmpty()).isEqualTo(true);
    }

    @Test
    public void deepStubs_OptionalLong_isPresent_isEmpty_Issue2865() {
        final Type type = mock(Type.class, RETURNS_DEEP_STUBS);
        OptionalLong opt = type.getOptLong();
        assertThat(opt.isPresent()).isEqualTo(false);
        assertThat(opt.isEmpty()).isEqualTo(true);
    }

    @Test
    public void deepStubs_OptionalDouble_isPresent_isEmpty_Issue2865() {
        final Type type = mock(Type.class, RETURNS_DEEP_STUBS);
        OptionalDouble opt = type.getOptDouble();
        assertThat(opt.isPresent()).isEqualTo(false);
        assertThat(opt.isEmpty()).isEqualTo(true);
    }

    @Test
    public void deepStubs_OptionalInt_isPresent_isEmpty_Issue2865() {
        final Type type = mock(Type.class, RETURNS_DEEP_STUBS);
        OptionalInt opt = type.getOptInt();
        assertThat(opt.isPresent()).isEqualTo(false);
        assertThat(opt.isEmpty()).isEqualTo(true);
    }

    @Test
    public void normal_mock_Optional_isPresent_isEmpty_Issue2865() {
        final Type type = mock(Type.class);

        Optional<String> opt = type.getOptString();
        assertThat(opt.isPresent()).isEqualTo(false);
        assertThat(opt.isEmpty()).isEqualTo(true);
    }

    @Test
    public void normal_mock_OptionalLong_isPresent_isEmpty_Issue2865() {
        final Type type = mock(Type.class);

        OptionalLong opt = type.getOptLong();
        assertThat(opt.isPresent()).isEqualTo(false);
        assertThat(opt.isEmpty()).isEqualTo(true);
    }

    @Test
    public void normal_mock_OptionalDouble_isPresent_isEmpty_Issue2865() {
        final Type type = mock(Type.class);

        OptionalDouble opt = type.getOptDouble();
        assertThat(opt.isPresent()).isEqualTo(false);
        assertThat(opt.isEmpty()).isEqualTo(true);
    }

    @Test
    public void normal_mock_OptionalInt_isPresent_isEmpty_Issue2865() {
        final Type type = mock(Type.class);

        OptionalInt opt = type.getOptInt();
        assertThat(opt.isPresent()).isEqualTo(false);
        assertThat(opt.isEmpty()).isEqualTo(true);
    }

    private void assertIsNoMock(Object mock) {
        assertThat(mockingDetails(mock).isMock()).isFalse();
    }
}
