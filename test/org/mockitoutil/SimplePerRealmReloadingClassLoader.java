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
@SuppressWarnings("rawtypes")
public class SimplePerRealmReloadingClassLoader extends URLClassLoader {

    private final Map<String, Class> classHashMap = new HashMap<String, Class>();
    private final ReloadClassPredicate reloadClassPredicate;

    public SimplePerRealmReloadingClassLoader(final ReloadClassPredicate reloadClassPredicate) {
        super(getPossibleClassPathsUrls());
        this.reloadClassPredicate = reloadClassPredicate;
    }

    public SimplePerRealmReloadingClassLoader(final ClassLoader parentClassLoader, final ReloadClassPredicate reloadClassPredicate) {
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
        final String className = SimplePerRealmReloadingClassLoader.class.getName();
        return obtainClassPath(className);
    }

    private static URL obtainClassPath(final String className) {
        final String path = className.replace('.', '/') + ".class";
        final String url = SimplePerRealmReloadingClassLoader.class.getClassLoader().getResource(path).toExternalForm();

        try {
            return new URL(url.substring(0, url.length() - path.length()));
        } catch (final MalformedURLException e) {
            throw new RuntimeException("Classloader couldn't obtain a proper classpath URL", e);
        }
    }



    @Override
    public Class<?> loadClass(final String qualifiedClassName) throws ClassNotFoundException {
        if(reloadClassPredicate.acceptReloadOf(qualifiedClassName)) {
            // return customLoadClass(qualifiedClassName);
//            Class<?> loadedClass = findLoadedClass(qualifiedClassName);
            if(!classHashMap.containsKey(qualifiedClassName)) {
                final Class<?> foundClass = findClass(qualifiedClassName);
                saveFoundClass(qualifiedClassName, foundClass);
                return foundClass;
            }

            return classHashMap.get(qualifiedClassName);
        }
        return useParentClassLoaderFor(qualifiedClassName);
    }

    private void saveFoundClass(final String qualifiedClassName, final Class<?> foundClass) {
        classHashMap.put(qualifiedClassName, foundClass);
    }


    private Class<?> useParentClassLoaderFor(final String qualifiedName) throws ClassNotFoundException {
        return super.loadClass(qualifiedName);
    }


    public Object doInRealm(final String callableCalledInClassLoaderRealm) throws Exception {
        final ClassLoader current = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(this);
            final Object instance = this.loadClass(callableCalledInClassLoaderRealm).getConstructor().newInstance();
            if (instance instanceof Callable) {
                final Callable<?> callableInRealm = (Callable<?>) instance;
                return callableInRealm.call();
            }
        } finally {
            Thread.currentThread().setContextClassLoader(current);
        }
        throw new IllegalArgumentException("qualified name '" + callableCalledInClassLoaderRealm + "' should represent a class implementing Callable");
    }


    public Object doInRealm(final String callableCalledInClassLoaderRealm, final Class[] argTypes, final Object[] args) throws Exception {
        final ClassLoader current = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(this);
            final Object instance = this.loadClass(callableCalledInClassLoaderRealm).getConstructor(argTypes).newInstance(args);
            if (instance instanceof Callable) {
                final Callable<?> callableInRealm = (Callable<?>) instance;
                return callableInRealm.call();
            }
        } finally {
            Thread.currentThread().setContextClassLoader(current);
        }

        throw new IllegalArgumentException("qualified name '" + callableCalledInClassLoaderRealm + "' should represent a class implementing Callable");
    }


    public interface ReloadClassPredicate {
        boolean acceptReloadOf(final String qualifiedName);
    }
}
