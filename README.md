<a href="http://site.mockito.org">
<img src="https://raw.githubusercontent.com/mockito/mockito/master/src/javadoc/org/mockito/logo.png"
     srcset="https://raw.githubusercontent.com/mockito/mockito/master/src/javadoc/org/mockito/logo@2x.png 2x"
     alt="Mockito" />
</a>

Tasty mocking framework for unit tests in Java

![build status](https://img.shields.io/badge/build-info =>-yellow.svg) [![Build Status](https://travis-ci.org/mockito/mockito.svg?branch=master)](https://travis-ci.org/mockito/mockito) [![Coverage Status](https://img.shields.io/codecov/c/github/mockito/mockito.svg)](https://codecov.io/github/mockito/mockito) [![MIT License](http://img.shields.io/badge/license-MIT-green.svg) ](https://github.com/mockito/mockito/blob/master/LICENSE)

![latest 2.x](https://img.shields.io/badge/latest-2.x =>-yellow.svg) [ ![Current release](https://api.bintray.com/packages/mockito/maven/mockito/images/download.svg) ](https://bintray.com/mockito/maven/mockito/_latestVersion) [![Maven Central](https://img.shields.io/badge/maven central-2.1.0-green.svg)](http://search.maven.org/#artifactdetails%7Corg.mockito%7Cmockito-core%7C2.1.0%7Cjar)

## Current release
See the [release notes page](https://github.com/mockito/mockito/blob/master/doc/release-notes/official.md) and [latest documentation](http://mockito.github.io/mockito/docs/current/org/mockito/Mockito.html).

## Older releases
![latest 1.x](https://img.shields.io/badge/latest stable-1.x =>-yellow.svg) [ ![latest stable 1.x on bintray](https://img.shields.io/badge/Download-1.10.19-blue.svg)](https://bintray.com/mockito/maven/mockito/1.10.19/view) [ ![latest stable 1.x on maven central](https://img.shields.io/badge/maven%20central-1.10.19-blue.svg)](http://search.maven.org/#artifactdetails|org.mockito|mockito-core|1.10.19|)

## Versioning

Mockito has an automated release system, which imposed some change on how the version numbers work.
The versions follow this scheme:

```
major.minor.patch-tag.tagVersion
```

| number | meaning                                                                               |
| ------ | ------------------------------------------------------------------------------------- |
| major  | major version, backwards incompatible with the previous major version                 |
| minor  | minor version, backwards compatible with added features                               |
| patch  | patch version, small bug fixes or stylistic improvements                              |
| tag    | *optional* beta or RC (release candidate). See below.                                 |

That means:

* `2.0.0` and `2.0.0-beta.5` are binary incompatible with `1.10.19`.
* `2.0.0-beta.5` is the fifth release beta of version `2.0.0`.
* `2.0.0-beta.5` could be (but is not necessarily) binary incompatible with version `2.0.0`.
* `2.0.0-RC.1` is binary compatible with release `2.0.0`.

> **Note :** During the 2.0 beta phase we unleashed beta builds with the following version schemes, like : `2.0.111-beta`, where the _build number_ is placed before the _tag_. The current scheme reuses the _build number_ and places it behind the _tag_. Hence those versions are to be considered as beta builds that happen either before or after a release candidate, but always before the final release.
> 
> ```
> 2.0.111-beta < 2.0.0-beta.112 < 2.0.0-RC.1 < 2.0.0-beta.200 < 2.0.0
> ```


### Tags
There are two different tags: beta or RC. Beta indicates that the version is directly generated from the master branch of the git repository.
Beta releases are automatically published whenever we merge a pull request or push a change to the master branch.

When we deem our master status worthy of a release, we publish a release candidate. The release candidate is scheduled to be officially published
in the official release a while later. There will be no breaking changes between a release candidate and its equivalent official release.
The only changes will include bug fixes or small updates. No additional features will be included.

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
