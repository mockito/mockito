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
  * bdd mockito cleanup [(#100)](https://github.com/mockito/mockito/pull/100)

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
  * Verification with timout measures time more more accurately [(#15)](https://github.com/mockito/mockito/pull/15)
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

Few minor bugfixes and a relatively small extension point added to improve the android experience.

* **StackTraceCleaner API** - to improve the experience of mocking on android platform we've added an extension point for cleaning the stack traces. This allows the friends behind the [dexmaker](http://code.google.com/p/dexmaker) to implement custom stack trace filter and hence make the Mockito verification errors contain clean and tidy stack traces. Clean stack trace is something Mockito always cares about! Thanks a lot **Jesse Wilson** for reporting, submitting the initial patch and validating the final solution.
* javadoc fix (issue 356) - thanks **konigsberg** for reporting and patching and **Brice** for merging.
* @InjectMocks inconsistency between java 6 and 7 (issue 353) - neat **Brice's** work.
* fixed a problem with autoboxed default return values, needed for the MockMaker extension point (issue 352). Thanks so much **Jesse Wilson** for reporting and the patch, and **Brice** for merging!

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
 * **THANKS** to all the community members who helped improving Mockito! **Brice Dutheil** is a new Mockito champion, having contributed a lot of excellent code in the recent months! Without him, there wouldn't be any release and your mailing list queries wouldn't be answered so promptly! Brice - thank you and welcome to the team! Some fresh mojitoes ought to be served to:
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
