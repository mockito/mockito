/*
 * Copyright (c) 2025 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

/**
 * Settings only applicable to static mocks.
 * <p>
 * Instantiate with {@link StaticMockSettings#builder()}.
 */
public class StaticMockSettings {

    public final boolean stubInstanceMethods;

    private StaticMockSettings(StaticMockSettings.Builder builder) {
        stubInstanceMethods = builder.stubInstanceMethods;
    }

    public static StaticMockSettings.Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Builder() {}

        private boolean stubInstanceMethods = false;

        /**
         * Whether to intercept invocations on instance methods for all existing and newly created instances of this
         * mock. Defaults to false if unset.
         * <p>
         * NOTE: You probably don't need this feature. Just construct mocks normally and pass the instance to the code
         * that needs it. This API exists to enable Kotlin-specific mocking controls in Mockito-Kotlin.
         */
        public Builder stubInstanceMethods(boolean stubInstanceMethods) {
            this.stubInstanceMethods = stubInstanceMethods;
            return this;
        }

        public StaticMockSettings build() {
            return new StaticMockSettings(this);
        }
    }
}
