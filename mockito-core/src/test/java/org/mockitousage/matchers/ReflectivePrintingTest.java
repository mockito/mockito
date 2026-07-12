/*
 * Copyright (c) 2026 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.matchers;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockitoutil.TestBase;

@SuppressWarnings("all")
public class ReflectivePrintingTest extends TestBase {

    /**
     * Class without own "toString()" method
     */
    class Credentials {
        private final String username;
        private final String password;

        Credentials(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    interface LoginService {
        void run(Credentials credentials);
    }

    private final Credentials actual = new Credentials("usr", "pwd");
    private final Credentials expected = new Credentials("john", "secret");
    private final LoginService mock = mock();

    @Test
    public void printsObjectFieldsUsingReflection() {
        mock.run(actual);

        assertThatThrownBy(() -> verify(mock).run(expected))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Argument(s) are different! Wanted:")
                .hasMessageContaining("Credentials[password=secret, username=john]")
                .hasMessageContaining("Actual invocations have different arguments:")
                .hasMessageContaining("Credentials[password=pwd, username=usr]");
    }

    @Test
    public void eq_printsObjectFieldsUsingReflection() {
        mock.run(actual);

        assertThatThrownBy(() -> verify(mock).run(eq(expected)))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Argument(s) are different! Wanted:")
                .hasMessageContaining("Credentials[password=secret, username=john]")
                .hasMessageContaining("Actual invocations have different arguments:")
                .hasMessageContaining("Credentials[password=pwd, username=usr]");
    }

    @Test
    public void refEq_printsObjectFieldsUsingReflection() {
        mock.run(actual);

        assertThatThrownBy(() -> verify(mock).run(refEq(expected)))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Argument(s) are different! Wanted:")
                .hasMessageContaining("refEq(Credentials[password=secret, username=john])")
                .hasMessageContaining("Actual invocations have different arguments:")
                .hasMessageContaining("Credentials[password=pwd, username=usr]");
    }

    @Test
    public void same_printsObjectFieldsUsingReflection() {
        mock.run(actual);

        assertThatThrownBy(() -> verify(mock).run(same(expected)))
                .isInstanceOf(AssertionError.class)
                .hasMessageContaining("Argument(s) are different! Wanted:")
                .hasMessageContaining("same(Credentials[password=secret, username=john])")
                .hasMessageContaining("Actual invocations have different arguments:")
                .hasMessageContaining("Credentials[password=pwd, username=usr]");
    }
}
