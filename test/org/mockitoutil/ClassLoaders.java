package org.mockitoutil;

import static java.util.Arrays.asList;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ClassLoaders {
    protected ClassLoaders() {}

    public static IsolatedURLClassLoaderBuilder isolatedClassLoader() {
        return new IsolatedURLClassLoaderBuilder();
    }

    public static InMemoryClassLoaderBuilder inMemoryClassLoader() {
        return new InMemoryClassLoaderBuilder();
    }


    public static class IsolatedURLClassLoaderBuilder extends ClassLoaders {
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

        public IsolatedURLClassLoaderBuilder withCurrentCodeSourceUrls() {
            codeSourceUrls.add(obtainClassPathOf(ClassLoaders.class.getName()));
            return this;
        }

        public ClassLoader build() {
            return new LocalIsolatedURLClassLoader(
                    codeSourceUrls.toArray(new URL[codeSourceUrls.size()]),
                    privateCopyPrefixes
            );
        }
    }

    static class LocalIsolatedURLClassLoader extends URLClassLoader {
        private final ArrayList<String> privateCopyPrefixes;

        public LocalIsolatedURLClassLoader(URL[] urls, ArrayList<String> privateCopyPrefixes) {
            super(urls, null);
            this.privateCopyPrefixes = privateCopyPrefixes;
        }

        @Override
        public Class<?> findClass(String name) throws ClassNotFoundException {
            if(classShouldBePrivate(name)) return super.findClass(name);
            throw new ClassNotFoundException("Can only load classes with prefix : " + privateCopyPrefixes);
        }

        private boolean classShouldBePrivate(String name) {
            for (String prefix : privateCopyPrefixes) {
                if (name.startsWith(prefix)) return true;
            }
            return false;
        }
    }

    public static class InMemoryClassLoaderBuilder extends ClassLoaders {
        private Map<String , byte[]> inMemoryClassObjects = new HashMap<String , byte[]>();

        public InMemoryClassLoaderBuilder withClassDefinition(String name, byte[] classDefinition) {
            inMemoryClassObjects.put(name, classDefinition);
            return this;
        }

        public ClassLoader build() {
            return new InMemoryClassLoader(inMemoryClassObjects);
        }
    }

    static class InMemoryClassLoader extends ClassLoader {
        private Map<String , byte[]> inMemoryClassObjects = new HashMap<String , byte[]>();

        public InMemoryClassLoader(Map<String, byte[]> inMemoryClassObjects) {
            this.inMemoryClassObjects = inMemoryClassObjects;
        }

        protected Class findClass(String name) throws ClassNotFoundException {
            byte[] classDefinition = inMemoryClassObjects.get(name);
            if (classDefinition != null) {
                return defineClass(name, classDefinition, 0, classDefinition.length);
            }
            throw new ClassNotFoundException(name);
        }


    }

    protected URL obtainClassPathOf(String className) {
        String path = className.replace('.', '/') + ".class";
        String url = ClassLoaders.class.getClassLoader().getResource(path).toExternalForm();

        try {
            return new URL(url.substring(0, url.length() - path.length()));
        } catch (MalformedURLException e) {
            throw new RuntimeException("Classloader couldn't obtain a proper classpath URL", e);
        }
    }

    protected List<URL> pathsToURLs(String... codeSourceUrls) {
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
}
