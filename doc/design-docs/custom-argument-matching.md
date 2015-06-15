Custom argument matching via user-implemented matcher classes.

# Custom argument matchers in Mockito 2.0+

## Goals

    * minimize compatibility issues by avoiding compile/runtime dependency to hamcrest
    * provide interoperability with hamcrest so that users can still use existing hamcerst matchers

## Plan

    * existing Matchers.argThat deprecated, existing ArgumentMatcher deprecated
    * new API method: org.mockito.hamcrest.MockitoHamcrest.argThat(Matcher target)
    * new type: MockitoMatcher
    * new method: MatchResult MockitoMatcher.matcher(Object target)

# Pros/cons

    + internally we stop using hamcrest and it becomes an optional runtime dependency. Interoperability with hamcrest is maintained via MockitoHamcrest interface
    + the previous behavior is deprecated so migration is relatively easy (no compile errors at migration, migration can be incremental)
    + new MockitoMatcher interface makes it easy to provide meaningful context to the failure description: it is easy to include target object information (e.g. mismatch description)
    + new MockitoMatcher interface requires matcher description via return type MatchResult. Hopefully, this makes debugging easier.

    - we need to hang onto the deprecated behavior a bit longer
    - Mockito matcher interface is different than well known hamcrest interface and requires providing the description of the mismatch via new MatchResult return type. Subjectively, it is a downside because it requires learning a new type, more documentation describing when to use hamcrest and when to use internal type
