/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

// Possible description on a IBM J9 VM (see #801)
//
// java.specification.version = 1.8
// java.vm.vendor = IBM Corporation
// java.vm.version = 2.8
// java.vm.name = IBM J9 VM
// java.runtime.version = pwa6480sr1fp10-20150711_01 (SR1 FP10)
// java.vm.info =
//   JRE 1.8.0 Windows 7 amd64-64 Compressed References 20150630_255633 (JIT enabled, AOT enabled)
//   J9VM - R28_jvm.28_20150630_1742_B255633
//   JIT  - tr.r14.java_20150625_95081.01
//   GC   - R28_jvm.28_20150630_1742_B255633_CMPRSS
//   J9CL - 20150630_255633
// os.name = Windows 7
// os.version = 6.1
public class PlatformTest {
    // TODO use ClassLoaders

    @Test
    public void const_are_initialized_from_system_properties() {
        System.out.println(Platform.describe());

        assertThat(Platform.JVM_VERSION).isEqualTo(System.getProperty("java.runtime.version"));
        assertThat(Platform.JVM_INFO).isEqualTo(System.getProperty("java.vm.info"));
        assertThat(Platform.JVM_NAME).isEqualTo(System.getProperty("java.vm.name"));
        assertThat(Platform.JVM_VENDOR).isEqualTo(System.getProperty("java.vm.vendor"));
        assertThat(Platform.JVM_VENDOR_VERSION).isEqualTo(System.getProperty("java.vm.version"));
    }

    @Test
    public void should_warn_for_jvm() throws Exception {
        assertThat(
                        Platform.warnForVM(
                                "Java HotSpot(TM) 64-Bit Server VM",
                                "HotSpot",
                                "hotspot warning",
                                "IBM",
                                "ibm warning"))
                .isEqualTo("hotspot warning");
        assertThat(
                        Platform.warnForVM(
                                "IBM J9 VM", "HotSpot", "hotspot warning", "IBM", "ibm warning"))
                .isEqualTo("ibm warning");
        assertThat(
                        Platform.warnForVM(
                                "whatever",
                                null,
                                "should not be returned",
                                null,
                                "should not be returned"))
                .isEqualTo("");
    }

    @Test
    public void should_parse_open_jdk_string_and_report_wether_below_or_nut_update_45() {
        // Given
        // Sources :
        //  - https://www.oracle.com/java/technologies/javase/versioning-naming.html
        //  - https://www.oracle.com/java/technologies/javase/jdk7-naming.html
        //  - https://www.oracle.com/java/technologies/javase/jdk8-naming.html
        //  -
        // https://stackoverflow.com/questions/35844985/how-do-we-get-sr-and-fp-of-ibm-jre-using-java
        //  -
        // https://www.ibm.com/support/knowledgecenter/SSYKE2_8.0.0/com.ibm.java.80.doc/user/build_number.html
        Map<String, Boolean> versions =
                new HashMap<String, Boolean>() {
                    {
                        put("1.8.0_92-b14", false);
                        put("1.8.0-b24", true);
                        put("1.8.0_5", true);
                        put("1.8.0b5_u44", true);
                        put("1.8.0b5_u92", false);
                        put("1.7.0_4", false);
                        put("1.4.0_03-b04", false);
                        put("1.4.0_03-ea-b01", false);
                        put("pxi3270_27sr4-20160303_03 (SR4)", false);
                        put("pwi3260sr11-20120412_01 (SR11)", false);
                        put("pwa6480sr1fp10-20150711_01 (SR1 FP10)", false);
                        put("null", false);
                    }
                };

        assertPlatformParsesCorrectlyVariousVersionScheme(versions);
    }

    @Test
    public void should_parse_open_jdk9_string() {
        // The tested method targets Java 8 but should be able to parse other Java version numbers
        // including Java 9

        // Given
        // Sources :
        //  - https://openjdk.java.net/jeps/223 (Java 9)
        //
        // System Property                 Existing      Proposed
        // ------------------------------- ------------  --------
        // Early Access
        // java.runtime.version            1.9.0-ea-b73  9-ea+73
        // java.vm.version                 1.9.0-ea-b73  9-ea+73
        // java.specification.version      1.9           9
        // java.vm.specification.version   1.9           9
        //
        // Major (GA)
        // java.version                    1.9.0         9
        // java.runtime.version            1.9.0-b100    9+100
        // java.vm.version                 1.9.0-b100    9+100
        // java.specification.version      1.9           9
        // java.vm.specification.version   1.9           9
        //
        // Minor #1 (GA)
        // java.version                    1.9.0_20      9.1.2
        // java.runtime.version            1.9.0_20-b62  9.1.2+62
        // java.vm.version                 1.9.0_20-b62  9.1.2+62
        // java.specification.version      1.9           9
        // java.vm.specification.version   1.9           9
        //
        // Security #1 (GA)
        // java.version                    1.9.0_5       9.0.1
        // java.runtime.version            1.9.0_5-b20   9.0.1+20
        // java.vm.version                 1.9.0_5-b20   9.0.1+20
        // java.specification.version      1.9           9
        // java.vm.specification.version   1.9           9
        //
        Map<String, Boolean> versions =
                new HashMap<String, Boolean>() {
                    {
                        put("9-ea+73", false);
                        put("9+100", false);
                        put("9.1.2+62", false);
                        put("9.0.1+20", false);
                    }
                };

        assertPlatformParsesCorrectlyVariousVersionScheme(versions);
    }

    private void assertPlatformParsesCorrectlyVariousVersionScheme(Map<String, Boolean> versions) {
        for (Map.Entry<String, Boolean> version : versions.entrySet()) {
            assertThat(Platform.isJava8BelowUpdate45(version.getKey()))
                    .describedAs(version.getKey())
                    .isEqualTo(version.getValue());
        }
    }
}
