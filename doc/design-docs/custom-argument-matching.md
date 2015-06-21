Custom argument matching via user-implemented matcher classes.

# Custom argument matchers in Mockito 2.0+

## Goals

    * minimize compatibility issues by avoiding compile/runtime dependency to hamcrest
    * provide interoperability with hamcrest so that users can still use existing hamcerst matchers

## Plan

    * breaking changes
        * existing Matchers.argThat takes MockitoMatcher instance as parameter instead of hamcrest Matcher
            * this is required to break runtime dependency between mockito core and hamcrest
        * options
            * (selected option) existing ArgumentMatcher is deprecated,
             without replacement (BaseMatcher or Matcher from vanilla hamcrest should be used)
                * during migration the user is forced review the usages of matcher (argThat method moved)
                * reviewing matcher implementations is optional - there is a deprecation message
            * existing ArgumentMatcher is completely removed
                * during migration to 2.0 the user is forced to review matcher implementations
                * and the usages of the matchers (argThat method for hamcrest has moved)

    * other changes
        * new class: org.mockito.hamcrest.MockitoHamcrest
        * new method: MockitoHamcrest.argThat(Matcher)
        * new class: MockitoMatcher<T>
        * new method: boolean MockitoMatcher.matches(Object)
        * new method: String MockitoMatcher.describe()

## Pros/cons

    + internally we stop using hamcrest and it becomes an optional runtime dependency.
        Interoperability with hamcrest is maintained via MockitoHamcrest
    + some of the previous behavior is deprecated so migration is easier no compile errors for matcher implementations

    - we need to hang onto the deprecated behavior a bit longer (only one type is deprecated)
    - new Mockito matcher interface overlaps with hamcrest which might confusing for users

## Test coverage

    * integration test that uses custom argument matcher but does not have hamcrest on classpath

## Open issues

    * test drive the migration and find out whether it is easy or not