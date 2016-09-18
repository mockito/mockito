package org.mockito.javadoc;

import com.sun.javadoc.*;
import com.sun.tools.doclets.formats.html.HtmlDoclet;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * Doclet built to exclude mockito internal packages from the outputted Javadoc.
 * All methods except the start method are copied from {@link com.sun.tools.doclets.standard.Standard}.
 * Inspired by https://gist.github.com/benjchristensen/1410681
 */
public class JavadocExclude {

    public static int optionLength(String var0) {
        return HtmlDoclet.optionLength(var0);
    }

    public static boolean start(RootDoc root) {
        Class clz = root.getClass();
        return HtmlDoclet.start((RootDoc) Proxy.newProxyInstance(clz.getClassLoader(), clz.getInterfaces(), new ExcludeHandler(root)));
    }

    public static boolean validOptions(String[][] var0, DocErrorReporter var1) {
        return HtmlDoclet.validOptions(var0, var1);
    }

    public static LanguageVersion languageVersion() {
        return HtmlDoclet.languageVersion();
    }

    /**
     * Proxy which filters the {@link RootDoc#classes()} method to filter {@link ClassDoc} based on their package name.
     */
    private static class ExcludeHandler implements InvocationHandler {
        private final RootDoc root;

        private ExcludeHandler(RootDoc root) {
            this.root = root;
        }

        @Override
        public Object invoke(Object o, Method method, Object[] args) throws Throwable {
            Object result = method.invoke(root, args);
            if (result instanceof Object[]) {
                List<ClassDoc> filteredDocs = new ArrayList<ClassDoc>();
                Object[] array = (Object[]) result;
                for (Object entry : array) {
                    if (entry instanceof ClassDoc) {
                        ClassDoc doc = (ClassDoc) entry;
                        if (!doc.containingPackage().name().startsWith("org.mockito.internal")) {
                            filteredDocs.add(doc);
                        }
                    }
                }
                // If no ClassDoc were found in the original array,
                // since PackageDoc area also included in the classes array.
                if (filteredDocs.size() > 0) {
                    ClassDoc[] docArray = new ClassDoc[filteredDocs.size()];
                    return filteredDocs.toArray(docArray);
                }
            }
            return result;
        }
    }
}
