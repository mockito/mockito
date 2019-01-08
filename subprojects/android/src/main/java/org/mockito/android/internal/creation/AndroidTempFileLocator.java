/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.android.internal.creation;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class AndroidTempFileLocator {

    final static File target;

    static {
        File t = null;
        try {
            String user = System.getProperty("org.mockito.android.target");
            if (user != null) {
                t = new File(user);
            }
        } catch (Throwable ignored) {
        }
        if (t == null) {
            t = getCacheDirFromInstrumentationRegistry("android.support.test.InstrumentationRegistry");
        }
        if (t == null) {
            t = getCacheDirFromInstrumentationRegistry("androidx.test.InstrumentationRegistry");
        }
        if (t == null) {
            try {
                Class<?> clazz = Class.forName("dalvik.system.PathClassLoader");
                Field pathField = clazz.getDeclaredField("path");
                pathField.setAccessible(true);
                String pathFromThisClassLoader = (String) pathField.get(AndroidTempFileLocator.class.getClassLoader());
                File[] results = guessPath(pathFromThisClassLoader);
                if (results.length > 0) {
                    t = results[0];
                }
            } catch (Throwable ignored) {
            }
        }
        target = t;
    }

    private static File getCacheDirFromInstrumentationRegistry(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            Object context = clazz.getDeclaredMethod("getTargetContext").invoke(clazz);
            return (File) context.getClass().getMethod("getCacheDir").invoke(context);
        } catch (Throwable ignored) {
        }
        return null;
    }

    private static File[] guessPath(String input) {
        List<File> results = new ArrayList<File>();
        for (String potential : splitPathList(input)) {
            if (!potential.startsWith("/data/app/")) {
                continue;
            }
            int start = "/data/app/".length();
            int end = potential.lastIndexOf(".apk");
            if (end != potential.length() - 4) {
                continue;
            }
            int dash = potential.indexOf("-");
            if (dash != -1) {
                end = dash;
            }
            String packageName = potential.substring(start, end);
            File dataDir = new File("/data/data/" + packageName);
            if (isWriteableDirectory(dataDir)) {
                File cacheDir = new File(dataDir, "cache");
                if (fileOrDirExists(cacheDir) || cacheDir.mkdir()) {
                    if (isWriteableDirectory(cacheDir)) {
                        results.add(cacheDir);
                    }
                }
            }
        }
        return results.toArray(new File[results.size()]);
    }

    private static String[] splitPathList(String input) {
        String trimmed = input;
        if (input.startsWith("dexPath=")) {
            int start = "dexPath=".length();
            int end = input.indexOf(',');

            trimmed = (end == -1) ? input.substring(start) : input.substring(start, end);
        }

        return trimmed.split(":");
    }

    private static boolean fileOrDirExists(File file) {
        return file.exists();
    }

    private static boolean isWriteableDirectory(File file) {
        return file.isDirectory() && file.canWrite();
    }
}
