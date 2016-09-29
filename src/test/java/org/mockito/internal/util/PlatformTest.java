package org.mockito.internal.util;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class PlatformTest {

    @Test
    public void const_are_initialized_from_system_properties() {
        assertThat(Platform.JVM_VERSION).isEqualTo(System.getProperty("java.runtime.version"));
        assertThat(Platform.JVM_INFO).isEqualTo(System.getProperty("java.vm.info"));
        assertThat(Platform.JVM_NAME).isEqualTo(System.getProperty("java.vm.name"));
        assertThat(Platform.JVM_VENDOR).isEqualTo(System.getProperty("java.vm.vendor"));
        assertThat(Platform.JVM_VENDOR_VERSION).isEqualTo(System.getProperty("java.vm.version"));
    }

    @Test
    public void should_parse_open_jdk_string() {
        // Given
        // Sources :
        //  - http://www.oracle.com/technetwork/java/javase/versioning-naming-139433.html
        //  - http://www.oracle.com/technetwork/java/javase/jdk7-naming-418744.html
        //  - http://www.oracle.com/technetwork/java/javase/jdk8-naming-2157130.html
        //  - http://stackoverflow.com/questions/35844985/how-do-we-get-sr-and-fp-of-ibm-jre-using-java
        //  - http://www.ibm.com/support/knowledgecenter/SSYKE2_6.0.0/com.ibm.java.doc.user.win32.60/user/java_version_check.html
        Map<String, Boolean> versions = new HashMap<String, Boolean>() {{
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
        }};

        assertPlatformParsesCorrectlyVariousVersionScheme(versions);
    }

    @Test
    public void should_parse_open_jdk9_string() {
        // The tested method targets Java 8 but should be able to parse other Java version numbers including Java 9

        // Given
        // Sources :
        //  - http://openjdk.java.net/jeps/223 (Java 9)
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
        Map<String, Boolean> versions = new HashMap<String, Boolean>() {{
            put("9-ea+73", false);
            put("9+100", false);
            put("9.1.2+62", false);
            put("9.0.1+20", false);
        }};

        assertPlatformParsesCorrectlyVariousVersionScheme(versions);
    }


    private void assertPlatformParsesCorrectlyVariousVersionScheme(Map<String, Boolean> versions) {
        for (Map.Entry<String, Boolean> version : versions.entrySet()) {
            assertThat(Platform.isJava8BelowUpdate45(version.getKey())).describedAs(version.getKey())
                                                                       .isEqualTo(version.getValue());
        }
    }
}
