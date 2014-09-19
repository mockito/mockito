/*
 * Copyright 2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mockito.cglib.proxy;

import java.lang.reflect.*;
import java.util.*;
import org.mockito.cglib.core.*;
import org.mockito.asm.ClassVisitor;
import org.mockito.asm.Type;

/**
 * Generates new interfaces at runtime.
 * By passing a generated interface to the Enhancer's list of interfaces to
 * implement, you can make your enhanced classes handle an arbitrary set
 * of method signatures.
 * @author Chris Nokleberg
 * @version $Id: InterfaceMaker.java,v 1.4 2006/03/05 02:43:19 herbyderby Exp $
 */
public class InterfaceMaker extends AbstractClassGenerator
{
    private static final Source SOURCE = new Source(InterfaceMaker.class.getName());
    private Map signatures = new HashMap();

    /**
     * Create a new <code>InterfaceMaker</code>. A new <code>InterfaceMaker</code>
     * object should be used for each generated interface, and should not
     * be shared across threads.
     */
    public InterfaceMaker() {
        super(SOURCE);
    }

    /**
     * Add a method signature to the interface.
     * @param sig the method signature to add to the interface
     * @param exceptions an array of exception types to declare for the method
     */
    public void add(Signature sig, Type[] exceptions) {
        signatures.put(sig, exceptions);
    }

    /**
     * Add a method signature to the interface. The method modifiers are ignored,
     * since interface methods are by definition abstract and public.
     * @param method the method to add to the interface
     */
    public void add(Method method) {
        add(ReflectUtils.getSignature(method),
            ReflectUtils.getExceptionTypes(method));
    }

    /**
     * Add all the public methods in the specified class.
     * Methods from superclasses are included, except for methods declared in the base
     * Object class (e.g. <code>getClass</code>, <code>equals</code>, <code>hashCode</code>).
     * @param class the class containing the methods to add to the interface
     */
    public void add(Class clazz) {
        Method[] methods = clazz.getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method m = methods[i];
            if (!m.getDeclaringClass().getName().equals("java.lang.Object")) {
                add(m);
            }
        }
    }

    /**
     * Create an interface using the current set of method signatures.
     */
    public Class create() {
        setUseCache(false);
        return (Class)super.create(this);
    }

    protected ClassLoader getDefaultClassLoader() {
        return null;
    }
    
    protected Object firstInstance(Class type) {
        return type;
    }

    protected Object nextInstance(Object instance) {
        throw new IllegalStateException("InterfaceMaker does not cache");
    }

    public void generateClass(ClassVisitor v) throws Exception {
        ClassEmitter ce = new ClassEmitter(v);
        ce.begin_class(Constants.V1_2,
                       Constants.ACC_PUBLIC | Constants.ACC_INTERFACE,
                       getClassName(),
                       null,
                       null,
                       Constants.SOURCE_FILE);
        for (Iterator it = signatures.keySet().iterator(); it.hasNext();) {
            Signature sig = (Signature)it.next();
            Type[] exceptions = (Type[])signatures.get(sig);
            ce.begin_method(Constants.ACC_PUBLIC | Constants.ACC_ABSTRACT,
                            sig,
                            exceptions).end_method();
        }
        ce.end_class();
    }
}
