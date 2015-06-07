package org.mockitoutil;

import static java.util.Arrays.asList;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class ClassLoaders {
    protected ClassLoader parent = currentClassLoader();

    protected ClassLoaders() {
    }

    public static IsolatedURLClassLoaderBuilder isolatedClassLoader() {
        return new IsolatedURLClassLoaderBuilder();
    }

    public static ExcludingURLClassLoaderBuilder excludingClassLoader() {
        return new ExcludingURLClassLoaderBuilder();
    }

    public static InMemoryClassLoaderBuilder inMemoryClassLoader() {
        return new InMemoryClassLoaderBuilder();
    }

    public static ReachableClassesFinder in(
            final ClassLoader classLoader_without_jUnit) {
        return new ReachableClassesFinder(classLoader_without_jUnit);
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

    public static class IsolatedURLClassLoaderBuilder extends ClassLoaders {
        private final ArrayList<String> privateCopyPrefixes = new ArrayList<String>();
        private final ArrayList<URL> codeSourceUrls = new ArrayList<URL>();

        public IsolatedURLClassLoaderBuilder withPrivateCopyOf(
                final String... privatePrefixes) {
            privateCopyPrefixes.addAll(asList(privatePrefixes));
            return this;
        }

        public IsolatedURLClassLoaderBuilder withCodeSourceUrls(final String... urls) {
            codeSourceUrls.addAll(pathsToURLs(urls));
            return this;
        }

        public IsolatedURLClassLoaderBuilder withCodeSourceUrlOf(
                final Class<?>... classes) {
            for (final Class<?> clazz : classes) {
                codeSourceUrls.add(obtainClassPathOf(clazz.getName()));
            }
            return this;
        }

        public IsolatedURLClassLoaderBuilder withCurrentCodeSourceUrls() {
            codeSourceUrls.add(obtainClassPathOf(ClassLoaders.class.getName()));
            return this;
        }

        public ClassLoader build() {
            return new LocalIsolatedURLClassLoader(jdkClassLoader(),
                    codeSourceUrls.toArray(new URL[codeSourceUrls.size()]),
                    privateCopyPrefixes);
        }
    }

    static class LocalIsolatedURLClassLoader extends URLClassLoader {
        private final ArrayList<String> privateCopyPrefixes;

        public LocalIsolatedURLClassLoader(final ClassLoader classLoader, final URL[] urls,
                final ArrayList<String> privateCopyPrefixes) {
            super(urls, classLoader);
            this.privateCopyPrefixes = privateCopyPrefixes;
        }

        @Override
        public Class<?> findClass(final String name) throws ClassNotFoundException {
            if (classShouldBePrivate(name)) {
                return super.findClass(name);
            }
            throw new ClassNotFoundException(
                    "Can only load classes with prefix : "
                            + privateCopyPrefixes);
        }

        private boolean classShouldBePrivate(final String name) {
            for (final String prefix : privateCopyPrefixes) {
                if (name.startsWith(prefix)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static class ExcludingURLClassLoaderBuilder extends ClassLoaders {
        private final ArrayList<String> privateCopyPrefixes = new ArrayList<String>();
        private final ArrayList<URL> codeSourceUrls = new ArrayList<URL>();

        public ExcludingURLClassLoaderBuilder without(final String... privatePrefixes) {
            privateCopyPrefixes.addAll(asList(privatePrefixes));
            return this;
        }

        public ExcludingURLClassLoaderBuilder withCodeSourceUrls(final String... urls) {
            codeSourceUrls.addAll(pathsToURLs(urls));
            return this;
        }

        public ExcludingURLClassLoaderBuilder withCodeSourceUrlOf(
                final Class<?>... classes) {
            for (final Class<?> clazz : classes) {
                codeSourceUrls.add(obtainClassPathOf(clazz.getName()));
            }
            return this;
        }

        public ExcludingURLClassLoaderBuilder withCurrentCodeSourceUrls() {
            codeSourceUrls.add(obtainClassPathOf(ClassLoaders.class.getName()));
            return this;
        }

        @Override
        public ClassLoader build() {
            return new LocalExcludingURLClassLoader(jdkClassLoader(),
                    codeSourceUrls.toArray(new URL[codeSourceUrls.size()]),
                    privateCopyPrefixes);
        }
    }

    static class LocalExcludingURLClassLoader extends URLClassLoader {
        private final ArrayList<String> privateCopyPrefixes;

        public LocalExcludingURLClassLoader(final ClassLoader classLoader,
                final URL[] urls, final ArrayList<String> privateCopyPrefixes) {
            super(urls, classLoader);
            this.privateCopyPrefixes = privateCopyPrefixes;
        }

        @Override
        public Class<?> findClass(final String name) throws ClassNotFoundException {
            if (classShouldBePrivate(name)) {
                throw new ClassNotFoundException("classes with prefix : "
                        + privateCopyPrefixes + " are excluded");
            }
            return super.findClass(name);
        }

        private boolean classShouldBePrivate(final String name) {
            for (final String prefix : privateCopyPrefixes) {
                if (name.startsWith(prefix)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static class InMemoryClassLoaderBuilder extends ClassLoaders {
        private final Map<String, byte[]> inMemoryClassObjects = new HashMap<String, byte[]>();

        public InMemoryClassLoaderBuilder withParent(final ClassLoader parent) {
            this.parent = parent;
            return this;
        }

        public InMemoryClassLoaderBuilder withClassDefinition(final String name,
                final byte[] classDefinition) {
            inMemoryClassObjects.put(name, classDefinition);
            return this;
        }

        @Override
        public ClassLoader build() {
            return new InMemoryClassLoader(parent, inMemoryClassObjects);
        }
    }

    static class InMemoryClassLoader extends ClassLoader {
        public static final String SCHEME = "mem";
        private Map<String, byte[]> inMemoryClassObjects = new HashMap<String, byte[]>();

        public InMemoryClassLoader(final ClassLoader parent,
                final Map<String, byte[]> inMemoryClassObjects) {
            super(parent);
            this.inMemoryClassObjects = inMemoryClassObjects;
        }

        @Override
        protected Class findClass(final String name) throws ClassNotFoundException {
            final byte[] classDefinition = inMemoryClassObjects.get(name);
            if (classDefinition != null) {
                return defineClass(name, classDefinition, 0,
                        classDefinition.length);
            }
            throw new ClassNotFoundException(name);
        }

        @Override
        public Enumeration<URL> getResources(final String ignored) throws IOException {
            return inMemoryOnly();
        }

        private Enumeration<URL> inMemoryOnly() {
            final Set<String> names = inMemoryClassObjects.keySet();
            return new Enumeration<URL>() {
                private final MemHandler memHandler = new MemHandler(
                        InMemoryClassLoader.this);
                private final Iterator<String> it = names.iterator();

                public boolean hasMoreElements() {
                    return it.hasNext();
                }

                public URL nextElement() {
                    try {
                        return new URL(null, SCHEME + ":" + it.next(),
                                memHandler);
                    } catch (final MalformedURLException rethrown) {
                        throw new IllegalStateException(rethrown);
                    }
                }
            };
        }
    }

    public static class MemHandler extends URLStreamHandler {
        private final InMemoryClassLoader inMemoryClassLoader;

        public MemHandler(final InMemoryClassLoader inMemoryClassLoader) {
            this.inMemoryClassLoader = inMemoryClassLoader;
        }

        @Override
        protected URLConnection openConnection(final URL url) throws IOException {
            return new MemURLConnection(url, inMemoryClassLoader);
        }

        private static class MemURLConnection extends URLConnection {
            private final InMemoryClassLoader inMemoryClassLoader;
            private final String qualifiedName;

            public MemURLConnection(final URL url,
                    final InMemoryClassLoader inMemoryClassLoader) {
                super(url);
                this.inMemoryClassLoader = inMemoryClassLoader;
                qualifiedName = url.getPath();
            }

            @Override
            public void connect() throws IOException {
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(
                        inMemoryClassLoader.inMemoryClassObjects
                                .get(qualifiedName));
            }
        }
    }

    protected URL obtainClassPathOf(final String className) {
        final String path = className.replace('.', '/') + ".class";
        final String url = ClassLoaders.class.getClassLoader().getResource(path)
                .toExternalForm();

        try {
            return new URL(url.substring(0, url.length() - path.length()));
        } catch (final MalformedURLException e) {
            throw new RuntimeException(
                    "Classloader couldn't obtain a proper classpath URL", e);
        }
    }

    protected List<URL> pathsToURLs(final String... codeSourceUrls) {
        return pathsToURLs(Arrays.asList(codeSourceUrls));
    }

    private List<URL> pathsToURLs(final List<String> codeSourceUrls) {
        final ArrayList<URL> urls = new ArrayList<URL>(codeSourceUrls.size());
        for (final String codeSourceUrl : codeSourceUrls) {
            final URL url = pathToUrl(codeSourceUrl);
            urls.add(url);
        }
        return urls;
    }

    private URL pathToUrl(final String path) {
        try {
            return new File(path).getAbsoluteFile().toURI().toURL();
        } catch (final MalformedURLException e) {
            throw new IllegalArgumentException("Path is malformed", e);
        }
    }

    public static class ReachableClassesFinder {
        private final ClassLoader classLoader;
        private final Set<String> qualifiedNameSubstring = new HashSet<String>();

        public ReachableClassesFinder(final ClassLoader classLoader) {
            this.classLoader = classLoader;
        }

        public ReachableClassesFinder omit(final String... qualifiedNameSubstring) {
            this.qualifiedNameSubstring.addAll(Arrays
                    .asList(qualifiedNameSubstring));
            return this;
        }

        public Set<String> listOwnedClasses() throws IOException,
                URISyntaxException {
            final Enumeration<URL> roots = classLoader.getResources("");

            final Set<String> classes = new HashSet<String>();
            while (roots.hasMoreElements()) {
                final URI uri = roots.nextElement().toURI();

                if (uri.getScheme().equalsIgnoreCase("file")) {
                    addFromFileBasedClassLoader(classes, uri);
                } else if (uri.getScheme().equalsIgnoreCase(
                        InMemoryClassLoader.SCHEME)) {
                    addFromInMemoryBasedClassLoader(classes, uri);
                } else {
                    throw new IllegalArgumentException(
                            String.format(
                                    "Given ClassLoader '%s' don't have reachable by File or vi ClassLoaders.inMemory",
                                    classLoader));
                }
            }
            return classes;
        }

        private void addFromFileBasedClassLoader(final Set<String> classes, final URI uri) {
            final File root = new File(uri);
            classes.addAll(findClassQualifiedNames(root, root,
                    qualifiedNameSubstring));
        }

        private void addFromInMemoryBasedClassLoader(final Set<String> classes,
                final URI uri) {
            final String qualifiedName = uri.getSchemeSpecificPart();
            if (excludes(qualifiedName, qualifiedNameSubstring)) {
                classes.add(qualifiedName);
            }
        }

        private Set<String> findClassQualifiedNames(final File root, final File file,
                final Set<String> packageFilters) {
            if (file.isDirectory()) {
                final File[] files = file.listFiles();
                final Set<String> classes = new HashSet<String>();
                for (final File children : files) {
                    classes.addAll(findClassQualifiedNames(root, children,
                            packageFilters));
                }
                return classes;
            } else {
                if (file.getName().endsWith(".class")) {
                    final String qualifiedName = classNameFor(root, file);
                    if (excludes(qualifiedName, packageFilters)) {
                        return Collections.singleton(qualifiedName);
                    }
                }
            }
            return Collections.emptySet();
        }

        private boolean excludes(final String qualifiedName,
                final Set<String> packageFilters) {
            for (final String filter : packageFilters) {
                if (qualifiedName.contains(filter)) {
                    return false;
                }
            }
            return true;
        }

        private String classNameFor(final File root, final File file) {
            final String temp = file.getAbsolutePath()
                    .substring(root.getAbsolutePath().length() + 1)
                    .replace(File.separatorChar, '.');
            return temp.subSequence(0, temp.indexOf(".class")).toString();
        }

    }
}
