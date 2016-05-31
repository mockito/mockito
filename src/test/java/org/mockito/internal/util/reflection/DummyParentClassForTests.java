/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import java.util.logging.Logger;

public class DummyParentClassForTests {

    @SuppressWarnings("unused")//I know, I know. We're doing nasty reflection hacks here...
    private static final Logger LOG = Logger.getLogger(DummyParentClassForTests.class.toString());

    @SuppressWarnings("unused")//I know, I know. We're doing nasty reflection hacks here...
    private String somePrivateField;
}
