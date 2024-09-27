# Android Testing Project
## Overview
Unlike the other subprojects, this project is designed to run against the Android toolchain rather than standard Kotlin/Java. This means it should be opened in Android Studio, rather than IntelliJ.

This project was introduced to provide a basic level of assurance for Mockito's correct operation on the Android platform.

This project features the following:
* the minimum app project prerequisites - it is not intended to be run as an actual app
* basic JUnit tests
* basic instrumented tests

While JUnit tests run on the local machine, instrumented tests in the Android context run on a device, which can be an emulator. Due to the Android runtime design, the implementation options for instrumented tests are constrained compared to normal JUnit tests; for example, it is not possible to mock final classes.

## Library Version Definitions / ByteBuddy

Libraries and their versions are defined in `ext.library-versions.gradle`. It is here that you can specify which version of Mockito to test.

ByteBuddy is an optional declaration. If you specify '0' for the ByteBuddy version, e.g.

`bytebuddy_version = '0'`

then ByteBuddy will be as declared by Mockito's own dependencies.

However you are able to specify a _newer_ version if you would like to test an update, and you can do this by specifying a real version, e.g.:

`bytebuddy_version = '1.11.7'`

This will cause ByteBuddy to be directly included in this project using the specified version.

This override is subject to Gradle's dependency resolution process where it reconciles different versions - Mockito's library inclusion vs. the project one. This usually means the newest version is accepted; therefore if you specify a version of ByteBuddy that's _older_ than Mockito's one, it's unlikely to apply it. You would need to use Gradle's `resolutionStrategy` definition to force this.
