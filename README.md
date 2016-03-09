<a href="http://site.mockito.org">
<img src="https://raw.githubusercontent.com/mockito/mockito/master/src/javadoc/org/mockito/logo.png"
     srcset="https://raw.githubusercontent.com/mockito/mockito/master/src/javadoc/org/mockito/logo@2x.png 2x"
     alt="Mockito" />
</a>

Tasty mocking framework for unit tests in Java

![build status](https://img.shields.io/badge/build-info =>-yellow.svg) [![Build Status](https://travis-ci.org/mockito/mockito.svg?branch=master)](https://travis-ci.org/mockito/mockito) [![Coverage Status](https://img.shields.io/codecov/c/github/mockito/mockito.svg)](https://codecov.io/github/mockito/mockito) [![MIT License](http://img.shields.io/badge/license-MIT-green.svg) ](https://github.com/mockito/mockito/blob/master/LICENSE)

![latest 1.x](https://img.shields.io/badge/latest stable-1.x =>-yellow.svg) [ ![latest stable 1.x on bintray](https://img.shields.io/badge/Download-1.10.19-blue.svg)](https://bintray.com/szczepiq/maven/mockito/1.10.19/view) [ ![latest stable 1.x on maven central](https://img.shields.io/badge/maven%20central-1.10.19-blue.svg)](http://search.maven.org/#artifactdetails|org.mockito|mockito-core|1.10.19|)

![latest 2.x](https://img.shields.io/badge/latest beta-2.x =>-yellow.svg) [ ![Current release](https://api.bintray.com/packages/szczepiq/maven/mockito/images/download.svg) ](https://bintray.com/szczepiq/maven/mockito/_latestVersion) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.mockito/mockito-core/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/org.mockito/mockito-core)

## Current release
See the [release notes page](https://github.com/mockito/mockito/blob/master/doc/release-notes/official.md) and [latest documentation](http://mockito.github.io/mockito/docs/current/org/mockito/Mockito.html).

## Versioning

Mockito has an automated release system, which imposed some change on how the version numbers work. While this is similar to [_semver_](http://semver.org/), there's some differences. Let's look at the following versions `1.10.19` and `2.0.5-beta` and `2.0.0` (not yet released). They follow this scheme:

```
major.minor.build-tag
```

| number | meaning                                                                               |
| ------ | ------------------------------------------------------------------------------------- |
| major  | major version, with most probably incompatible change in API and behavior             |
| minor  | minor version, important enough change to bump this number                            |
| build  | a released build number incremented automatically when a pull request is merged       |
| tag    | will probably be `-beta` or just nothing (during beta, breaking changes are expected) |

That means:

* `2.0.0` and `2.0.5-beta` are binary incompatible with `1.10.19`.
* `2.0.5-beta` is the fifth release beta of version `2.0.0`.
* `2.0.5-beta` is a work in progress, api may change and may not be graduated in version `2.0.0`.

## Looking for support

* Go to the [mockito mailing-list](http://groups.google.com/group/mockito) (moderated)
* Search / Ask question on [stackoverflow](http://stackoverflow.com/questions/tagged/mockito)

## How to build?

To build locally:

     ./gradlew build

To develop in IntelliJ IDEA you can use built-in Gradle import wizard in IDEA.
Alternatively generate the importable IDEA metadata files using:

     ./gradlew idea

Then, _open_ the generated *.ipr file in IDEA.

## More information

All you want to know about Mockito is hosted at [The Mockito Site](http://site.mockito.org) which is [Open Source](https://github.com/mockito/mockito.github.io) and likes [pull requests](https://github.com/mockito/mockito.github.io/pulls), too.

Want to contribute? Take a look at the [Contributing Guide](https://github.com/mockito/mockito/blob/master/.github/CONTRIBUTING.md).

Enjoy Mockito!
