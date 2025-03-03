/*
 * Copyright (c) 2025 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
module org.mockito.junit.jupiter {
    requires transitive org.mockito;
    requires transitive org.junit.jupiter.api;

    exports org.mockito.junit.jupiter;
    exports org.mockito.junit.jupiter.resolver;
}
