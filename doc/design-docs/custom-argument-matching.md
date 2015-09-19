Custom argument matching via user-implemented matcher classes.

# Custom argument matchers in Mockito 2.0+

## ArgumentMatcher API is changed

### Goals

    * minimize compatibility issues by avoiding compile/runtime dependency to hamcrest
        * Hamcrest is very stable and useful library and we want to take advantage of it.
        There have been incompatible changes between hamcrest 1.1 and 1.3 that have impacted our users.
        Compile/runtime dependency on hamcrest might cause issues in future and reduce perceived quality of Mockito.
    * provide interoperability with hamcrest so that users can still use existing hamcrest matchers
    * minimize dependencies on 3rd party libraries
        * Mockito is a library and the less transitive dependencies it brings to client projects the better.
        This lowers the risk of version conflicts that may impact the users.

### Changes
    * ArgumentMatcher no longer inherits from hamcrest Matcher.
        * Necessary to break runtime dependency between mockito core and hamcrest.
    * ArgumentMatcher is an interface and not an abstract class.
        * Interface offers better interoperability than an abstract class.
         Complicated class hierarchies are harder to maintain / comprehend.
    * ArgumentMatcher.toString() method is used to print the matcher in verification errors instead of describeTo(Description)
        * This approach is more consistent with how Mockito naturally uses toString() in verification errors.
        It also simplifies the ArgumentMatcher API.
    * In order to use existing hamcrest matchers, new org.mockito.hamcrest.MockitoHamcrest.argThat() method is introduced

### Migration
    * All existing custom implementations of ArgumentMatcher will no longer compile. Steps to fix:
        * use 'implements ArgumentMatcher' instead of 'extends ArgumentMatcher'
        * refactor describeTo() method into toString() method
    * All locations where hamcrest matchers are passed to argThat() will no longer compile. Steps to fix:
        * use org.mockito.hamcrest.MockitoHamcrest.argThat() instead of Mockito.argThat()

###  Test coverage

    * integration test that uses custom argument matcher but does not have hamcrest on classpath