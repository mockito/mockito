/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoutil;

import net.bytebuddy.ByteBuddy;

public class SimpleClassGenerator {

    public static byte[] makeMarkerInterface(String qualifiedName) {
        return new ByteBuddy().makeInterface().name(qualifiedName).make().getBytes();
    }
}
