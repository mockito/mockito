/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitousage.bugs.creation.otherpackage;

public class PublicParentClass {
    public void method_with_non_public_argument(PackageLocalArg arg) {}

    static class PackageLocalArg {}
}
