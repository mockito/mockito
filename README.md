<a href="https://site.mockito.org">
<img src="https://raw.githubusercontent.com/mockito/mockito/main/config/javadoc/resources/org/mockito/logo.png"
     srcset="https://raw.githubusercontent.com/mockito/mockito/main/config/javadoc/resources/org/mockito/logo@2x.png 2x"
     alt="Mockito" />
</a>

Most popular mocking framework for Java

[![CI](https://github.com/mockito/mockito/workflows/CI/badge.svg)](https://github.com/mockito/mockito/actions?query=workflow%3ACI)
[![Coverage Status](https://img.shields.io/codecov/c/github/mockito/mockito.svg)](https://codecov.io/github/mockito/mockito)
[![MIT License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/mockito/mockito/blob/main/LICENSE)

[![Release Notes](https://img.shields.io/badge/release%20notes-5.x-yellow.svg)](https://github.com/mockito/mockito/releases/)
[![Maven Central](https://img.shields.io/maven-central/v/org.mockito/mockito-core.svg)](https://search.maven.org/artifact/org.mockito/mockito-core/)
[![Javadoc](https://www.javadoc.io/badge/org.mockito/mockito-core.svg)](https://www.javadoc.io/doc/org.mockito/mockito-core)


## Current version is 5.x
Still on Mockito 1.x? See [what's new](https://github.com/mockito/mockito/wiki/What%27s-new-in-Mockito-2) in Mockito 2!
[Mockito 3](https://github.com/mockito/mockito/releases/tag/v3.0.0) does not introduce any breaking API changes, but now requires Java 8 over Java 6 for Mockito 2.
[Mockito 4](https://github.com/mockito/mockito/releases/tag/v4.0.0) removes deprecated API.
[Mockito 5](https://github.com/mockito/mockito/releases/tag/v5.0.0) switches the default mockmaker to mockito-inline, and now requires Java 11.
Only one major version is supported at a time, and changes are not backported to older versions.

## Mockito for enterprise

Available as part of the [Tidelift](https://tidelift.com/subscription/pkg/maven-org-mockito-mockito-core) Subscription.

The maintainers of org.mockito:mockito-core and thousands of other packages are working with Tidelift to deliver
commercial support and maintenance for the open source dependencies you use to build your applications. Save time,
reduce risk, and improve code health, while paying the maintainers of the exact dependencies you use.
[Learn more.](https://tidelift.com/subscription/pkg/maven-org-mockito-mockito-core?utm_source=maven-org-mockito-mockito-core&utm_medium=referral&utm_campaign=enterprise&utm_term=repo)

## Development

Mockito publishes every change as a `-SNAPSHOT` version to a public Sonatype repository. Roughly once a month, we
publish a new minor or patch version to Maven Central. For release automation we use
[Shipkit library](http://shipkit.org), [Gradle Nexus Publish Plugin](https://github.com/gradle-nexus/publish-plugin).
Fully automated releases are awesome, and you should do that for your libraries, too!
See the [latest release notes](https://github.com/mockito/mockito/releases/)
and [latest documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org.mockito/org/mockito/Mockito.html). Docs in
javadoc.io are available 24h after release. Read also
about [semantic versioning in Mockito](https://github.com/mockito/mockito/wiki/Semantic-Versioning).

Older 1.x and 2.x releases are available in
[Central Repository](https://search.maven.org/artifact/org.mockito/mockito-core/1.10.19/jar)
and [javadoc.io](https://javadoc.io/doc/org.mockito/mockito-core/1.10.19/org/mockito/Mockito.html) (documentation).

## More information

All you want to know about Mockito is hosted at [The Mockito Site](https://site.mockito.org) which is [Open Source](https://github.com/mockito/mockito.github.io) and likes [pull requests](https://github.com/mockito/mockito.github.io/pulls), too.

Want to contribute? Take a look at the [Contributing Guide](https://github.com/mockito/mockito/blob/main/.github/CONTRIBUTING.md).

Enjoy Mockito!

## Need help?

* Search / Ask question on [stackoverflow](https://stackoverflow.com/questions/tagged/mockito)
* Go to the [mockito mailing-list](https://groups.google.com/group/mockito) (moderated)
* Open a ticket in GitHub [issue tracker](https://github.com/mockito/mockito/issues)

## How to develop Mockito?

To build locally:

```shell
./gradlew build
```
You can open in any IDE that support Gradle, e.g. IntelliJ IDEA, or Fleet.
For Eclipse, one may need to run `./gradlew eclipse` before importing the project.

## How to release new version?

1. Every change on the main development branch is released as `-SNAPSHOT` version to Sonatype snapshot repo
   at https://s01.oss.sonatype.org/content/repositories/snapshots/org/mockito/mockito-core.
2. To release a non-snapshot version to Maven Central push an annotated tag, for example:

    ```shell
    git tag -a -m "Release 3.4.5" v3.4.5
    git push origin v3.4.5
    ```

3. At the moment, you **may not create releases from GitHub Web UI**. Doing so will make the CI build fail because the
   CI creates the changelog and posts to GitHub releases. We'll support this in the future.

### Renewing the release signing key (maintainers)

The signing key expires on **2030-10-11**. Renew the existing key to preserve verification of old releases.
CI uses the organization secrets `PGP_KEY_RENEWED` and `PGP_PWD_RENEWED`.

1. Obtain the private key and passphrase from a secure backup. If only Actions has them, use a temporary
   recovery workflow ([example](https://github.com/mockito/mockito/pull/3883)) with a **fresh local recovery key**;
   upload only encrypted artifacts. Replace the example's recipient and use the current secret names. GitHub cannot display secret values;
   a workflow with access to both secrets can recover them securely for an authorized maintainer.
2. Import locally, then extend the primary key and encryption subkey (enter the signing-key passphrase when prompted):

   ```sh
   export GPG_TTY="$(tty)"
   gpg --quick-set-expire 147B691A19097624902F4EA9689CBE64F4BC997F 4y
   gpg --quick-set-expire 147B691A19097624902F4EA9689CBE64F4BC997F 4y 251AE196A5B326556BA3B943C0058C509A81C102
   gpg --list-options show-unusable-subkeys --list-secret-keys --with-subkey-fingerprint 147B691A19097624902F4EA9689CBE64F4BC997F
   ```

3. To change the passphrase, run `gpg --passwd 147B691A19097624902F4EA9689CBE64F4BC997F` before exporting.
   Export with `gpg --armor --export-secret-keys 147B691A19097624902F4EA9689CBE64F4BC997F` directly into
   `gh secret set <NEW_KEY_SECRET> --org mockito --visibility selected --repos mockito,mockito-kotlin,mockito-scala,mockito-testng`.
   Create the matching passphrase secret with the same access using `gh secret set`'s hidden prompt.
   Use new secret names for each renewal; retain the previous pair for rollback. Organization secret management
   requires suitable permissions (`gh auth refresh --hostname github.com --scopes admin:org` for classic OAuth).
4. Test signing and verification with the new secret pair before changing CI references in each consuming repository
   ([validation example](https://github.com/mockito/mockito/commit/29094dbee)). Keep `[skip release]` in Mockito's
   maintenance merge message. Roll back by restoring both previous secret references.
5. Publish the renewed **public** key:

   ```sh
   gpg --keyserver hkps://keyserver.ubuntu.com --send-keys 147B691A19097624902F4EA9689CBE64F4BC997F
   ```

   Consumers must refresh cached public keys. Keep an encrypted backup of the renewed private key and its passphrase,
   update the expiration date above, and remove temporary recovery/validation workflows and artifacts.
