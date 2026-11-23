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
import java.io.Serializable;
import java.lang.StackWalker.Option;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    @FunctionalInterface
    public interface SerializableCallable<T> extends Callable<T>, Serializable {}

    public static class ClassLoaderExecutor {
        private final ClassLoader classLoader;

        public ClassLoaderExecutor(ClassLoader classLoader) {
            this.classLoader = classLoader;
        }

        /** Runs a lambda defined by the given loader, passing its captured values unchanged. */
        public <T> T execute(SerializableCallable<T> task) throws Exception {
            ExecutorService executorService =
                    Executors.newSingleThreadExecutor(
                            r -> {
                                Thread thread = Executors.defaultThreadFactory().newThread(r);
                                thread.setContextClassLoader(classLoader);
                                return thread;
                            });
            try {
                return executorService.submit(() -> reloadTaskInClassLoader(task).call()).get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw e;
            } catch (ExecutionException e) {
                Throwable cause = e.getCause();
                throw new IllegalStateException(
                        format(
                                "Could not execute task in class loader '%s': %s",
                                classLoader, cause.getMessage()),
                        cause);
            } finally {
                executorService.shutdownNow();
            }
        }

        @SuppressWarnings("unchecked")
        private <T> Callable<T> reloadTaskInClassLoader(SerializableCallable<T> task)
                throws ReflectiveOperationException {
            Method writeReplace = task.getClass().getDeclaredMethod("writeReplace");
            writeReplace.setAccessible(true);
            SerializedLambda lambda = (SerializedLambda) writeReplace.invoke(task);

            // Recreate the lambda with the isolated class's factory. JaCoCo owns its probe state.
            Class<?> capturingClass =
                    classLoader.loadClass(lambda.getCapturingClass().replace('/', '.'));
            Method deserialize =
                    capturingClass.getDeclaredMethod("$deserializeLambda$", SerializedLambda.class);
            deserialize.setAccessible(true);
            return (Callable<T>) deserialize.invoke(null, lambda);
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
            Class<?> callerClass =
                    StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE).getCallerClass();

            codeSourceUrls.add(obtainCurrentClassPathOf(callerClass.getName()));
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
        protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
            if (classShouldBeExcluded(name))
                throw new ClassNotFoundException(
                        "classes with prefix : " + excludedPrefixes + " are excluded");
            return super.loadClass(name, resolve);
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
        String location;
        if (url.startsWith("jar:")) { // multi-release jar file location
            location = url.substring(4, url.lastIndexOf(".jar!") + 4);
        } else if (url.endsWith(path)) {
            location = url.substring(0, url.length() - path.length());
        } else {
            throw new IllegalStateException("Unknown URL format for class location: " + url);
        }

        try {
            return new URL(location);
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
