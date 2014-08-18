### 1.10.40-dev (2014-08-18 07:26 UTC)

* Authors: 1
* Commits: 7
  * 7: Szczepan Faber
* Improvements: 0

### 1.10.38-dev (2014-08-17 21:54 UTC)

* Authors: 25
  * Commits: 310
  * Brice Dutheil: 134
  * Szczepan Faber: 120
  * marcingrzejszczak: 10
  * Tim Perry: 6
  * Lovro Pandzic: 5
  * alberski: 4
  * Dmytro Chyzhykov: 3
  * Gunnar Wagenknecht: 3
  * Kamil Szymanski: 3
  * pimterry: 3
  * Ian Parkinson: 3
  * Emory Merryman: 2
  * Michal Borek: 2
  * Vivian Pennel: 1
  * Kuangshi Yan: 1
  * Marius Volkhart: 1
  * Marcin Zajaczkowski: 1
  * Andrei Solntsev: 1
  * Markus Wüstenberg: 1
  * Marius Volkhart: 1
  * Ken Dombeck: 1
  * jrrickard: 1
  * Philip Aston: 1
  * Ivan Vershinin: 1
  * ludovic.meurillon@gmail.com: 1
* Improvements: 33
  * Added casts to supress varargs warnings [(#59)](https://github.com/mockito/mockito/pull/59)
  * Update README.md [(#58)](https://github.com/mockito/mockito/pull/58)
  * Fixes env var to TERM for readability purpose on travis log when building with gradle [(#19)](https://github.com/mockito/mockito/pull/19)
  * Fixed wrong javadoc for AdditionalAnswers [(#56)](https://github.com/mockito/mockito/pull/56)
  * Apply travis and coveralls [(#18)](https://github.com/mockito/mockito/pull/18)
  * inner implementations of Filter and Comparator should be static [(#36)](https://github.com/mockito/mockito/pull/36)
  * Fix timeout sleep handling to track time more accurately, and tidy timeout impl variable names [(#15)](https://github.com/mockito/mockito/pull/15)
  * Remove old Hg artifacts [(#13)](https://github.com/mockito/mockito/pull/13)
  * [#456] Enable calling real implementation on extensions method from jdk8 [(#39)](https://github.com/mockito/mockito/pull/39)
  * Deprecated timeout().never(), in line with timeout().atMost() [(#14)](https://github.com/mockito/mockito/pull/14)
  * should avoid static keyword in inner interface definition [(#37)](https://github.com/mockito/mockito/pull/37)
  * - then feature for BddMockito [(#38)](https://github.com/mockito/mockito/pull/38)
  * Added check to the EqualsWithDelta matcher [(#21)](https://github.com/mockito/mockito/pull/21)
  * Upgrade to Gradle 1.11 [(#42)](https://github.com/mockito/mockito/pull/42)
  * Add casted generic methods for getting arguments from InvocationOnMock [(#41)](https://github.com/mockito/mockito/pull/41)
  * Cleanup ignores and fix 1.5 compatibility [(#65)](https://github.com/mockito/mockito/pull/65)
  * Make source code preview available in Coveralls 2 [(#62)](https://github.com/mockito/mockito/pull/62)
  * Improve NoInteractionsWanted report to include the name of the mock. [(#63)](https://github.com/mockito/mockito/pull/63)
  * removed then-verify [(#44)](https://github.com/mockito/mockito/pull/44)
  * Answer javadocs correction [(#22)](https://github.com/mockito/mockito/pull/22)
  * Fixed some typos in README.md [(#23)](https://github.com/mockito/mockito/pull/23)
  * Eclipse plugins classloader issue  [(#24)](https://github.com/mockito/mockito/pull/24)
  * Gradle 1.9 [(#25)](https://github.com/mockito/mockito/pull/25)
  * Issue 418: Removed .java files from installed mockito artifacts [(#28)](https://github.com/mockito/mockito/pull/28)
  * Issue 421 [(#29)](https://github.com/mockito/mockito/pull/29)
  * changes for issue 178, change to DefaultMockingDetail and a simple test [(#10)](https://github.com/mockito/mockito/pull/10)
  * Remove unused import [(#7)](https://github.com/mockito/mockito/pull/7)
  * Issue 399: Added serialization for mocks returned by deep stubbing [(#30)](https://github.com/mockito/mockito/pull/30)
  * MockitoTestNGListener should reset Captors before each test [(#6)](https://github.com/mockito/mockito/pull/6)
  * Enable Mockito mock serialization/deserialization across classloader/JVM [(#5)](https://github.com/mockito/mockito/pull/5)
  * [#467] Fix return value of compareTo for the same objects [(#32)](https://github.com/mockito/mockito/pull/32)
  * Updated Travis CI configuration file to use the Gradle wrapper. [(#53)](https://github.com/mockito/mockito/pull/53)
  * Updates .travis fil to gradle 1.11 [(#50)](https://github.com/mockito/mockito/pull/50)

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

 * Annotations are smarter. They can use constructor injection and instantiate objects. More information [http://docs.mockito.googlecode.com/hg/1.9.0-rc1/org/mockito/Mockito.html#23 here].
 * To keep the test code slim you can now create a stub [http://docs.mockito.googlecode.com/hg/1.9.0-rc1/org/mockito/Mockito.html#24 in one line].
 * Made it possible to verify interactions [http://docs.mockito.googlecode.com/hg/1.9.0-rc1/org/mockito/Mockito.html#25 ignoring stubs].
 * Fixed various bugs & enhancements. Full list is [http://code.google.com/p/mockito/issues/list?can=1&q=label%3AMilestone-Release1.9&colspec=ID+Type+Status+Priority+Milestone+Owner+Summary&cells=tiles here].
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
