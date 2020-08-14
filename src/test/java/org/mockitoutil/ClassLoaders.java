/*
 * Copyright (c) 2017 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockitoutil;

import static java.lang.String.format;
import static java.util.Arrays.asList;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.plugins.MemberAccessor;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

public abstract class ClassLoaders {
    protected ClassLoader parent = currentClassLoader();

    protected ClassLoaders() {}

    public static IsolatedURLClassLoaderBuilder isolatedClassLoader() {
        return new IsolatedURLClassLoaderBuilder();
    }

    public static ExcludingURLClassLoaderBuilder excludingClassLoader() {
        return new ExcludingURLClassLoaderBuilder();
    }

    public static InMemoryClassLoaderBuilder inMemoryClassLoader() {
        return new InMemoryClassLoaderBuilder();
    }

    public static ReachableClassesFinder in(ClassLoader classLoader) {
        return new ReachableClassesFinder(classLoader);
    }

    public static ClassLoader jdkClassLoader() {
        return String.class.getClassLoader();
    }

    public static ClassLoader systemClassLoader() {
        return ClassLoader.getSystemClassLoader();
    }

    public static ClassLoader currentClassLoader() {
        return ClassLoaders.class.getClassLoader();
    }

    public abstract ClassLoader build();

    public static Class<?>[] coverageTool() {
        HashSet<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(safeGetClass("net.sourceforge.cobertura.coveragedata.TouchCollector"));
        classes.add(safeGetClass("org.slf4j.LoggerFactory"));

        classes.remove(null);
        return classes.toArray(new Class<?>[classes.size()]);
    }

    private static Class<?> safeGetClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static ClassLoaderExecutor using(final ClassLoader classLoader) {
        return new ClassLoaderExecutor(classLoader);
    }

    public static class ClassLoaderExecutor {
        private ClassLoader classLoader;

        public ClassLoaderExecutor(ClassLoader classLoader) {
            this.classLoader = classLoader;
        }

        public void execute(final Runnable task) throws Exception {
            ExecutorService executorService =
                    Executors.newSingleThreadExecutor(
                            new ThreadFactory() {
                                @Override
                                public Thread newThread(Runnable r) {
                                    Thread thread = Executors.defaultThreadFactory().newThread(r);
                                    thread.setContextClassLoader(classLoader);
                                    return thread;
                                }
                            });
            try {
                Future<?> taskFuture =
                        executorService.submit(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            reloadTaskInClassLoader(task).run();
                                        } catch (Throwable throwable) {
                                            throw new IllegalStateException(
                                                    format(
                                                            "Given task could not be loaded properly in the given classloader '%s', error '%s",
                                                            task, throwable.getMessage()),
                                                    throwable);
                                        }
                                    }
                                });
                taskFuture.get();
                executorService.shutdownNow();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                throw this.<Exception>unwrapAndThrows(e);
            }
        }

        @SuppressWarnings("unchecked")
        private <T extends Throwable> T unwrapAndThrows(ExecutionException ex) throws T {
            throw (T) ex.getCause();
        }

        Runnable reloadTaskInClassLoader(Runnable task) {
            try {
                @SuppressWarnings("unchecked")
                Class<Runnable> taskClassReloaded =
                        (Class<Runnable>) classLoader.loadClass(task.getClass().getName());

                Objenesis objenesis = new ObjenesisStd();
                ObjectInstantiator<Runnable> thingyInstantiator =
                        objenesis.getInstantiatorOf(taskClassReloaded);
                Runnable reloaded = thingyInstantiator.newInstance();

                // lenient shallow copy of class compatible fields
                for (Field field : task.getClass().getDeclaredFields()) {
                    Field declaredField = taskClassReloaded.getDeclaredField(field.getName());
                    int modifiers = declaredField.getModifiers();
                    if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)) {
                        // Skip static final fields (e.g. jacoco fields)
                        // otherwise IllegalAccessException (can be bypassed with Unsafe though)
                        // We may also miss coverage data.
                        continue;
                    }
                    if (declaredField.getType() == field.getType()) { // don't copy this
                        MemberAccessor accessor = Plugins.getMemberAccessor();
                        accessor.set(declaredField, reloaded, accessor.get(field, task));
                    }
                }

                return reloaded;
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException(e);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            } catch (NoSuchFieldException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public static class IsolatedURLClassLoaderBuilder extends ClassLoaders {
        private final ArrayList<String> excludedPrefixes = new ArrayList<String>();
        private final ArrayList<String> privateCopyPrefixes = new ArrayList<String>();
        private final ArrayList<URL> codeSourceUrls = new ArrayList<URL>();

        public IsolatedURLClassLoaderBuilder withPrivateCopyOf(String... privatePrefixes) {
            privateCopyPrefixes.addAll(asList(privatePrefixes));
            return this;
        }

        public IsolatedURLClassLoaderBuilder withCodeSourceUrls(String... urls) {
            codeSourceUrls.addAll(pathsToURLs(urls));
            return this;
        }

        public IsolatedURLClassLoaderBuilder withCodeSourceUrlOf(Class<?>... classes) {
            for (Class<?> clazz : classes) {
                codeSourceUrls.add(obtainCurrentClassPathOf(clazz.getName()));
            }
            return this;
        }

        public IsolatedURLClassLoaderBuilder withCurrentCodeSourceUrls() {
            codeSourceUrls.add(obtainCurrentClassPathOf(ClassLoaders.class.getName()));
            return this;
        }

        public IsolatedURLClassLoaderBuilder without(String... privatePrefixes) {
            excludedPrefixes.addAll(asList(privatePrefixes));
            return this;
        }

        public ClassLoader build() {
            return new LocalIsolatedURLClassLoader(
                    jdkClassLoader(),
                    codeSourceUrls.toArray(new URL[codeSourceUrls.size()]),
                    privateCopyPrefixes,
                    excludedPrefixes);
        }
    }

    static class LocalIsolatedURLClassLoader extends URLClassLoader {
        private final ArrayList<String> privateCopyPrefixes;
        private final ArrayList<String> excludedPrefixes;

        LocalIsolatedURLClassLoader(
                ClassLoader classLoader,
                URL[] urls,
                ArrayList<String> privateCopyPrefixes,
                ArrayList<String> excludedPrefixes) {
            super(urls, classLoader);
            this.privateCopyPrefixes = privateCopyPrefixes;
            this.excludedPrefixes = excludedPrefixes;
        }

        @Override
        public Class<?> findClass(String name) throws ClassNotFoundException {
            if (!classShouldBePrivate(name) || classShouldBeExcluded(name)) {
                throw new ClassNotFoundException(
                        format(
                                "Can only load classes with prefixes : %s, but not : %s",
                                privateCopyPrefixes, excludedPrefixes));
            }
            try {
                return super.findClass(name);
            } catch (ClassNotFoundException cnfe) {
                throw new ClassNotFoundException(
                        format(
                                "%s%n%s%n",
                                cnfe.getMessage(),
                                "    Did you forgot to add the code source url 'withCodeSourceUrlOf' / 'withCurrentCodeSourceUrls' ?"),
                        cnfe);
            }
        }

        private boolean classShouldBePrivate(String name) {
            for (String prefix : privateCopyPrefixes) {
                if (name.startsWith(prefix)) return true;
            }
            return false;
        }

        private boolean classShouldBeExcluded(String name) {
            for (String prefix : excludedPrefixes) {
                if (name.startsWith(prefix)) return true;
            }
            return false;
        }
    }

    public static class ExcludingURLClassLoaderBuilder extends ClassLoaders {
        private final ArrayList<String> excludedPrefixes = new ArrayList<String>();
        private final ArrayList<URL> codeSourceUrls = new ArrayList<URL>();

        public ExcludingURLClassLoaderBuilder without(String... privatePrefixes) {
            excludedPrefixes.addAll(asList(privatePrefixes));
            return this;
        }

        public ExcludingURLClassLoaderBuilder withCodeSourceUrls(String... urls) {
            codeSourceUrls.addAll(pathsToURLs(urls));
            return this;
        }

        public ExcludingURLClassLoaderBuilder withCodeSourceUrlOf(Class<?>... classes) {
            for (Class<?> clazz : classes) {
                codeSourceUrls.add(obtainCurrentClassPathOf(clazz.getName()));
            }
            return this;
        }

        public ExcludingURLClassLoaderBuilder withCurrentCodeSourceUrls() {
            codeSourceUrls.add(obtainCurrentClassPathOf(ClassLoaders.class.getName()));
            return this;
        }

        public ClassLoader build() {
            return new LocalExcludingURLClassLoader(
                    jdkClassLoader(),
                    codeSourceUrls.toArray(new URL[codeSourceUrls.size()]),
                    excludedPrefixes);
        }
    }

    static class LocalExcludingURLClassLoader extends URLClassLoader {
        private final ArrayList<String> excludedPrefixes;

        LocalExcludingURLClassLoader(
                ClassLoader classLoader, URL[] urls, ArrayList<String> excludedPrefixes) {
            super(urls, classLoader);
            this.excludedPrefixes = excludedPrefixes;
        }

        @Override
        public Class<?> findClass(String name) throws ClassNotFoundException {
            if (classShouldBeExcluded(name))
                throw new ClassNotFoundException(
                        "classes with prefix : " + excludedPrefixes + " are excluded");
            return super.findClass(name);
        }

        private boolean classShouldBeExcluded(String name) {
            for (String prefix : excludedPrefixes) {
                if (name.startsWith(prefix)) return true;
            }
            return false;
        }
    }

    public static class InMemoryClassLoaderBuilder extends ClassLoaders {
        private Map<String, byte[]> inMemoryClassObjects = new HashMap<String, byte[]>();

        public InMemoryClassLoaderBuilder withParent(ClassLoader parent) {
            this.parent = parent;
            return this;
        }

        public InMemoryClassLoaderBuilder withClassDefinition(String name, byte[] classDefinition) {
            inMemoryClassObjects.put(name, classDefinition);
            return this;
        }

        public ClassLoader build() {
            return new InMemoryClassLoader(parent, inMemoryClassObjects);
        }
    }

    static class InMemoryClassLoader extends ClassLoader {
        public static final String SCHEME = "mem";
        private Map<String, byte[]> inMemoryClassObjects = new HashMap<String, byte[]>();

        public InMemoryClassLoader(ClassLoader parent, Map<String, byte[]> inMemoryClassObjects) {
            super(parent);
            this.inMemoryClassObjects = inMemoryClassObjects;
        }

        protected Class<?> findClass(String name) throws ClassNotFoundException {
            byte[] classDefinition = inMemoryClassObjects.get(name);
            if (classDefinition != null) {
                return defineClass(name, classDefinition, 0, classDefinition.length);
            }
            throw new ClassNotFoundException(name);
        }

        @Override
        public Enumeration<URL> getResources(String ignored) throws IOException {
            return inMemoryOnly();
        }

        private Enumeration<URL> inMemoryOnly() {
            final Set<String> names = inMemoryClassObjects.keySet();
            return new Enumeration<URL>() {
                private final MemHandler memHandler = new MemHandler(InMemoryClassLoader.this);
                private final Iterator<String> it = names.iterator();

                public boolean hasMoreElements() {
                    return it.hasNext();
                }

                public URL nextElement() {
                    try {
                        return new URL(null, SCHEME + ":" + it.next(), memHandler);
                    } catch (MalformedURLException rethrown) {
                        throw new IllegalStateException(rethrown);
                    }
                }
            };
        }
    }

    public static class MemHandler extends URLStreamHandler {
        private InMemoryClassLoader inMemoryClassLoader;

        public MemHandler(InMemoryClassLoader inMemoryClassLoader) {
            this.inMemoryClassLoader = inMemoryClassLoader;
        }

        @Override
        protected URLConnection openConnection(URL url) throws IOException {
            return new MemURLConnection(url, inMemoryClassLoader);
        }

        private static class MemURLConnection extends URLConnection {
            private final InMemoryClassLoader inMemoryClassLoader;
            private String qualifiedName;

            public MemURLConnection(URL url, InMemoryClassLoader inMemoryClassLoader) {
                super(url);
                this.inMemoryClassLoader = inMemoryClassLoader;
                qualifiedName = url.getPath();
            }

            @Override
            public void connect() throws IOException {}

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(
                        inMemoryClassLoader.inMemoryClassObjects.get(qualifiedName));
            }
        }
    }

    URL obtainCurrentClassPathOf(String className) {
        String path = className.replace('.', '/') + ".class";
        String url = ClassLoaders.class.getClassLoader().getResource(path).toExternalForm();

        try {
            return new URL(url.substring(0, url.length() - path.length()));
        } catch (MalformedURLException e) {
            throw new RuntimeException("Classloader couldn't obtain a proper classpath URL", e);
        }
    }

    List<URL> pathsToURLs(String... codeSourceUrls) {
        return pathsToURLs(Arrays.asList(codeSourceUrls));
    }

    private List<URL> pathsToURLs(List<String> codeSourceUrls) {
        ArrayList<URL> urls = new ArrayList<URL>(codeSourceUrls.size());
        for (String codeSourceUrl : codeSourceUrls) {
            URL url = pathToUrl(codeSourceUrl);
            urls.add(url);
        }
        return urls;
    }

    private URL pathToUrl(String path) {
        try {
            return new File(path).getAbsoluteFile().toURI().toURL();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Path is malformed", e);
        }
    }

    public static class ReachableClassesFinder {
        private ClassLoader classLoader;
        private Set<String> qualifiedNameSubstring = new HashSet<String>();

        ReachableClassesFinder(ClassLoader classLoader) {
            this.classLoader = classLoader;
        }

        public ReachableClassesFinder omit(String... qualifiedNameSubstring) {
            this.qualifiedNameSubstring.addAll(Arrays.asList(qualifiedNameSubstring));
            return this;
        }

        public Set<String> listOwnedClasses() throws IOException, URISyntaxException {
            Enumeration<URL> roots = classLoader.getResources("");

            Set<String> classes = new HashSet<String>();
            while (roots.hasMoreElements()) {
                URI uri = roots.nextElement().toURI();

                if (uri.getScheme().equalsIgnoreCase("file")) {
                    addFromFileBasedClassLoader(classes, uri);
                } else if (uri.getScheme().equalsIgnoreCase(InMemoryClassLoader.SCHEME)) {
                    addFromInMemoryBasedClassLoader(classes, uri);
                } else if (uri.getScheme().equalsIgnoreCase("jar")) {
                    // Java 9+ returns "jar:file:" style urls for modules.
                    // It's not a classes owned by mockito itself.
                    // Just ignore it.
                    continue;
                } else {
                    throw new IllegalArgumentException(
                            format(
                                    "Given ClassLoader '%s' don't have reachable by File or vi ClassLoaders.inMemory",
                                    classLoader));
                }
            }
            return classes;
        }

        private void addFromFileBasedClassLoader(Set<String> classes, URI uri) {
            File root = new File(uri);
            classes.addAll(findClassQualifiedNames(root, root, qualifiedNameSubstring));
        }

        private void addFromInMemoryBasedClassLoader(Set<String> classes, URI uri) {
            String qualifiedName = uri.getSchemeSpecificPart();
            if (excludes(qualifiedName, qualifiedNameSubstring)) {
                classes.add(qualifiedName);
            }
        }

        private Set<String> findClassQualifiedNames(
                File root, File file, Set<String> packageFilters) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                Set<String> classes = new HashSet<String>();
                for (File children : files) {
                    classes.addAll(findClassQualifiedNames(root, children, packageFilters));
                }
                return classes;
            } else {
                if (file.getName().endsWith(".class")) {
                    String qualifiedName = classNameFor(root, file);
                    if (excludes(qualifiedName, packageFilters)) {
                        return Collections.singleton(qualifiedName);
                    }
                }
            }
            return Collections.emptySet();
        }

        private boolean excludes(String qualifiedName, Set<String> packageFilters) {
            for (String filter : packageFilters) {
                if (qualifiedName.contains(filter)) return false;
            }
            return true;
        }

        private String classNameFor(File root, File file) {
            String temp =
                    file.getAbsolutePath()
                            .substring(root.getAbsolutePath().length() + 1)
                            .replace(File.separatorChar, '.');
            return temp.subSequence(0, temp.indexOf(".class")).toString();
        }
    }
}
