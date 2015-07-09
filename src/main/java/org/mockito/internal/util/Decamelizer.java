/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */

package org.mockito.internal.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO SF move to matchers.text
public class Decamelizer {

private static final Pattern CAPS = Pattern.compile("([A-Z\\d][^A-Z\\d]*)");
    
    public static String decamelizeMatcher(String className) {
        if (className.length() == 0) {
            return "<custom argument matcher>";
        }
        
        String decamelized = decamelizeClassName(className);
        
        if (decamelized.length() == 0) {
            return "<" + className + ">";
        }
        
        return "<" + decamelized + ">";
    }

    private static String decamelizeClassName(String className) {
        Matcher match = CAPS.matcher(className);
        StringBuilder deCameled = new StringBuilder();
        while(match.find()) {
            if (deCameled.length() == 0) {
                deCameled.append(match.group());
            } else {
                deCameled.append(" ");
                deCameled.append(match.group().toLowerCase());
            }
        }
        return deCameled.toString();
    }
}
