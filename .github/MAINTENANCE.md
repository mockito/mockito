# Maintainer procedures

## Renewing the release signing key

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
