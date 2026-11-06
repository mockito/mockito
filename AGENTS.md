# Working on Mockito

Follow the [contribution guide](.github/CONTRIBUTING.md) and the
[pull request template](.github/PULL_REQUEST_TEMPLATE.md). Keep this file aligned
with those documents and the build configuration.

## Project layout

| Path                                        | Purpose                                                                   |
| ------------------------------------------- | ------------------------------------------------------------------------- |
| `mockito-core/src/main/java/org/mockito/`   | Public API and implementation under `internal/`.                          |
| `mockito-core/src/test/java/`               | Unit tests, usage tests, integration tests and concurrency tests.         |
| `mockito-core/src/testFixtures/java/`       | Shared test helpers and fixtures.                                         |
| `mockito-extensions/`                       | JUnit Jupiter, Android, Error Prone and mock maker selection artefacts.   |
| `mockito-integration-tests/`                | Dedicated tests for languages, modules, runtimes and framework support.   |
| `mockito-bom/`                              | Published dependency version alignment.                                   |
| `buildSrc/src/main/kotlin/`                 | Gradle convention plugins and shared build helpers.                       |
| `gradle/libs.versions.toml`                 | Dependency and plugin versions.                                           |
| `config/`                                   | Checkstyle, Spotless, Javadoc and profiling configuration.                |
| `.github/` and `doc/`                       | Contribution guidance, CI, maintainer procedures and design documents.    |

Core unit tests live under `org.mockito`.
Acceptance tests live under `org.mockitousage`; aside from their intent keep them outside `org.mockito`
because stack trace filtering depends on the package.
Reuse existing fixtures and nearby test patterns.

## Changes and API boundaries

- Keep changes focused on the requested behavior. Avoid unrelated refactoring
  and additional runtime dependencies.
- Public API includes types outside `org.mockito.internal`, including lower-level
  contracts in packages such as `org.mockito.invocation`, `org.mockito.stubbing`
  and `org.mockito.plugins`. Review changes from both callers' and documented
  implementors' perspectives.
- Document new public APIs with examples and an `@since` tag. For new features,
  consider adding an entry to the main `Mockito` class documentation.
- Follow each type's documented extension contract and [`@NotExtensible`](mockito-core/src/main/java/org/mockito/NotExtensible.java).
  The absence of that annotation does not promise that a type supports custom
  implementations. Preserve documented plugin discovery and fallback behaviour.
- `org.mockito.internal` is implementation code without a client compatibility
  guarantee. Check its consumers across core, extensions and integration tests
  before changing it. Keep implementation details out of public signatures.
- Preserve the source and bytecode baseline used in the build and the existing JDK/Android
  API signature checks. A newer build JDK should not raise the supported baseline.

## Tests and verification

Include tests for behavior changes: focused unit tests and acceptance coverage
in `org.mockitousage`. Use `snake_case` for new test methods and names ending in
`Test` for test classes so the build discovers them. Add coverage in the relevant
extension or integration project when the change affects it.
Prefer AssertJ assertions over JUnit assertions.

Use the Gradle wrapper. Start with the relevant test; for example:

```shell
./gradlew :mockito-core:test --tests org.mockitousage.stubbing.BasicStubbingTest
```

For a core change, run the module checks and the separate formatting check:

```shell
./gradlew :mockito-core:check :mockito-core:spotlessCheck
```

For public API or Javadoc changes, also run `./gradlew :mockito-core:javadoc`.
Use the corresponding tasks for other affected modules. The full local build is:

```shell
./gradlew build spotlessCheck
```

Consult [.github/workflows/ci.yml](.github/workflows/ci.yml) for the current JDK,
mock maker, member accessor and platform combinations. Tests can select a JDK
with `-Pmockito.test.java=11`; core tests use `MOCK_MAKER` and `MEMBER_ACCESSOR`
for implementation selection. Exercise the relevant combinations when changing
mock creation, reflection or instrumentation.

Android projects are included only when `ANDROID_HOME` is set or root
`local.properties` exists. A build without that configuration does not verify
Android. Report what ran, what failed and what could not be checked.

## Style and pull requests

- Follow [.editorconfig](.editorconfig): UTF-8, LF, a final newline and spaces.
  Java uses four spaces and no wildcard imports. Preserve source licence headers.
- Use `./gradlew spotlessApply` for formatting and inspect the resulting diff.
  Keep formatting changes within the scope of the contribution.
- Write helpful user-facing errors that explain how to correct the problem.
  Follow `Reporter` for usage errors; technical failures need not all go there.
- Follow the PR template. Explain the problem, benefit and an example when useful.
  Keep commits meaningful and focused. When resolving an issue, include
  `Fixes #<issue number>` in the PR description and at the end of at least one
  commit message, as required by the contribution guidance.
