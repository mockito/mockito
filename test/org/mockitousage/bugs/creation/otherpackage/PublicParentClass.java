package org.mockitousage.bugs.creation.otherpackage;

public class PublicParentClass {
    public void method_with_non_public_argument(PackageLocalArg arg) { }
    static class PackageLocalArg { }
}
