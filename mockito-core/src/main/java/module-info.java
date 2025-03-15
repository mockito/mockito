/*
 * Copyright (c) 2025 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
module org.mockito {
    requires java.instrument;
    requires jdk.attach;
    requires net.bytebuddy;
    requires net.bytebuddy.agent;
    requires static junit;
    requires static org.hamcrest;
    requires static org.objenesis;
    requires static org.opentest4j;
    requires static jdk.unsupported;

    exports org.mockito;
    exports org.mockito.configuration;
    exports org.mockito.creation.instance;
    exports org.mockito.exceptions.base;
    exports org.mockito.exceptions.misusing;
    exports org.mockito.exceptions.verification;
    exports org.mockito.exceptions.verification.junit;
    exports org.mockito.exceptions.verification.opentest4j;
    exports org.mockito.hamcrest;
    exports org.mockito.invocation;
    exports org.mockito.junit;
    exports org.mockito.listeners;
    exports org.mockito.mock;
    exports org.mockito.plugins;
    exports org.mockito.quality;
    exports org.mockito.session;
    exports org.mockito.stubbing;
    exports org.mockito.verification;
    exports org.mockito.internal to
            java.instrument;
    exports org.mockito.internal.configuration to
            org.mockito.junit.jupiter;
    exports org.mockito.internal.configuration.plugins to
            org.mockito.junit.jupiter;
    exports org.mockito.internal.session to
            org.mockito.junit.jupiter;
    exports org.mockito.internal.util to
            org.mockito.junit.jupiter;
}
