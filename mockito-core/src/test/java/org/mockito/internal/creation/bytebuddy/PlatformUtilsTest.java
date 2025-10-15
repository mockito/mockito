/*
 * Copyright (c) 2023 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import java.util.Properties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.parallel.Isolated;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Ashley Scopes
 */
@Isolated("modifies system properties temporarily")
class PlatformUtilsTest {
    Properties globalProperties;

    @BeforeEach
    void setUp() {
        globalProperties = new Properties();
        globalProperties.putAll(System.getProperties());
        System.getProperties().clear();
    }

    @AfterEach
    void tearDown() {
        System.getProperties().clear();
        System.getProperties().putAll(globalProperties);
    }

    @CsvSource(
            value = {
                "java.specification.vendor, expected result",
                "                  android,            true",
                "                  AnDrOId,            true",
                "             anythingElse,           false",
                "                         ,           false",
            },
            useHeadersInDisplayName = true)
    @ParameterizedTest
    void isAndroidPlatform_returns_expected_value(String vendor, boolean result) {
        // Given
        setProperty("java.specification.vendor", vendor);

        // Then
        assertThat(PlatformUtils.isAndroidPlatform()).isEqualTo(result);
    }

    @CsvSource(
            value = {
                "      os.name,                      java.home, expected result",
                "        linux, /data/data/com.termux/whatever,            true",
                "        linux,    /foo/bar/com.termux/somedir,            true",
                "        LINUX, /data/data/com.termux/whatever,            true",
                "        Linux,    /foo/bar/com.termux/somedir,            true",
                "Mac OS X 13.0,    /foo/bar/com.termux/somedir,           false",
                "   Windows 10,    /foo/bar/com.termux/somedir,           false",
                "         OS/2,    /foo/bar/com.termux/somedir,           false",
                "        Linux,                /usr/share/java,           false",
                "             ,                               ,           false",
                "          foo,                               ,           false",
                "             ,                            foo,           false",
                "        linux,                               ,           false",
                "             , /data/data/com.termux/whatever,           false",
            },
            useHeadersInDisplayName = true)
    @ParameterizedTest
    void isProbablyTermuxEnvironment_returns_expected_value(
            String os, String javaHome, boolean result) {
        // Given
        setProperty("os.name", os);
        setProperty("java.home", javaHome);

        // Then
        assertThat(PlatformUtils.isProbablyTermuxEnvironment()).isEqualTo(result);
    }

    static void setProperty(String name, String value) {
        if (value == null) {
            System.clearProperty(name);
        } else {
            System.setProperty(name, value);
        }
    }
}
