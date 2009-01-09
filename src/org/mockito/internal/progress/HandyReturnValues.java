/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.progress;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class HandyReturnValues {

    public byte returnZero() {
        return 0;
    }

    public char returnChar() {
        return 0;
    }

    public <T> T returnNull() {
        return null;
    }

    public boolean returnFalse() {
        return false;
    }

    public String returnString() {
        return "";
    }

    public Map returnMap() {
        return new HashMap();
    }

    public List returnList() {
        return new LinkedList();
    }
}