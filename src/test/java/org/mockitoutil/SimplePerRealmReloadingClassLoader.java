package org.mockitoutil;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Custom classloader to load classes in hierarchic realm.
 *
 * Each class can be reloaded in the realm if the LoadClassPredicate says so.
 */
public class SimplePerRealmReloadingClassLoader extends URLClassLoader {

    private final Map<String,Class> classHashMap = new HashMap<String, Class>();
    private ReloadClassPredicate reloadClassPredicate;

    public SimplePerRealmReloadingClassLoader(ReloadClassPredicate reloadClassPredicate) {
        super(getPossibleClassPathsUrls());
        this.reloadClassPredicate = reloadClassPredicate;
    }

    public SimplePerRealmReloadingClassLoader(ClassLoader parentClassLoader, ReloadClassPredicate reloadClassPredicate) {
        super(getPossibleClassPathsUrls(), parentClassLoader);
        this.reloadClassPredicate = reloadClassPredicate;
    }

    private static URL[] getPossibleClassPathsUrls() {
        return new URL[]{
                obtainClassPath(),
                obtainClassPath("org.mockito.Mockito"),
                obtainClassPath("net.bytebuddy.ByteBuddy")
        };
    }

    private static URL obtainClassPath() {
        String className = SimplePerRealmReloadingClassLoader.class.getName();
        return obtainClassPath(className);
    }

    private static URL obtainClassPath(String className) {
        String path = className.replace('.', '/') + ".class";
        String url = SimplePerRealmReloadingClassLoader.class.getClassLoader().getResource(path).toExternalForm();

        try {
            return new URL(url.substring(0, url.length() - path.length()));
        } catch (MalformedURLException e) {
            throw new RuntimeException("Classloader couldn't obtain a proper classpath URL", e);
        }
    }



    @Override
    public Class<?> loadClass(String qualifiedClassName) throws ClassNotFoundException {
        if(reloadClassPredicate.acceptReloadOf(qualifiedClassName)) {
            // return customLoadClass(qualifiedClassName);
//            Class<?> loadedClass = findLoadedClass(qualifiedClassName);
            if(!classHashMap.containsKey(qualifiedClassName)) {
                Class<?> foundClass = findClass(qualifiedClassName);
                saveFoundClass(qualifiedClassName, foundClass);
                return foundClass;
            }

            return classHashMap.get(qualifiedClassName);
        }
        return useParentClassLoaderFor(qualifiedClassName);
    }

    private void saveFoundClass(String qualifiedClassName, Class<?> foundClass) {
        classHashMap.put(qualifiedClassName, foundClass);
    }


    private Class<?> useParentClassLoaderFor(String qualifiedName) throws ClassNotFoundException {
        return super.loadClass(qualifiedName);
    }


    public Object doInRealm(String callableCalledInClassLoaderRealm) throws Exception {
        ClassLoader current = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(this);
            Object instance = this.loadClass(callableCalledInClassLoaderRealm).getConstructor().newInstance();
            if (instance instanceof Callable) {
                Callable<?> callableInRealm = (Callable<?>) instance;
                return callableInRealm.call();
            }
        } finally {
            Thread.currentThread().setContextClassLoader(current);
        }
        throw new IllegalArgumentException("qualified name '" + callableCalledInClassLoaderRealm + "' should represent a class implementing Callable");
    }


    public Object doInRealm(String callableCalledInClassLoaderRealm, Class<?>[] argTypes, Object[] args) throws Exception {
        ClassLoader current = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(this);
            Object instance = this.loadClass(callableCalledInClassLoaderRealm).getConstructor(argTypes).newInstance(args);
            if (instance instanceof Callable) {
                Callable<?> callableInRealm = (Callable<?>) instance;
                return callableInRealm.call();
            }
        } finally {
            Thread.currentThread().setContextClassLoader(current);
        }

        throw new IllegalArgumentException("qualified name '" + callableCalledInClassLoaderRealm + "' should represent a class implementing Callable");
    }


    public interface ReloadClassPredicate {
        boolean acceptReloadOf(String qualifiedName);
    }
}
