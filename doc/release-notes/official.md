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
