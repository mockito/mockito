package org.mockito.javadoc;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doc;
import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.LanguageVersion;
import com.sun.javadoc.RootDoc;
import com.sun.tools.doclets.formats.html.HtmlDoclet;
import com.sun.tools.javadoc.Main;

/**
 * Doclet built to exclude mockito internal packages from the outputted Javadoc.
 * All methods except the start method are copied from {@link com.sun.tools.doclets.standard.Standard}.
 * Inspired by https://gist.github.com/benjchristensen/1410681
 */
public class JavadocExclude {

    public static void main(String[] args) {
        String name = JavadocExclude.class.getName();
        Main.execute(name, name, args);
    }

    public static int optionLength(String var0) {
        return HtmlDoclet.optionLength(var0);
    }

    public static boolean start(RootDoc root) {
        Class clz = root.getClass();
        return HtmlDoclet.start((RootDoc) Proxy.newProxyInstance(clz.getClassLoader(),
                                                                 clz.getInterfaces(),
                                                                 new ExcludeHandler(root)));
    }

    public static boolean validOptions(String[][] optionsAndValue, DocErrorReporter docErrorReporter) {
        return HtmlDoclet.validOptions(optionsAndValue, docErrorReporter);
    }

    public static LanguageVersion languageVersion() {
        return HtmlDoclet.languageVersion();
    }

    private static boolean exclude(Doc doc) {
        if (doc instanceof ClassDoc) {
            ClassDoc classDoc = (ClassDoc) doc;
            if (classDoc.containingPackage().name().contains("org.mockito.internal")) {
                return true;
            }
        }
//        if (doc.name().contains("UnitTest")) {
//            return true;
//        } else if (doc.tags("@ExcludeFromJavadoc").length > 0) {
//            return true;
//        } else if (doc instanceof ProgramElementDoc) {
//            if (((ProgramElementDoc) doc).containingPackage().tags("@ExcludeFromJavadoc").length > 0)
//                return true;
//        }
        // nothing above found a reason to exclude
        return false;
    }

    private static Object process(Object obj, Class expect) {
        if (obj == null)
            return null;
        Class cls = obj.getClass();
        if (cls.getName().startsWith("com.sun.")) {
            return Proxy.newProxyInstance(cls.getClassLoader(), cls.getInterfaces(), new ExcludeHandler(obj));
        } else if (obj instanceof Object[]) {
            Class componentType = expect.getComponentType();
            Object[] array = (Object[]) obj;
            List list = new ArrayList(array.length);
            for (int i = 0; i < array.length; i++) {
                Object entry = array[i];
                if ((entry instanceof Doc) && exclude((Doc) entry)) {
                    continue;
                }
                list.add(process(entry, componentType));
            }
            return list.toArray((Object[]) Array.newInstance(componentType != null ? componentType : Object.class,
                                                             list.size()));
        } else {
//            System.out.println(obj +":" + obj.getClass());
            return obj;
        }
    }


    /**
     * Proxy which filters the {@link RootDoc#classes()} method to filter {@link ClassDoc} based on their package name.
     */
    private static class ExcludeHandler implements InvocationHandler {
        private final Object root;

        private ExcludeHandler(Object root) {
            this.root = root;
        }

//        @Override
//        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//            Object result = method.invoke(root, args);
//            if (result instanceof Object[]) {
//                List<ClassDoc> filteredDocs = new ArrayList<ClassDoc>();
//                Object[] array = (Object[]) result;
//                for (Object entry : array) {
//                    if (entry instanceof ClassDoc) {
//                        ClassDoc doc = (ClassDoc) entry;
//                        if (!doc.containingPackage().name().startsWith("org.mockito.internal")) {
//                            filteredDocs.add(doc);
//                        }
//                    }
//                }
//                // If no ClassDoc were found in the original array,
//                // since PackageDoc area also included in the classes array.
//                if (filteredDocs.size() > 0) {
//                    ClassDoc[] docArray = new ClassDoc[filteredDocs.size()];
//                    return filteredDocs.toArray(docArray);
//                }
//            }
//            return result;
//        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (args != null) {
                String methodName = method.getName();
                if (methodName.equals("compareTo")
                    || methodName.equals("equals")
                    || methodName.equals("overrides")
                    || methodName.equals("subclassOf")) {
//                    System.out.println("args[0] : " + args[0].getClass());
                    args[0] = unwrap(args[0]);
                }
                if (methodName.equals("inlineTags")) {

                    Object[] invoke = (Object[]) method.invoke(root, args);
                    if (invoke.length != 0) {
                        System.out.println(methodName);
                        System.out.println(Arrays.toString(invoke));

                    }
//                    args[0] = unwrap(args[0]);
                }
            }
            try {
                return process(method.invoke(root, args),
                               method.getReturnType());
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        }

        private Object unwrap(Object proxy) {
            if (proxy instanceof Proxy) {
                return ((ExcludeHandler) Proxy.getInvocationHandler(proxy)).root;
            }
            return proxy;
        }
    }
}
