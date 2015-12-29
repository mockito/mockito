package org.mockito.release.comparison

import spock.lang.Specification

class PomComparatorTest extends Specification {

    def "compares poms"() {
        def pom = """<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <modelVersion>4.0.0</modelVersion>
  <groupId>org.mockito</groupId>
  <artifactId>mockito-core</artifactId>
  <version>1.10.11</version>
  <dependencies>
    <dependency>
      <groupId>org.hamcrest</groupId>
      <artifactId>hamcrest-core</artifactId>
      <version>1.1</version>
      <scope>runtime</scope>
    </dependency>
  </dependencies>
</project>"""

        def differentVersion = """<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <modelVersion>4.0.0</modelVersion>
  <groupId>org.mockito</groupId>
  <artifactId>mockito-core</artifactId>
  <version>1.10.12</version>
  <dependencies>
    <dependency>
      <groupId>org.hamcrest</groupId>
      <artifactId>hamcrest-core</artifactId>
      <version>1.1</version>
      <scope>runtime</scope>
    </dependency>
  </dependencies>
</project>"""

        def differentDependencyVersion = """<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <modelVersion>4.0.0</modelVersion>
  <groupId>org.mockito</groupId>
  <artifactId>mockito-core</artifactId>
  <version>1.10.12</version>
  <dependencies>
    <dependency>
      <groupId>org.hamcrest</groupId>
      <artifactId>hamcrest-core</artifactId>
      <version>1.2</version>
      <scope>runtime</scope>
    </dependency>
  </dependencies>
</project>"""

        expect:
        new PomComparator().setPair({pom}, {pom}).areEqual()
        new PomComparator().setPair({pom}, {differentVersion}).areEqual()
        !new PomComparator().setPair({pom}, {differentDependencyVersion}).areEqual()
    }

    def "does not allow null content"() {
        when:
        new PomComparator().setPair({null}, {null}).areEqual()
        then:
        thrown(IllegalArgumentException)
    }
}
