package org.mockito.javadoc;

import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclets.StandardDoclet;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * Doclet built to exclude mockito internal packages from the outputted Javadoc.
 */
public class JavadocExclude1_9 extends StandardDoclet {

    @Override
    public boolean run(DocletEnvironment docEnv) {
        Class clz = docEnv.getClass();
        return htmlDoclet.run(Proxy.newProxyInstance(clz.getClassLoader(), clz.getInterfaces(), new ExcludeHandler(docEnv)));
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
