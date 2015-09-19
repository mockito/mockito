/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.util;

import static java.lang.Thread.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Inspired on jMock (thanks jMock guys!!!)
 */
public class SearchingClassLoader extends ClassLoader {

    //TODO SF potentially not needed

    private final ClassLoader nextToSearch;
    
    public SearchingClassLoader(ClassLoader parent, ClassLoader nextToSearch) {
        super(parent);
        this.nextToSearch = nextToSearch;
    }
    
    public static ClassLoader combineLoadersOf(Class<?>... classes) {
        return combineLoadersOf(classes[0], classes);
    }
    
    private static ClassLoader combineLoadersOf(Class<?> first, Class<?>... others) {
        List<ClassLoader> loaders = new ArrayList<ClassLoader>();
        
        addIfNewElement(loaders, first.getClassLoader());
        for (Class<?> c : others) {
            addIfNewElement(loaders, c.getClassLoader());
        }
        
        // To support Eclipse Plug-in tests.
        // In an Eclipse plug-in, we will not be on the system class loader
        // but in the class loader of the plug-in.
        //
        // Note: I've been unable to reproduce the error in the test suite.
        addIfNewElement(loaders, SearchingClassLoader.class.getClassLoader());
        
        // To support the Maven Surefire plugin.
        // Note: I've been unable to reproduce the error in the test suite.
        addIfNewElement(loaders, currentThread().getContextClassLoader());

        //Had to comment that out because it didn't work with in-container Spring tests
        //addIfNewElement(loaders, ClassLoader.getSystemClassLoader());
        
        return combine(loaders);
    }
    
    private static ClassLoader combine(List<ClassLoader> parentLoaders) {
        ClassLoader loader = parentLoaders.get(parentLoaders.size()-1);
        
        for (int i = parentLoaders.size()-2; i >= 0; i--) {
            loader = new SearchingClassLoader(parentLoaders.get(i), loader);
        }
        
        return loader;
    }
    
    private static void addIfNewElement(List<ClassLoader> loaders, ClassLoader c) {
        if (c != null && !loaders.contains(c)) {
            loaders.add(c);
        }
    }
    
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (nextToSearch != null) {
            return nextToSearch.loadClass(name);
        } else {
            return super.findClass(name); // will throw ClassNotFoundException
        }
    }
}