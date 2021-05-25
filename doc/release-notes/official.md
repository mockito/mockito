# Deprecated

As of Oct 2020, this file is no longer updated.
The most current Mockito changelog is automatically generated to [GitHub Releases](https://github.com/mockito/mockito/releases).
The generation is implemented using https://shipkit.org.

# Old release notes

#### 3.6.0
 - 2020-10-27 - [7 commits](https://github.com/mockito/mockito/compare/v3.5.15...v3.6.0) by [Szczepan Faber](https://github.com/mockitoguy) (4), [shipkit-org](https://github.com/shipkit-org) (2), [Tim van der Lippe](https://github.com/TimvdLippe) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.6.0-green.svg)](https://bintray.com/mockito/maven/mockito/3.6.0)
 - Retry the release [(#2078)](https://github.com/mockito/mockito/pull/2078)
 - Retry 3.6.0 release [(#2077)](https://github.com/mockito/mockito/pull/2077)

#### 3.5.15
 - 2020-10-19 - [4 commits](https://github.com/mockito/mockito/compare/v3.5.14...v3.5.15) by [Rafael Winterhalter](https://github.com/raphw) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.5.15-green.svg)](https://bintray.com/mockito/maven/mockito/3.5.15)
 - Mock resolver plugin [(#2042)](https://github.com/mockito/mockito/pull/2042)

#### 3.5.14
 - 2020-10-17 - [2 commits](https://github.com/mockito/mockito/compare/v3.5.13...v3.5.14) by [Marcono1234](https://github.com/Marcono1234) (1), [shestee](https://github.com/shestee) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.5.14-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.5.14)
 - Fixes #2061: ArgumentMatcher error messages use lambda class names [(#2071)](https://github.com/mockito/mockito/pull/2071)
 - Fixed typo in osgi.gradle [(#2070)](https://github.com/mockito/mockito/pull/2070)
 - Lambda used as ArgumentMatcher causes decamelized lambda name to appear in error message [(#2061)](https://github.com/mockito/mockito/issues/2061)

#### 3.5.13
 - 2020-09-24 - [1 commit](https://github.com/mockito/mockito/compare/v3.5.12...v3.5.13) by [Sinan Kozak](https://github.com/kozaxinan) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.5.13-green.svg)](https://bintray.com/mockito/maven/mockito/3.5.13)
 - Use single version for strictly in mockito-android [(#2053)](https://github.com/mockito/mockito/pull/2053)

#### 3.5.12
 - 2020-09-18 - [1 commit](https://github.com/mockito/mockito/compare/v3.5.11...v3.5.12) by [Rafael Winterhalter](https://github.com/raphw) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.5.12-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.5.12)
 - Update Byte Buddy. [(#2050)](https://github.com/mockito/mockito/pull/2050)

#### 3.5.11
 - 2020-09-17 - [2 commits](https://github.com/mockito/mockito/compare/v3.5.10...v3.5.11) by [Rafael Winterhalter](https://github.com/raphw) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.5.11-green.svg)](https://bintray.com/mockito/maven/mockito/3.5.11)
 - Do not exclude synthetic constructors from instrumentation. Fixes #2040. [(#2046)](https://github.com/mockito/mockito/pull/2046)
 - Mockito.spy(Activity).getBaseContext() returns null on Robolectric 4.4 and Java8 [(#2040)](https://github.com/mockito/mockito/issues/2040)

#### 3.5.10
 - 2020-09-03 - [2 commits](https://github.com/mockito/mockito/compare/v3.5.9...v3.5.10) by [Rafael Winterhalter](https://github.com/raphw) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.5.10-green.svg)](https://bintray.com/mockito/maven/mockito/3.5.10)
 - Escape mock during method dispatch on mock to avoid premature garbage collection. [(#2034)](https://github.com/mockito/mockito/pull/2034)
 - Exception "The mock object was garbage collected." [(#1802)](https://github.com/mockito/mockito/issues/1802)

#### 3.5.9
 - 2020-09-01 - [1 commit](https://github.com/mockito/mockito/compare/v3.5.8...v3.5.9) by [Sinan Kozak](https://github.com/kozaxinan) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.5.9-green.svg)](https://bintray.com/mockito/maven/mockito/3.5.9)
 - Fixes #2007 : Downgrade objenesis version for mockito-android [(#2024)](https://github.com/mockito/mockito/pull/2024)
 - Android instrumentation test packaging fails for mockito-android 3.5.0 with minSdk < 26 [(#2007)](https://github.com/mockito/mockito/issues/2007)

#### 3.5.8
 - 2020-08-27 - [1 commit](https://github.com/mockito/mockito/compare/v3.5.7...v3.5.8) by [ahmadmoawad](https://github.com/ahmadmoawad) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.5.8-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.5.8)
 - Fix typo in CONTRIBUTING.md and SpyOnInjectedFieldsHandler [(#1994)](https://github.com/mockito/mockito/pull/1994)

#### 3.5.7
 - 2020-08-25 - [2 commits](https://github.com/mockito/mockito/compare/v3.5.6...v3.5.7) by [Rafael Winterhalter](https://github.com/raphw) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.5.7-green.svg)](https://bintray.com/mockito/maven/mockito/3.5.7)
 - Initializes classes prior to instrumentation to avoid uncontrolled code execution. [(#2023)](https://github.com/mockito/mockito/pull/2023)
 - Stackoverflow error when upgrading to v3.5.2 [(#2011)](https://github.com/mockito/mockito/issues/2011)

#### 3.5.6
 - 2020-08-24 - [5 commits](https://github.com/mockito/mockito/compare/v3.5.5...v3.5.6) by [Rafael Winterhalter](https://github.com/raphw) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.5.6-green.svg)](https://bintray.com/mockito/maven/mockito/3.5.6)
 - Only apply argument on illegal module access for inline tests if Java version is at least 9. [(#2022)](https://github.com/mockito/mockito/pull/2022)
 - Constructor dispatch [(#2021)](https://github.com/mockito/mockito/pull/2021)

#### 3.5.5
 - 2020-08-22 - [3 commits](https://github.com/mockito/mockito/compare/v3.5.4...v3.5.5) by [Rafael Winterhalter](https://github.com/raphw) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.5.5-green.svg)](https://bintray.com/mockito/maven/mockito/3.5.5)
 - Constructor dispatch [(#2020)](https://github.com/mockito/mockito/pull/2020)

#### 3.5.4
 - 2020-08-21 - [2 commits](https://github.com/mockito/mockito/compare/v3.5.3...v3.5.4) by [Rafael Winterhalter](https://github.com/raphw) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.5.4-green.svg)](https://bintray.com/mockito/maven/mockito/3.5.4)
 - Only enable mocking of types right before instantiation to avoid circular interception of constructor creation. [(#2017)](https://github.com/mockito/mockito/pull/2017)

#### 3.5.3
 - 2020-08-20 - [3 commits](https://github.com/mockito/mockito/compare/v3.5.2...v3.5.3) by [Rafael Winterhalter](https://github.com/raphw) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.5.3-green.svg)](https://bintray.com/mockito/maven/mockito/3.5.3)
 - [ci maven-central-release] Constructor dispatch [(#2013)](https://github.com/mockito/mockito/pull/2013)
 - Constructor not called when using mockito-inline (3.5.x) [(#2012)](https://github.com/mockito/mockito/issues/2012)

#### 3.5.2
 - 2020-08-18 - [1 commit](https://github.com/mockito/mockito/compare/v3.5.1...v3.5.2) by [Tim van der Lippe](https://github.com/TimvdLippe) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.5.2-green.svg)](https://bintray.com/mockito/maven/mockito/3.5.2)
 - No pull requests referenced in commit messages.

#### 3.5.1
 - 2020-08-18 - [3 commits](https://github.com/mockito/mockito/compare/v3.5.0...v3.5.1) by [Rafael Winterhalter](https://github.com/raphw) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.5.1-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.5.1)
 - Introduce animal sniffer [(#2006)](https://github.com/mockito/mockito/pull/2006)

#### 3.5.0
 - 2020-08-15 - [9 commits](https://github.com/mockito/mockito/compare/v3.4.8...v3.5.0) by [Rafael Winterhalter](https://github.com/raphw) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.5.0-green.svg)](https://bintray.com/mockito/maven/mockito/3.5.0)
 - Pre release 3.5.0 [(#2004)](https://github.com/mockito/mockito/pull/2004)

#### 3.4.8
 - 2020-08-09 - [1 commit](https://github.com/mockito/mockito/compare/v3.4.7...v3.4.8) by [Erhard Pointl](https://github.com/epeee) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.4.8-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.4.8)
 - Update objenesis to 3.1 [(#1998)](https://github.com/mockito/mockito/pull/1998)

#### 3.4.7
 - 2020-08-05 - [1 commit](https://github.com/mockito/mockito/compare/v3.4.6...v3.4.7) by [Per Lundberg](https://github.com/perlun) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.4.7-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.4.7)
 - Mockito.verify(): fix typo in Javadoc [(#1991)](https://github.com/mockito/mockito/pull/1991)

#### 3.4.6
 - 2020-07-29 - [3 commits](https://github.com/mockito/mockito/compare/v3.4.5...v3.4.6) by [Rafael Winterhalter](https://github.com/raphw) (2), [Valery Yatsynovich](https://github.com/valfirst) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.4.6-green.svg)](https://bintray.com/mockito/maven/mockito/3.4.6)
 - [Bugfixes] Do not pass static mocks to regular listener callback. [(#1989)](https://github.com/mockito/mockito/pull/1989)
 - MockitoJUnitRunner causes NPE when using @Mock on MockedStatic fields [(#1988)](https://github.com/mockito/mockito/issues/1988)
 - Fixes #1985 : Update README to refer the latest documentation [(#1986)](https://github.com/mockito/mockito/pull/1986)
 - README should refer the latest available documentation [(#1985)](https://github.com/mockito/mockito/issues/1985)

#### 3.4.5
 - 2020-07-20 - [2 commits](https://github.com/mockito/mockito/compare/v3.4.4...v3.4.5) by [Erhard Pointl](https://github.com/epeee) (1), [Johnny Lim](https://github.com/izeye) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.4.5-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.4.5)
 - Fix typo [(#1984)](https://github.com/mockito/mockito/pull/1984)
 - Update assertj to 3.16.1 [(#1982)](https://github.com/mockito/mockito/pull/1982)

#### 3.4.4
 - 2020-07-18 - [2 commits](https://github.com/mockito/mockito/compare/v3.4.3...v3.4.4) by [Rafael Winterhalter](https://github.com/raphw) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.4.4-green.svg)](https://bintray.com/mockito/maven/mockito/3.4.4)
 - Fixes #1855 and #939: improve error message when the inline mock maker cannot be used. [(#1974)](https://github.com/mockito/mockito/pull/1974)
 - javax.tools.ToolProvider could not be found in InlineByteBuddyMockMaker [(#1855)](https://github.com/mockito/mockito/issues/1855)

#### 3.4.3
 - 2020-07-17 - [1 commit](https://github.com/mockito/mockito/compare/v3.4.2...v3.4.3) by [Robert Chmielowiec](https://github.com/chmielowiec) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.4.3-green.svg)](https://bintray.com/mockito/maven/mockito/3.4.3)
 - Fix Javadoc invalid syntax [(#1978)](https://github.com/mockito/mockito/pull/1978)
 - Broken documentation [(#1977)](https://github.com/mockito/mockito/issues/1977)

#### 3.4.2
 - 2020-07-16 - [2 commits](https://github.com/mockito/mockito/compare/v3.4.1...v3.4.2) by [Rafael Winterhalter](https://github.com/raphw) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.4.2-green.svg)](https://bintray.com/mockito/maven/mockito/3.4.2)
 - Fixes #1967: Correctly handle mocks with limited life-cycle in listeners. [(#1968)](https://github.com/mockito/mockito/pull/1968)
 - Static method mocks incompatible with MockitoExtension (NotAMockException) [(#1967)](https://github.com/mockito/mockito/issues/1967)

#### 3.4.1
 - 2020-07-15 - [1 commit](https://github.com/mockito/mockito/compare/v3.4.0...v3.4.1) by [mickroll](https://github.com/mickroll) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.4.1-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.4.1)
 - update dependency to byte buddy version 1.10.13 [(#1973)](https://github.com/mockito/mockito/pull/1973)
 - Update dependency to byte buddy version 1.10.13 [(#1972)](https://github.com/mockito/mockito/issues/1972)

#### 3.4.0
 - 2020-07-10 - [19 commits](https://github.com/mockito/mockito/compare/v3.3.12...v3.4.0) by 9 authors - published to [![Bintray](https://img.shields.io/badge/Bintray-3.4.0-green.svg)](https://bintray.com/mockito/maven/mockito/3.4.0)
 - Commits: [Tim van der Lippe](https://github.com/TimvdLippe) (5), [Erhard Pointl](https://github.com/epeee) (4), [Rafael Winterhalter](https://github.com/raphw) (3), [Eitan Adler](https://github.com/grimreaper) (2), adrianriley (1), akluball (1), [Artem Prigoda](https://github.com/arteam) (1), [Jamie Tanna](https://github.com/jamietanna) (1), [Naoki Takezoe](https://github.com/takezoe) (1)
 - [Android support] Enable mocking static methods in Mockito [(#1013)](https://github.com/mockito/mockito/issues/1013)
 - Document using `@Mock` with method parameters [(#1961)](https://github.com/mockito/mockito/pull/1961)
 - Documentation: `@Mock` on method parameters [(#1960)](https://github.com/mockito/mockito/issues/1960)
 - Update errorprone gradle plugin to v1.2.1 [(#1958)](https://github.com/mockito/mockito/pull/1958)
 - Update spotless Travis job name to be more descriptive [(#1957)](https://github.com/mockito/mockito/pull/1957)
 - Fix a confusing typo in subclassing error message [(#1953)](https://github.com/mockito/mockito/pull/1953)
 - Update bnd gradle plugin to v5.1.1 [(#1952)](https://github.com/mockito/mockito/pull/1952)
 - Use errorprone 2.4.0 [(#1951)](https://github.com/mockito/mockito/pull/1951)
 - Use jacoco v0.8.5 [(#1950)](https://github.com/mockito/mockito/pull/1950)
 - Fixes #1712 : prepend description to AssertionError thrown in verification [(#1949)](https://github.com/mockito/mockito/pull/1949)
 - Update gradle 6 [(#1948)](https://github.com/mockito/mockito/pull/1948)
 - Move spotless check to separate build task [(#1946)](https://github.com/mockito/mockito/pull/1946)
 - [Travis] Replace JDK 9/10 with 14 [(#1945)](https://github.com/mockito/mockito/pull/1945)
 - Fixes #1898 : Return mock name from toString method for deep stub mocks [(#1942)](https://github.com/mockito/mockito/pull/1942)
 - [checkstyle] switch to new DTD [(#1940)](https://github.com/mockito/mockito/pull/1940)
 - Use google-java-format in spotless [(#1934)](https://github.com/mockito/mockito/pull/1934)
 - Update report message to use any() instead of anyObject() [(#1931)](https://github.com/mockito/mockito/pull/1931)
 - [build] bump gradle to latest 5.x release [(#1923)](https://github.com/mockito/mockito/pull/1923)
 - [build] update gradle-errorprone-plugin to 1.1.0 [(#1908)](https://github.com/mockito/mockito/pull/1908)
 - RETURNS_DEEP_STUBS override a mock's toString to `null` [(#1898)](https://github.com/mockito/mockito/issues/1898)
 - "description" not printing when verify args don't match [(#1712)](https://github.com/mockito/mockito/issues/1712)

#### 3.3.12
 - 2020-05-25 - [1 commit](https://github.com/mockito/mockito/compare/v3.3.11...v3.3.12) by [Vinicius Scheidegger](https://github.com/vinischeidegger) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.3.12-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.3.12)
 - Update javadoc - remove deprecated class [(#1938)](https://github.com/mockito/mockito/pull/1938)

#### 3.3.11
 - 2020-05-14 - [5 commits](https://github.com/mockito/mockito/compare/v3.3.10...v3.3.11) by [Andrei Silviu Dragnea](https://github.com/andreisilviudragnea) (2), [Szczepan Faber](https://github.com/mockitoguy) (2), [Eitan Adler](https://github.com/grimreaper) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.3.11-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.3.11)
 - JUnit 5 strict stubs check should not suppress the regular test failure [(#1928)](https://github.com/mockito/mockito/pull/1928)
 - Fix import order [(#1927)](https://github.com/mockito/mockito/pull/1927)
 - [build] add ben-manes dependency upgrade finder [(#1922)](https://github.com/mockito/mockito/pull/1922)

#### 3.3.10
 - 2020-04-30 - [1 commit](https://github.com/mockito/mockito/compare/v3.3.9...v3.3.10) by [netbeansuser2019](https://github.com/netbeansuser2019) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.3.10-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.3.10)
 - Update dependencies.gradle [(#1920)](https://github.com/mockito/mockito/pull/1920)

#### 3.3.9
 - 2020-04-20 - [1 commit](https://github.com/mockito/mockito/compare/v3.3.8...v3.3.9) by [dean-burdaky](https://github.com/dean-burdaky) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.3.9-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.3.9)
 - Fix Pattern matcher not matching to subregion [(#1914)](https://github.com/mockito/mockito/pull/1914)
 - ArgumentMatchers.matches not working [(#1905)](https://github.com/mockito/mockito/issues/1905)

#### 3.3.8
 - 2020-04-15 - [2 commits](https://github.com/mockito/mockito/compare/v3.3.7...v3.3.8) by [Eitan Adler](https://github.com/grimreaper) (1), [NanjanChung](https://github.com/NanjanChung) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.3.8-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.3.8)
 - Fixes #1910: update description of ArgumentMatcher javadoc [(#1911)](https://github.com/mockito/mockito/pull/1911)
 - Documentation of ArgumentMatchers any() is confusing [(#1910)](https://github.com/mockito/mockito/issues/1910)
 - [tests] use ArgumentMatchers over Matchers [(#1907)](https://github.com/mockito/mockito/pull/1907)

#### 3.3.7
 - 2020-04-12 - [1 commit](https://github.com/mockito/mockito/compare/v3.3.6...v3.3.7) by [Eitan Adler](https://github.com/grimreaper) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.3.7-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.3.7)
 - [documentation] change deprecated warnings from 3.x -> 4.x [(#1906)](https://github.com/mockito/mockito/pull/1906)

#### 3.3.6
 - 2020-03-25 - [3 commits](https://github.com/mockito/mockito/compare/v3.3.5...v3.3.6) by [Marcin Mikołajczyk](https://github.com/mikolajczykmarcin) (2), [Alex Wilson](https://github.com/mrwilson) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.3.6-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.3.6)
 - Feature/returns empty java8 time [(#1896)](https://github.com/mockito/mockito/pull/1896)
 - Fixes #1894 checkstyle error on windows [(#1895)](https://github.com/mockito/mockito/pull/1895)
 - Checkstyle error on windows [(#1894)](https://github.com/mockito/mockito/issues/1894)
 - Make JARs build reproducibly [(#1892)](https://github.com/mockito/mockito/pull/1892)
 - Build is not reproducible [(#1891)](https://github.com/mockito/mockito/issues/1891)
 - Add java.time types to ReturnsEmptyValues [(#1885)](https://github.com/mockito/mockito/issues/1885)

#### 3.3.5
 - 2020-03-23 - [1 commit](https://github.com/mockito/mockito/compare/v3.3.4...v3.3.5) by [Shyam Sundar J](https://github.com/severussundar) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.3.5-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.3.5)
 - Fixes #id : 1888 [(#1893)](https://github.com/mockito/mockito/pull/1893)
 - Documentation typo [(#1888)](https://github.com/mockito/mockito/issues/1888)

#### 3.3.4
 - 2020-03-21 - [1 commit](https://github.com/mockito/mockito/compare/v3.3.3...v3.3.4) by [dean-burdaky](https://github.com/dean-burdaky) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.3.4-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.3.4)
 - Fix mocks throwing same instance with throwable class [(#1890)](https://github.com/mockito/mockito/pull/1890)
 - thenThrow(Class) no longer creates new instances [(#1875)](https://github.com/mockito/mockito/issues/1875)

#### 3.3.3
 - 2020-03-13 - [1 commit](https://github.com/mockito/mockito/compare/v3.3.2...v3.3.3) by [Tim van der Lippe](https://github.com/TimvdLippe) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.3.3-green.svg)](https://bintray.com/mockito/maven/mockito/3.3.3)
 - No pull requests referenced in commit messages.

#### 3.3.2
 - 2020-02-28 - [1 commit](https://github.com/mockito/mockito/compare/v3.3.1...v3.3.2) by [Jean-Michel Leclercq](https://github.com/LeJeanbono) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.3.2-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.3.2)
 - Fix UnnecessaryStubbingException javadoc [(#1881)](https://github.com/mockito/mockito/pull/1881)

#### 3.3.1
 - 2020-02-26 - [1 commit](https://github.com/mockito/mockito/compare/v3.3.0...v3.3.1) by [Tim van der Lippe](https://github.com/TimvdLippe) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.3.1-green.svg)](https://bintray.com/mockito/maven/mockito/3.3.1)
 - Revert "Fixed undetected unused stubbing when matching previous stubbed call" [(#1878)](https://github.com/mockito/mockito/pull/1878)

#### 3.3.0
 - 2020-02-21 - [1 commit](https://github.com/mockito/mockito/compare/v3.2.11...v3.3.0) by [Tim van der Lippe](https://github.com/TimvdLippe) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.3.0-green.svg)](https://bintray.com/mockito/maven/mockito/3.3.0)
 - No pull requests referenced in commit messages.

#### 3.2.11
 - 2020-02-06 - [1 commit](https://github.com/mockito/mockito/compare/v3.2.10...v3.2.11) by [Tim van der Lippe](https://github.com/TimvdLippe) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.2.11-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.2.11)
 - Add TestRule to MockitoJUnit [(#1858)](https://github.com/mockito/mockito/pull/1858)

#### 3.2.10
 - 2020-01-31 - [2 commits](https://github.com/mockito/mockito/compare/v3.2.9...v3.2.10) by [Stefano Cordio](https://github.com/scordio) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.2.10-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.2.10)
 - Update Mockito version number for deletion of deprecated classes [(#1868)](https://github.com/mockito/mockito/pull/1868)
 - Upgrade assertj-core to version 3.15.0 [(#1867)](https://github.com/mockito/mockito/pull/1867)

#### 3.2.9
 - 2020-01-27 - [3 commits](https://github.com/mockito/mockito/compare/v3.2.8...v3.2.9) by [Brice Dutheil](https://github.com/bric3) (2), [Tim van der Lippe](https://github.com/TimvdLippe) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.2.9-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.2.9)
 - Adds the Official Gradle Wrapper Validation GitHub Action [(#1863)](https://github.com/mockito/mockito/pull/1863)
 - Revert "Performance optimization by using Method.getParameterCount() where possible" [(#1862)](https://github.com/mockito/mockito/pull/1862)

#### 3.2.8
 - 2020-01-15 - [2 commits](https://github.com/mockito/mockito/compare/v3.2.7...v3.2.8) by [Szczepan Faber](https://github.com/mockitoguy) (1), [Tim van der Lippe](https://github.com/TimvdLippe) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.2.8-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.2.8)
 - Add NotExtensible to OngoingStubbing [(#1859)](https://github.com/mockito/mockito/pull/1859)

#### 3.2.7
 - 2020-01-02 - [1 commit](https://github.com/mockito/mockito/compare/v3.2.6...v3.2.7) by [Fabian Mendez](https://github.com/fabianMendez) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.2.7-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.2.7)
 - Fixes #1853: Allow @MockitoSettings to be inherited [(#1854)](https://github.com/mockito/mockito/pull/1854)
 - Allow @MockitoSettings to be inherited [(#1853)](https://github.com/mockito/mockito/issues/1853)

#### 3.2.6
 - 2019-12-31 - [2 commits](https://github.com/mockito/mockito/compare/v3.2.5...v3.2.6) by [Christoph Dreis](https://github.com/dreis2211) (1), [Szczepan Faber](https://github.com/mockitoguy) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.2.6-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.2.6)
 - Performance optimization by using Method.getParameterCount() where possible [(#1849)](https://github.com/mockito/mockito/pull/1849)
 - Use Method.getParameterCount() where possible [(#1848)](https://github.com/mockito/mockito/issues/1848)

#### 3.2.5
 - 2019-12-31 - [4 commits](https://github.com/mockito/mockito/compare/v3.2.4...v3.2.5) by [Szczepan Faber](https://github.com/mockitoguy) (2), Andrei Silviu Dragnea (1), theogimonde (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.2.5-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.2.5)
 - Fixes  #522 : Basic test suite for OSGi framework [(#1850)](https://github.com/mockito/mockito/pull/1850)
 - Fixed undetected unused stubbing when matching previous stubbed call [(#1847)](https://github.com/mockito/mockito/pull/1847)
 - Broken unused stubbing reporting when matching previous stubbed call [(#1846)](https://github.com/mockito/mockito/issues/1846)
 - Provide OSGi test project to validate OSGi headers automatically. [(#522)](https://github.com/mockito/mockito/issues/522)

#### 3.2.4
 - 2019-12-16 - [1 commit](https://github.com/mockito/mockito/compare/v3.2.3...v3.2.4) by [Tim van der Lippe](https://github.com/TimvdLippe) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.2.4-green.svg)](https://bintray.com/mockito/maven/mockito/3.2.4)
 - No pull requests referenced in commit messages.

#### 3.2.3
 - 2019-12-16 - [1 commit](https://github.com/mockito/mockito/compare/v3.2.2...v3.2.3) by [Tim van der Lippe](https://github.com/TimvdLippe) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.2.3-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.2.3)
 - Revert "Add Duration overloads. Fixes #1815" [(#1845)](https://github.com/mockito/mockito/pull/1845)
 - Add Duration overloads. Fixes #1815 [(#1818)](https://github.com/mockito/mockito/pull/1818)

#### 3.2.2
 - 2019-12-12 - [5 commits](https://github.com/mockito/mockito/compare/v3.2.0...v3.2.2) by 4 authors - published to [![Bintray](https://img.shields.io/badge/Bintray-3.2.2-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.2.2)
 - Commits: [Tim van der Lippe](https://github.com/TimvdLippe) (2), [Dr. Thomas Tautenhahn](https://github.com/DrTTom) (1), [Pavel Roskin](https://github.com/proski) (1), [Ville Saukkonen](https://github.com/villesau) (1)
 - Upgrade byte buddy to 1.10.5 [(#1842)](https://github.com/mockito/mockito/pull/1842)
 - Update byte-buddy to 1.10.5 [(#1841)](https://github.com/mockito/mockito/issues/1841)
 - Fixes #1839 : Badge improvements in README.md [(#1840)](https://github.com/mockito/mockito/pull/1840)
 - Bintray badge is out of date [(#1839)](https://github.com/mockito/mockito/issues/1839)
 - Add matchers for incompatible type matchers [(#1832)](https://github.com/mockito/mockito/pull/1832)
 - Typos and upcoming warnings [(#1795)](https://github.com/mockito/mockito/pull/1795)

#### 3.2.0
 - 2019-11-29 - [1 commit](https://github.com/mockito/mockito/compare/v3.1.13...v3.2.0) by [Tim van der Lippe](https://github.com/TimvdLippe) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.2.0-green.svg)](https://bintray.com/mockito/maven/mockito/3.2.0)
 - No pull requests referenced in commit messages.

#### 3.1.13
 - 2019-11-26 - [1 commit](https://github.com/mockito/mockito/compare/v3.1.12...v3.1.13) by [Dmitry Timofeev](https://github.com/dmitry-timofeev) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.1.13-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.1.13)
 - Upgrade byte buddy to 1.10.3 [(#1828)](https://github.com/mockito/mockito/pull/1828)

#### 3.1.12
 - 2019-11-20 - [1 commit](https://github.com/mockito/mockito/compare/v3.1.11...v3.1.12) by [Tim van der Lippe](https://github.com/TimvdLippe) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.1.12-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.1.12)
 - Deprecate MockitoHamcrest [(#1819)](https://github.com/mockito/mockito/pull/1819)

#### 3.1.11
 - 2019-11-18 - [1 commit](https://github.com/mockito/mockito/compare/v3.1.10...v3.1.11) by [Tim van der Lippe](https://github.com/TimvdLippe) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.1.11-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.1.11)
 - Deprecate MockitoHamcrest [(#1819)](https://github.com/mockito/mockito/pull/1819)

#### 3.1.10
 - 2019-11-13 - [2 commits](https://github.com/mockito/mockito/compare/v3.1.9...v3.1.10) by [Brice Dutheil](https://github.com/bric3) (1), [Serż Kwiatkowski](https://github.com/scadgek) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.1.10-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.1.10)
 - fix a word mistake [(#1823)](https://github.com/mockito/mockito/pull/1823)

#### 3.1.9
 - 2019-11-10 - [1 commit](https://github.com/mockito/mockito/compare/v3.1.8...v3.1.9) by [Marc Philipp](https://github.com/marcphilipp) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.1.9-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.1.9)
 - Guard against multiple evaluations of before statement [(#1821)](https://github.com/mockito/mockito/pull/1821)
 - 'MismatchReportingTestListener' has already been added and not removed using MockitoJUnitRunner [(#1767)](https://github.com/mockito/mockito/issues/1767)
 - Report initialization failures per test method [(#1672)](https://github.com/mockito/mockito/pull/1672)

#### 3.1.8
 - 2019-11-07 - [1 commit](https://github.com/mockito/mockito/compare/v3.1.7...v3.1.8) by [Tim van der Lippe](https://github.com/TimvdLippe) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.1.8-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.1.8)
 - Indent with spaces using spotless [(#1820)](https://github.com/mockito/mockito/pull/1820)

#### 3.1.7
 - 2019-11-07 - [1 commit](https://github.com/mockito/mockito/compare/v3.1.6...v3.1.7) by [Kurt Alfred Kluever](https://github.com/kluever) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.1.7-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.1.7)
 - Add Duration overloads. Fixes #1815 [(#1818)](https://github.com/mockito/mockito/pull/1818)
 - FR: add Mockito.timeout(java.time.Duration) and after(java.time.Duration) [(#1815)](https://github.com/mockito/mockito/issues/1815)

#### 3.1.6
 - 2019-11-06 - [1 commit](https://github.com/mockito/mockito/compare/v3.1.5...v3.1.6) by [diguage](https://github.com/diguage) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.1.6-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.1.6)
 - fix document error [(#1816)](https://github.com/mockito/mockito/pull/1816)

#### 3.1.5
 - 2019-10-31 - [1 commit](https://github.com/mockito/mockito/compare/v3.1.4...v3.1.5) by [Tim van der Lippe](https://github.com/TimvdLippe) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.1.5-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.1.5)
 - Apply consistent import ordering with spotless [(#1811)](https://github.com/mockito/mockito/pull/1811)

#### 3.1.4
 - 2019-10-31 - [4 commits](https://github.com/mockito/mockito/compare/v3.1.3...v3.1.4) by [Szczepan Faber](https://github.com/mockitoguy) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.1.4-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.1.4)
 - Fixed JUnit5 concurrency bug [(#1806)](https://github.com/mockito/mockito/pull/1806)
 - Added Tidelift info to README.md [(#1805)](https://github.com/mockito/mockito/pull/1805)

#### 3.1.3
 - 2019-10-29 - [7 commits](https://github.com/mockito/mockito/compare/v3.1.2...v3.1.3) by 5 authors - published to [![Bintray](https://img.shields.io/badge/Bintray-3.1.3-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.1.3)
 - Commits: [Tim van der Lippe](https://github.com/TimvdLippe) (2), [Tokuhiro Matsuno](https://github.com/tokuhirom) (2), [Federico Fissore](https://github.com/ffissore) (1), [Max Zhu](https://github.com/maxcellent) (1), [Szczepan Faber](https://github.com/mockitoguy) (1)
 - Fixes #1771: Upgrade byte-buddy to 1.10.2 (from 1.9.10) [(#1813)](https://github.com/mockito/mockito/pull/1813)
 - Revert "Fixes #1587 : Remove unnecessary loop from InjectingAnnotationEngine" [(#1810)](https://github.com/mockito/mockito/pull/1810)
 - update javadoc.io link from /page/ to /doc/, which gives an extra nav… [(#1809)](https://github.com/mockito/mockito/pull/1809)
 - Add default implementation for VerificationMode#description [(#1807)](https://github.com/mockito/mockito/pull/1807)
 - Fixes placeholder `3.x.x` version in javadoc with correct `3.0.1` [(#1799)](https://github.com/mockito/mockito/pull/1799)
 - Enable spotless to automatically format our source code [(#1790)](https://github.com/mockito/mockito/pull/1790)
 - Upgrade byte-buddy to a 1.10.x release [(#1771)](https://github.com/mockito/mockito/issues/1771)
 - Fixes #1587 : Remove unnecessary loop from InjectingAnnotationEngine [(#1588)](https://github.com/mockito/mockito/pull/1588)
 - InjectingAnnotationEngine does an unnecessary loop. [(#1587)](https://github.com/mockito/mockito/issues/1587)

#### 3.1.2
 - 2019-10-05 - [4 commits](https://github.com/mockito/mockito/compare/v3.1.1...v3.1.2) by [Szczepan Faber](https://github.com/mockitoguy) (2), [Dinesh Bolkensteyn](https://github.com/dbolkensteyn) (1), [Yusuf Kemal Özcan](https://github.com/yfklon) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.1.2-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.1.2)
 - Fix typo in the wiki link [(#1798)](https://github.com/mockito/mockito/pull/1798)
 - Delete dummy-commit.txt [(#1793)](https://github.com/mockito/mockito/pull/1793)
 - Typo in Mockito.java Javadoc: "verification" in wiki link [(#1791)](https://github.com/mockito/mockito/issues/1791)

#### 3.1.1
 - 2019-10-01 - [1 commit](https://github.com/mockito/mockito/compare/v3.1.0...v3.1.1) by [Michael Keppler](https://github.com/Bananeweizen) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.1.1-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.1.1)
 - Fix grammar [(#1792)](https://github.com/mockito/mockito/pull/1792)

#### 3.1.0
 - 2019-10-01 - [1 commit](https://github.com/mockito/mockito/compare/v3.0.12...v3.1.0) by [Tim van der Lippe](https://github.com/TimvdLippe) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.1.0-green.svg)](https://bintray.com/mockito/maven/mockito/3.1.0)
 - No pull requests referenced in commit messages.

#### 3.0.12
 - 2019-09-30 - [3 commits](https://github.com/mockito/mockito/compare/v3.0.11...v3.0.12) by [Szczepan Faber](https://github.com/mockitoguy) (2), [Tim van der Lippe](https://github.com/TimvdLippe) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.0.12-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.0.12)
 - Fixed JUnit Jupiter parallel issue [(#1789)](https://github.com/mockito/mockito/pull/1789)
 - Mockito JUnit Jupiter extension does not correctly support parallel test execution [(#1630)](https://github.com/mockito/mockito/issues/1630)

#### 3.0.11
 - 2019-09-28 - [2 commits](https://github.com/mockito/mockito/compare/v3.0.10...v3.0.11) by [Szczepan Faber](https://github.com/mockitoguy) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.0.11-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.0.11)
 - Upgraded JUnit Jupiter 5.1.1 -> 5.4.2 [(#1788)](https://github.com/mockito/mockito/pull/1788)
 - Mockito JUnit Jupiter extension does not correctly support parallel test execution [(#1630)](https://github.com/mockito/mockito/issues/1630)

#### 3.0.10
 - 2019-09-28 - [1 commit](https://github.com/mockito/mockito/compare/v3.0.9...v3.0.10) by [Danny Mösch](https://github.com/SimplyDanny) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.0.10-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.0.10)
 - Fixes #1786 : Clarify differences between 'timeout' and 'after' [(#1787)](https://github.com/mockito/mockito/pull/1787)
 - Difference between 'timeout' and 'after' not immediately clear [(#1786)](https://github.com/mockito/mockito/issues/1786)

#### 3.0.9
 - 2019-09-25 - [2 commits](https://github.com/mockito/mockito/compare/v3.0.8...v3.0.9) by [Szczepan Faber](https://github.com/mockitoguy) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.0.9-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.0.9)
 - Cleaned up state after stubbing misuse exception [(#1783)](https://github.com/mockito/mockito/pull/1783)
 - Stubbing not stopped properly when running suite of tests [(#1655)](https://github.com/mockito/mockito/issues/1655)

#### 3.0.8
 - 2019-09-17 - [2 commits](https://github.com/mockito/mockito/compare/v3.0.7...v3.0.8) by [Dominik Stadler](https://github.com/centic9) (1), [Erhard Pointl](https://github.com/epeee) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.0.8-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.0.8)
 - Fixes #1780: Close file handle to avoid a file-handle-leak [(#1781)](https://github.com/mockito/mockito/pull/1781)
 - File-handle leak in InlineByteBuddyMockMaker [(#1780)](https://github.com/mockito/mockito/issues/1780)
 - Get rid of no longer used Assertor [(#1777)](https://github.com/mockito/mockito/pull/1777)

#### 3.0.7
 - 2019-09-04 - [2 commits](https://github.com/mockito/mockito/compare/v3.0.6...v3.0.7) by [Erhard Pointl](https://github.com/epeee) (1), [Guillermo Pascual](https://github.com/pasku) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.0.7-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.0.7)
 - Fixes #1769: Clarify default strict stubbing behaviour in 3.0.0 [(#1773)](https://github.com/mockito/mockito/pull/1773)
 - Are strict stubs really the default in v3? [(#1769)](https://github.com/mockito/mockito/issues/1769)
 - Update assertj (v3.13.2) [(#1765)](https://github.com/mockito/mockito/pull/1765)

#### 3.0.6
 - 2019-08-19 - [1 commit](https://github.com/mockito/mockito/compare/v3.0.5...v3.0.6) by [Sergey](https://github.com/serssp) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.0.6-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.0.6)
 - Fixes #1758: Method GenericTypeExtractor#findGenericInterface always … [(#1762)](https://github.com/mockito/mockito/pull/1762)
 - Method GenericTypeExtractor#findGenericInterface always returns first  interface [(#1758)](https://github.com/mockito/mockito/issues/1758)

#### 3.0.5
 - 2019-08-16 - [4 commits](https://github.com/mockito/mockito/compare/v3.0.4...v3.0.5) by [Piotrek Żygieło](https://github.com/pzygielo) (2), [Mateusz Mrozewski](https://github.com/mateuszmrozewski) (1), [Tim van der Lippe](https://github.com/TimvdLippe) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.0.5-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.0.5)
 - Minor bug fixes [(#1760)](https://github.com/mockito/mockito/pull/1760)
 - Remove JDK env, not corresponding with jdk used [(#1752)](https://github.com/mockito/mockito/pull/1752)
 - Use OpenJDKs [(#1751)](https://github.com/mockito/mockito/pull/1751)
 - Typo in error message  [(#1747)](https://github.com/mockito/mockito/issues/1747)
 - Mockito docs / javadocs - monospaced font all over the place after point 44 [(#1513)](https://github.com/mockito/mockito/issues/1513)

#### 3.0.4
 - 2019-07-26 - [1 commit](https://github.com/mockito/mockito/compare/v3.0.3...v3.0.4) by [Arend v. Reinersdorff](https://github.com/arend-von-reinersdorff) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.0.4-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.0.4)
 - Fixes #1743: Clarify default answer in 3.0.0 [(#1745)](https://github.com/mockito/mockito/pull/1745)
 - Clarify Javadoc of RETURNS_SMART_NULLS, default answer in Mockito 3.0.0? [(#1743)](https://github.com/mockito/mockito/issues/1743)

#### 3.0.3
 - 2019-07-26 - [2 commits](https://github.com/mockito/mockito/compare/v3.0.2...v3.0.3) by [Fr Jeremy Krieg](https://github.com/kriegfrj) (1), [Piotrek Żygieło](https://github.com/pzygielo) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.0.3-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.0.3)
 - Travis: Explicit distribution of trusty [(#1750)](https://github.com/mockito/mockito/pull/1750)
 - Change implementation of ExceptionFactory to explicitly test for dependent classes [(#1723)](https://github.com/mockito/mockito/pull/1723)
 - mockito-android 2.26+ tries to use missing opentest4j exceptions [(#1716)](https://github.com/mockito/mockito/issues/1716)

#### 3.0.2
 - 2019-07-11 - [1 commit](https://github.com/mockito/mockito/compare/v3.0.1...v3.0.2) by [Andrew Ash](https://github.com/ash211) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.0.2-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.0.2)
 - Consistent punctuation in hints list for UnfinishedStubbingException [(#1739)](https://github.com/mockito/mockito/pull/1739)

#### 3.0.1
 - 2019-07-08 - [1 commit](https://github.com/mockito/mockito/compare/v3.0.0...v3.0.1) by [Federico Fissore](https://github.com/ffissore) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.0.1-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.0.1)
 -  verifyNoInteractions guarantees no interactions with mock [(#1733)](https://github.com/mockito/mockito/pull/1733)

#### 3.0.0
 - 2019-07-08 - [2 commits](https://github.com/mockito/mockito/compare/v3.0.0-beta.2...v3.0.0) by [Tim van der Lippe](https://github.com/TimvdLippe) (1), [William Collishaw](https://github.com/WilliamCollishaw) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.0.0-green.svg)](https://bintray.com/mockito/maven/mockito/3.0.0)
 - [3.x] Fix missing README logo and other broken links [(#1727)](https://github.com/mockito/mockito/pull/1727)

#### 3.0.0-beta.2
 - 2019-06-06 - [41 commits](https://github.com/mockito/mockito/compare/v3.0.0-beta.1...v3.0.0-beta.2) by 15 authors - published to [![Bintray](https://img.shields.io/badge/Bintray-3.0.0-beta.2-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.0.0-beta.2)
 - Commits: [Tim van der Lippe](https://github.com/TimvdLippe) (15), shipkit-org (9), [Erhard Pointl](https://github.com/epeee) (4), [Jakub Vrána](https://github.com/vrana) (2), [Christoph Wagner](https://github.com/WagnerChristoph) (1), [David Genesis Geniza Cruz](https://github.com/davidgenesiscruz) (1), [Frank Pavageau](https://github.com/fpavageau) (1), [Marc Philipp](https://github.com/marcphilipp) (1), Marcono1234 (1), [Ryan Pickett](https://github.com/hdpe) (1), [Stephan Schroevers](https://github.com/Stephan202) (1), [TDurak](https://github.com/Ingvir) (1), [Tom Ball](https://github.com/tomball) (1), tombrown52 (1), [önder sezgin](https://github.com/onderson) (1)
 - [Enhancements] Consider renaming TooLittleActualInvocations [(#1623)](https://github.com/mockito/mockito/issues/1623)
 - Core 2.27.5 is not published to Maven Central [(#1721)](https://github.com/mockito/mockito/issues/1721)
 - Add missing 'be' in javadoc [(#1720)](https://github.com/mockito/mockito/pull/1720)
 - Fixes #1717 : configure the MethodVisitor for Java 11 compatibility [(#1718)](https://github.com/mockito/mockito/pull/1718)
 - Incompatibility between the inline mocks and JaCoCo 0.8.4 [(#1717)](https://github.com/mockito/mockito/issues/1717)
 - Fixes #298: replaces Objenesis references from stubbing classes with plugin equivalents. [(#1715)](https://github.com/mockito/mockito/pull/1715)
 - Fixes #1713: fix typo in Mockito.java javadoc [(#1714)](https://github.com/mockito/mockito/pull/1714)
 - Typo in Mockito.java javadoc [(#1713)](https://github.com/mockito/mockito/issues/1713)
 - Fixes #1623 : Rename TooLittleActualInvocations [(#1708)](https://github.com/mockito/mockito/pull/1708)
 - Change master to release/3.x in pull request template [(#1707)](https://github.com/mockito/mockito/pull/1707)
 - Fix a typo [(#1706)](https://github.com/mockito/mockito/pull/1706)
 - Remove no longer relevant code from settings.gradle.kts [(#1705)](https://github.com/mockito/mockito/pull/1705)
 - Migrate junitJupiterExtensionTest subproject to kotlin dsl [(#1704)](https://github.com/mockito/mockito/pull/1704)
 - Use require instead of assert in settings.gradle.kts [(#1703)](https://github.com/mockito/mockito/pull/1703)
 - Migrate settings.gradle to settings.gradle.kts [(#1702)](https://github.com/mockito/mockito/pull/1702)
 - Show multiple invocations on argumentsAreDifferent [(#1701)](https://github.com/mockito/mockito/pull/1701)
 - use of capital letter at the start as a small [(#1700)](https://github.com/mockito/mockito/pull/1700)
 - Fix JaCoCo code coverage report [(#1699)](https://github.com/mockito/mockito/pull/1699)
 - Fix ClonesArguments for null and arrays [(#1698)](https://github.com/mockito/mockito/pull/1698)
 - Fix javadoc typo and use HTML list [(#1695)](https://github.com/mockito/mockito/pull/1695)
 - Make `MockitoNotExtensible` service-loadable [(#1693)](https://github.com/mockito/mockito/pull/1693)
 - The new Error Prone plugin isn't service-loadable [(#1692)](https://github.com/mockito/mockito/issues/1692)
 - Add ErrorProne MockitoInternalUsage checker [(#1690)](https://github.com/mockito/mockito/pull/1690)
 - Coverage report is broken since Gradle 5 [(#1689)](https://github.com/mockito/mockito/issues/1689)
 - when mock is called multiple times, and verify fails, the error message reports only the first invocation [(#1542)](https://github.com/mockito/mockito/issues/1542)
 - Fixes #1386: Adding atMostOnce(), rename ThreadVerifiesContinuoslyInteractingMockTest and one TODO [(#1387)](https://github.com/mockito/mockito/pull/1387)
 - Add AtMostOnce(),rename one testClass and one TODO. [(#1386)](https://github.com/mockito/mockito/issues/1386)
 - ObjenesisHelper references in MockMaker-independent code [(#298)](https://github.com/mockito/mockito/issues/298)

#### 3.0.0
 - 2019-06-06 - [39 commits](https://github.com/mockito/mockito/compare/v3.0.0-beta.1...v3.0.0) by 15 authors - published to [![Bintray](https://img.shields.io/badge/Bintray-3.0.0-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.0.0)
 - Commits: [Tim van der Lippe](https://github.com/TimvdLippe) (14), shipkit-org (8), [Erhard Pointl](https://github.com/epeee) (4), [Jakub Vrána](https://github.com/vrana) (2), [Christoph Wagner](https://github.com/WagnerChristoph) (1), [David Genesis Geniza Cruz](https://github.com/davidgenesiscruz) (1), [Frank Pavageau](https://github.com/fpavageau) (1), [Marc Philipp](https://github.com/marcphilipp) (1), Marcono1234 (1), [Ryan Pickett](https://github.com/hdpe) (1), [Stephan Schroevers](https://github.com/Stephan202) (1), [TDurak](https://github.com/Ingvir) (1), [Tom Ball](https://github.com/tomball) (1), tombrown52 (1), [önder sezgin](https://github.com/onderson) (1)
 - [Enhancements] Consider renaming TooLittleActualInvocations [(#1623)](https://github.com/mockito/mockito/issues/1623)
 - Core 2.27.5 is not published to Maven Central [(#1721)](https://github.com/mockito/mockito/issues/1721)
 - Add missing 'be' in javadoc [(#1720)](https://github.com/mockito/mockito/pull/1720)
 - Fixes #1717 : configure the MethodVisitor for Java 11 compatibility [(#1718)](https://github.com/mockito/mockito/pull/1718)
 - Incompatibility between the inline mocks and JaCoCo 0.8.4 [(#1717)](https://github.com/mockito/mockito/issues/1717)
 - Fixes #298: replaces Objenesis references from stubbing classes with plugin equivalents. [(#1715)](https://github.com/mockito/mockito/pull/1715)
 - Fixes #1713: fix typo in Mockito.java javadoc [(#1714)](https://github.com/mockito/mockito/pull/1714)
 - Typo in Mockito.java javadoc [(#1713)](https://github.com/mockito/mockito/issues/1713)
 - Fixes #1623 : Rename TooLittleActualInvocations [(#1708)](https://github.com/mockito/mockito/pull/1708)
 - Change master to release/3.x in pull request template [(#1707)](https://github.com/mockito/mockito/pull/1707)
 - Fix a typo [(#1706)](https://github.com/mockito/mockito/pull/1706)
 - Remove no longer relevant code from settings.gradle.kts [(#1705)](https://github.com/mockito/mockito/pull/1705)
 - Migrate junitJupiterExtensionTest subproject to kotlin dsl [(#1704)](https://github.com/mockito/mockito/pull/1704)
 - Use require instead of assert in settings.gradle.kts [(#1703)](https://github.com/mockito/mockito/pull/1703)
 - Migrate settings.gradle to settings.gradle.kts [(#1702)](https://github.com/mockito/mockito/pull/1702)
 - Show multiple invocations on argumentsAreDifferent [(#1701)](https://github.com/mockito/mockito/pull/1701)
 - use of capital letter at the start as a small [(#1700)](https://github.com/mockito/mockito/pull/1700)
 - Fix JaCoCo code coverage report [(#1699)](https://github.com/mockito/mockito/pull/1699)
 - Fix ClonesArguments for null and arrays [(#1698)](https://github.com/mockito/mockito/pull/1698)
 - Fix javadoc typo and use HTML list [(#1695)](https://github.com/mockito/mockito/pull/1695)
 - Make `MockitoNotExtensible` service-loadable [(#1693)](https://github.com/mockito/mockito/pull/1693)
 - The new Error Prone plugin isn't service-loadable [(#1692)](https://github.com/mockito/mockito/issues/1692)
 - Add ErrorProne MockitoInternalUsage checker [(#1690)](https://github.com/mockito/mockito/pull/1690)
 - Coverage report is broken since Gradle 5 [(#1689)](https://github.com/mockito/mockito/issues/1689)
 - when mock is called multiple times, and verify fails, the error message reports only the first invocation [(#1542)](https://github.com/mockito/mockito/issues/1542)
 - Fixes #1386: Adding atMostOnce(), rename ThreadVerifiesContinuoslyInteractingMockTest and one TODO [(#1387)](https://github.com/mockito/mockito/pull/1387)
 - Add AtMostOnce(),rename one testClass and one TODO. [(#1386)](https://github.com/mockito/mockito/issues/1386)
 - ObjenesisHelper references in MockMaker-independent code [(#298)](https://github.com/mockito/mockito/issues/298)

#### 3.0.0-beta.1
 - 2019-04-25 - [5 commits](https://github.com/mockito/mockito/compare/v2.27.0...v3.0.0-beta.1) by [Tim van der Lippe](https://github.com/TimvdLippe) - published to [![Bintray](https://img.shields.io/badge/Bintray-3.0.0-beta.1-green.svg)](https://bintray.com/mockito/maven/mockito-development/3.0.0-beta.1)
 - No pull requests referenced in commit messages.

#### 2.28.2
 - 2019-05-29 - [1 commit](https://github.com/mockito/mockito/compare/v2.28.1...v2.28.2) by [Tim van der Lippe](https://github.com/TimvdLippe) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.28.2-green.svg)](https://bintray.com/mockito/maven/mockito/2.28.2)
 - No pull requests referenced in commit messages.

#### 2.28.1
 - 2019-05-28 - [1 commit](https://github.com/mockito/mockito/compare/v2.28.0...v2.28.1) by [Tim van der Lippe](https://github.com/TimvdLippe) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.28.1-green.svg)](https://bintray.com/mockito/maven/mockito/2.28.1)
 - No pull requests referenced in commit messages.

#### 2.28.0
 - 2019-05-28 - [3 commits](https://github.com/mockito/mockito/compare/v2.27.5...v2.28.0) by [Tim van der Lippe](https://github.com/TimvdLippe) (2), [Christoph Wagner](https://github.com/WagnerChristoph) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.28.0-green.svg)](https://bintray.com/mockito/maven/mockito/2.28.0)
 - Add missing 'be' in javadoc [(#1720)](https://github.com/mockito/mockito/pull/1720)

#### 2.27.5
 - 2019-05-24 - [9 commits](https://github.com/mockito/mockito/compare/v2.27.4...v2.27.5) by 6 authors - published to [![Bintray](https://img.shields.io/badge/Bintray-2.27.5-green.svg)](https://bintray.com/mockito/maven/mockito/2.27.5)
 - Commits: [Erhard Pointl](https://github.com/epeee) (3), [Jakub Vrána](https://github.com/vrana) (2), [David Genesis Geniza Cruz](https://github.com/davidgenesiscruz) (1), [Frank Pavageau](https://github.com/fpavageau) (1), [Ryan Pickett](https://github.com/hdpe) (1), [Tom Ball](https://github.com/tomball) (1)
 - [Enhancements] Consider renaming TooLittleActualInvocations [(#1623)](https://github.com/mockito/mockito/issues/1623)
 - Fixes #1717 : configure the MethodVisitor for Java 11 compatibility [(#1718)](https://github.com/mockito/mockito/pull/1718)
 - Incompatibility between the inline mocks and JaCoCo 0.8.4 [(#1717)](https://github.com/mockito/mockito/issues/1717)
 - Fixes #298: replaces Objenesis references from stubbing classes with plugin equivalents. [(#1715)](https://github.com/mockito/mockito/pull/1715)
 - Fixes #1713: fix typo in Mockito.java javadoc [(#1714)](https://github.com/mockito/mockito/pull/1714)
 - Typo in Mockito.java javadoc [(#1713)](https://github.com/mockito/mockito/issues/1713)
 - Fixes #1623 : Rename TooLittleActualInvocations [(#1708)](https://github.com/mockito/mockito/pull/1708)
 - Change master to release/3.x in pull request template [(#1707)](https://github.com/mockito/mockito/pull/1707)
 - Fix a typo [(#1706)](https://github.com/mockito/mockito/pull/1706)
 - Remove no longer relevant code from settings.gradle.kts [(#1705)](https://github.com/mockito/mockito/pull/1705)
 - Migrate junitJupiterExtensionTest subproject to kotlin dsl [(#1704)](https://github.com/mockito/mockito/pull/1704)
 - Use require instead of assert in settings.gradle.kts [(#1703)](https://github.com/mockito/mockito/pull/1703)
 - ObjenesisHelper references in MockMaker-independent code [(#298)](https://github.com/mockito/mockito/issues/298)

#### 2.27.4
 - 2019-05-03 - [11 commits](https://github.com/mockito/mockito/compare/v2.27.2...v2.27.4) by 8 authors - published to [![Bintray](https://img.shields.io/badge/Bintray-2.27.4-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.27.4)
 - Commits: [Tim van der Lippe](https://github.com/TimvdLippe) (4), [Erhard Pointl](https://github.com/epeee) (1), [Marc Philipp](https://github.com/marcphilipp) (1), [Marcono1234](https://github.com/Marcono1234) (1), shipkit-org (1), [TDurak](https://github.com/Ingvir) (1), tombrown52 (1), [önder sezgin](https://github.com/onderson) (1)
 - Migrate settings.gradle to settings.gradle.kts [(#1702)](https://github.com/mockito/mockito/pull/1702)
 - Show multiple invocations on argumentsAreDifferent [(#1701)](https://github.com/mockito/mockito/pull/1701)
 - use of capital letter at the start as a small [(#1700)](https://github.com/mockito/mockito/pull/1700)
 - Fix JaCoCo code coverage report [(#1699)](https://github.com/mockito/mockito/pull/1699)
 - Fix ClonesArguments for null and arrays [(#1698)](https://github.com/mockito/mockito/pull/1698)
 - Fix javadoc typo and use HTML list [(#1695)](https://github.com/mockito/mockito/pull/1695)
 - Coverage report is broken since Gradle 5 [(#1689)](https://github.com/mockito/mockito/issues/1689)
 - when mock is called multiple times, and verify fails, the error message reports only the first invocation [(#1542)](https://github.com/mockito/mockito/issues/1542)
 - Fixes #1386: Adding atMostOnce(), rename ThreadVerifiesContinuoslyInteractingMockTest and one TODO [(#1387)](https://github.com/mockito/mockito/pull/1387)
 - Add AtMostOnce(),rename one testClass and one TODO. [(#1386)](https://github.com/mockito/mockito/issues/1386)

#### 2.27.3
 - 2019-05-01 - [4 commits](https://github.com/mockito/mockito/compare/v2.27.2...v2.27.3) by 4 authors - published to [![Bintray](https://img.shields.io/badge/Bintray-2.27.3-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.27.3)
 - Commits: [Marc Philipp](https://github.com/marcphilipp) (1), [Tim van der Lippe](https://github.com/TimvdLippe) (1), [tombrown52](https://github.com/tombrown52) (1), [önder sezgin](https://github.com/onderson) (1)
 - use of capital letter at the start as a small [(#1700)](https://github.com/mockito/mockito/pull/1700)
 - Fix JaCoCo code coverage report [(#1699)](https://github.com/mockito/mockito/pull/1699)
 - Fix ClonesArguments for null and arrays [(#1698)](https://github.com/mockito/mockito/pull/1698)
 - Coverage report is broken since Gradle 5 [(#1689)](https://github.com/mockito/mockito/issues/1689)

#### 2.27.2
 - 2019-04-30 - [4 commits](https://github.com/mockito/mockito/compare/v2.27.1...v2.27.2) by [Tim van der Lippe](https://github.com/TimvdLippe) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.27.2-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.27.2)
 - Add ErrorProne MockitoInternalUsage checker [(#1690)](https://github.com/mockito/mockito/pull/1690)

#### 2.27.1
 - 2019-04-30 - [2 commits](https://github.com/mockito/mockito/compare/v2.27.0...v2.27.1) by [Stephan Schroevers](https://github.com/Stephan202) (1), [Tim van der Lippe](https://github.com/TimvdLippe) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.27.1-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.27.1)
 - Make `MockitoNotExtensible` service-loadable [(#1693)](https://github.com/mockito/mockito/pull/1693)
 - The new Error Prone plugin isn't service-loadable [(#1692)](https://github.com/mockito/mockito/issues/1692)

#### 2.27.0
 - 2019-04-10 - [1 commit](https://github.com/mockito/mockito/compare/v2.26.2...v2.27.0) by [Tim van der Lippe](https://github.com/TimvdLippe) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.27.0-green.svg)](https://bintray.com/mockito/maven/mockito/2.27.0)
 - No pull requests referenced in commit messages.

#### 2.26.2
 - 2019-04-10 - [11 commits](https://github.com/mockito/mockito/compare/v2.26.1...v2.26.2) by [Tim van der Lippe](https://github.com/TimvdLippe) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.26.2-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.26.2)
 - Eagerly compute stackTraceLine in LocationImpl [(#1683)](https://github.com/mockito/mockito/pull/1683)

#### 2.26.1
 - 2019-04-10 - [2 commits](https://github.com/mockito/mockito/compare/v2.26.0...v2.26.1) by [Tim van der Lippe](https://github.com/TimvdLippe) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.26.1-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.26.1)
 - Fix handling of generic Void type for doNothing() [(#1680)](https://github.com/mockito/mockito/pull/1680)

#### 2.26.0
 - 2019-04-04 - [1 commit](https://github.com/mockito/mockito/compare/v2.25.7...v2.26.0) by [Tim van der Lippe](https://github.com/TimvdLippe) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.26.0-green.svg)](https://bintray.com/mockito/maven/mockito/2.26.0)
 - No pull requests referenced in commit messages.

#### 2.25.7
 - 2019-03-28 - [4 commits](https://github.com/mockito/mockito/compare/v2.25.6...v2.25.7) by [Bruno Bonanno](https://github.com/bbonanno) (3), [Szczepan Faber](https://github.com/mockitoguy) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.25.7-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.25.7)
 - New public API: invocation.getArgumentsAsMatchers() [(#1665)](https://github.com/mockito/mockito/pull/1665)

#### 2.25.6
 - 2019-03-27 - [2 commits](https://github.com/mockito/mockito/compare/v2.25.5...v2.25.6) by [Szczepan Faber](https://github.com/mockitoguy) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.25.6-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.25.6)
 - Pinned ByteBuddy to 1.9.10 [(#1676)](https://github.com/mockito/mockito/pull/1676)

#### 2.25.5
 - 2019-03-26 - [2 commits](https://github.com/mockito/mockito/compare/v2.25.4...v2.25.5) by [Szczepan Faber](https://github.com/mockitoguy) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.25.5-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.25.5)
 - Bumped ByteBuddy to 1.9.11 [(#1673)](https://github.com/mockito/mockito/pull/1673)

#### 2.25.4
 - 2019-03-23 - [2 commits](https://github.com/mockito/mockito/compare/v2.25.3...v2.25.4) by [Marc Philipp](https://github.com/marcphilipp) (1), [Tim van der Lippe](https://github.com/TimvdLippe) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.25.4-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.25.4)
 - Report initialization failures per test method [(#1672)](https://github.com/mockito/mockito/pull/1672)
 - Master [(#1599)](https://github.com/mockito/mockito/pull/1599)

#### 2.25.3
 - 2019-03-22 - [2 commits](https://github.com/mockito/mockito/compare/v2.25.2...v2.25.3) by [Per Lundberg](https://github.com/perlun) (1), [Szczepan Faber](https://github.com/mockitoguy) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.25.3-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.25.3)
 - Add support for Answer6 and VoidAnswer6 [(#1670)](https://github.com/mockito/mockito/pull/1670)

#### 2.25.2
 - 2019-03-22 - [8 commits](https://github.com/mockito/mockito/compare/v2.25.1...v2.25.2) by [Tim van der Lippe](https://github.com/TimvdLippe) (5), kriegfrj (2), [Szczepan Faber](https://github.com/mockitoguy) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.25.2-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.25.2)
 - [New features] Add subproject for errorprone Mockito plugins [(#1668)](https://github.com/mockito/mockito/issues/1668)
 - Add ErrorProne subproject [(#1669)](https://github.com/mockito/mockito/pull/1669)
 - Improved IDE experience for JUnit5 - visual comparison failure [(#1667)](https://github.com/mockito/mockito/pull/1667)
 - Improve IDE UX comparison failure for JUnit5 [(#1663)](https://github.com/mockito/mockito/issues/1663)

#### 2.25.1
 - 2019-03-15 - [5 commits](https://github.com/mockito/mockito/compare/v2.25.0...v2.25.1) by [Tim van der Lippe](https://github.com/TimvdLippe) (4), Jason Brown (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.25.1-green.svg)](https://bintray.com/mockito/maven/mockito/2.25.1)
 - Fix typo [(#1660)](https://github.com/mockito/mockito/pull/1660)
 - Add back getArgument(int, Class) to circumvent compilation issues [(#1646)](https://github.com/mockito/mockito/pull/1646)
 - Add back InvocationOnMock.getArgument<T>(int, Class<T>) [(#1609)](https://github.com/mockito/mockito/issues/1609)

#### 2.25.0
 - 2019-03-05 - [11 commits](https://github.com/mockito/mockito/compare/v2.24.10...v2.25.0) by [Szczepan Faber](https://github.com/mockitoguy) (10), Garfield Tan (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.25.0-green.svg)](https://bintray.com/mockito/maven/mockito/2.25.0)
 - New API to clean up all inline mocks after test [(#1619)](https://github.com/mockito/mockito/pull/1619)
 - Memory leak in mockito-inline calling method on mock with at least a mock as parameter [(#1614)](https://github.com/mockito/mockito/issues/1614)
 - Cross-references and a single spy cause memory leak [(#1533)](https://github.com/mockito/mockito/issues/1533)
 - Nested spies cause memory leaks  [(#1532)](https://github.com/mockito/mockito/issues/1532)

#### 2.24.10
 - 2019-03-05 - [4 commits](https://github.com/mockito/mockito/compare/v2.24.9...v2.24.10) by [Tim van der Lippe](https://github.com/TimvdLippe) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.24.10-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.24.10)
 - Prevent NPE in findTypeFromGenericInArguments [(#1648)](https://github.com/mockito/mockito/pull/1648)

#### 2.24.9
 - 2019-03-04 - [12 commits](https://github.com/mockito/mockito/compare/v2.24.7...v2.24.9) by 6 authors - published to [![Bintray](https://img.shields.io/badge/Bintray-2.24.9-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.24.9)
 - Commits: [Brice Dutheil](https://github.com/bric3) (5), [Tim van der Lippe](https://github.com/TimvdLippe) (3), [epeee](https://github.com/epeee) (1), [Fr Jeremy Krieg](https://github.com/kriegfrj) (1), [Paweł Pamuła](https://github.com/PawelPamula) (1), shipkit-org (1)
 - [Java 9 support] ClassCastExceptions with JDK9 javac [(#357)](https://github.com/mockito/mockito/issues/357)
 - [Bugfixes] RETURNS_DEEP_STUBS causes "Raw extraction not supported for : 'null'" in some cases [(#1621)](https://github.com/mockito/mockito/issues/1621)
 - Update shipkit plugin (v2.1.6) [(#1647)](https://github.com/mockito/mockito/pull/1647)
 - VerificationCollector to handle non-matching args and other assertions [(#1644)](https://github.com/mockito/mockito/pull/1644)
 - VerificationCollector doesn't work for invocations with non-matching args [(#1642)](https://github.com/mockito/mockito/issues/1642)
 - Fix returns mocks for final classes [(#1641)](https://github.com/mockito/mockito/pull/1641)
 - Removes inaccessible links from javadocs in Mockito.java [(#1639)](https://github.com/mockito/mockito/pull/1639)
 - Mockito.java contains inaccessible links to articles. [(#1638)](https://github.com/mockito/mockito/issues/1638)
 - Handle terminal type var with bounds [(#1624)](https://github.com/mockito/mockito/pull/1624)
 - Return null instead of causing a CCE [(#1612)](https://github.com/mockito/mockito/pull/1612)

#### 2.24.7
 - 2019-02-28 - [2 commits](https://github.com/mockito/mockito/compare/v2.24.6...v2.24.7) by [shipkit-org](https://github.com/shipkit-org) (1), [Tim van der Lippe](https://github.com/TimvdLippe) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.24.7-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.24.7)
 - Fix handling of generics in ReturnsMocks [(#1635)](https://github.com/mockito/mockito/pull/1635)

#### 2.24.6
 - 2019-02-27 - [16 commits](https://github.com/mockito/mockito/compare/v2.24.5...v2.24.6) by [Szczepan Faber](https://github.com/mockitoguy) (15), [Marcin Stachniuk](https://github.com/mstachniuk) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.24.6-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.24.6)
 - Make use of Shipkit v2.1.3 [(#1626)](https://github.com/mockito/mockito/pull/1626)
 - Exposed new API - StubbingLookupListener [(#1543)](https://github.com/mockito/mockito/pull/1543)

#### 2.24.5
 - 2019-02-18 - [2 commits](https://github.com/mockito/mockito/compare/v2.24.4...v2.24.5) by [Tim van der Lippe](https://github.com/TimvdLippe) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.24.5-green.svg)](https://bintray.com/mockito/maven/mockito/2.24.5)
 - No pull requests referenced in commit messages.

#### 2.24.4
 - 2019-02-13 - [1 commit](https://github.com/mockito/mockito/compare/v2.24.3...v2.24.4) by [Alex Simkin](https://github.com/SimY4) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.24.4-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.24.4)
 - Fixes #1618 : Fix strict stubbing profile serialization support. [(#1620)](https://github.com/mockito/mockito/pull/1620)
 - Serializable flag doesn't make mock serializable [(#1618)](https://github.com/mockito/mockito/issues/1618)

#### 2.24.3
 - 2019-02-12 - [2 commits](https://github.com/mockito/mockito/compare/v2.24.2...v2.24.3) by [Marcin Zajączkowski](https://github.com/szpak) (1), [Tim van der Lippe](https://github.com/TimvdLippe) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.24.3-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.24.3)
 - [Java 9 support] ClassCastExceptions with JDK9 javac [(#357)](https://github.com/mockito/mockito/issues/357)
 - Return null instead of causing a CCE [(#1612)](https://github.com/mockito/mockito/pull/1612)
 - Automatic dependency update with Dependabot [(#1600)](https://github.com/mockito/mockito/pull/1600)
 - Fix/bug 1551 cce on smart not null answers [(#1576)](https://github.com/mockito/mockito/pull/1576)

#### 2.24.2
 - 2019-02-11 - [1 commit](https://github.com/mockito/mockito/compare/v2.24.1...v2.24.2) by [Tim van der Lippe](https://github.com/TimvdLippe) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.24.2-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.24.2)
 - Fix issue with mocking of java.util.* classes [(#1617)](https://github.com/mockito/mockito/pull/1617)
 - Issue with mocking type in "java.util.*", Java 12 [(#1615)](https://github.com/mockito/mockito/issues/1615)

#### 2.24.1
 - 2019-02-04 - [1 commit](https://github.com/mockito/mockito/compare/v2.24.0...v2.24.1) by [zoujinhe](https://github.com/zoujinhe) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.24.1-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.24.1)
 - typo? ... 'thenReturn' instruction if completed -> ... 'thenReturn' instruction is completed [(#1608)](https://github.com/mockito/mockito/pull/1608)

#### 2.24.0
 - 2019-02-01 - [1 commit](https://github.com/mockito/mockito/compare/v2.23.20...v2.24.0) by [Tim van der Lippe](https://github.com/TimvdLippe) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.24.0-green.svg)](https://bintray.com/mockito/maven/mockito/2.24.0)
 - No pull requests referenced in commit messages.

#### 2.23.20
 - 2019-01-31 - [1 commit](https://github.com/mockito/mockito/compare/v2.23.19...v2.23.20) by [Matthew Ouyang](https://github.com/mouyang) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.23.20-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.23.20)
 - Fixes #1578: outer class mocks unavailable from inner class [(#1596)](https://github.com/mockito/mockito/pull/1596)
 - UnfinishedMockingSessionException with inner test classes [(#1578)](https://github.com/mockito/mockito/issues/1578)

#### 2.23.19
 - 2019-01-31 - [1 commit](https://github.com/mockito/mockito/compare/v2.23.18...v2.23.19) by [Ivo Šmíd](https://github.com/bedla) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.23.19-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.23.19)
 - Allow mockito-logger to be configurable as Mockito extension [(#1430)](https://github.com/mockito/mockito/pull/1430)

#### 2.23.18
 - 2019-01-29 - [1 commit](https://github.com/mockito/mockito/compare/v2.23.17...v2.23.18) by [Nikolas Falco](https://github.com/nfalco79) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.23.18-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.23.18)
 - Fix #1584 where MultipleParentClassLoader lacks of classloaders of superclasses [(#1585)](https://github.com/mockito/mockito/pull/1585)
 - ClassLoader built by mockito is not able to load declared types in the hierarchy in OSGi [(#1584)](https://github.com/mockito/mockito/issues/1584)

#### 2.23.17
 - 2019-01-28 - [6 commits](https://github.com/mockito/mockito/compare/v2.23.16...v2.23.17) by [Rafael Winterhalter](https://github.com/raphw) (5), [Tim van der Lippe](https://github.com/TimvdLippe) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.23.17-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.23.17)
 - No pull requests referenced in commit messages.

#### 2.23.16
 - 2019-01-14 - [39 commits](https://github.com/mockito/mockito/compare/v2.23.15...v2.23.16) by [Rafael Winterhalter](https://github.com/raphw) (35), [Tim van der Lippe](https://github.com/TimvdLippe) (3), [Ravi van Rooijen](https://github.com/HoldYourWaffle) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.23.16-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.23.16)
 - Fix #1577: Refactoring naming rule for sealed packages to better work with the Java module system [(#1582)](https://github.com/mockito/mockito/pull/1582)
 - IllegalAccessError on Java 11 when using module path [(#1577)](https://github.com/mockito/mockito/issues/1577)
 - Improve eclipse project setup [(#1573)](https://github.com/mockito/mockito/pull/1573)

#### 2.23.15
 - 2019-01-10 - [1 commit](https://github.com/mockito/mockito/compare/v2.23.14...v2.23.15) by [LihMeh](https://github.com/LihMeh) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.23.15-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.23.15)
 - Fixes #1587 : Remove unnecessary loop from InjectingAnnotationEngine [(#1588)](https://github.com/mockito/mockito/pull/1588)
 - InjectingAnnotationEngine does an unnecessary loop. [(#1587)](https://github.com/mockito/mockito/issues/1587)

#### 2.23.14
 - 2019-01-09 - [1 commit](https://github.com/mockito/mockito/compare/v2.23.13...v2.23.14) by [Tim van der Lippe](https://github.com/TimvdLippe) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.23.14-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.23.14)
 - Add some missing `@Deprecated` annotations [(#1586)](https://github.com/mockito/mockito/pull/1586)

#### 2.23.13
 - 2019-01-08 - [1 commit](https://github.com/mockito/mockito/compare/v2.23.12...v2.23.13) by [brettchabot](https://github.com/brettchabot) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.23.13-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.23.13)
 - Add support for android tests using androidx.test. [(#1583)](https://github.com/mockito/mockito/pull/1583)

#### 2.23.12
 - 2019-01-06 - [1 commit](https://github.com/mockito/mockito/compare/v2.23.11...v2.23.12) by [Patouche](https://github.com/Patouche) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.23.12-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.23.12)
 - Fix/bug 1551 cce on smart not null answers [(#1576)](https://github.com/mockito/mockito/pull/1576)
 - ClassCastException with generics and smart nulls [(#1551)](https://github.com/mockito/mockito/issues/1551)

#### 2.23.11
 - 2018-12-13 - [1 commit](https://github.com/mockito/mockito/compare/v2.23.10...v2.23.11) by [Ravi van Rooijen](https://github.com/HoldYourWaffle) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.23.11-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.23.11)
 - Add warning to inline mock maker when running on a JRE [(#1567)](https://github.com/mockito/mockito/pull/1567)

#### 2.23.10
 - 2018-12-12 - [12 commits](https://github.com/mockito/mockito/compare/v2.23.9...v2.23.10) by [Erhard Pointl](https://github.com/epeee) (6), [Szczepan Faber](https://github.com/mockitoguy) (5), [Tim van der Lippe](https://github.com/TimvdLippe) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.23.10-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.23.10)
 - Prevent StackOverflowError when processing reversed generics [(#1565)](https://github.com/mockito/mockito/pull/1565)
 -  Simplify travis.yml (openjdk10 and openjdk11) [(#1560)](https://github.com/mockito/mockito/pull/1560)
 - Use jacoco 0.8.2 [(#1559)](https://github.com/mockito/mockito/pull/1559)
 - Use errorprone gradle plugin v0.6 and extract errorprone config [(#1558)](https://github.com/mockito/mockito/pull/1558)
 - Fixed JaCoCo build cache issue [(#1555)](https://github.com/mockito/mockito/pull/1555)
 - Enable gradle build cache [(#1546)](https://github.com/mockito/mockito/pull/1546)

#### 2.23.9
 - 2018-12-02 - [4 commits](https://github.com/mockito/mockito/compare/v2.23.8...v2.23.9) by [Szczepan Faber](https://github.com/mockitoguy) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.23.9-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.23.9)
 - [Bugfixes] Mockito remains in unfinished stubbing state if a stubbing fails [(#1514)](https://github.com/mockito/mockito/issues/1514)
 - Minimal test to repro generic issue with smart nulls [(#1552)](https://github.com/mockito/mockito/pull/1552)
 - Fixed issue with leftover state when stubbing with bad throwables [(#1549)](https://github.com/mockito/mockito/pull/1549)

#### 2.23.8
 - 2018-11-29 - [6 commits](https://github.com/mockito/mockito/compare/v2.23.7...v2.23.8) by [Szczepan Faber](https://github.com/mockitoguy) (5), [Theodore Ni](https://github.com/tjni) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.23.8-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.23.8)
 - Fixed VerifyError when JUnit 3 is in the classpath [(#1554)](https://github.com/mockito/mockito/pull/1554)
 - VerifyError thrown when JUnit 3 is in the classpath [(#1553)](https://github.com/mockito/mockito/issues/1553)
 - Bumped Gradle to last 4.x [(#1548)](https://github.com/mockito/mockito/pull/1548)

#### 2.23.7
 - 2018-11-27 - [6 commits](https://github.com/mockito/mockito/compare/v2.23.6...v2.23.7) by [Szczepan Faber](https://github.com/mockitoguy) (5), [Sangwoo Lee](https://github.com/marchpig) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.23.7-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.23.7)
 - Prevent stubOnly() from being verified (#1460) [(#1463)](https://github.com/mockito/mockito/pull/1463)

#### 2.23.6
 - 2018-11-26 - [5 commits](https://github.com/mockito/mockito/compare/v2.23.5...v2.23.6) by [Szczepan Faber](https://github.com/mockitoguy) (4), [Max Grabenhorst](https://github.com/maxgrabenhorst) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.23.6-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.23.6)
 - Fixes #1541: Prevent premature garbage collection of mock objects [(#1544)](https://github.com/mockito/mockito/pull/1544)
 - Mock object premature garbage collected when using 'One-liner stubs' [(#1541)](https://github.com/mockito/mockito/issues/1541)

#### 2.23.5
 - 2018-11-25 - [3 commits](https://github.com/mockito/mockito/compare/v2.23.4...v2.23.5) by [Szczepan Faber](https://github.com/mockitoguy) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.23.5-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.23.5)
 - Improved sensitivity of potential stubbing problem [(#1539)](https://github.com/mockito/mockito/pull/1539)

#### 2.23.4
 - 2018-11-20 - [3 commits](https://github.com/mockito/mockito/compare/v2.23.3...v2.23.4) by [Szczepan Faber](https://github.com/mockitoguy) (2), [Walter Johnson](https://github.com/li-wjohnson) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.23.4-green.svg)](https://bintray.com/mockito/maven/mockito/2.23.4)
 - Allow delegating to non-public methods for AdditionalAnswers#delegatesTo [(#1536)](https://github.com/mockito/mockito/pull/1536)

#### 2.23.3
 - 2018-11-19 - [3 commits](https://github.com/mockito/mockito/compare/v2.23.2...v2.23.3) by [Szczepan Faber](https://github.com/mockitoguy) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.23.3-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.23.3)
 - Added 'lenient' annotation toggle [(#1523)](https://github.com/mockito/mockito/pull/1523)

#### 2.23.2
 - 2018-10-31 - [3 commits](https://github.com/mockito/mockito/compare/v2.23.1...v2.23.2) by [Ismael Juma](https://github.com/ijuma) (2), [Rafael Winterhalter](https://github.com/raphw) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.23.2-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.23.2)
 - Update bytebuddy, asm and objenesis for full Java 11 support [(#1525)](https://github.com/mockito/mockito/pull/1525)

#### 2.23.1
 - 2018-10-19 - [3 commits](https://github.com/mockito/mockito/compare/v2.23.0...v2.23.1) by [Bastien Jansen](https://github.com/bjansen) (1), jmetertea (1), [Szczepan Faber](https://github.com/mockitoguy) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.23.1-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.23.1)
 - Minor improvements in the Javadoc of Mockito.java [(#1521)](https://github.com/mockito/mockito/pull/1521)
 - Fix typos [(#1508)](https://github.com/mockito/mockito/pull/1508)

#### 2.23.0
 - 2018-10-05 - [1 commit](https://github.com/mockito/mockito/compare/v2.22.2...v2.23.0) by [Tim van der Lippe](https://github.com/TimvdLippe) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.23.0-green.svg)](https://bintray.com/mockito/maven/mockito/2.23.0)
 - No pull requests referenced in commit messages.

#### 2.22.2
 - 2018-10-05 - [1 commit](https://github.com/mockito/mockito/compare/v2.22.1...v2.22.2) by [Denis Zharkov](https://github.com/dzharkov) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.22.2-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.22.2)
 - Support mocking kotlin suspend functions compiled by Kotlin 1.3 (#1500) [(#1501)](https://github.com/mockito/mockito/pull/1501)
 - Mockito 2.x.x does not support Kotlin release coroutines [(#1500)](https://github.com/mockito/mockito/issues/1500)

#### 2.22.1
 - 2018-09-30 - [1 commit](https://github.com/mockito/mockito/compare/v2.22.0...v2.22.1) by [Rafael Winterhalter](https://github.com/raphw) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.22.1-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.22.1)
 - Updates Byte Buddy to next version for Java 11 support. [(#1505)](https://github.com/mockito/mockito/pull/1505)

#### 2.22.0
 - 2018-09-09 - [2 commits](https://github.com/mockito/mockito/compare/v2.21.5...v2.22.0) by [Szczepan Faber](https://github.com/mockitoguy) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.22.0-green.svg)](https://bintray.com/mockito/maven/mockito/2.22.0)
 - Bump minor version [(#1489)](https://github.com/mockito/mockito/pull/1489)

#### 2.21.5
 - 2018-09-05 - [2 commits](https://github.com/mockito/mockito/compare/v2.21.4...v2.21.5) by [Rafael Winterhalter](https://github.com/raphw) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.21.5-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.21.5)
 - Updates Byte Buddy to latest version. [(#1487)](https://github.com/mockito/mockito/pull/1487)

#### 2.21.4
 - 2018-08-25 - [1 commit](https://github.com/mockito/mockito/compare/v2.21.3...v2.21.4) by [Allan Wang](https://github.com/AllanWang) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.21.4-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.21.4)
 - Fix some typos [(#1484)](https://github.com/mockito/mockito/pull/1484)

#### 2.21.3
 - 2018-08-22 - [3 commits](https://github.com/mockito/mockito/compare/v2.21.2...v2.21.3) by [Szczepan Faber](https://github.com/mockitoguy) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.21.3-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.21.3)
 - stripAnnotations fix [(#1464)](https://github.com/mockito/mockito/pull/1464)

#### 2.21.2
 - 2018-08-19 - [4 commits](https://github.com/mockito/mockito/compare/v2.21.1...v2.21.2) by [Szczepan Faber](https://github.com/mockitoguy) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.21.2-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.21.2)
 - [Bugfixes] MockitoSession API does not clean up listener when initMocks fails [(#1475)](https://github.com/mockito/mockito/pull/1475)

#### 2.21.1
 - 2018-08-19 - [8 commits](https://github.com/mockito/mockito/compare/v2.21.0...v2.21.1) by [Szczepan Faber](https://github.com/mockitoguy) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.21.1-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.21.1)
 - Comprehensive coverage for timeout + inOrder [(#1474)](https://github.com/mockito/mockito/pull/1474)
 - Moved TestNG from the main repo into separate mockito/mockito-testng repo [(#1465)](https://github.com/mockito/mockito/pull/1465)
 - Added Gradle-based retry logic for tests [(#1462)](https://github.com/mockito/mockito/pull/1462)

#### 2.21.0
 - 2018-07-31 - [4 commits](https://github.com/mockito/mockito/compare/v2.20.7...v2.21.0) by [Sangwoo Lee](https://github.com/marchpig) (2), [Szczepan Faber](https://github.com/mockitoguy) (2) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.21.0-green.svg)](https://bintray.com/mockito/maven/mockito/2.21.0)
 - stubOnly() must prevent verifyNoMoreInvocations() [(#1459)](https://github.com/mockito/mockito/pull/1459)

#### 2.20.7
 - 2018-07-30 - [9 commits](https://github.com/mockito/mockito/compare/v2.20.6...v2.20.7) by [Szczepan Faber](https://github.com/mockitoguy) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.20.7-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.20.7)
 - Comprehensive test coverage for timeout() and after() [(#1452)](https://github.com/mockito/mockito/pull/1452)

#### 2.20.6
 - 2018-07-30 - [3 commits](https://github.com/mockito/mockito/compare/v2.20.5...v2.20.6) by [Sangwoo Lee](https://github.com/marchpig) (2), [Szczepan Faber](https://github.com/mockitoguy) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.20.6-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.20.6)
 - Add StubbingLookupEvent and Notifier (a part of #793) [(#1458)](https://github.com/mockito/mockito/pull/1458)

#### 2.20.5
 - 2018-07-28 - [5 commits](https://github.com/mockito/mockito/compare/v2.20.4...v2.20.5) by [Szczepan Faber](https://github.com/mockitoguy) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.20.5-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.20.5)
 - Added validation of artifact IDs [(#1450)](https://github.com/mockito/mockito/pull/1450)
 - Artifact ID changes [(#1444)](https://github.com/mockito/mockito/issues/1444)
 - Updated test coverage for async verification [(#1429)](https://github.com/mockito/mockito/pull/1429)

#### 2.20.4
 - 2018-07-27 - [3 commits](https://github.com/mockito/mockito/compare/v2.20.3...v2.20.4) by [Szczepan Faber](https://github.com/mockitoguy) (2), [Sangwoo Lee](https://github.com/marchpig) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.20.4-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.20.4)
 - Javadoc: #1438 Update ignoreStubs to include strictness [(#1439)](https://github.com/mockito/mockito/pull/1439)

#### 2.20.3
 - 2018-07-27 - [5 commits](https://github.com/mockito/mockito/compare/v2.20.2...v2.20.3) by [Szczepan Faber](https://github.com/mockitoguy) (3), [Tim van der Lippe](https://github.com/TimvdLippe) (2) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.20.3-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.20.3)
 - Support local parameters in test methods with JUnit Jupiter [(#1350)](https://github.com/mockito/mockito/pull/1350)

#### 2.20.2
 - 2018-07-27 - [2 commits](https://github.com/mockito/mockito/compare/v2.20.1...v2.20.2) by [Rafael Winterhalter](https://github.com/raphw) (1), [Szczepan Faber](https://github.com/mockitoguy) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.20.2-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.20.2)
 - Updates Byte Buddy to add preliminary support for Java 12. [(#1448)](https://github.com/mockito/mockito/pull/1448)

# 2.20.1 (2018-07-26) published to [JCenter](https://bintray.com/mockito/maven/mockito/2.20.1)/[Maven Central](http://search.maven.org/#artifactdetails%7Corg.mockito%7Cmockito-core%7C2.20.1%7Cjar)

Important changes since previous notable release:
 - new lenient() API - see blog post
 - Java 11 compatibility
 - fixed wrong artifact ids (regression in 2.20.0)

2.20.1 changes:
 - 2018-07-26 - [2 commits](https://github.com/mockito/mockito/compare/v2.20.0...v2.20.1) by [Szczepan Faber](https://github.com/mockitoguy) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.20.1-green.svg)](https://bintray.com/mockito/maven/mockito/2.20.1)
 - Workaround for wrong artifact ids [(#1446)](https://github.com/mockito/mockito/pull/1446)

#### 2.20.0

__Deprecated__ because auxiliary artifacts were published with wrong identifiers ([#1444](https://github.com/mockito/mockito/issues/1444))

 - 2018-07-24 - [55 commits](https://github.com/mockito/mockito/compare/v2.19.6...v2.20.0) by [Szczepan Faber](https://github.com/mockitoguy) (52), epeee (3) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.20.0-green.svg)](https://bintray.com/mockito/maven/mockito/2.20.0)
 - Update gradle (v4.9) and shipkit (v2.0.28) [(#1435)](https://github.com/mockito/mockito/pull/1435)
 - Strictness configurable per mock / stubbing [(#1272)](https://github.com/mockito/mockito/pull/1272)

#### 2.19.6
 - 2018-07-24 - [3 commits](https://github.com/mockito/mockito/compare/v2.19.5...v2.19.6) by [Sangwoo Lee](https://github.com/marchpig) (2), [Szczepan Faber](https://github.com/mockitoguy) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.19.6-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.19.6)
 - Javadoc: Correct wrong links and code examples [(#1440)](https://github.com/mockito/mockito/pull/1440)

#### 2.19.5
 - 2018-07-21 - [2 commits](https://github.com/mockito/mockito/compare/v2.19.4...v2.19.5) by [Sangwoo Lee](https://github.com/marchpig) (1), [Szczepan Faber](https://github.com/mockitoguy) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.19.5-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.19.5)
 - Javadoc: Update code example (ignoreStubs) [(#1436)](https://github.com/mockito/mockito/pull/1436)

#### 2.19.4
 - 2018-07-20 - [4 commits](https://github.com/mockito/mockito/compare/v2.19.3...v2.19.4) by [Szczepan Faber](https://github.com/mockitoguy) (2), epeee (1), [Sangwoo Lee](https://github.com/marchpig) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.19.4-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.19.4)
 - Javadoc: Update broken code example [(#1434)](https://github.com/mockito/mockito/pull/1434)
 - Update gradle plugins [(#1432)](https://github.com/mockito/mockito/pull/1432)

#### 2.19.3
 - 2018-07-18 - [1 commit](https://github.com/mockito/mockito/compare/v2.19.2...v2.19.3) by [Szczepan Faber](https://github.com/mockitoguy) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.19.3-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.19.3)
 - Fixed Travis build [(#1433)](https://github.com/mockito/mockito/pull/1433)

#### 2.19.2
 - 2018-07-16 - [2 commits](https://github.com/mockito/mockito/compare/v2.19.1...v2.19.2) by [Szczepan Faber](https://github.com/mockitoguy) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.19.2-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.19.2)
 - [Documentation] Updated documentation of timeout() and after() [(#1425)](https://github.com/mockito/mockito/pull/1425)

#### 2.19.1
 - 2018-07-13 - [2 commits](https://github.com/mockito/mockito/compare/v2.19.0...v2.19.1) by [Robert Stupp](https://github.com/snazy) (1), [Szczepan Faber](https://github.com/mockitoguy) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.19.1-green.svg)](https://bintray.com/mockito/maven/mockito/2.19.1)
 - mockito on Java 11-ea+21 [(#1426)](https://github.com/mockito/mockito/pull/1426)
 - Provide Java 11 compatibility [(#1419)](https://github.com/mockito/mockito/issues/1419)

#### 2.19.0
 - 2018-06-15 - [1 commit](https://github.com/mockito/mockito/compare/v2.18.9...v2.19.0) by [Tim van der Lippe](https://github.com/TimvdLippe) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.19.0-green.svg)](https://bintray.com/mockito/maven/mockito/2.19.0)
 - No pull requests referenced in commit messages.

#### 2.18.9
 - 2018-06-14 - [1 commit](https://github.com/mockito/mockito/compare/v2.18.8...v2.18.9) by [Kayvan Najafzadeh](https://github.com/kayvannj) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.18.9-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.18.9)
 - removing redundent sentence [(#1407)](https://github.com/mockito/mockito/pull/1407)

#### 2.18.8
 - 2018-06-11 - [1 commit](https://github.com/mockito/mockito/compare/v2.18.7...v2.18.8) by [Kengo TODA](https://github.com/KengoTODA) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.18.8-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.18.8)
 - Fix wrong artifact name in javadoc, and link to proper javadoc page [(#1404)](https://github.com/mockito/mockito/pull/1404)

#### 2.18.7
 - 2018-06-08 - [3 commits](https://github.com/mockito/mockito/compare/v2.18.6...v2.18.7) by [Szczepan Faber](https://github.com/mockitoguy) (1), [Venkata Jaswanth](https://github.com/aj-jaswanth) (1), [訾明华](https://github.com/ziminghua) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.18.7-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.18.7)
 - Fixes #1401 : Correctly handle VarargsMatcher check for HamcrestMatchers [(#1402)](https://github.com/mockito/mockito/pull/1402)
 - VarargsMatcher doesn't work correctly when using HamcrestMatchers in Mockito 2 [(#1401)](https://github.com/mockito/mockito/issues/1401)
 - Update CONTRIBUTING.md [(#1399)](https://github.com/mockito/mockito/pull/1399)

#### 2.18.6
 - 2018-05-17 - [1 commit](https://github.com/mockito/mockito/compare/v2.18.5...v2.18.6) by [Anuraag Agrawal](https://github.com/anuraaga) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.18.6-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.18.6)
 - Make MockitoExtension constructor public [(#1391)](https://github.com/mockito/mockito/pull/1391)

#### 2.18.5
 - 2018-04-28 - [2 commits](https://github.com/mockito/mockito/compare/v2.18.4...v2.18.5) by [Erhard Pointl](https://github.com/epeee) (1), [Rafael Winterhalter](https://github.com/raphw) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.18.5-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.18.5)
 - Upgrade errorprone to 2.3.1 [(#1380)](https://github.com/mockito/mockito/pull/1380)
 - Avoid circularity: avoid endless loop during mock creation [(#1378)](https://github.com/mockito/mockito/pull/1378)
 - Mockito Inline can't mock Finatra Response Class [(#1240)](https://github.com/mockito/mockito/issues/1240)

#### 2.18.4
 - 2018-04-21 - [1 commit](https://github.com/mockito/mockito/compare/v2.18.3...v2.18.4) by [Rafael Winterhalter](https://github.com/raphw) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.18.4-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.18.4)
 - Update Byte Buddy and cache some expensive values [(#1377)](https://github.com/mockito/mockito/pull/1377)

#### 2.18.3
 - 2018-04-20 - [2 commits](https://github.com/mockito/mockito/compare/v2.18.2...v2.18.3) by [Erhard Pointl](https://github.com/epeee) (1), [Tim van der Lippe](https://github.com/TimvdLippe) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.18.3-green.svg)](https://bintray.com/mockito/maven/mockito/2.18.3)
 - Upgrade Gradle to 4.7 [(#1375)](https://github.com/mockito/mockito/pull/1375)

#### 2.18.2
 - 2018-04-15 - [1 commit](https://github.com/mockito/mockito/compare/v2.18.1...v2.18.2) by [Rafael Winterhalter](https://github.com/raphw) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.18.2-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.18.2)
 - [Bugfixes] Unable to mock interfaces in Android instrumentation tests [(#1364)](https://github.com/mockito/mockito/issues/1364)
 - Performance improvements [(#1369)](https://github.com/mockito/mockito/pull/1369)

#### 2.18.1
 - 2018-04-13 - [1 commit](https://github.com/mockito/mockito/compare/v2.18.0...v2.18.1) by [Rafael Winterhalter](https://github.com/raphw) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.18.1-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.18.1)
 - Resolve the correct protection domain for the codegen package when using reflection. [(#1368)](https://github.com/mockito/mockito/pull/1368)

#### 2.18.0
 - 2018-04-07 - [1 commit](https://github.com/mockito/mockito/compare/v2.17.8...v2.18.0) by [Tim van der Lippe](https://github.com/TimvdLippe) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.18.0-green.svg)](https://bintray.com/mockito/maven/mockito/2.18.0)
 - No pull requests referenced in commit messages.

#### 2.17.8
 - 2018-04-07 - [1 commit](https://github.com/mockito/mockito/compare/v2.17.7...v2.17.8) by [Rafael Winterhalter](https://github.com/raphw) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.17.8-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.17.8)
 - Use java.lang.MethodHandles.Lookup::defineClass  [(#1355)](https://github.com/mockito/mockito/pull/1355)

#### 2.17.7
 - 2018-04-04 - [15 commits](https://github.com/mockito/mockito/compare/v2.17.6...v2.17.7) by 4 authors - published to [![Bintray](https://img.shields.io/badge/Bintray-2.17.7-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.17.7)
 - Commits: [Szczepan Faber](https://github.com/mockitoguy) (7), [Dmitry Timofeev](https://github.com/dmitry-timofeev) (5), [Erhard Pointl](https://github.com/epeee) (2), [Tim van der Lippe](https://github.com/TimvdLippe) (1)
 - [Bugfixes] When custom exception fillInstackTrace() returns null, Exception mock cannot work properly [(#866)](https://github.com/mockito/mockito/issues/866)
 - Apply errorprone to all java projects [(#1358)](https://github.com/mockito/mockito/pull/1358)
 - Make use of shipkit v2.0.15 [(#1357)](https://github.com/mockito/mockito/pull/1357)
 - Add JDK 10 and 11 to the build matrix [(#1356)](https://github.com/mockito/mockito/pull/1356)
 - Improved stubbing internals and test coverage [(#1324)](https://github.com/mockito/mockito/pull/1324)
 - Improve BaseStubbing & ThrowsException internals [(#1323)](https://github.com/mockito/mockito/issues/1323)

#### 2.17.6
 - 2018-04-01 - [1 commit](https://github.com/mockito/mockito/compare/v2.17.5...v2.17.6) by [Stephan Schroevers](https://github.com/Stephan202) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.17.6-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.17.6)
 - Cleaner code for Mockito users by better integration with static code checkers, more @CheckReturnValue [(#1270)](https://github.com/mockito/mockito/pull/1270)
 - Add @CheckReturnValue to stubbing/verification methods [(#1228)](https://github.com/mockito/mockito/pull/1228)
 - Mockito should annotate when() methods with a custom annotation @CheckReturnValue [(#1130)](https://github.com/mockito/mockito/issues/1130)

#### 2.17.5
 - 2018-03-31 - [1 commit](https://github.com/mockito/mockito/compare/v2.17.4...v2.17.5) by [Philip P. Moltmann](https://github.com/moltmann) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.17.5-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.17.5)
 - Enable errorprone build [(#1339)](https://github.com/mockito/mockito/pull/1339)
 - Statements in org.mockito.internal.util.PrimitivesTest#should_check_that_value_type_is_assignable_to_wrapper_reference are not true [(#1338)](https://github.com/mockito/mockito/issues/1338)
 - org.mockitousage.bugs.varargs.VarargsNotPlayingWithAnyObjectTest#shouldNotAllowUsingAnyObjectForVarArgs reaches fail [(#1337)](https://github.com/mockito/mockito/issues/1337)

#### 2.17.4
 - 2018-03-31 - [1 commit](https://github.com/mockito/mockito/compare/v2.17.3...v2.17.4) by [Erhard Pointl](https://github.com/epeee) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.17.4-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.17.4)
 - Make use of shipkit v2.0.13 [(#1354)](https://github.com/mockito/mockito/pull/1354)

**2.17.3 (2018-03-29)** - [2 commits](https://github.com/mockito/mockito/compare/v2.17.2...v2.17.3) by [Rafael Winterhalter](http://github.com/raphw) (1), [Szczepan Faber](http://github.com/mockitoguy) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.17.3-green.svg)](https://bintray.com/mockito/maven/mockito-development2.17.3)
 - Update Byte Buddy and ASM for bug fix releases [(#1351)](https://github.com/mockito/mockito/pull/1351)

**2.17.2 (2018-03-26)** - [3 commits](https://github.com/mockito/mockito/compare/v2.17.1...v2.17.2) by [Erhard Pointl](https://github.com/epeee) (1), [Serge Bishyr](https://github.com/SeriyBg) (1), [Tim van der Lippe](http://github.com/TimvdLippe) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.17.2-green.svg)](https://bintray.com/mockito/maven/mockito-development2.17.2)
 - [Bugfixes] Different mocks are used for @Mock and @InjectMock in the same test class with JUnit 5 extension [(#1346)](https://github.com/mockito/mockito/issues/1346)
 - Fix #1346 - Different mocks are used for @Mock and @InjectMock in the same test class with JUnit 5 extension [(#1349)](https://github.com/mockito/mockito/pull/1349)
 - Update shipkit to v2.0.12 [(#1347)](https://github.com/mockito/mockito/pull/1347)

**2.17.1 (2018-03-25)** - [3 commits](https://github.com/mockito/mockito/compare/v2.17.0...v2.17.1) by [Erhard Pointl](https://github.com/epeee) (2), [Serge Bishyr](https://github.com/SeriyBg) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.17.1-green.svg)](https://bintray.com/mockito/maven/mockito-development2.17.1)
 - junit jupiter extension dependencies [(#1344)](https://github.com/mockito/mockito/pull/1344)
 - Update gradle plugins (buildscan and animalsniffer) [(#1343)](https://github.com/mockito/mockito/pull/1343)
 - Fixes #1314 : Include all the invocation in mock verification error message [(#1319)](https://github.com/mockito/mockito/pull/1319)
 - Undesired invocation message improvements [(#1314)](https://github.com/mockito/mockito/issues/1314)

**2.17.0 (2018-03-24)** - [1 commit](https://github.com/mockito/mockito/compare/v2.16.3...v2.17.0) by [Tim van der Lippe](http://github.com/TimvdLippe) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.17.0-green.svg)](https://bintray.com/mockito/maven/mockito2.17.0)
 - No pull requests referenced in commit messages.

**2.16.3 (2018-03-24)** - [1 commit](https://github.com/mockito/mockito/compare/v2.16.2...v2.16.3) by [Christian Schwarz](https://github.com/ChristianSchwarz) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.16.3-green.svg)](https://bintray.com/mockito/maven/mockito-development2.16.3)
 - MockitoExtension for JUnit5  [(#1221)](https://github.com/mockito/mockito/pull/1221)

**2.16.2 (2018-03-23)** - [5 commits](https://github.com/mockito/mockito/compare/v2.16.1...v2.16.2) by [Pascal Schumacher](https://github.com/PascalSchumacher) (3), [Rafael Winterhalter](http://github.com/raphw) (2) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.16.2-green.svg)](https://bintray.com/mockito/maven/mockito-development2.16.2)
 - Update to AssertJ 2.9.0 [(#1342)](https://github.com/mockito/mockito/pull/1342)
 - Update to ASM 6.1 [(#1341)](https://github.com/mockito/mockito/pull/1341)
 - Update to Byte Buddy 1.8.0 [(#1340)](https://github.com/mockito/mockito/pull/1340)

**2.16.1 (2018-03-15)** - [3 commits](https://github.com/mockito/mockito/compare/v2.16.0...v2.16.1) by [Szczepan Faber](http://github.com/mockitoguy) (2), [Rafael Winterhalter](http://github.com/raphw) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.16.1-green.svg)](https://bintray.com/mockito/maven/mockito-development2.16.1)
 - Prevent class loading race condition [(#1258)](https://github.com/mockito/mockito/pull/1258)
 - Deadlock after upgrading to Mockito 2 and parallel tests execution [(#1067)](https://github.com/mockito/mockito/issues/1067)

**2.16.0 (2018-03-13)** - [14 commits](https://github.com/mockito/mockito/compare/v2.15.6...v2.16.0) by [Szczepan Faber](http://github.com/mockitoguy) (13), [Philip P. Moltmann](https://github.com/moltmann) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.16.0-green.svg)](https://bintray.com/mockito/maven/mockito2.16.0)
 - Prevent memory leaks with inline MockMakers by using weak refs to mocks [(#1321)](https://github.com/mockito/mockito/pull/1321)
 - InlineByteBuddyMockMaker does not clean up stale mocks [(#1313)](https://github.com/mockito/mockito/issues/1313)

**2.15.6 (2018-03-05)** - [2 commits](https://github.com/mockito/mockito/compare/v2.15.5...v2.15.6) by [Philip P. Moltmann](https://github.com/moltmann) (1), [Szczepan Faber](http://github.com/mockitoguy) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.15.6-green.svg)](https://bintray.com/mockito/maven/mockito-development2.15.6)
 - Avoid bytebuddy import Issue to enable repackaging without bytebuddy [(#1320)](https://github.com/mockito/mockito/pull/1320)

**2.15.5 (2018-03-02)** - [1 commit](https://github.com/mockito/mockito/compare/v2.15.4...v2.15.5) by [Sanne Grinovero](https://github.com/Sanne) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.15.5-green.svg)](https://bintray.com/mockito/maven/mockito-development2.15.5)
 - Fixes #1326 : Reduce the allocation rate for the typical use of Locat… [(#1327)](https://github.com/mockito/mockito/pull/1327)
 - Reduce memory consumption of the typical LocationImpl [(#1326)](https://github.com/mockito/mockito/issues/1326)

**2.15.4 (2018-02-19)** - [26 commits](https://github.com/mockito/mockito/compare/v2.15.3...v2.15.4) by [Szczepan Faber](http://github.com/mockitoguy) (20), [Philip P. Moltmann](https://github.com/moltmann) (5), [Tim van der Lippe](http://github.com/TimvdLippe) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.15.4-green.svg)](https://bintray.com/mockito/maven/mockito-development2.15.4)
 - Update Kotlin version to be compatible with the IntelliJ runtime version [(#1315)](https://github.com/mockito/mockito/pull/1315)
 - Make  org.mockito.internal.creation.instance.Instantiator a public API [(#1303)](https://github.com/mockito/mockito/issues/1303)

**2.15.3 (2018-02-15)** - [4 commits](https://github.com/mockito/mockito/compare/v2.15.2...v2.15.3) by [Szczepan Faber](http://github.com/mockitoguy) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.15.3-green.svg)](https://bintray.com/mockito/maven/mockito-development2.15.3)
 - Clean up, removed dead code, rename job, documentation tidy up [(#1290)](https://github.com/mockito/mockito/pull/1290)

**2.15.2 (2018-02-13)** - [2 commits](https://github.com/mockito/mockito/compare/v2.15.1...v2.15.2) by [Alberto Scotto](https://github.com/alb-i986) (1), [Szczepan Faber](http://github.com/mockitoguy) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.15.2-green.svg)](https://bintray.com/mockito/maven/mockito-development2.15.2)
 - Javadoc: Improve discoverability of #ignoreStubs [(#1294)](https://github.com/mockito/mockito/pull/1294)

**2.15.1 (2018-02-12)** - [8 commits](https://github.com/mockito/mockito/compare/v2.15.0...v2.15.1) by [Szczepan Faber](http://github.com/mockitoguy) (5), r-smirnov (3) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.15.1-green.svg)](https://bintray.com/mockito/maven/mockito-development2.15.1)
 - [Bugfixes] Ensure isolation of stubbings [(#1310)](https://github.com/mockito/mockito/pull/1310)
 - Cannot override stubbed method that calls a stubbed method [(#1279)](https://github.com/mockito/mockito/issues/1279)

# 2.15.0 (2018-02-10) published to [JCenter](https://bintray.com/mockito/maven/mockito/2.15.0)/[Maven Central](http://search.maven.org/#artifactdetails%7Corg.mockito%7Cmockito-core%7C2.15.0%7Cjar)

Notable changes:
 - new APIs to ```MockitoSession``` feature that can be used by framework integrations such as JUnit Jupiter.
 - ```InvocationFactory``` API improvements driven by integrations with Android.
 - Javadoc updates.

**2.15.0 (2018-02-10)** - [16 commits](https://github.com/mockito/mockito/compare/v2.14.0...v2.15.0) by [Marc Philipp](https://github.com/marcphilipp) (10), [Szczepan Faber](http://github.com/mockitoguy) (6) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.15.0-green.svg)](https://bintray.com/mockito/maven/mockito2.15.0)
 - Extend MockitoSession(Builder) API to enable usage by testing frameworks [(#1301)](https://github.com/mockito/mockito/pull/1301)
 - [JUnit Jupiter] MockitoSession#initMocks should support multiple test instances   [(#1232)](https://github.com/mockito/mockito/issues/1232)
 - Design MockitoSession API improvements for unit test frameworks [(#898)](https://github.com/mockito/mockito/issues/898)

**2.14.0 (2018-02-09)** - [12 commits](https://github.com/mockito/mockito/compare/v2.13.3...v2.14.0) by 4 authors - published to [![Bintray](https://img.shields.io/badge/Bintray-2.14.0-green.svg)](https://bintray.com/mockito/maven/mockito-development2.14.0)
 - Commits: [Szczepan Faber](http://github.com/mockitoguy) (9), [Arend v. Reinersdorff](https://github.com/arend-von-reinersdorff) (1), Philip P. Moltmann (1), [Tim van der Lippe](http://github.com/TimvdLippe) (1)
 - Update public API of InvocationFactory needed for Android static mocking [(#1307)](https://github.com/mockito/mockito/pull/1307)
 - InvocationFactory.createInvocation's realmethod cannot throw a Throwable [(#1306)](https://github.com/mockito/mockito/issues/1306)
 - Fix link of Maven Central badge [(#1299)](https://github.com/mockito/mockito/pull/1299)

**2.13.3 (2018-01-16)** - [3 commits](https://github.com/mockito/mockito/compare/v2.13.2...v2.13.3) by [Szczepan Faber](http://github.com/mockitoguy) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.13.3-green.svg)](https://bintray.com/mockito/maven/mockito-development2.13.3)
 - Extra test case, javadoc update [(#1282)](https://github.com/mockito/mockito/pull/1282)

**2.13.2 (2018-01-16)** - [2 commits](https://github.com/mockito/mockito/compare/v2.13.1...v2.13.2) by [Serge Bishyr](https://github.com/SeriyBg) (1), [Szczepan Faber](http://github.com/mockitoguy) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.13.2-green.svg)](https://bintray.com/mockito/maven/mockito-development2.13.2)
 - [Documentation] Improve documentation about partial mocks and doReturn() style of stubbing [(#1262)](https://github.com/mockito/mockito/issues/1262)
 - Fixes #1262: update doc for Answers.CALLS_REAL_METHODS [(#1268)](https://github.com/mockito/mockito/pull/1268)

**2.13.1 (2017-12-27)** - [1 commit](https://github.com/mockito/mockito/compare/v2.13.0...v2.13.1) by [Niklas Baudy](https://github.com/vanniktech) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.13.1-green.svg)](https://bintray.com/mockito/maven/mockito-development2.13.1)
 - [Documentation] Documentation update - MockitoJUnit.strictness(STRICT_STUBS) does not do verifyNoMoreInteractions [(#1086)](https://github.com/mockito/mockito/issues/1086)
 - Update STRICT_STUBS documentation for verifyNoMoreInteractions [(#1280)](https://github.com/mockito/mockito/pull/1280)

# 2.13.0 (2017-12-06) published to [JCenter](https://bintray.com/mockito/maven/mockito/2.13.0)/[Maven Central](http://search.maven.org/#artifactdetails%7Corg.mockito%7Cmockito-core%7C2.13.0%7Cjar)

 - [1 commit](https://github.com/mockito/mockito/compare/v2.12.2...v2.13.0) by [Tim van der Lippe](http://github.com/TimvdLippe)
 - No pull requests referenced in commit messages.

**2.12.2 (2017-11-28)** - [2 commits](https://github.com/mockito/mockito/compare/v2.12.1...v2.12.2) by [Rafael Winterhalter](http://github.com/raphw) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.12.2-green.svg)](https://bintray.com/mockito/maven/mockito-development2.12.2)
 - [Bugfixes] Wanted but not invoked on 2.12.0, but not on 2.11.0 [(#1254)](https://github.com/mockito/mockito/issues/1254)
 - Fixes #1254 and #1256: improved check for self-invocation. [(#1257)](https://github.com/mockito/mockito/pull/1257)
 - Unbounded recursion when spying with `mockito-inline` [(#1256)](https://github.com/mockito/mockito/issues/1256)

**2.12.1 (2017-11-28)** - [6 commits](https://github.com/mockito/mockito/compare/v2.12.0...v2.12.1) by epeee (3), [Tim van der Lippe](http://github.com/TimvdLippe) (2), [Szczepan Faber](http://github.com/mockitoguy) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.12.1-green.svg)](https://bintray.com/mockito/maven/mockito-development2.12.1)
 - Update shipkit and configure assertReleaseNeeded task [(#1265)](https://github.com/mockito/mockito/pull/1265)
 - Update Kotlin to latest version [(#1263)](https://github.com/mockito/mockito/pull/1263)
 - Remove several container classes and inline code [(#1247)](https://github.com/mockito/mockito/pull/1247)

# 2.12.0 (2017-11-07) published to [JCenter](https://bintray.com/mockito/maven/mockito/2.12.0)/[Maven Central](http://search.maven.org/#artifactdetails%7Corg.mockito%7Cmockito-core%7C2.12.0%7Cjar)

 - [4 commits](https://github.com/mockito/mockito/compare/v2.11.7...v2.12.0) by [Rafael Winterhalter](http://github.com/raphw) (2), [Tim van der Lippe](http://github.com/TimvdLippe) (2)
 - No pull requests referenced in commit messages.

**2.11.7 (2017-11-06)** - [2 commits](https://github.com/mockito/mockito/compare/v2.11.6...v2.11.7) by [Rafael Winterhalter](http://github.com/raphw) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.11.7-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.11.7)
 - [Bugfixes] Provide Java 10 compatibility [(#1243)](https://github.com/mockito/mockito/issues/1243)
 - Fixes #1243: Support for new version number 10. [(#1250)](https://github.com/mockito/mockito/pull/1250)

**2.11.6 (2017-11-05)** - [3 commits](https://github.com/mockito/mockito/compare/v2.11.5...v2.11.6) by [Tim van der Lippe](http://github.com/TimvdLippe) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.11.6-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.11.6)
 - [Enhancements] Enable jacoco coverage on subprojects [(#1229)](https://github.com/mockito/mockito/pull/1229)
 - Remove service-worker from Javadoc generation [(#1244)](https://github.com/mockito/mockito/pull/1244)
 - Enable Checkstyle for checking license headers [(#1242)](https://github.com/mockito/mockito/pull/1242)

**2.11.5 (2017-10-31)** - [4 commits](https://github.com/mockito/mockito/compare/v2.11.4...v2.11.5) by [Tim van der Lippe](http://github.com/TimvdLippe) (3), [Szczepan Faber](http://github.com/mockitoguy) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.11.5-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.11.5)
 - Add @CheckReturnValue to stubbing/verification methods [(#1228)](https://github.com/mockito/mockito/pull/1228)
 - Add regression test for issue #1174 [(#1219)](https://github.com/mockito/mockito/pull/1219)

**2.11.4 (2017-10-29)** - [2 commits](https://github.com/mockito/mockito/compare/v2.11.3...v2.11.4) by [Szczepan Faber](http://github.com/mockitoguy) (1), [Tim van der Lippe](http://github.com/TimvdLippe) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.11.4-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.11.4)
 - Update license plugin and add missing license headers [(#1227)](https://github.com/mockito/mockito/pull/1227)

**2.11.3 (2017-10-28)** - [1 commit](https://github.com/mockito/mockito/compare/v2.11.2...v2.11.3) by [Allon Murienik](https://github.com/mureinik) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.11.3-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.11.3)
 - InvocationsPrinter string concatination [(#1231)](https://github.com/mockito/mockito/pull/1231)

**2.11.2 (2017-10-22)** - [7 commits](https://github.com/mockito/mockito/compare/v2.11.1...v2.11.2) by [Rafael Winterhalter](http://github.com/raphw) (6), [Allon Murienik](https://github.com/mureinik) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.11.2-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.11.2)
 - Updated Byte Buddy and ASM dependencies. Fixes #1215. [(#1218)](https://github.com/mockito/mockito/pull/1218)
 - Fixes #1183: Make override check more forgiving to accomondate Kotlin compile patterns. [(#1217)](https://github.com/mockito/mockito/pull/1217)
 - Adresses #1206: allow opting out from annotation copying within mocks. [(#1216)](https://github.com/mockito/mockito/pull/1216)
 - ClassFormatError when trying to mock certain interfaces [(#1215)](https://github.com/mockito/mockito/issues/1215)
 - Standardize JUnit imports [(#1213)](https://github.com/mockito/mockito/pull/1213)
 - Mockito should not copy annotations in all cases [(#1206)](https://github.com/mockito/mockito/issues/1206)
 - UnfinishedVerificationException with Kotlin after updating to 2.9.0 [(#1183)](https://github.com/mockito/mockito/issues/1183)

**2.11.1 (2017-10-20)** - [4 commits](https://github.com/mockito/mockito/compare/v2.11.0...v2.11.1) by [Szczepan Faber](http://github.com/mockitoguy) (2), [Allon Murienik](https://github.com/mureinik) (1), [rberghegger](https://github.com/rberghegger) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.11.1-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.11.1)
 - Fixes #1211: Improve @deprecated JavaDoc [(#1214)](https://github.com/mockito/mockito/pull/1214)
 - improve @deprecated JavaDoc of MockitoDebugger [(#1211)](https://github.com/mockito/mockito/issues/1211)
 - Clean up JUnit imports [(#1209)](https://github.com/mockito/mockito/pull/1209)
 - Updated my GitHub user name [(#1208)](https://github.com/mockito/mockito/pull/1208)

# 2.11.0 (2017-10-14) published to [JCenter](https://bintray.com/mockito/maven/mockito/2.11.0)/[Maven Central](http://search.maven.org/#artifactdetails%7Corg.mockito%7Cmockito-core%7C2.11.0%7Cjar)

New public API for Spring Boot integration will improve compatibility of Mockito and reduce version conflict problems. We carefully grow our public API for framework integrations. This time we added new "Verification Started Listener", needed to resolve [double proxy use case](https://github.com/mockito/mockito/issues/1191). Using this API, Spring Boot framework no longer needs to depend on internal Mockito API.

We also added `Automatic-Module-Name` to mockito jar, useful in Java9 context [(#1189)](https://github.com/mockito/mockito/issues/1189).

**2.11.0 (2017-10-14)** - [20 commits](https://github.com/mockito/mockito/compare/v2.10.5...v2.11.0) by [Szczepan Faber](http://github.com/szczepiq) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.11.0-green.svg)](https://bintray.com/mockito/maven/mockito/2.11.0)
 - Added new public API for spring-boot use case [(#1192)](https://github.com/mockito/mockito/pull/1192)

**2.10.5 (2017-10-14)** - [12 commits](https://github.com/mockito/mockito/compare/v2.10.4...v2.10.5) by [Marcin Zajączkowski](https://github.com/szpak) (8), [Szczepan Faber](http://github.com/mockitoguy) (3), [Allon Murienik](https://github.com/mureinik) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.10.5-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.10.5)
 - [Java 9 support] JDK9: set a stable module name with `Automatic-Module-Name` entry in MANIFEST [(#1189)](https://github.com/mockito/mockito/issues/1189)
 - Fixed documentation issue, added unit tests [(#1203)](https://github.com/mockito/mockito/pull/1203)
 - [#1202] Java 9 CI build for Mockito 2.x [(#1202)](https://github.com/mockito/mockito/pull/1202)
 - Fixes #1189: Add stable module name [(#1195)](https://github.com/mockito/mockito/pull/1195)

**2.10.4 (2017-10-08)** - [1 commit](https://github.com/mockito/mockito/compare/v2.10.3...v2.10.4) by [Allon Murienik](https://github.com/mureinik) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.10.4-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.10.4)
 - Fix javadoc in PluginLoader#withAlias' javadoc [(#1201)](https://github.com/mockito/mockito/pull/1201)

**2.10.3 (2017-10-07)** - [1 commit](https://github.com/mockito/mockito/compare/v2.10.2...v2.10.3) by [Christian Schwarz](https://github.com/ChristianSchwarz) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.10.3-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.10.3)
 - Fix for #1155 thenThrow(Class)  [(#1162)](https://github.com/mockito/mockito/pull/1162)
 - thenThrow(Class) should only throw checked exceptions that match the signature of the stubbed method [(#1155)](https://github.com/mockito/mockito/issues/1155)

**2.10.2 (2017-10-06)** - [3 commits](https://github.com/mockito/mockito/compare/v2.10.1...v2.10.2) by [Allon Murienik](https://github.com/mureinik) (1), [Ben Yu](https://github.com/fluentfuture) (1), [Roman Elizarov](https://github.com/elizarov) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.10.2-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.10.2)
 - Fixes 977: Javadoc on verifyZeroInteractions [(#1199)](https://github.com/mockito/mockito/pull/1199)
 - Introduce test for mock-maker-inline with Kotlin suspend functions [(#1165)](https://github.com/mockito/mockito/pull/1165)
 - Add tests to verify and show that @Spy can be used to allow stubbing/verification of List parameters using varargs. [(#1157)](https://github.com/mockito/mockito/pull/1157)

**2.10.1 (2017-10-04)** - [1 commit](https://github.com/mockito/mockito/compare/v2.10.0...v2.10.1) by [Dennis Cheung](https://github.com/hkdennis2k) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.10.1-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.10.1)
 - Enable stubOnly() on @Mock annotation [(#1146)](https://github.com/mockito/mockito/pull/1146)

# 2.10.0 (2017-09-08) published to [JCenter](https://bintray.com/mockito/maven/mockito/2.10.0)/[Maven Central](http://search.maven.org/#artifactdetails%7Corg.mockito%7Cmockito-core%7C2.10.0%7Cjar)

The main highlight of this release is the additions to the public API to accommodate advanced framework integrations. It will help other frameworks to cleanly integrate with Mockito. For more details, see [#1121](https://github.com/mockito/mockito/pull/1121).

**2.10.0 (2017-09-08)** - [42 commits](https://github.com/mockito/mockito/compare/v2.9.2...v2.10.0) by [Szczepan Faber](http://github.com/mockitoguy) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.10.0-green.svg)](https://bintray.com/mockito/maven/mockito/2.10.0)
 - Improve and develop APIs required for framework integrators [(#1121)](https://github.com/mockito/mockito/pull/1121)

**2.9.2 (2017-09-05)** - [2 commits](https://github.com/mockito/mockito/compare/v2.9.1...v2.9.2) by [Rafael Winterhalter](http://github.com/raphw) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.9.2-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.9.2)
 - Compare method of graph to defined form of method. [(#1186)](https://github.com/mockito/mockito/pull/1186)

**2.9.1 (2017-09-04)** - [11 commits](https://github.com/mockito/mockito/compare/v2.9.0...v2.9.1) by [Rafael Winterhalter](http://github.com/raphw) (9), [Szczepan Faber](http://github.com/mockitoguy) (2) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.9.1-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.9.1)
 - Travis [(#1184)](https://github.com/mockito/mockito/pull/1184)
 - #1179: Fix performance regression caused by use of method graph compiler. [(#1181)](https://github.com/mockito/mockito/pull/1181)
 - Mockito 2.9.0 is significantly slower [(#1179)](https://github.com/mockito/mockito/issues/1179)
 - Adds support for Java 9 in inline mock maker by Byte Buddy update. [(#1158)](https://github.com/mockito/mockito/pull/1158)

# 2.9.0 (2017-08-26) published to [JCenter](https://bintray.com/mockito/maven/mockito/2.9.0)/[Maven Central](http://search.maven.org/#artifactdetails%7Corg.mockito%7Cmockito-core%7C2.9.0%7Cjar)

Notable changes:
 - bumped ByteBuddy 1.6.14 -> 1.7.0 (inline mocking, bugfixes)
 - bumped Objenesis 2.5 -> 2.6 (Java9, bugfixes)

**2.9.0 (2017-08-26)** - [5 commits](https://github.com/mockito/mockito/compare/v2.8.55...v2.9.0) by [Szczepan Faber](http://github.com/mockitoguy) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.9.0-green.svg)](https://bintray.com/mockito/maven/mockito/2.9.0)
 - Updated README.md wrt maven central release [(#1172)](https://github.com/mockito/mockito/pull/1172)
 - Bumped minor version so signify new Objenesis version [(#1171)](https://github.com/mockito/mockito/pull/1171)

**2.8.55 (2017-08-21)** - [7 commits](https://github.com/mockito/mockito/compare/v2.8.54...v2.8.55) by [Szczepan Faber](http://github.com/mockitoguy) (6), [Pascal Schumacher](https://github.com/PascalSchumacher) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.8.55-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.8.55)
 - Update objenesis version 2.5 -> 2.6 (Java9, bugfixes) [(#1170)](https://github.com/mockito/mockito/pull/1170)
 - Updated to latest Shipkit, tidied up configuration [(#1169)](https://github.com/mockito/mockito/pull/1169)

**2.8.54 (2017-08-09)** - [5 commits](https://github.com/mockito/mockito/compare/v2.8.53...v2.8.54) by [Marcin Zajączkowski](https://github.com/szpak) (3), [Igor C. A. de Lima](https://github.com/igorcadelima) (1), [Szczepan Faber](http://github.com/mockitoguy) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.8.54-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.8.54)
 - [Bugfixes] Unnecessary release was triggered [(#1144)](https://github.com/mockito/mockito/issues/1144)
 - Remove duplicated word in Javadoc [(#1161)](https://github.com/mockito/mockito/pull/1161)
 - [#1147] Provide version in release commit message [(#1147)](https://github.com/mockito/mockito/pull/1147)
 - Bumped shipkit to fix unnecessary releasing problem [(#1145)](https://github.com/mockito/mockito/pull/1145)

**2.8.53 (2017-07-06)** - [6 commits](https://github.com/mockito/mockito/compare/v2.8.52...v2.8.53) by [Szczepan Faber](http://github.com/mockitoguy) (5), [Wojtek Wilk](https://github.com/wwilk) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.8.53-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.8.53)
 - Bumped shipkit, simplified Travis file, removed unnecessary code [(#1141)](https://github.com/mockito/mockito/pull/1141)
 - org.shipkit.publications-comparator plugin applied [(#1139)](https://github.com/mockito/mockito/pull/1139)

**2.8.52 (2017-06-24)** - [8 commits](https://github.com/mockito/mockito/compare/v2.8.51...v2.8.52) by [Szczepan Faber](http://github.com/mockitoguy) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.8.52-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.8.52)
 - Simplified and documented .travis.yml file [(#1133)](https://github.com/mockito/mockito/pull/1133)

**2.8.51 (2017-06-24)** - [1 commit](https://github.com/mockito/mockito/compare/v2.8.50...v2.8.51) by [Allon Murienik](https://github.com/mureinik) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.8.51-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.8.51)
 - Don't use raw rypes in UnusedStubbingsTest [(#1137)](https://github.com/mockito/mockito/pull/1137)

**2.8.50 (2017-06-24)** - [1 commit](https://github.com/mockito/mockito/compare/v2.8.49...v2.8.50) by [Rafael Winterhalter](http://github.com/raphw) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.8.50-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.8.50)
 - Fixes #1135: Properly resolve visibility bridges. [(#1136)](https://github.com/mockito/mockito/pull/1136)
 - [mock-maker-inline] Method calls on mock forwarded to real instance [(#1135)](https://github.com/mockito/mockito/issues/1135)

**2.8.49 (2017-06-22)** - [2 commits](https://github.com/mockito/mockito/compare/v2.8.48...v2.8.49) by [Rafael Winterhalter](http://github.com/raphw) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.8.49-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.8.49)
 - Improve detection on non-mockable types - ByteBuddy bump 1.6.14->1.7.0 [(#1128)](https://github.com/mockito/mockito/pull/1128)

**2.8.48 (2017-06-18)** - [2 commits](https://github.com/mockito/mockito/compare/v2.8.47...v2.8.48) by epeee (1), [Szczepan Faber](http://github.com/mockitoguy) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.8.48-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.8.48)
 - make use of shipkit v0.8.92 [(#1132)](https://github.com/mockito/mockito/pull/1132)

# 2.8.47 (2017-06-15) published to [JCenter](https://bintray.com/mockito/maven/mockito/2.8.47)/[Maven Central](http://search.maven.org/#artifactdetails%7Corg.mockito%7Cmockito-core%7C2.8.47%7Cjar)

**2.8.47 (2017-06-15)** - [1 commit](https://github.com/mockito/mockito/compare/v2.8.46...v2.8.47) by [Marcin Zajączkowski](https://github.com/szpak) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.8.47-green.svg)](https://bintray.com/mockito/maven/mockito/2.8.47)
 - No pull requests referenced in commit messages.

**2.8.46 (2017-06-15)** - [2 commits](https://github.com/mockito/mockito/compare/v2.8.45...v2.8.46) by [Szczepan Faber](http://github.com/mockitoguy) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.8.46-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.8.46)
 - Fixed issue with releases to Maven Central [(#1129)](https://github.com/mockito/mockito/pull/1129)
 - Version not released to Maven Central [(#1127)](https://github.com/mockito/mockito/issues/1127)

**2.8.45 (2017-06-13)** - [1 commit](https://github.com/mockito/mockito/compare/v2.8.44...v2.8.45) by [VChirp](https://github.com/VChirp) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.8.45-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.8.45)
 - Fix typo: Remove repeated "a" [(#1125)](https://github.com/mockito/mockito/pull/1125)

**2.8.44 (2017-06-12)** - [3 commits](https://github.com/mockito/mockito/compare/v2.8.43...v2.8.44) by [Marcin Zajaczkowski](https://github.com/szpak) (1), [Marcin Zajączkowski](https://github.com/szpak) (1), [Myrle Krantz](https://github.com/myrle-krantz) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.8.44-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.8.44)
 - [New features] Answer with delay in mock or spy to improve testing of asynchronous code [(#1117)](https://github.com/mockito/mockito/issues/1117)

**2.8.43 (2017-06-10)** - [2 commits](https://github.com/mockito/mockito/compare/v2.8.42...v2.8.43) by [Szczepan Faber](http://github.com/mockitoguy) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.8.43-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.8.43)
 - No pull requests referenced in commit messages.

**2.8.42 (2017-06-10)** - [3 commits](https://github.com/mockito/mockito/compare/v2.8.41...v2.8.42) by epeee (2), [Szczepan Faber](http://github.com/mockitoguy) (1) - published to [![Bintray](https://img.shields.io/badge/Bintray-2.8.42-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.8.42)
 - update to shipkit v0.8.72 [(#1115)](https://github.com/mockito/mockito/pull/1115)

**2.8.41** - [1 commit](https://github.com/mockito/mockito/compare/v2.8.40...v2.8.41) by [Allon Murienik](https://github.com/mureinik) - *2017-06-10* - published to [![Bintray](https://img.shields.io/badge/Bintray-2.8.41-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.8.41)
 - ArgsMismatchFinder#getStubbingArgMismatches generics [(#1118)](https://github.com/mockito/mockito/pull/1118)

**2.8.40** - [4 commits](https://github.com/mockito/mockito/compare/v2.8.39...v2.8.40) by [Szczepan Faber](http://github.com/mockitoguy) (2), [Alex Simkin](https://github.com/SimY4) (1), mkuster (1) - *2017-06-05* - published to [![Bintray](https://img.shields.io/badge/Bintray-2.8.40-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.8.40)
 - [Bugfixes] Fixed Travis configuration for matrix build [(#1109)](https://github.com/mockito/mockito/pull/1109)
 - Renamed configuration extension 'releasing' into 'shipkit' [(#1104)](https://github.com/mockito/mockito/pull/1104)
 - Made CaturingMatcher threadsafe [(#986)](https://github.com/mockito/mockito/pull/986)

**2.8.39** - [1 commit](https://github.com/mockito/mockito/compare/v2.8.38...v2.8.39) by [Szczepan Faber](http://github.com/mockitoguy) - *2017-05-30* - published to [![Bintray](https://img.shields.io/badge/Bintray-2.8.39-green.svg)](https://bintray.com/mockito/maven/mockito-development/2.8.39)
 - Fixed contributor links and bintray badge in release notes [(#1103)](https://github.com/mockito/mockito/pull/1103)

**2.8.38** - [1 commit](https://github.com/mockito/mockito/compare/v2.8.37...v2.8.38) by [Szczepan Faber](http://github.com/mockitoguy) - *2017-05-30* - published to [maven/mockito-development](https://bintray.com/mockito/maven/mockito-development)
 - [Documentation] Fixed broken link in Javadoc, improved console message [(#1100)](https://github.com/mockito/mockito/pull/1100)

**2.8.37** - [4 commits](https://github.com/mockito/mockito/compare/v2.8.36...v2.8.37) by [epeee](https://github.com/epeee), [Szczepan Faber](http://github.com/mockitoguy) - *2017-05-28* - published to [maven/mockito-development](https://bintray.com/mockito/maven/mockito-development)
 - update gradle to v3.5 [(#1099)](https://github.com/mockito/mockito/pull/1099)
 - Moved some more dependencies to dependencies.gradle [(#1098)](https://github.com/mockito/mockito/pull/1098)

**2.8.36** - [1 commit](https://github.com/mockito/mockito/compare/v2.8.35...v2.8.36) by [Allon Murienik](https://github.com/mureinik) - *2017-05-27* - published to [maven/mockito-development](https://bintray.com/mockito/maven/mockito-development)
 - InOrderImpl constructor signature [(#1096)](https://github.com/mockito/mockito/pull/1096)

**2.8.35** - [4 commits](https://github.com/mockito/mockito/compare/v2.8.34...v2.8.35) by [Szczepan Faber](http://github.com/mockitoguy) - *2017-05-27* - published to [maven/mockito-development](https://bintray.com/mockito/maven/mockito-development)
 - Fixed bug with git push for tag during the release [(#1095)](https://github.com/mockito/mockito/pull/1095)
 - update assertj (v2.8.0) [(#1094)](https://github.com/mockito/mockito/pull/1094)

**2.8.34** - [2 commits](https://github.com/mockito/mockito/compare/v2.8.33...v2.8.34) by [Szczepan Faber](http://github.com/mockitoguy) - *2017-05-27* - published to [maven/mockito-development](https://bintray.com/mockito/maven/mockito-development)
 - Updated build for latest release tools [(#1093)](https://github.com/mockito/mockito/pull/1093)

**2.8.33** - [2 commits](https://github.com/mockito/mockito/compare/v2.8.32...v2.8.33) by [Szczepan Faber](http://github.com/mockitoguy) (1), Tim Cooke (1) - *2017-05-27* - published to [maven/mockito-development](https://bintray.com/mockito/maven/mockito-development)
 - [Documentation] Javadoc Example Throws Unexpected Exception [(#1088)](https://github.com/mockito/mockito/issues/1088)
 - Fixes #1088 : Updating documentation of verify feature to correct a s… [(#1091)](https://github.com/mockito/mockito/pull/1091)

**2.8.32** - [1 commit](https://github.com/mockito/mockito/compare/v2.8.31...v2.8.32) by Arend v. Reinersdorff - *2017-05-26* - published to [maven/mockito-development](https://bintray.com/mockito/maven/mockito-development)
 - Fix link to Mockito JUnit rule heading [(#1092)](https://github.com/mockito/mockito/pull/1092)

**2.8.31** - [1 commit](https://github.com/mockito/mockito/compare/v2.8.30...v2.8.31) by [Rafael Winterhalter](http://github.com/raphw) - *2017-05-22* - published to [maven/mockito-development](https://bintray.com/mockito/maven/mockito-development)
 - Fixes #1083: Interfaces only declare toString implicitly and should t… [(#1090)](https://github.com/mockito/mockito/pull/1090)
 - Mockito 2 mock-maker-inline not able to mock Object methods on an interface [(#1083)](https://github.com/mockito/mockito/issues/1083)

**2.8.30 (2017-05-18)** - [3 commits](https://github.com/mockito/mockito/compare/v2.8.29...v2.8.30) by epeee (2), [Szczepan Faber](http://github.com/mockitoguy) (1) - *2017-05-18* - published to [maven/mockito-development](https://bintray.com/mockito/maven/mockito-development/2.8.30)
 - Streamline assertj and junit4 usage in gradle files and update assertj (v2.7.0) [(#1075)](https://github.com/mockito/mockito/pull/1075)

**2.8.29 (2017-05-15)** - [7 commits](https://github.com/mockito/mockito/compare/v2.8.28...v2.8.29) by [Szczepan Faber](http://github.com/mockitoguy) (5), Christian Schwarz (1), Serge Bishyr (1) - published to [maven/mockito-development](https://bintray.com/mockito/maven/mockito-development/2.8.29)
 - Bumped version of tools to pick up release automation fixes [(#1084)](https://github.com/mockito/mockito/pull/1084)
 - More concise release notes and codecov issue fix [(#1080)](https://github.com/mockito/mockito/pull/1080)
 - Fixed #1065. Add information about doNothing() method to CannotStubVo… [(#1079)](https://github.com/mockito/mockito/pull/1079)
 - Fixed validation in returnArgumentAt(int) in case of type erasure on the parameter [(#1076)](https://github.com/mockito/mockito/pull/1076)
 - Mockito AdditionalAnswers.returnsFirstArg() doesn't work with generic first arg [(#1071)](https://github.com/mockito/mockito/issues/1071)
 - Complement error message [(#1065)](https://github.com/mockito/mockito/issues/1065)

**2.8.28 (2017-05-06)** - [1 commit](https://github.com/mockito/mockito/compare/v2.8.27...v2.8.28) by [Allon Murienik](https://github.com/mureinik), published to: [maven/mockito-development](https://bintray.com/mockito/maven/mockito-development/2.8.28)
 - README.md release FAQ newlines [(#1073)](https://github.com/mockito/mockito/pull/1073)

**2.8.27 (2017-05-05)** - [1 commit](https://github.com/mockito/mockito/compare/v2.8.26...v2.8.27) by [Roman Elizarov](https://github.com/elizarov), published to: [maven/mockito-development](https://bintray.com/mockito/maven/mockito-development/2.8.27)
  - Support Kotlin suspending functions [(#1032)](https://github.com/mockito/mockito/pull/1032)

**2.8.26 (2017-05-05)** - [3 commits](https://github.com/mockito/mockito/compare/v2.8.25...v2.8.26) by [Szczepan Faber](https://github.com/mockitoguy), published to: [maven/mockito-development](https://bintray.com/mockito/maven/mockito-development/2.8.26)
  - Bumped release tools to 0.8.4 - concise release notes [(#1069)](https://github.com/mockito/mockito/pull/1069)

**2.8.25 (2017-05-01)** - [2 commits](https://github.com/mockito/mockito/compare/v2.8.24...v2.8.25) by [epeee](https://github.com/epeee), [Szczepan Faber](https://github.com/mockitoguy), published to: [maven/mockito-development](https://bintray.com/mockito/maven/mockito-development/2.8.25)
  - Test code refactoring - updated assertj test dependency from 1.x to 2.6.0 [(#1058)](https://github.com/mockito/mockito/pull/1058)

**2.8.24 (2017-05-01)** - [1 commit](https://github.com/mockito/mockito/compare/v2.8.23...v2.8.24) by [epeee](https://github.com/epeee), published to: [maven/mockito-development](https://bintray.com/mockito/maven/mockito-development/2.8.24)
 - Fixed  typos across the codebase [(#1060)](https://github.com/mockito/mockito/pull/1060)

**2.8.23 (2017-05-01)** - [11 commits](https://github.com/mockito/mockito/compare/v2.8.22...v2.8.23) by [Szczepan Faber](https://github.com/mockitoguy), [Tim van der Lippe](https://github.com/TimvdLippe), published to: [maven/mockito-development](https://bintray.com/mockito/maven/mockito-development/2.8.23)
  - Bumped version of tools [(#1062)](https://github.com/mockito/mockito/pull/1062)
  - Enabled automatic releasing without using skip-release keyword yet [(#1061)](https://github.com/mockito/mockito/pull/1061)
  - Ensured the release actually happens automatically [(#1057)](https://github.com/mockito/mockito/pull/1057)
  - Bumped version of release tools [(#1055)](https://github.com/mockito/mockito/pull/1055)
  - Fixed problems with releases [(#1052)](https://github.com/mockito/mockito/pull/1052)
  - Fix some issues reported by SonarQube [(#1027)](https://github.com/mockito/mockito/pull/1027)

# 2.8.9 (2017-04-26) published to [JCenter](https://bintray.com/mockito/maven/mockito/2.8.9)/[Maven Central](http://search.maven.org/#artifactdetails%7Corg.mockito%7Cmockito-core%7C2.8.9%7Cjar)

Notable changes sincle last release to Maven Central (2.7.22):
 - Updated ByteBuddy to 1.6.14, mostly for Java9
 - Fixed NPE with MockitoJUnitRunner [(#1035)](https://github.com/mockito/mockito/pull/1035)

**2.8.9 (2017-04-26)** - [11 commits](https://github.com/mockito/mockito/compare/v2.8.8...v2.8.9) by [Rafael Winterhalter](https://github.com/raphw), published to: [JCenter](https://bintray.com/mockito/maven/mockito/2.8.9)/[Maven Central](http://search.maven.org/#artifactdetails%7Corg.mockito%7Cmockito-core%7C2.8.9%7Cjar)
 - Updated ByteBuddy to 1.6.14, fixes agent attachment on Java 9 VM

**2.8.8 (2017-04-24)** - [3 commits](https://github.com/mockito/mockito/compare/v2.8.7...v2.8.8) by [Szczepan Faber](https://github.com/mockitoguy), published to: [maven/mockito-development](https://bintray.com/mockito/maven/mockito-development/2.8.8)
 - Release notes tidy-up: new notable version 2.7.22, bumped release tools version [(#1048)](https://github.com/mockito/mockito/pull/1048)

**2.8.7 (2017-04-20)** - [1 commit](https://github.com/mockito/mockito/compare/v2.8.6...v2.8.7) by [Szczepan Faber](https://github.com/mockitoguy), published to: [maven/mockito-development](https://bintray.com/mockito/maven/mockito-development/2.8.7)
 - Release automation: automated publishing in Bintray, documentation [(#1039)](https://github.com/mockito/mockito/pull/1039)

**2.8.6 (2017-04-19)** - [1 commit](https://github.com/mockito/mockito/compare/v2.8.5...v2.8.6) by [Serge Bishyr](https://github.com/SeriyBg), published to: [maven/mockito-development](https://bintray.com/mockito/maven/mockito-development/2.8.6)
 - Fixes #1044: change example for alignment of throws declaration [(#1045)](https://github.com/mockito/mockito/pull/1045)
 - Mistake in Contribution guide, Code style example [(#1044)](https://github.com/mockito/mockito/issues/1044)

**2.8.5 (2017-04-18)** - [1 commit](https://github.com/mockito/mockito/compare/v2.8.4...v2.8.5) by [Rafael Winterhalter](https://github.com/raphw), published to: [maven/mockito-development](https://bintray.com/mockito/maven/mockito-development/2.8.5)
 - Do not suppress instrumentation exceptions [(#1012)](https://github.com/mockito/mockito/pull/1012)

**2.8.4 (2017-04-17)** - [1 commit](https://github.com/mockito/mockito/compare/v2.8.3...v2.8.4) by [Allon Murienik](https://github.com/mureinik), published to: [maven/mockito-development](https://bintray.com/mockito/maven/mockito-development/2.8.4)
 - Fix javadoc issues [(#1040)](https://github.com/mockito/mockito/pull/1040)

**2.8.3 (2017-04-16)** - [1 commit](https://github.com/mockito/mockito/compare/v2.8.2...v2.8.3) by [yyvess](https://github.com/yyvess), published to: [maven/mockito-development](https://bintray.com/mockito/maven/mockito-development/2.8.3)
 - Fixed NPE with MockitoJUnitRunner - MockitoTestListener must be remove on case test fail on the initialization. [(#1035)](https://github.com/mockito/mockito/pull/1035)

**2.8.2 (2017-04-14)** - [1 commit](https://github.com/mockito/mockito/compare/v2.8.1...v2.8.2) by [Szczepan Faber](https://github.com/mockitoguy), published to: [maven/mockito-development](https://bintray.com/mockito/maven/mockito-development/2.8.2)
 - Fixed glitches with notable releases [(#1038)](https://github.com/mockito/mockito/pull/1038)

**2.8.1 (2017-04-13)** - [3 commits](https://github.com/mockito/mockito/compare/v2.8.0...v2.8.1) by [Marcin Zajączkowski](https://github.com/szpak), [Szczepan Faber](https://github.com/mockitoguy), [yyvess](https://github.com/yyvess), published to: [maven/mockito-development](https://bintray.com/mockito/maven/mockito-development/2.8.1)
 - Remove duplicated code [(#1036)](https://github.com/mockito/mockito/pull/1036)
 - Fixed typo in configuration [(#1034)](https://github.com/mockito/mockito/pull/1034)

**2.8.0 (2017-04-12)** - [23 commits](https://github.com/mockito/mockito/compare/v2.7.22...v2.8.0) by [Szczepan Faber](https://github.com/mockitoguy), [Allon Mureinik](https://github.com/mureinik), published to: [maven/mockito-development](https://bintray.com/mockito/maven/mockito-development/2.8.0)
 - Fixed notable version [(#1030)](https://github.com/mockito/mockito/pull/1030)
 - SmartPrinterTest toString() calls [(#1025)](https://github.com/mockito/mockito/pull/1025)
 - Enabled continuous delivery via robust mockito-release-tools project [(#1018)](https://github.com/mockito/mockito/pull/1018)

# 2.7.22 (2017-04-07), published to [JCenter](https://bintray.com/mockito/maven/mockito/2.7.22)/[Maven Central](http://search.maven.org/#artifactdetails%7Corg.mockito%7Cmockito-core%7C2.7.22%7Cjar).

Last version published to Maven Central after the team adopted [Continuous Delivery Pipeline 2.0](https://github.com/mockito/mockito/issues/911).

* Authors: 4, commits: 8
  * 3: Marcin Zajączkowski
  * 2: Allon Mureinik
  * 2: Szczepan Faber
  * 1: Marcin Zajaczkowski
* Improvements: 3
  * Fix broken link in Mockito javadoc [(#1023)](https://github.com/mockito/mockito/pull/1023)
  * MatchersTest numeric literals [(#1022)](https://github.com/mockito/mockito/pull/1022)
  * Change release build to JDK 7 with binary compatibility for Java 6 [(#1021)](https://github.com/mockito/mockito/pull/1021)

### 2.7.21 (2017-04-01 11:07 UTC)

* Authors: 2
* Commits: 4
  * 3: Szczepan Faber
  * 1: Tim van der Lippe
* Improvements: 2
  * Documentation: 1
    * Javadoc, Answer1 code example does not compile. [(#952)](https://github.com/mockito/mockito/issues/952)
  * Remaining changes: 1
    * Tidy-up in build automation [(#1011)](https://github.com/mockito/mockito/pull/1011)

### 2.7.20 (2017-03-29 17:23 UTC)

* Authors: 7
* Commits: 12
  * 3: Marcin Zajączkowski
  * 2: Allon Mureinik
  * 2: Szczepan Faber
  * 2: Tim van der Lippe
  * 1: Jesse Englert
  * 1: Nirvanall
  * 1: Tsuyoshi Murakami
* Improvements: 11
  * Bugfixes: 1
    * Enabled mocking interface clone method [(#688)](https://github.com/mockito/mockito/issues/688)
  * Enhancements: 1
    * Make MockSettings.useConstructor able to choose the right constructor [(#976)](https://github.com/mockito/mockito/issues/976)
  * Remaining changes: 9
    * Bumped version to fix CI build [(#1010)](https://github.com/mockito/mockito/pull/1010)
    * Clean up unused imports [(#1009)](https://github.com/mockito/mockito/pull/1009)
    * Fix release notes badge [(#1001)](https://github.com/mockito/mockito/pull/1001)
    * Add explicit mention for code style check [(#996)](https://github.com/mockito/mockito/pull/996)
    * fix the broken link on 1357 line in Mockito.java [(#994)](https://github.com/mockito/mockito/pull/994)
    * Fixes #976: Resolve ambiguous constructors [(#980)](https://github.com/mockito/mockito/pull/980)
    * Fixes #974: Fix to get correct stubbing location with inline mocking [(#979)](https://github.com/mockito/mockito/pull/979)
    * MockitoJUnitRunner.StrictStubs does not detect 'Unnecessary Stubbing' when inline mock maker is enabled [(#974)](https://github.com/mockito/mockito/issues/974)
    * Verifies #688: Acceptance test to prevent future regression [(#972)](https://github.com/mockito/mockito/pull/972)

### 2.7.18 (2017-03-18 16:40 UTC)

* Authors: 1
* Commits: 1
  * 1: L-KID
* Improvements: 1
  * Corrected a comment in exceptions package-info [(#975)](https://github.com/mockito/mockito/pull/975)

### 2.7.17 (2017-03-12 18:39 UTC)

* Authors: 1
* Commits: 1
  * 1: Rafael Winterhalter
* No notable improvements. See the commits for detailed changes.

### 2.7.16 (2017-03-10 11:57 UTC)

* Authors: 1
* Commits: 1
  * 1: Tsuyoshi Murakami
* Improvements: 2
  * Fixes #981: Update Javadoc about the 'mockito-inline' artifact [(#985)](https://github.com/mockito/mockito/pull/985)
  * Update documentation about mockito-inline artifact [(#981)](https://github.com/mockito/mockito/issues/981)

### 2.7.15 (2017-03-09 12:05 UTC)

* Authors: 1
* Commits: 1
  * 1: paulduffin
* Improvements: 1
  * Break cyclical compile time dependency on hideRecursiveCall [(#983)](https://github.com/mockito/mockito/pull/983)

### 2.7.14 (2017-03-05 23:25 UTC)

* Authors: 3
* Commits: 7
  * 3: Allon Mureinik
  * 3: Szczepan Faber
  * 1: Tim van der Lippe
* Improvements: 3
  * New features: 2
    * New feature - enable mocking using constructor arguments [(#935)](https://github.com/mockito/mockito/pull/935)
    * Support constructor parameters for spying on abstract classes [(#685)](https://github.com/mockito/mockito/issues/685)
  * Remaining changes: 1
    * Add editor config to automatically adhere to code style guide [(#966)](https://github.com/mockito/mockito/pull/966)

### 2.7.13 (2017-02-28 05:38 UTC)

* Authors: 3
* Commits: 6
  * 3: Ben Yu
  * 2: Szczepan Faber
  * 1: Marcin Zajączkowski
* Improvements: 2
  * Enabled local testing of release tools [(#968)](https://github.com/mockito/mockito/pull/968)
  * Allow spying on interfaces so that it is convenient to work with Java 8 default methods [(#942)](https://github.com/mockito/mockito/pull/942)

### 2.7.12 (2017-02-25 23:25 UTC)

* Authors: 1
* Commits: 1
  * 1: Rafael Winterhalter
* Improvements: 2
  * Bugfixes: 1
    * Invalid property reported by mocks and partial mocks [(#963)](https://github.com/mockito/mockito/issues/963)
  * Remaining changes: 1
    * With Inline Mocking enabled, Mockito throws when mocking interface with default method [(#944)](https://github.com/mockito/mockito/issues/944)

### 2.7.11 (2017-02-21 20:43 UTC)

* Authors: 1
* Commits: 1
  * 1: Christian Schwarz
* Improvements: 1
  * Simplified the creation of ArgumentsAreDifferent-Exceptions. [(#908)](https://github.com/mockito/mockito/pull/908)

### 2.7.10 (2017-02-19 19:37 UTC)

* Authors: 2
* Commits: 2
  * 1: Allon Murienik
  * 1: Marcin Zajączkowski
* Improvements: 2
  * Fix broken build [(#959)](https://github.com/mockito/mockito/pull/959)
  * Display release warning only when needed [(#955)](https://github.com/mockito/mockito/pull/955)

### 2.7.9 (2017-02-17 16:18 UTC)

* Authors: 2
* Commits: 2
  * 1: Allon Murienik
  * 1: Rafael Winterhalter
* Improvements: 3
  * Checkstyle conf. to enforce whitespace rules [(#932)](https://github.com/mockito/mockito/pull/932)
  * Fix whitespace issues throughout the code [(#928)](https://github.com/mockito/mockito/pull/928)
  * Add detection for Android environments and give warnings if used incorrectly. Corrected documentation. [(#893)](https://github.com/mockito/mockito/pull/893)

### 2.7.8 (2017-02-17 13:45 UTC)

* Authors: 4
* Commits: 4
  * 1: Brice Dutheil
  * 1: Marcin Zajaczkowski
  * 1: Marcin Zajączkowski
  * 1: Stuart Blair
* Improvements: 2
  * Remove Gradle 5 depracation warning [(#954)](https://github.com/mockito/mockito/pull/954)
  * Fix Javadocs on MockitoRule.java to correct an error on its usage. [(#953)](https://github.com/mockito/mockito/pull/953)

### 2.7.7 (2017-02-16 15:09 UTC)

* Authors: 1
* Commits: 1
  * 1: Tim van der Lippe
* Improvements: 1
  * RunnerFactory InvocationTargetException to return one more possible reason [(#949)](https://github.com/mockito/mockito/issues/949)

### 2.7.6 (2017-02-13 15:40 UTC)

* Authors: 3
* Commits: 6
  * 3: tmurakami
  * 2: Tim van der Lippe
  * 1: Rafael Winterhalter
* Improvements: 3
  * Fix JavaDoc generation on recent JDK 8 builds [(#934)](https://github.com/mockito/mockito/pull/934)
  * Fixes #929: Move the resources directory to src/main [(#931)](https://github.com/mockito/mockito/pull/931)
  * New 'mockito-inline' artifact does not work [(#929)](https://github.com/mockito/mockito/issues/929)

### 2.7.5 (2017-02-07 15:36 UTC)

* Authors: 1
* Commits: 1
  * 1: Christian Schwarz
* Improvements: 2
  * Support for return type 'Void' in DoesNothingAnswer [(#933)](https://github.com/mockito/mockito/pull/933)
  * Mockito can't unbox Void [(#927)](https://github.com/mockito/mockito/issues/927)

### 2.7.4 (2017-02-06 15:04 UTC)

* Authors: 1
* Commits: 1
  * 1: Allon Murienik
* Improvements: 1
  * Fix whitespace issues throughout the code [(#928)](https://github.com/mockito/mockito/pull/928)

### 2.7.3 (2017-02-06 12:51 UTC)

* Authors: 1
* Commits: 1
  * 1: Christian Schwarz
* Improvements: 1
  * Small improvements [(#912)](https://github.com/mockito/mockito/pull/912)

### 2.7.2 (2017-02-04 12:05 UTC)

* Authors: 2
* Commits: 2
  * 1: Rafael Winterhalter
  * 1: Szczepan Faber
* Improvements: 2
  * Deleted dead code [(#924)](https://github.com/mockito/mockito/pull/924)
  * Added subproject for configuration-free inline mock making. [(#920)](https://github.com/mockito/mockito/pull/920)

### 2.7.1 (2017-01-31 16:48 UTC)

* Authors: 0
* Commits: 0
* No notable improvements. See the commits for detailed changes.

### 2.7.0 (2017-01-29 16:14 UTC)

* Authors: 1
* Commits: 29
  * 29: Szczepan Faber
* Improvements: 1
  * New features: 1
    * New strict mocking API - MockitoSession. Overview: [(#857)](https://github.com/mockito/mockito/issues/857), pull request: [(#865)](https://github.com/mockito/mockito/pull/865).

### 2.6.9 (2017-01-27 22:02 UTC)

* Authors: 1
* Commits: 1
  * 1: Christian Schwarz
* Improvements: 1
  * Unified all number of invocations checker in NumberOfInvocationsChecker [(#907)](https://github.com/mockito/mockito/pull/907)

### 2.6.8 (2017-01-23 14:53 UTC)

* Authors: 1
* Commits: 2
  * 2: Brice Dutheil
* Improvements: 2
  * Documentation: 2
    * Fixes #814 : A elements were missing the name attribute [(#905)](https://github.com/mockito/mockito/pull/905)
    * Javadoc links in main Mockito doc are not linkable [(#814)](https://github.com/mockito/mockito/issues/814)

### 2.6.7 (2017-01-23 13:52 UTC)

* Authors: 2
* Commits: 2
  * 1: Brice Dutheil
  * 1: Rafael Winterhalter
* Improvements: 2
  * Fix TypeCache dead lock [(#902)](https://github.com/mockito/mockito/pull/902)
  * Mockito 2.6.4 hangs on JDK 1.8.0_31 [(#892)](https://github.com/mockito/mockito/issues/892)

### 2.6.6 (2017-01-23 10:12 UTC)

* Authors: 1
* Commits: 5
  * 5: Brice Dutheil
* Improvements: 1
  * Enhancements: 1
    * Spy annotation reports better error message if instance creation is impossible [(#885)](https://github.com/mockito/mockito/pull/885)

### 2.6.5 (2017-01-21 12:58 UTC)

* Authors: 2
* Commits: 4
  * 2: Brice Dutheil
  * 2: Raptis
* Improvements: 3
  * Bugfixes: 1
    * Overridden abstract methods using generics are not longer called for partial mock (working with Mockito 1.10.19) [(#874)](https://github.com/mockito/mockito/issues/874)
  * Documentation: 1
    * Clarify documentation for consecutive stubbing [(#896)](https://github.com/mockito/mockito/pull/896)
  * Remaining changes: 1
    * Update to Byte Buddy 1.6.4: Fixes bridge method resolution for generic types [(#891)](https://github.com/mockito/mockito/pull/891)

### 2.6.4 (2017-01-19 17:12 UTC)

* Authors: 2
* Commits: 2
  * 1: Marcin Zajączkowski
  * 1: Rafael Winterhalter
* Improvements: 1
  * Update to Byte Buddy 1.6.4: Fixes bridge method resolution for generic types [(#891)](https://github.com/mockito/mockito/pull/891)

### 2.6.3 (2017-01-15 21:07 UTC)

* Authors: 2
* Commits: 2
  * 1: Allon Murienik
  * 1: Szczepan Faber
* Improvements: 3
  * Java 9 support: 1
    * Upgrading to Objenesis 2.5 [(#882)](https://github.com/mockito/mockito/issues/882)
  * Remaining changes: 2
    * Fixes #882: Upgrade to Objenesis 2.5 [(#887)](https://github.com/mockito/mockito/pull/887)
    * Fixed build for external contributors [(#886)](https://github.com/mockito/mockito/pull/886)

### 2.6.2 (2017-01-13 10:58 UTC)

* Authors: 2
* Commits: 2
  * 1: Allon Mureinik
  * 1: Brice Dutheil
* Improvements: 2
  * Fixed #879: ArgumentMatchers grammar fix [(#881)](https://github.com/mockito/mockito/pull/881)
  * Grammar issue in ArgumentMatchers' javadoc. [(#879)](https://github.com/mockito/mockito/issues/879)

### 2.6.1 (2017-01-12 13:55 UTC)

* Authors: 1
* Commits: 2
  * 2: Tim van der Lippe
* No notable improvements. See the commits for detailed changes.

### 2.6.0 (2017-01-12 12:42 UTC)

* Authors: 1
* Commits: 1
  * 1: Tim van der Lippe
* Improvements: 1
  * New features: 1
    * Prepare Android library for publication and bump version to 2.6.0 [(#872)](https://github.com/mockito/mockito/pull/872)

### 2.5.7 (2017-01-11 08:28 UTC)

* Authors: 2
* Commits: 2
  * 1: Marcin Zajączkowski
  * 1: Rafael Winterhalter
* Improvements: 1
  * Update Byte Buddy and enable injecting loading strategy for Android [(#875)](https://github.com/mockito/mockito/pull/875)

### 2.5.6 (2017-01-10 13:20 UTC)

* Authors: 2
* Commits: 2
  * 1: Brice Dutheil
  * 1: Johnny Lim
* Improvements: 1
  * Remove redundant defaultAnswer(RETURNS_DEFAULTS) [(#870)](https://github.com/mockito/mockito/pull/870)

### 2.5.5 (2017-01-06 11:19 UTC)

* Authors: 1
* Commits: 2
  * 2: Brice Dutheil
* Improvements: 2
  * Bugfixes: 1
    * [VarArgs] unexpected behaviour of returnsArgAt(int) [(#820)](https://github.com/mockito/mockito/issues/820)
  * Remaining changes: 1
    * Fixes #820 ReturnsArgAt to handle returning vararg as arrays [(#821)](https://github.com/mockito/mockito/pull/821)

### 2.5.4 (2017-01-04 17:02 UTC)

* Authors: 2
* Commits: 3
  * 2: Brice Dutheil
  * 1: wenwu
* Improvements: 3
  * Bugfixes: 2
    * When custom exception fillInstackTrace() returns null, Exception mock cannot work properly [(#866)](https://github.com/mockito/mockito/issues/866)
    * Jacoco 0.7.8 breaks tests for the new plugin mechanism [(#860)](https://github.com/mockito/mockito/issues/860)
  * Remaining changes: 1
    * Fixes #866 should not throw NPE, when custom Exception fillInStackTrace returns null [(#867)](https://github.com/mockito/mockito/pull/867)

### 2.5.3 (2017-01-02 16:48 UTC)

* Authors: 2
* Commits: 2
  * 1: Brice Dutheil
  * 1: Rafael Winterhalter
* Improvements: 1
  * Updated to Byte Buddy 1.6.0 [(#864)](https://github.com/mockito/mockito/pull/864)

### 2.5.2 (2017-01-02 00:50 UTC)

* Authors: 1
* Commits: 6
  * 6: Szczepan Faber
* Improvements: 1
  * Enhancements: 1
    * Safeguard for MockitoListener API [(#858)](https://github.com/mockito/mockito/pull/858)

### 2.5.1 (2017-01-02 00:13 UTC)

* Authors: 1
* Commits: 9
  * 9: Szczepan Faber
* Improvements: 1
  * New features: 1
    * Mockito JUnit Runner supports strict stubbing [(#854)](https://github.com/mockito/mockito/pull/854)

### 2.5.0 (2016-12-28 08:10 UTC)

* Authors: 1
* Commits: 9
  * 9: Szczepan Faber
* Improvements: 2
  * Stopped publishing java source inside main jar [(#850)](https://github.com/mockito/mockito/pull/850)
  * JUnit rule strictness can be tweaked per test method [(#843)](https://github.com/mockito/mockito/pull/843)

### 2.4.5 (2016-12-27 17:52 UTC)

* Authors: 3
* Commits: 6
  * 3: Rafael Winterhalter
  * 2: Szczepan Faber
  * 1: ceduardo.melo
* Improvements: 2
  * Rename compiled MockMethodDispatcher class so that it works with Robolectric [(#847)](https://github.com/mockito/mockito/pull/847)
  * Rename MockMethodDispatcher.class to MockMethodDispatcher.raw on build [(#845)](https://github.com/mockito/mockito/issues/845)

### 2.4.4 (2016-12-27 14:45 UTC)

* Authors: 2
* Commits: 3
  * 2: Szczepan Faber
  * 1: Rafael Winterhalter
* Improvements: 2
  * Update Byte Buddy to support recent Java 9 builds [(#852)](https://github.com/mockito/mockito/pull/852)
  * Automatically publish main jar to central [(#844)](https://github.com/mockito/mockito/pull/844)

### 2.4.3 (2016-12-25 15:39 UTC)

* Authors: 1
* Commits: 1
  * 1: Brice Dutheil
* Improvements: 1
  * Added subproject for supporting class loading on Android. [(#774)](https://github.com/mockito/mockito/pull/774)

### 2.4.2 (2016-12-23 23:44 UTC)

* Authors: 1
* Commits: 9
  * 9: Szczepan Faber
* Improvements: 1
  * Added missing Javadoc and few other tidy-ups [(#842)](https://github.com/mockito/mockito/pull/842)

### 2.4.1 (2016-12-22 14:02 UTC)

* Authors: 1
* Commits: 2
  * 2: Brice Dutheil
* Improvements: 2
  * Enhancements: 2
    * Fixes #838 : Adds a matches(Pattern) [(#839)](https://github.com/mockito/mockito/pull/839)
    * Feature request: ArgumentMatchers.matches(Pattern regex) [(#838)](https://github.com/mockito/mockito/issues/838)

### 2.4.0 (2016-12-21 05:35 UTC)

* Authors: 3
* Commits: 6
  * 4: Szczepan Faber
  * 1: Brice Dutheil
  * 1: jakobjo
* Improvements: 3
  * Enhancements: 2
    * JUnit runner reports argument stubbing mismatches [(#808)](https://github.com/mockito/mockito/pull/808)
    * Verification listeners [(#719)](https://github.com/mockito/mockito/pull/719)
  * Remaining changes: 1
    * Test clean up according to discussions for RememberingListeners [(#831)](https://github.com/mockito/mockito/pull/831)

### 2.3.11 (2016-12-20 12:25 UTC)

* Authors: 2
* Commits: 5
  * 4: Szczepan Faber
  * 1: Christian Schwarz
* Improvements: 3
  * Disabled maven central sync [(#837)](https://github.com/mockito/mockito/pull/837)
  * Main jar automatically publishes to central [(#836)](https://github.com/mockito/mockito/pull/836)
  * Simplified ArgumentMatcherStorageImpl and logic matchers [(#833)](https://github.com/mockito/mockito/pull/833)

### 2.3.10 (2016-12-19 13:38 UTC)

* Authors: 1
* Commits: 1
  * 1: Brice Dutheil
* Improvements: 1
  * Enhancements: 1
    * Introduce validable answers [(#826)](https://github.com/mockito/mockito/pull/826)

### 2.3.9 (2016-12-19 03:09 UTC)

* Release automation bugfixes needed for mockito-android publication

### 2.3.7 (2016-12-18 04:31 UTC)

* Authors: 1
* Commits: 24
  * 24: Szczepan Faber
* Improvements: 2
  * Fixed problem with publication [(#832)](https://github.com/mockito/mockito/pull/832)
  * Automated publication of mockito submodule like mockito-android [(#809)](https://github.com/mockito/mockito/pull/809)

### 2.3.6 (2016-12-17 16:36 UTC)

* Authors: 1
* Commits: 2
  * 2: Brice Dutheil
* Improvements: 2
  * Improves exception message in case a matcher is used for a primitive parameter [(#830)](https://github.com/mockito/mockito/pull/830)
  * Test for verification listeners test can introduces bogus erro in other test cases. [(#827)](https://github.com/mockito/mockito/pull/827)

### 2.3.5 (2016-12-15 10:24 UTC)

* Authors: 1
* Commits: 2
  * 2: Rafael Winterhalter
* Improvements: 1
  * Only resolve instrumented method after validating that an instance is… [(#823)](https://github.com/mockito/mockito/pull/823)

### 2.3.4 (2016-12-14 16:19 UTC)

* Authors: 1
* Commits: 2
  * 2: Brice Dutheil
* Improvements: 3
  * Bugfixes: 1
    * Fixes bug were previously verified invocations could not capture argu… [(#819)](https://github.com/mockito/mockito/pull/819)
  * Enhancements: 1
    * Verification using After and ArgumentCaptor with Times or AtLeast methods returns unexpected size of captured values list. [(#379)](https://github.com/mockito/mockito/issues/379)
  * Remaining changes: 1
    * Fixes the issue #379 by removing previously verified invocations, some tests were added to verify that works [(#380)](https://github.com/mockito/mockito/pull/380)

### 2.3.3 (2016-12-13 22:39 UTC)

* Authors: 1
* Commits: 1
  * 1: LiamClark
* Improvements: 1
  * Enhancements: 1
    * Verification listeners [(#719)](https://github.com/mockito/mockito/pull/719)

### 2.3.2 (2016-12-13 18:33 UTC)

* Authors: 2
* Commits: 7
  * 6: Brice Dutheil
  * 1: Rafael Winterhalter
* Improvements: 1
  * Enable annotation engine as plugin [(#811)](https://github.com/mockito/mockito/pull/811)

### 2.3.1 (2016-12-13 18:26 UTC)

* Authors: 1
* Commits: 2
  * 2: Rafael Winterhalter
* Improvements: 1
  * Never throw mockito exceptions from a plugin type's constructor [(#805)](https://github.com/mockito/mockito/pull/805)

### 2.3.0 (2016-12-10 17:48 UTC)

* Authors: 1
* Commits: 50
  * 50: Szczepan Faber
* Improvements: 1
  * New features: 1
    * New opt-in stubbing strictness implemented in JUnit rules [(#807)](https://github.com/mockito/mockito/pull/807)

### 2.2.29 (2016-12-07 13:17 UTC)

* Authors: 1
* Commits: 2
  * 2: Brice Dutheil
* Improvements: 2
  * Bugfixes: 1
    * Mockito 2 fails when running on IBM J9 (SR1 FP10) VM [(#801)](https://github.com/mockito/mockito/issues/801)
  * Enhancements: 1
    * Adds a warning for IBM J9 VMs if mock generation fails [(#803)](https://github.com/mockito/mockito/pull/803)

### 2.2.28 (2016-12-02 13:45 UTC)

* Authors: 2
* Commits: 4
  * 3: Brice Dutheil
  * 1: Szczepan Faber
* Improvements: 2
  * Bugfixes: 1
    * Deep stubs no longer cause unnecessary stubbing exception with JUnit runner [(#795)](https://github.com/mockito/mockito/pull/795)
  * Remaining changes: 1
    * Test improvements [(#791)](https://github.com/mockito/mockito/pull/791)

### 2.2.27 (2016-11-30 12:05 UTC)

* Authors: 3
* Commits: 3
  * 1: Christian Schwarz
  * 1: Marcin Zajączkowski
  * 1: Szczepan Faber
* Improvements: 2
  * Add showing stacktrace in Travis build [(#786)](https://github.com/mockito/mockito/pull/786)
  * Removed argument from MockingProgress.stubbingCompleted(..) [(#779)](https://github.com/mockito/mockito/pull/779)

### 2.2.26 (2016-11-27 09:05 UTC)

* Authors: 2
* Commits: 2
  * 1: Christian Schwarz
  * 1: Stephan Schroevers
* No notable improvements. See the commits for detailed changes.

### 2.2.25 (2016-11-25 18:10 UTC)

* Authors: 3
* Commits: 3
  * 1: Brice Dutheil
  * 1: Igor Kostenko
  * 1: Stephan Schroevers
* Improvements: 4
  * Enhancements: 1
    * Introduce default answers for primitive Optionals/Streams [(#782)](https://github.com/mockito/mockito/pull/782)
  * Remaining changes: 3
    * Add default answers for java.util.Optional{Double,Int,Long} and java.util.stream.{Double,Int,Long}Stream [(#781)](https://github.com/mockito/mockito/issues/781)
    * Fixes #765: Set files encoding to UTF-8 [(#780)](https://github.com/mockito/mockito/pull/780)
    * Cannot build Mockito on Windows 64b with Java 8  [(#765)](https://github.com/mockito/mockito/issues/765)

### 2.2.24 (2016-11-25 15:08 UTC)

* Authors: 1
* Commits: 1
  * 1: Christian Schwarz
* Improvements: 1
  * Removed private isMockitoMock(..) in MockUtil [(#775)](https://github.com/mockito/mockito/pull/775)

### 2.2.23 (2016-11-25 14:26 UTC)

* Authors: 3
* Commits: 5
  * 2: Brice Dutheil
  * 2: Christian Schwarz
  * 1: Felix Dekker
* Improvements: 4
  * Replaced ObjectBox with AtomicReference [(#777)](https://github.com/mockito/mockito/pull/777)
  * InvocationMatcher internal improvements [(#776)](https://github.com/mockito/mockito/pull/776)
  * Fixes #731 Implements retry rule for flaky tests [(#771)](https://github.com/mockito/mockito/pull/771)
  * Consider rerunning failed tests [(#731)](https://github.com/mockito/mockito/issues/731)

### 2.2.22 (2016-11-21 11:17 UTC)

* Authors: 1
* Commits: 10
  * 10: Brice Dutheil
* Improvements: 2
  * Documentation: 1
    * Public javadoc offers links to internal classes [(#762)](https://github.com/mockito/mockito/issues/762)
  * Remaining changes: 1
    * Fixes #762 Removes ReturnsEmptyValue javadoc references [(#763)](https://github.com/mockito/mockito/pull/763)

### 2.2.21 (2016-11-18 08:52 UTC)

* Authors: 1
* Commits: 1
  * 1: Rafael Winterhalter
* Improvements: 1
  * Mockito 2.2.17 regression: `mock-maker-inline` drops `-parameters` [(#764)](https://github.com/mockito/mockito/issues/764)

### 2.2.20 (2016-11-17 18:31 UTC)

* Authors: 1
* Commits: 4
  * 4: Brice Dutheil
* Improvements: 1
  * Regroup Junit classes in junit packages [(#748)](https://github.com/mockito/mockito/pull/748)

### 2.2.19 (2016-11-17 08:24 UTC)

* Authors: 1
* Commits: 1
  * 1: Rafael Winterhalter
* No notable improvements. See the commits for detailed changes.

### 2.2.18 (2016-11-17 05:50 UTC)

* Authors: 3
* Commits: 3
  * 1: Liam Clark
  * 1: Szczepan Faber
  * 1: Tim van der Lippe
* Improvements: 2
  * Documentation: 2
    * Updated the verification documentation to correctly use argThat with Java8 lambdas [(#759)](https://github.com/mockito/mockito/pull/759)
    * Generate service-worker after generating JavaDoc for offline access [(#602)](https://github.com/mockito/mockito/pull/602)

### 2.2.17 (2016-11-15 19:05 UTC)

* Authors: 1
* Commits: 2
  * 2: Rafael Winterhalter
* Improvements: 1
  * "mock-maker-inline" prevents collection of code coverage for spys [(#757)](https://github.com/mockito/mockito/issues/757)

### 2.2.16 (2016-11-14 11:13 UTC)

* Authors: 3
* Commits: 35
  * 33: Szczepan Faber
  * 1: Brice Dutheil
  * 1: Continuous Delivery Drone
* Improvements: 4
  * Documentation: 1
    * Improved documentation regarding unused stubbing detection [(#754)](https://github.com/mockito/mockito/pull/754)
  * Remaining changes: 3
    * Pushed release automation code into separate GitHub repository [(#751)](https://github.com/mockito/mockito/pull/751)
    * Added new release workflow for release automation [(#724)](https://github.com/mockito/mockito/pull/724)
    * DelayedExecution uses now a more precise approach to call a mock async. [(#704)](https://github.com/mockito/mockito/pull/704)

### 2.2.15 (2016-11-08 20:43 UTC)

* Authors: 2
* Commits: 4
  * 3: Brice Dutheil
  * 1: Szczepan Faber
* Improvements: 1
  * Enhancements: 1
    * Allow fluent usage of Mockito.framework() listeners methods [(#747)](https://github.com/mockito/mockito/pull/747)

### 2.2.14 (2016-11-08 20:11 UTC)

* Authors: 1
* Commits: 1
  * 1: Dmitriy Zaitsev
* Improvements: 1
  * Add missing copyright headers [(#746)](https://github.com/mockito/mockito/pull/746)

### 2.2.13 (2016-11-07 22:59 UTC)

* Authors: 1
* Commits: 1
  * 1: Brice Dutheil
* Improvements: 1
  * Removes deprecated way of documenting package by package-info.java [(#745)](https://github.com/mockito/mockito/pull/745)

### 2.2.12 (2016-11-07 04:29 UTC)

* Authors: 1
* Commits: 11
  * 11: Szczepan Faber
* Improvements: 1
  * Enhancements: 1
    * Exposed existing verification API so that it no longer leaks internal API [(#735)](https://github.com/mockito/mockito/pull/735)

### 2.2.11 (2016-11-04 04:51 UTC)

* Authors: 2
* Commits: 4
  * 2: bric3
  * 2: Szczepan Faber
* Improvements: 1
  * Enhancements: 1
    * New nullable(Class<T>) matcher for convenient matching of nullable arguments [(#734)](https://github.com/mockito/mockito/pull/734)

### 2.2.10 (2016-11-04 04:23 UTC)

* Authors: 2
* Commits: 11
  * 9: Szczepan Faber
  * 2: bric3
* Improvements: 2
  * Upgrade ByteBuddy to 1.5.3 [(#737)](https://github.com/mockito/mockito/issues/723)
  * Release automation tidy up [(#723)](https://github.com/mockito/mockito/pull/723)

### 2.2.9 (2016-10-26 14:44 UTC)

* Authors: 2
* Commits: 6
  * 5: Szczepan Faber
  * 1: Rafael Winterhalter
* Improvements: 2
  * Bugfixes: 1
    * Mockito 2 cannot mock kohsuke/github-api classes [(#701)](https://github.com/mockito/mockito/issues/701)
  * Remaining changes: 1
    * Simplified release process by hosting javadoc on javadoc.io [(#709)](https://github.com/mockito/mockito/pull/709)

### 2.2.8 (2016-10-24 11:04 UTC)

* Authors: 1
* Commits: 1
  * 1: Brice Dutheil
* Improvements: 1
  * Bugfixes: 1
    * Improve usability of arity Answers with regards to checked exceptions [(#707)](https://github.com/mockito/mockito/issues/707)

### 2.2.7 (2016-10-22 09:21 UTC)

* Authors: 1
* Commits: 1
  * 1: Gaëtan Muller
* Improvements: 1
  * Improved exception message [(#705)](https://github.com/mockito/mockito/pull/705)

### 2.2.6 (2016-10-21 01:04 UTC)

* Authors: 1
* Commits: 12
  * 12: Szczepan Faber
* Improvements: 1
  * New features: 1
    * New MockingDetails.printInvocations() API for debugging tests and edge cases [(#543)](https://github.com/mockito/mockito/issues/543)

### 2.2.5 (2016-10-17 16:34 UTC)

* Authors: 2
* Commits: 3
  * 2: Christian Schwarz
  * 1: Brice Dutheil
* Improvements: 1
  * Enhancements: 1
    * Unified logic of argument matching and capturing [(#635)](https://github.com/mockito/mockito/pull/635)

### 2.2.4 (2016-10-17 15:40 UTC)

* Authors: 1
* Commits: 1
  * 1: Rafael Winterhalter
* Improvements: 2
  * Bugfixes: 1
    * Fails to create mock of inner class hierarchy with type variable from outer class [(#699)](https://github.com/mockito/mockito/issues/699)
  * Remaining changes: 1
    * Mocking final classes with Java8 when compiling with -parameters flag [(#695)](https://github.com/mockito/mockito/issues/695)

### 2.2.3 (2016-10-16 14:12 UTC)

* Authors: 2
* Commits: 13
  * 12: Szczepan Faber
  * 1: Continuous Delivery Drone
* Improvements: 1
  * New features: 1
    * new API MockingDetails.getStubbings() for advanced users and integrations [(#542)](https://github.com/mockito/mockito/issues/542)

### 2.2.2 (2016-10-16 04:41 UTC)

* Authors: 3
* Commits: 5
  * 2: Brice Dutheil
  * 2: Szczepan Faber
  * 1: Continuous Delivery Drone
* Improvements: 3
  * Enhancements: 1
    * Improved the format of arguments in verification failures when describing short and byte values [(#693)](https://github.com/mockito/mockito/pull/693)
  * Remaining changes: 2
    * Enabled automated sync to central repository [(#690)](https://github.com/mockito/mockito/pull/690)
    * Pretty print primitive and wrappers types in Maps [(#571)](https://github.com/mockito/mockito/pull/571)

### 2.2.1 (2016-10-11 15:26 UTC)

* Authors: 2
* Commits: 2
  * 1: Continuous Delivery Drone
  * 1: Rafael Winterhalter
* Improvements: 1
  * Bugfixes: 1
    * Enabled mocking interface clone method [(#688)](https://github.com/mockito/mockito/issues/688)

### 2.2.0 (2016-10-09 23:52 UTC)

* Authors: 5
* Commits: 17
  * 13: Szczepan Faber
  * 1: Brice Dutheil
  * 1: Christian Schwarz
  * 1: Marcin Zajączkowski
  * 1: mgrafl
* Improvements: 5
  * Enhancements: 1
    * OSGI bundle problem - correct version specification syntax for bytebuddy [(#679)](https://github.com/mockito/mockito/pull/679)
  * Documentation: 1
    * Fix typo in README [(#674)](https://github.com/mockito/mockito/pull/674)
  * Remaining changes: 3
    * Updated release process so that Mockito can continuously deliver high quality features [(#683)](https://github.com/mockito/mockito/pull/683)
    * changed stackoverflow link to mockito questions [(#671)](https://github.com/mockito/mockito/pull/671)
    * Artifacts are now published to 'mockito' org in Bintray [(#670)](https://github.com/mockito/mockito/pull/670)

### 2.1.0 ( 2016-10-03 12:28 UTC)

# Brand new Mockito 2

Mockito 2: even cleaner tests!!! THANK you for writing great tests with us, your patience waiting for v2, and kudos to fantastic gang of contributors!

For comprehensive overview of the brand new release see [What's new in Mockito 2](https://github.com/mockito/mockito/wiki/What%27s-new-in-Mockito-2) wiki page.

### 2.1.0-RC.2 (2016-09-29 08:50 UTC)

* Authors: 7
* Commits: 38
  * 15: Rafael Winterhalter
  * 9: Brice Dutheil
  * 9: Tim van der Lippe
  * 2: Szczepan Faber
  * 1: Bruno Krebs
  * 1: Continuous Delivery Drone
  * 1: Oliver Gierke
* Improvements: 6
  * New features: 1
    * Added InlineByteBuddyMockMaker which uses the instrumentation API for redefining classes and inlining the mocking logic. [(#648)](https://github.com/mockito/mockito/pull/648)
  * Enhancements: 1
    * Improve exception message to hint at upgrading java minor version to latest [(#640)](https://github.com/mockito/mockito/issues/640)
  * Remaining changes: 4
    * Renamed FailureDetecter to Failure Detector. [(#654)](https://github.com/mockito/mockito/pull/654)
    * Typos in FailureDetecter [(#653)](https://github.com/mockito/mockito/issues/653)
    * A small fix on Mockito javadocs. Adding some styling and an anchor to section 12. [(#647)](https://github.com/mockito/mockito/pull/647)
    * Fixes #640 Warns user to upgrade if Java 8 version is to low [(#646)](https://github.com/mockito/mockito/pull/646)

### 2.1.0-RC.1 (2016-09-10 17:03 UTC)

* Authors: 49
* Commits: 726
  * 210: Brice Dutheil
  * 184: Szczepan Faber
  * 102: Rafael Winterhalter
  * 48: Continuous Delivery Drone
  * 36: Pascal Schumacher
  * 24: Tim van der Lippe
  * 20: Christian Schwarz
  * 15: Lukasz Szewc
  * 15: rafwin
  * 8: david
  * 5: pimterry
  * 4: Joseph Walton
  * 4: Marcin Zajączkowski
  * 3: Hans Joachim Desserud
  * 3: Michal Kordas
  * 2: Carlos Aguayo
  * 2: Clark Brewer
  * 2: Jan Tarnowski
  * 2: Jazzepi
  * 2: Jeffrey Falgout
  * 2: Krzysztof Wolny
  * 2: Philipp Jardas
  * 2: Roland Hauser
  * 2: thesnowgoose
  * 2: Urs Metz
  * 2: Vineet Kumar
  * 1: alberskib
  * 1: Alberto Scotto
  * 1: Andrey
  * 1: Ariel-isaacm
  * 1: ashleyfrieze
  * 1: Bartosz Miller
  * 1: bruce
  * 1: Christian Persson
  * 1: christian.schwarz
  * 1: David Xia
  * 1: Divyansh Gupta
  * 1: Eugene Ivakhno
  * 1: Evgeny Astafev
  * 1: fluentfuture
  * 1: Geoff Schoeman
  * 1: lloydjm77
  * 1: Marcin Zajaczkowski
  * 1: Roi Atalla
  * 1: Scott Markwell
  * 1: Shaun Abram
  * 1: Simen Bekkhus
  * 1: Tokuhiro Matsuno
  * 1: Tom Ball
* Improvements: 239
  * Incompatible changes with previous major version (v1.x): 13
    * Fixes #194 tweaks any matchers [(#510)](https://github.com/mockito/mockito/pull/510)
    * Moves Reporter friendly exception factory to internal package [(#495)](https://github.com/mockito/mockito/pull/495)
    * Drop support of Runner for <= JUnit4.4 [(#402)](https://github.com/mockito/mockito/issues/402)
    * JUnit runner detects unused stubs [(#401)](https://github.com/mockito/mockito/issues/401)
    * Get rid of ReturnValues [(#273)](https://github.com/mockito/mockito/issues/273)
    * use Gradle built-in osgi plugin [(#249)](https://github.com/mockito/mockito/issues/249)
    * push cglib into a separate jar [(#248)](https://github.com/mockito/mockito/issues/248)
    * Rework stubbing api with consecutive vararg to avoid JDK7+ warnings [(#239)](https://github.com/mockito/mockito/pull/239)
    * Moves responsibility of isTypeMockable to MockMaker [(#238)](https://github.com/mockito/mockito/pull/238)
    * Tweaks Matchers.any family matchers behavior [(#194)](https://github.com/mockito/mockito/issues/194)
    * stop depending on hamcrest internally [(#154)](https://github.com/mockito/mockito/issues/154)
    * stop producing mockito-all [(#153)](https://github.com/mockito/mockito/issues/153)
    * Argument matcher anyXxx() (i.e. anyString(), anyList()) should not match nulls [(#134)](https://github.com/mockito/mockito/issues/134)
  * Java 8 support: 5
    * Moves arity interfaces of java8 helper answers to public API [(#617)](https://github.com/mockito/mockito/pull/617)
    * AdditionalAnswers.answer family leaks internal classes [(#614)](https://github.com/mockito/mockito/issues/614)
    * Added default answer for java.util.stream.Stream [(#429)](https://github.com/mockito/mockito/pull/429)
    * Functional interfaces for Java 8 support in Mockito 2 [(#338)](https://github.com/mockito/mockito/pull/338)
    * Mock returning java.util.Optional should return Optional.empty() by default (Java 8) [(#191)](https://github.com/mockito/mockito/issues/191)
  * Behavior-Driven Development support: 6
    * Add shouldHaveNoMoreInteractions() to BDDMockito [(#314)](https://github.com/mockito/mockito/pull/314)
    * Add BDD version of verifyNoMoreInteractions() [(#311)](https://github.com/mockito/mockito/issues/311)
    * Fixes #203 : Introduce BDD InOrder verification [(#222)](https://github.com/mockito/mockito/pull/222)
    * Fixes #212 : Add shouldHaveZeroInteractions as BDD version of verifyZeroInteractions [(#221)](https://github.com/mockito/mockito/pull/221)
    * Add BDD version of verifyZeroInteractions() [(#212)](https://github.com/mockito/mockito/issues/212)
    * Introduce BDD InOrder verification [(#203)](https://github.com/mockito/mockito/issues/203)
  * Bugfixes: 17
    * Fixes #497 : RETURNS_DEEP_STUBS may try to mock final classes [(#615)](https://github.com/mockito/mockito/pull/615)
    * Mockito.when() fails when method could originate from superclass or interface  [(#508)](https://github.com/mockito/mockito/issues/508)
    * Stubbing with some AdditionalMatchers can NPE with null actuals [(#457)](https://github.com/mockito/mockito/issues/457)
    * Fixed OSGi metadata generation [(#388)](https://github.com/mockito/mockito/pull/388)
    * Problem verifying bridge methods [(#304)](https://github.com/mockito/mockito/issues/304)
    * MockUtil.isMock() no longer checks null [(#243)](https://github.com/mockito/mockito/issues/243)
    * mockito 2.0.14 fails to mock jetty httpclient [(#233)](https://github.com/mockito/mockito/issues/233)
    * 2.0.8-beta -> 2.0.9-beta 'Unable to initialize @Spy annotated field [(#220)](https://github.com/mockito/mockito/issues/220)
    * ArgumentCaptor no longer working for varargs [(#211)](https://github.com/mockito/mockito/pull/211)
    * Fixes #197 : Blocks ability to use negative value for timeout() and after() method. [(#207)](https://github.com/mockito/mockito/pull/207)
    * NoJUnitDependenciesTest is failing on Windows machine. [(#206)](https://github.com/mockito/mockito/issues/206)
    * Better protection against incompatible returned value of default answer, and get safely mock name [(#202)](https://github.com/mockito/mockito/pull/202)
    * Mockito.after() method accepts negative timeperiods and subsequent verifications always pass [(#197)](https://github.com/mockito/mockito/issues/197)
    * ArgumentCaptor no longer working for varargs [(#188)](https://github.com/mockito/mockito/issues/188)
    * java.lang.ClassCastException: java.lang.Class cannot be cast to java.lang.String [(#187)](https://github.com/mockito/mockito/issues/187)
    * Mockito 1.10.x timeout verification needs JUnit classes (VerifyError, NoClassDefFoundError) [(#152)](https://github.com/mockito/mockito/issues/152)
    * Deep stubbing with generic responses in the call chain is not working [(#128)](https://github.com/mockito/mockito/issues/128)
  * Enhancements: 28
    * make VerificationWithTimeoutTest#shouldAllowMixingOnlyWithTimeout mor… [(#587)](https://github.com/mockito/mockito/pull/587)
    * Checks.checkNotNull should emit IllegalArgumentException instead of NPE [(#554)](https://github.com/mockito/mockito/issues/554)
    * Fully register a class ancestry for GenericMetadataSupport [(#549)](https://github.com/mockito/mockito/pull/549)
    * improve stubbing warnings formatting [(#544)](https://github.com/mockito/mockito/issues/544)
    * improve Mockito.mockingDetails API [(#541)](https://github.com/mockito/mockito/issues/541)
    * Improved exception message for wanted but not invoked [(#506)](https://github.com/mockito/mockito/issues/506)
    * Reference correct types for multiple parent class loader if user class and Mockito are loaded by different loaders (e.g. OSGi) [(#471)](https://github.com/mockito/mockito/pull/471)
    * Refactored mock cache to be non-blocking. [(#470)](https://github.com/mockito/mockito/pull/470)
    * update objenesis version to 2.4 [(#447)](https://github.com/mockito/mockito/pull/447)
    * enable some ignored tests of BridgeMethodsHitAgainTest and DetectingF… [(#442)](https://github.com/mockito/mockito/pull/442)
    * Clean up issues reported by IntelliJ [(#436)](https://github.com/mockito/mockito/pull/436)
    * Inorder timeouts [(#424)](https://github.com/mockito/mockito/pull/424)
    * Fixed #407 Vararg method call on mock object fails [(#412)](https://github.com/mockito/mockito/pull/412)
    * Lazily verify without calling collector.verify() [(#389)](https://github.com/mockito/mockito/pull/389)
    * speedup travis build a bit by downloading gradle-bin instead of gradl… [(#371)](https://github.com/mockito/mockito/pull/371)
    * Issue #345 : Removes previously verified invocations when capturing argument is combined with after and atMost verifiers [(#349)](https://github.com/mockito/mockito/pull/349)
    * Introduce functional interfaces to improve Java 8 utilisation of Mockito 2 [(#337)](https://github.com/mockito/mockito/issues/337)
    * Modify StackTraceFilter to not exclude "good" stack trace elements [(#317)](https://github.com/mockito/mockito/pull/317)
    * Implement VerificationCollector which can collect multiple verifications. [(#287)](https://github.com/mockito/mockito/pull/287)
    * Add new API method to reset invocations of a mock, while maintaining all existing stubbing [(#286)](https://github.com/mockito/mockito/pull/286)
    * Upgraded to Byte Buddy 0.6.11 and took improved features in use. [(#242)](https://github.com/mockito/mockito/pull/242)
    * Test error after upgrading Mockito from 2.0.14-beta to 2.0.15-beta [(#237)](https://github.com/mockito/mockito/issues/237)
    * nicer textual printing of typed parameters [(#236)](https://github.com/mockito/mockito/issues/236)
    * Improves InjectMocks behavior when injectee has multiple fields of the same type [(#215)](https://github.com/mockito/mockito/pull/215)
    * Return empty value for Iterables [(#210)](https://github.com/mockito/mockito/issues/210)
    * Fixes #200 : ArgumentCaptor.forClass is more friendly with generic types [(#201)](https://github.com/mockito/mockito/pull/201)
    * ArgumentCaptor.fromClass's return type should match a parameterized type [(#200)](https://github.com/mockito/mockito/issues/200)
    * Make PropertyAndSetterInjection field sorting consistent [(#176)](https://github.com/mockito/mockito/pull/176)
  * Documentation: 10
    * Mockito Javadoc has a TODO about hamcrest [(#593)](https://github.com/mockito/mockito/issues/593)
    * Reintroduces javadoc stylesheet [(#589)](https://github.com/mockito/mockito/pull/589)
    * Update version explanation with bad beta versions [(#588)](https://github.com/mockito/mockito/pull/588)
    * Update documentation links in travis config comments [(#558)](https://github.com/mockito/mockito/pull/558)
    * Fix again javadoc stylesheet [(#552)](https://github.com/mockito/mockito/issues/552)
    * Changes to InvocationOnMock API should include version info [(#420)](https://github.com/mockito/mockito/issues/420)
    * Fixed method name to verifyNoMoreInteractions [(#413)](https://github.com/mockito/mockito/pull/413)
    * Fixes #312.  Added documentation in OngoingStubbing.thenThrow(). [(#381)](https://github.com/mockito/mockito/pull/381)
    * Improve the custom argument matching documentation [(#334)](https://github.com/mockito/mockito/issues/334)
    * ThrowsExceptionClass is urealiable - exception doesn't containt stack trace [(#312)](https://github.com/mockito/mockito/issues/312)
  * Remaining changes: 160
    * Fixes #629 - width of some headers in javadoc [(#630)](https://github.com/mockito/mockito/pull/630)
    * Javadoc CSS width issue [(#629)](https://github.com/mockito/mockito/issues/629)
    * Fixed javadoc documentation in the main class [(#628)](https://github.com/mockito/mockito/pull/628)
    * Release procedure for branches and some javadoc [(#627)](https://github.com/mockito/mockito/pull/627)
    * Ensured javadocs are correct [(#626)](https://github.com/mockito/mockito/pull/626)
    * Tweak javadoc [(#625)](https://github.com/mockito/mockito/issues/625)
    * Updated the javadocs [(#623)](https://github.com/mockito/mockito/pull/623)
    * Allow build script to release any release parent branch [(#622)](https://github.com/mockito/mockito/pull/622)
    * Fixes #548 from now on verification happens always call in other thre… [(#619)](https://github.com/mockito/mockito/pull/619)
    * Fixes #595, going toward 2.1.0 instead of 2.0.0 [(#605)](https://github.com/mockito/mockito/pull/605)
    * Release notes group improvements by labels [(#604)](https://github.com/mockito/mockito/pull/604)
    * Release notes group improvements by labels [(#603)](https://github.com/mockito/mockito/issues/603)
    * suppressed compiler warning and unchecked collections cast [(#600)](https://github.com/mockito/mockito/pull/600)
    * Improved release notes generation [(#599)](https://github.com/mockito/mockito/pull/599)
    * Generated release notes contain unsorted and duplicate committers [(#598)](https://github.com/mockito/mockito/issues/598)
    * Removed TODO and dead code [(#597)](https://github.com/mockito/mockito/pull/597)
    * Handle beta non-semantic versioning scheme [(#595)](https://github.com/mockito/mockito/issues/595)
    * Make the build script aware of the release branch [(#594)](https://github.com/mockito/mockito/issues/594)
    * stylesheet-tweaks-for-openjdk6 [(#592)](https://github.com/mockito/mockito/pull/592)
    * refactored ArgumentMatchingTool to a static utillity class [(#591)](https://github.com/mockito/mockito/pull/591)
    * removed package org.mockito.internal.listeners [(#590)](https://github.com/mockito/mockito/pull/590)
    * fix some rawtype warnings [(#579)](https://github.com/mockito/mockito/pull/579)
    * made some timeouts in VerificationAfterDelayTest and VerificationWith… [(#578)](https://github.com/mockito/mockito/pull/578)
    * Bumped Gradle and enabled Build Scans [(#576)](https://github.com/mockito/mockito/pull/576)
    * fix some rawtype warnings [(#574)](https://github.com/mockito/mockito/pull/574)
    * replace TestBase#assertNotEquals with AssertJ #isNotEqualTo [(#573)](https://github.com/mockito/mockito/pull/573)
    * replace TestBase#assertContainsType(final Collection<?> list, final C… [(#572)](https://github.com/mockito/mockito/pull/572)
    * Pretty print primitive and wrappers types in Maps [(#571)](https://github.com/mockito/mockito/pull/571)
    * Improved the public API of MockingDetails [(#569)](https://github.com/mockito/mockito/pull/569)
    * Ensured that MockitoJUnitRunner is thread safe wrt unused stubs detection [(#568)](https://github.com/mockito/mockito/pull/568)
    * Make travis use OracleJDK7 instead of OpenJDK7 [(#566)](https://github.com/mockito/mockito/pull/566)
    * Publish Mockito build results to Gradle Build Scans [(#564)](https://github.com/mockito/mockito/issues/564)
    * Fixes #554 : Checks.checkNotNull now throws IAE instead of NPE [(#560)](https://github.com/mockito/mockito/pull/560)
    * Replace or remove code.google.com links in documentation [(#557)](https://github.com/mockito/mockito/pull/557)
    * Move Mockito internal classes to internal package [(#556)](https://github.com/mockito/mockito/pull/556)
    * JUnit rules report unused stubs - fixes #384 [(#555)](https://github.com/mockito/mockito/pull/555)
    * Uses the hosts addon in order to avoid the buffer overflow affecting … [(#553)](https://github.com/mockito/mockito/pull/553)
    * Fixed #538 changed error message in case initialization for mock injection fails. [(#550)](https://github.com/mockito/mockito/pull/550)
    * VerificationWithTimeoutTest is unstable [(#548)](https://github.com/mockito/mockito/issues/548)
    * refactored ObjectMethodsGuru to a static utility class [(#547)](https://github.com/mockito/mockito/pull/547)
    * inlined ArrayUtils.isEmpty() in ArgumentsProcessor [(#540)](https://github.com/mockito/mockito/pull/540)
    * Improve error message when @InjectMocks is uses on an interface or enum field [(#538)](https://github.com/mockito/mockito/issues/538)
    * Move release skipping logic to Gradle [(#536)](https://github.com/mockito/mockito/pull/536)
    * refactored SuperTypesLastSorter to a static utility class [(#535)](https://github.com/mockito/mockito/pull/535)
    * Fix typo in Javadocs [(#532)](https://github.com/mockito/mockito/pull/532)
    * Missing generics info on collection matchers [(#528)](https://github.com/mockito/mockito/pull/528)
    * Add regression test for #508 [(#525)](https://github.com/mockito/mockito/pull/525)
    * made some timeouts in VerificationAfterDelayTest and VerificationWith… [(#523)](https://github.com/mockito/mockito/pull/523)
    * replace TestBase#assertContains(String sub, String string) with Asser… [(#519)](https://github.com/mockito/mockito/pull/519)
    * Verify build on Travis also with Java 7 and 8 [(#518)](https://github.com/mockito/mockito/pull/518)
    * Make ciBuild depends also on subprojects state [(#517)](https://github.com/mockito/mockito/pull/517)
    * replace TestBase#assertNotContains(String sub, String string) with As… [(#516)](https://github.com/mockito/mockito/pull/516)
    * refactored AllInvocationsFinder and VerifiableInvocationsFinder to st… [(#515)](https://github.com/mockito/mockito/pull/515)
    * refactored MockUtil to a static utility class (#426) [(#514)](https://github.com/mockito/mockito/pull/514)
    * replace TestBase#assertContainsIgnoringCase(String sub, String string… [(#513)](https://github.com/mockito/mockito/pull/513)
    * fix some raw type warnings in tests [(#512)](https://github.com/mockito/mockito/pull/512)
    * Fix some warnings [(#511)](https://github.com/mockito/mockito/pull/511)
    * Improved exception message - fixes issue 506 [(#507)](https://github.com/mockito/mockito/pull/507)
    * fixed some rawtype warnings [(#504)](https://github.com/mockito/mockito/pull/504)
    * refactored NonGreedyNumberOfInvocationsInOrderChecker to a static uti… [(#503)](https://github.com/mockito/mockito/pull/503)
    * refactored ArgumentsComparator to a static utility class (#426) [(#502)](https://github.com/mockito/mockito/pull/502)
    * refactored TestMethodsFinder to a static utility class (#426) [(#501)](https://github.com/mockito/mockito/pull/501)
    * remove unused imports [(#498)](https://github.com/mockito/mockito/pull/498)
    * DEEP_STUBS tries to mock final class [(#497)](https://github.com/mockito/mockito/issues/497)
    * Renames Matchers to ArgumentMatchers to avoid name clash with Hamcrest Matchers class [(#496)](https://github.com/mockito/mockito/pull/496)
    * Code cov on releases [(#493)](https://github.com/mockito/mockito/pull/493)
    * Revert "Remove deprecated method" [(#492)](https://github.com/mockito/mockito/pull/492)
    * Deprecate whitebox and corresponding verboserunner and junitfailureha… [(#491)](https://github.com/mockito/mockito/pull/491)
    * Cleanup: removed dead/unnecessary classes [(#486)](https://github.com/mockito/mockito/pull/486)
    * Update version scheme to publish release candidate [(#483)](https://github.com/mockito/mockito/pull/483)
    * Exclude mockito internal packages from the Javadoc [(#481)](https://github.com/mockito/mockito/pull/481)
    * fix grammar of sentence in Mockito javadoc [(#479)](https://github.com/mockito/mockito/pull/479)
    * refactored ThreadSafeMockingProgress to a singleton [(#476)](https://github.com/mockito/mockito/pull/476)
    * Typo fix [(#475)](https://github.com/mockito/mockito/pull/475)
    * fix some rawtype warnings in tests [(#469)](https://github.com/mockito/mockito/pull/469)
    * add missing since javadoc tags for recently added methods and classes… [(#468)](https://github.com/mockito/mockito/pull/468)
    * fix some rawtype warnings in tests [(#467)](https://github.com/mockito/mockito/pull/467)
    * fix some rawtype warnings in tests [(#464)](https://github.com/mockito/mockito/pull/464)
    * refactored InvocationsFinder to static utility class [(#462)](https://github.com/mockito/mockito/pull/462)
    * delete disabled test for removed objenesis missing reporting feature [(#460)](https://github.com/mockito/mockito/pull/460)
    * fix some rawtype warnings in tests [(#459)](https://github.com/mockito/mockito/pull/459)
    * remove dead code in ClassCacheVersusClassReloadingTest [(#458)](https://github.com/mockito/mockito/pull/458)
    * fix some rawtype warnings [(#456)](https://github.com/mockito/mockito/pull/456)
    * activate VerificationWithTimeoutTest#shouldAllowTimeoutVerificationIn… [(#455)](https://github.com/mockito/mockito/pull/455)
    * Modified JavaDoc for ArgumentMatcher [(#454)](https://github.com/mockito/mockito/pull/454)
    * javadoc: improve grammar of some sentences [(#452)](https://github.com/mockito/mockito/pull/452)
    * Refactored Timeout and After concurrent test [(#451)](https://github.com/mockito/mockito/pull/451)
    * Make tests which test for timeouts with Thread#sleep more lenient. [(#446)](https://github.com/mockito/mockito/pull/446)
    * Add PARAMETER ElementType to @Mock [(#444)](https://github.com/mockito/mockito/pull/444)
    * downgrade assertj-core version to 1.7.1 because this version is java … [(#443)](https://github.com/mockito/mockito/pull/443)
    * delete ignored cglib related tests [(#441)](https://github.com/mockito/mockito/pull/441)
    * PluginStackTraceFilteringTest failing locally [(#435)](https://github.com/mockito/mockito/issues/435)
    * Very tiny typo. [(#434)](https://github.com/mockito/mockito/pull/434)
    * Fixes #426 Refactored InvocationMarker to a static utility class [(#432)](https://github.com/mockito/mockito/pull/432)
    * Fixes #426 Dropped class HandyReturnValues [(#431)](https://github.com/mockito/mockito/pull/431)
    * Refactored class Reporter to a static utillity [(#427)](https://github.com/mockito/mockito/pull/427)
    * BDDMockito: rename willNothing to willDoNothing [(#419)](https://github.com/mockito/mockito/pull/419)
    * Vararg method call on mock object fails when used org.mockito.AdditionalAnswers#delegatesTo [(#407)](https://github.com/mockito/mockito/issues/407)
    * Fixes #374 Removed deprecated classes and methods [(#404)](https://github.com/mockito/mockito/pull/404)
    * Bump JUnit to 4.12 [(#400)](https://github.com/mockito/mockito/issues/400)
    * Mocking Class that inherits from Abstract class in a different JAR doesn't override methods [(#398)](https://github.com/mockito/mockito/issues/398)
    * Remove deprecated code [(#386)](https://github.com/mockito/mockito/pull/386)
    * OSGi metadata is incorrect [(#385)](https://github.com/mockito/mockito/issues/385)
    * JUnit rule logs warnings about unsued / misused stubs [(#384)](https://github.com/mockito/mockito/issues/384)
    * correct package declaration of VerificationWithDescriptionTest [(#382)](https://github.com/mockito/mockito/pull/382)
    * Remove duplication. [(#377)](https://github.com/mockito/mockito/pull/377)
    * Fix typo in example in javadoc. [(#376)](https://github.com/mockito/mockito/pull/376)
    * Remove deprecated API from Mockito 2  [(#374)](https://github.com/mockito/mockito/issues/374)
    * Fixes #365 Simplify the InvocationOnMock-API to get a casted argument [(#373)](https://github.com/mockito/mockito/pull/373)
    * Travis / CI improvements [(#369)](https://github.com/mockito/mockito/pull/369)
    * Simplify the InvocationOnMock-API to get a casted argument [(#365)](https://github.com/mockito/mockito/issues/365)
    * Use the new issue/pr templates [(#361)](https://github.com/mockito/mockito/pull/361)
    * Show correct location of unwanted interaction with mock when using MockitoJUnitRule [(#344)](https://github.com/mockito/mockito/pull/344)
    * Fixes #256 :Alternative fix to #259, windows build [(#342)](https://github.com/mockito/mockito/pull/342)
    * Mockito Hamcrest integration does not handle argument primitive matching well [(#336)](https://github.com/mockito/mockito/issues/336)
    * Ensure CI build fails when release task breaks [(#333)](https://github.com/mockito/mockito/issues/333)
    * Move build and release automation logic to Kotlin [(#331)](https://github.com/mockito/mockito/issues/331)
    * Minor formatting, typo and clarification fixes in README [(#313)](https://github.com/mockito/mockito/pull/313)
    * Tweaks to the main Mockito javadocs to aid readability [(#309)](https://github.com/mockito/mockito/pull/309)
    * Eliminate direct dependency on ObjenesisInstantiator [(#306)](https://github.com/mockito/mockito/pull/306)
    * Refactor some utilities and TODO done [(#301)](https://github.com/mockito/mockito/pull/301)
    * Update StackOverflow link to Mockito tag [(#296)](https://github.com/mockito/mockito/pull/296)
    * Removed deprecated ReturnValues and all it's occurrences [(#294)](https://github.com/mockito/mockito/pull/294)
    * Remove validateSerializable() [(#293)](https://github.com/mockito/mockito/pull/293)
    * Add optional answer to support mocked Builders [(#288)](https://github.com/mockito/mockito/pull/288)
    * Correcting public website url in Maven POM [(#281)](https://github.com/mockito/mockito/pull/281)
    * Reintroduce null check on MockUtil.isMock() [(#280)](https://github.com/mockito/mockito/pull/280)
    * Issue #268: Added support for generic arrays as return types. [(#270)](https://github.com/mockito/mockito/pull/270)
    * RETURN_DEEP_STUBS and toArray(T[]) stops working with versions > 1.9.5 [(#268)](https://github.com/mockito/mockito/issues/268)
    * Ignore Groovy meta methods when instrumenting. [(#266)](https://github.com/mockito/mockito/pull/266)
    * Fix typo in docs, missing breaklines. [(#264)](https://github.com/mockito/mockito/pull/264)
    * Fixes #260: Typo in documentation [(#261)](https://github.com/mockito/mockito/pull/261)
    * Typo in documentation [(#260)](https://github.com/mockito/mockito/issues/260)
    * Removing new line in bottom script. it seems that javadoc… [(#259)](https://github.com/mockito/mockito/pull/259)
    * Minify the JS file [(#258)](https://github.com/mockito/mockito/pull/258)
    * Upgraded to Byte Buddy 0.6.12.  [(#257)](https://github.com/mockito/mockito/pull/257)
    * task mockitoJavadoc fails when compiling in windows [(#256)](https://github.com/mockito/mockito/issues/256)
    * [#251] Migrate Fest Assert code to AssertJ [(#252)](https://github.com/mockito/mockito/pull/252)
    * Unit tests improvements: migrate from legacy FEST Assert code to AssertJ [(#251)](https://github.com/mockito/mockito/issues/251)
    * no jars in source code [(#250)](https://github.com/mockito/mockito/issues/250)
    * Serializable check is too harsh [(#245)](https://github.com/mockito/mockito/issues/245)
    * Replaces cobertura/coveralls by jacoco/codecov [(#241)](https://github.com/mockito/mockito/pull/241)
    * Fixes coverage reports [(#240)](https://github.com/mockito/mockito/pull/240)
    * Fixes #220 constructor invoking methods raise NPE [(#235)](https://github.com/mockito/mockito/pull/235)
    * Cannot instantiate type with public method of a public parent class having a non public types in signature [(#234)](https://github.com/mockito/mockito/pull/234)
    * Fixes #228: fixed a verify() call example in @Captor javadoc [(#229)](https://github.com/mockito/mockito/pull/229)
    * @Captor javadoc contains a wrong call example [(#228)](https://github.com/mockito/mockito/issues/228)
    * [#206] Fix issue related to windows path [(#223)](https://github.com/mockito/mockito/pull/223)
    * Add .gitattributes to enforce LF [(#219)](https://github.com/mockito/mockito/pull/219)
    * InjectMocks injects mock into wrong field [(#205)](https://github.com/mockito/mockito/issues/205)
    * mockito spy lost annotations from the spied instance class [(#204)](https://github.com/mockito/mockito/issues/204)
    * Fixes typo [(#184)](https://github.com/mockito/mockito/pull/184)
    * Replace CGLIB by Bytebuddy [(#171)](https://github.com/mockito/mockito/pull/171)
    * @InjectMocks and @Spy on same field should cause MockitoException [(#169)](https://github.com/mockito/mockito/issues/169)
    * GitHubIssues fetcher is now aware of GitHub pagination [(#163)](https://github.com/mockito/mockito/pull/163)
    * Excluded missing transitive dependency of the coveralls gradle plugin to fix failing build. [(#161)](https://github.com/mockito/mockito/pull/161)
    * Internal Comparator violates its general contract [(#155)](https://github.com/mockito/mockito/issues/155)
    * Concise way to collect multiple verify failures, ideally with JUnitCollector or  derivative [(#124)](https://github.com/mockito/mockito/issues/124)
    * Allow convenient spying on abstract classes [(#92)](https://github.com/mockito/mockito/issues/92)
    * Added custom failure message to Mockito.verify. Issue 482 [(#68)](https://github.com/mockito/mockito/pull/68)

### 1.10.19 (2014-12-31 17:04 UTC)

* Authors: 4
* Commits: 10
  * 6: pbielicki
  * 2: Szczepan Faber
  * 1: Brice Dutheil
  * 1: Radim Kubacki
* Improvements: 2
  * Deep stubbing with generic responses in the call chain is not working [(#128)](https://github.com/mockito/mockito/issues/128)
  * Make org.mockito.asm.signature package optional in Import-Packages. [(#125)](https://github.com/mockito/mockito/pull/125)

### 1.10.18 (2014-12-30 09:45 UTC)

* Authors: 4
* Commits: 14
  * 11: Szczepan Faber
  * 1: pbielicki
  * 1: Brice Dutheil
  * 1: Matthew Dean
* Improvements: 2
  * Fix grammar issues in the javadoc documentation [(#149)](https://github.com/mockito/mockito/pull/149)
  * 1.10 regression (StackOverflowError) with interface where generic type has itself as upper bound [(#114)](https://github.com/mockito/mockito/issues/114)

### 1.10.17 (2014-12-16 09:38 UTC)

* Authors: 2
* Commits: 11
  * 8: Szczepan Faber
  * 3: jerzykrlk
* Improvements: 2
  * Make Mockito JUnit rule easier to use [(#140)](https://github.com/mockito/mockito/issues/140)
  * Tidy-up public API of Mockito JUnit rule [(#139)](https://github.com/mockito/mockito/issues/139)

### 1.10.16 (2014-12-14 23:56 UTC)

* Authors: 1
* Commits: 15
  * 15: Szczepan Faber
* Improvements: 2
  * Promote incubating features [(#137)](https://github.com/mockito/mockito/issues/137)
  * Automate maven central sync [(#136)](https://github.com/mockito/mockito/issues/136)

### 1.10.15 (2014-12-12 23:42 UTC)

* Authors: 3
* Commits: 27
  * 24: Szczepan Faber
  * 2: Lovro Pandzic
  * 1: Dhruv Arora
* Improvements: 2
  * Introduce PluginSwitch extension point [(#135)](https://github.com/mockito/mockito/issues/135)
  * BDD mockito cleanup [(#100)](https://github.com/mockito/mockito/pull/100)

### 1.10.14 (2014-12-02 10:01 UTC)

* Authors: 3
* Commits: 23
  * 16: Szczepan Faber
  * 5: Werner Beroux
  * 2: fluentfuture
* Improvements: 1
  * @Spy annotation supports abstract classes [(#126)](https://github.com/mockito/mockito/pull/126)

### 1.10.13 (2014-11-22 21:28 UTC)

* Authors: 2
* Commits: 32
  * 31: Szczepan Faber
  * 1: fluentfuture
* Improvements: 2
  * Problem spying on abstract classes [(#122)](https://github.com/mockito/mockito/issues/122)
  * tidy-up and rework release notes automation [(#119)](https://github.com/mockito/mockito/issues/119)

### 1.10.12 (2014-11-17 00:09 UTC)

Special thanks to _Ben Yu_ for the original idea and implementation of the abstract classes spying!

* Authors: 2
* Commits: 23
  * 22: Szczepan Faber based on Ben Yu's PR
  * 1: jerzykrlk
* Improvements: 2
  * Other: 2
    * Make the build working on windows [(#115)](https://github.com/mockito/mockito/pull/115)
    * Allow convenient spying on abstract classes [(#92)](https://github.com/mockito/mockito/issues/92)

### 1.10.11 (2014-11-15 17:46 UTC)

* Authors: 3
* Commits: 23
  * 17: Szczepan Faber
  * 3: Marcin Zajaczkowski, David Maciver
* Improvements: 1
  * Other: 1
    * Allow instances of other classes in AdditionalAnswers.delegatesTo [(#112)](https://github.com/mockito/mockito/issues/112)

### 1.10.10 (2014-10-28 07:26 UTC)

* Authors: 2
* Commits: 2
  * 1: Szczepan Faber, Ross Black
* Improvements: 1
  * Improved exception handling of AdditionalAnswers#delegatesTo [(#113)](https://github.com/mockito/mockito/pull/113)

### 1.10.9 (2014-10-22 08:51 UTC)

* Authors: 1
* Commits: 15
  * 15: Szczepan Faber
* Improvements: 2
  * Improve internal implementation so that it is possible to implement mocking abstract classes [(#107)](https://github.com/mockito/mockito/issues/107)
  * Continuous deployment should not release new version if binaries are equal [(#105)](https://github.com/mockito/mockito/issues/105)

### 1.10.8 (2014-10-10 00:58 UTC)

* Authors: 1
* Commits: 3
  * 3: Brice Dutheil
* Improvements: 1
  * Fixes issue [#99](https://github.com/mockito/mockito/issues/99) : RETURNS_DEEP_STUBS automatically tries to create serializable mocks [(#103)](https://github.com/mockito/mockito/pull/103)

### 1.10.7 (2014-10-08 18:52 UTC)

* Authors: 2
* Commits: 4
  * 2: Szczepan Faber
  * 2: Continuous Delivery Drone
* Improvements: 2
  * possible NPE exception when class cannot be mocked via PowerMockito [(#98)](https://github.com/mockito/mockito/issues/98)
  * Documentation mentions non-existing version 1.9.8 in few places [(#101)](https://github.com/mockito/mockito/issues/101)

### 1.10.6 (2014-10-07 19:38 UTC)

* Authors: 1
* Commits: 4
  * 4: Szczepan Faber
* Improvements: 1
  * Document minimum JUnit requirement for MockitoJUnitRule [(#96)](https://github.com/mockito/mockito/issues/96)

### 1.10.5 (2014-10-06 19:20 UTC)

* Authors: 2
* Commits: 8
  * 6: Szczepan Faber
  * 2: Continuous Delivery Drone
  * 1: jerzykrlk@gmail.com
* Improvements: 1
  * Added a Mockito @Rule for JUnit [(#85)](https://github.com/mockito/mockito/pull/85)

### 1.10.4 (2014-09-28 18:20 UTC)

* Authors: 1
* Commits: 4
  * 4: Szczepan Faber
* Improvements: 1
  * Improve exception messages when user mocks a method declared on non-public parent [(#90)](https://github.com/mockito/mockito/issues/90)

### 1.10.3 (2014-09-27 20:45 UTC)

* Authors: 2
* Commits: 4
  * 2: Marcin Grzejszczak, Szczepan Faber
* Improvements: 1
  * Fix flaky test: TimeoutTest [(#66)](https://github.com/mockito/mockito/pull/66)

### 1.10.2 (2014-09-26 16:37 UTC)

* Authors: 2
* Commits: 8
  * 5: Szczepan Faber
  * 3: Hugh Hamill
* Improvements: 2
  * Fixed DelegatingMethod.equals() so that it's easier to extend Mockito by custom verification modes [(#87)](https://github.com/mockito/mockito/pull/87)
  * Ensure continuous deployment process does not produce "-dev" versions [(#88)](https://github.com/mockito/mockito/issues/88)

### 1.10.0 (2014-09-25 22:25 UTC)

Thanks everybody for great contributions! Next release should come quicker thanks to continuous deployment goodness.

* Authors: 25
* Commits: 307
  * 134: Brice Dutheil
  * 117: Szczepan Faber
  * 10: Marcin Grzejszczak
  * 6: Tim Perry
  * 5: Lovro Pandzic
  * 4: alberski
  * 3: Dmytro Chyzhykov, Gunnar Wagenknecht, Kamil Szymanski, pimterry, Ian Parkinson
  * 2: Emory Merryman, U-Michal-Komputer\Michal
  * 1: Vivian Pennel, Kuangshi Yan, Marius Volkhart, Marcin Zajaczkowski, Andrei Solntsev, Markus Wüstenberg, Marius Volkhart, Ken Dombeck, jrrickard, Philip Aston, Ivan Vershinin, ludovic.meurillon@gmail.com
* Improvements: 21
  * Added useful links to README.md [(#58)](https://github.com/mockito/mockito/pull/58)
  * Fixed wrong javadoc for AdditionalAnswers [(#56)](https://github.com/mockito/mockito/pull/56)
  * Enabled continuous integration with Travis CI and coverage tracking with coveralls [(#18)](https://github.com/mockito/mockito/pull/18)
  * Verification with timeout measures time more accurately [(#15)](https://github.com/mockito/mockito/pull/15)
  * Allow calling real implementation of jdk8 extension methods [(#39)](https://github.com/mockito/mockito/pull/39)
  * Deprecated timeout().never(), in line with timeout().atMost() [(#14)](https://github.com/mockito/mockito/pull/14)
  * New "then" method for BDD-style interaction testing [(#38)](https://github.com/mockito/mockito/pull/38)
  * Improved behavior of EqualsWithDelta with regards to null handling [(#21)](https://github.com/mockito/mockito/pull/21)
  * New "getArgumentAt" method for convenient implementation of custom Answers [(#41)](https://github.com/mockito/mockito/pull/41)
  * Coveralls coverage tracking tool allows Mockito source code preview [(#62)](https://github.com/mockito/mockito/pull/62)
  * Improve NoInteractionsWanted report to include the name of the mock [(#63)](https://github.com/mockito/mockito/pull/63)
  * New lightweight, stub-only mocks for scenarios where high performance is needed [(#86)](https://github.com/mockito/mockito/issues/86)
  * Improved the javadoc example of custom Answer implementation [(#22)](https://github.com/mockito/mockito/pull/22)
  * Avoided classloader issue when testing in Eclipse plugins environment [(#24)](https://github.com/mockito/mockito/pull/24)
  * Removed .java files from main mockito jar artifacts [(#28)](https://github.com/mockito/mockito/pull/28)
  * Smarter constructor injection by choosing "biggest" constructor instead of the default one [(#29)](https://github.com/mockito/mockito/pull/29)
  * New "MockingDetails.getInvocations" method for inspecting what happened with the mock [(#10)](https://github.com/mockito/mockito/pull/10)
  * Deep stub style mocks can be serialized [(#30)](https://github.com/mockito/mockito/pull/30)
  * Improved MockitoTestNGListener by making it reset argument captors before each test [(#6)](https://github.com/mockito/mockito/pull/6)
  * Mock serialization/deserialization across classloader/JVM [(#5)](https://github.com/mockito/mockito/pull/5)
  * Fixed the behavior of compareTo method of the mock objects [(#32)](https://github.com/mockito/mockito/pull/32)

Older implemented improvements were managed in the original issue tracker and can be viewed at [Google Code site](https://code.google.com/p/mockito/issues/list?can=1&q=label%3AMilestone-Release1.10.0).

### 1.9.5 (06-10-2012)

Few minor bug fixes and a relatively small extension point added to improve the android experience.

* **StackTraceCleaner API** - to improve the experience of mocking on android platform we've added an extension point for cleaning the stack traces. This allows the friends behind the [dexmaker](http://code.google.com/p/dexmaker) to implement custom stack trace filter and hence make the Mockito verification errors contain clean and tidy stack traces. Clean stack trace is something Mockito always cares about! Thanks a lot **Jesse Wilson** for reporting, submitting the initial patch and validating the final solution.
* javadoc fix (issue 356) - thanks **konigsberg** for reporting and patching and **Brice** for merging.
* @InjectMocks inconsistency between java 6 and 7 (issue 353) - neat **Brice's** work.
* Fixed a problem with auto boxed default return values, needed for the MockMaker extension point (issue 352). Thanks so much **Jesse Wilson** for reporting and the patch, and **Brice** for merging!

### 1.9.5 rc-1 (03-06-2012)

Thanks a lot to all community members for reporting issues, submitting patches and ideas! The complete list of bug fixes and features is listed [here](http://code.google.com/p/mockito/issues/list?can=1&q=label%3AMilestone-Release1.9.5-rc1&colspec=ID+Type+Status+Priority+Milestone+Owner+Summary&cells=tiles).

* **MockMaker API**. The [MockMaker](http://docs.mockito.googlecode.com/hg/1.9.5-rc1/org/mockito/plugins/MockMaker.html) extension point enables replacing the default proxy-creation implementation (cglib) with... something else :) For example, with a help from [dexmaker](http://code.google.com/p/dexmaker) you will be able to use Mockito with Android tests. Special thanks to friends from Google: **Jesse Wilson** and **Crazy Bob** for initiating the whole idea, for the patches, and for friendly pings to get the new feature released.
* [**MockingDetails**](http://docs.mockito.googlecode.com/hg/1.9.5-rc1/org/mockito/Mockito.html#mocking_details) can be used to inspect objects and find out if they are Mockito mocks or spies. Special thanks to **David Wallace**, one of the new members of the Mockito team, who was championing the feature.
* [**The delegating answer**](http://docs.mockito.googlecode.com/hg/1.9.5-rc1/org/mockito/Mockito.html#delegating_call_to_real_instance) is useful for spying some objects difficult to spy in the typical way. Thanks to **Brice Dutheil** (who is one of the most active contributors :) for making it happen!
* Driven by changes needed by the MockMaker API we started externalizing some internal interfaces. Hence some new public types. Down the road it will make Mockito more flexible and extensible.
* We moved some classes from the public interface 'org.mockito.exceptions' to an internal interface (Pluralizer, Discrepancy, JUnitTool). Don't worry though, those classes were not exposed by our API at all so chance that someone is uses them is minimal. Just in case, though, I left the deprecated variants.
* Special thanks for the participants of **Hackergarten Paris**:
  * **Eric Lefevre** for his contribution on the simple answers, yet useful.
  * **José Paumard**, who contributed several issues including the delegating answer.
  * **Julien Meddah** for an even better error reporting.

### 1.9.0 (16-12-2011)

If you're upgrading from 1.8.5 please read about all the goodies delivered by 1.9.0-rc1! This release contains 2 bug fixes and 1 awesome improvement. Full details of this release are listed [here](http://code.google.com/p/mockito/issues/list?can=1&q=label%3AMilestone-Release1.9&colspec=ID+Type+Status+Priority+Milestone+Owner+Summary&cells=tiles).

* Thanks to our mysterious friend **Dharapvj**, we now have most beautiful documentation. Take a look [here](http://docs.mockito.googlecode.com/hg/latest/org/mockito/Mockito.html)
* Credits to **Daniel Spilker** for helping out with the issue related to mocks in superclasses
* **Dpredovic** helped making the Mockito.reset() even better :)

### 1.9.0-rc1 (23-07-2011)

 * Annotations are smarter. They can use constructor injection and instantiate objects. More information [here](http://docs.mockito.googlecode.com/hg/1.9.0-rc1/org/mockito/Mockito.html#23).
 * To keep the test code slim you can now create a stub [in one line](http://docs.mockito.googlecode.com/hg/1.9.0-rc1/org/mockito/Mockito.html#24).
 * Made it possible to verify interactions [ignoring stubs](http://docs.mockito.googlecode.com/hg/1.9.0-rc1/org/mockito/Mockito.html#25).
 * Fixed various bugs & enhancements. Full list is [here](http://code.google.com/p/mockito/issues/list?can=1&q=label%3AMilestone-Release1.9&colspec=ID+Type+Status+Priority+Milestone+Owner+Summary&cells=tiles).
 * **THANKS** to all the community members who helped improving Mockito! **Brice Dutheil** is a new Mockito champion, having contributed a lot of excellent code in the recent months! Without him, there wouldn't be any release and your mailing list queries wouldn't be answered so promptly! Brice - thank you and welcome to the team! Some fresh mojitos ought to be served to:
  * *Steven Baker* for sharing the one-liner stubs idea
  * *Konrad Garus* for reporting the inconsistencies in the docs & exception messages
  * *Murat Knecht* for the verbose logging
  * *Krisztian Milesz* for the maven javadoc enhancement
  * *Edwinstang* for patience and patches to injection logic
  * *Kristofer Karlsson* for important bug reports and help with the mailing list
  * *Gordon Tyler* for his vigilance and help on getting the main docs sorted
  * *lucasmrtuner* for patches
  * *jakubholy.net* for excellent doc updates
  * *Andre Rigon* for patches on constructor injection
  * *Ulrich Hobelmann* for important doc updates
  * *Peter Knista, Ivan Koblik, Slawek Garwol, Ruediger Herrmann, Robert Thibaut, Clive Evans* for reporting important issues
  * *rdamazio, kenpragma, mszczytowski, albelsky, everflux, twillhorn, nurkiewicz, hanriseldon, exortech, edwinstang, dodozhang21* for some more issue reports :)

### Older versions are documented [here](https://code.google.com/p/mockito/wiki/ReleaseNotes).
