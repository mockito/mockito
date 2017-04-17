<a href="http://site.mockito.org">
<img src="https://raw.githubusercontent.com/mockito/mockito/master/src/javadoc/org/mockito/logo.png"
     srcset="https://raw.githubusercontent.com/mockito/mockito/master/src/javadoc/org/mockito/logo@2x.png 2x"
     alt="Mockito" />
</a>

Most popular mocking framework for Java

[![Build Status](https://travis-ci.org/mockito/mockito.svg?branch=master)](https://travis-ci.org/mockito/mockito) [![Coverage Status](https://img.shields.io/codecov/c/github/mockito/mockito.svg)](https://codecov.io/github/mockito/mockito) [![MIT License](http://img.shields.io/badge/license-MIT-green.svg) ](https://github.com/mockito/mockito/blob/master/LICENSE)

[![latest release](https://img.shields.io/badge/release%20notes-3.x-yellow.svg)](https://github.com/mockito/mockito/blob/master/doc/release-notes/official.md) [ ![Latest release](https://api.bintray.com/packages/mockito/maven/mockito/images/download.svg)](https://bintray.com/mockito/maven/mockito/_latestVersion) [ ![Maven Central](https://img.shields.io/maven-central/v/org.mockito/mockito-core.svg)](https://maven-badges.herokuapp.com/maven-central/org.mockito/mockito-core) [ ![Javadocs](http://www.javadoc.io/badge/org.mockito/mockito-core.svg?color=red)](http://www.javadoc.io/doc/org.mockito/mockito-core)


## Current version is 2.x
Still on Mockito 1.x? See [what's new](https://github.com/mockito/mockito/wiki/What%27s-new-in-Mockito-2) in Mockito 2!

Mockito [continuously delivers](https://github.com/mockito/mockito/wiki/Continuous-Delivery-Overview) improvements. See the [latest release notes](https://github.com/mockito/mockito/blob/release/2.x/doc/release-notes/official.md) and [latest documentation](http://javadoc.io/page/org.mockito/mockito-core/2/org/mockito/Mockito.html). Docs in javadoc.io are available 24h after release. Read also about [semantic versioning in Mockito](https://github.com/mockito/mockito/wiki/Semantic-Versioning).

Older 1.x releases are available in
[Central Repository](http://search.maven.org/#artifactdetails|org.mockito|mockito-core|1.10.19|jar)
, [Bintray](https://bintray.com/mockito/maven/mockito/1.10.19/view)
and [javadoc.io](http://javadoc.io/page/org.mockito/mockito-core/1.10.19/org/mockito/Mockito.html) (documentation).

## More information

All you want to know about Mockito is hosted at [The Mockito Site](http://site.mockito.org) which is [Open Source](https://github.com/mockito/mockito.github.io) and likes [pull requests](https://github.com/mockito/mockito.github.io/pulls), too.

Want to contribute? Take a look at the [Contributing Guide](https://github.com/mockito/mockito/blob/master/.github/CONTRIBUTING.md).

Enjoy Mockito!

## Need help?

* Search / Ask question on [stackoverflow](http://stackoverflow.com/questions/tagged/mockito)
* Go to the [mockito mailing-list](http://groups.google.com/group/mockito) (moderated)
* Open a ticket in GitHub [issue tracker](https://github.com/mockito/mockito/issues)

## How to develop Mockito?

To build locally:

     ./gradlew build

To develop in IntelliJ IDEA you can use built-in Gradle import wizard in IDEA.
Alternatively generate the importable IDEA metadata files using:

     ./gradlew idea

Then, _open_ the generated *.ipr file in IDEA.

## How to release new version?

Mockito [implements Continuous Delivery model](https://github.com/mockito/mockito/wiki/Continuous-Delivery-Overview).
Every change on main branch (for example merging a pull request) triggers Travis CI release build.
The build publishes new version if criteria are met: all tests green, no 'ci skip release' used in commit message, see the build log for more.
New version is published to ["mockito/maven" Bintray repository](https://bintray.com/mockito/maven).
Notable versions are automatically included in [JCenter](https://bintray.com/bintray/jcenter) and [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.mockito%22).

* Q: What's new in Mockito release model? What are notable versions?
  A: In April 2017 we implemented [Mockito Continuous Delivery Pipeline 2.0](https://github.com/mockito/mockito/issues/911).
* Q: How to publish new notable version?
  A: Update "version.properties" file and set the next minor/major version like: "2.8.0", "2.9.0", "3.0.0".
* Q: How to promote already released version to a notable version?
  A: There is no easy way at the moment. [What's the use case?](https://github.com/mockito/mockito/issues/911)
* Q: How to publish new notable version that is not a new minor/major (e.g. "2.8.34")?
  A: There is no easy way at the moment but we're planning to add this. [Want to contribute?](https://github.com/mockito/mockito-release-tools/issues/67)
