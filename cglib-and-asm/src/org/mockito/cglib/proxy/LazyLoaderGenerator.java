/*
 * Copyright 2003,2004 The Apache Software Foundation
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

import java.util.*;
import org.mockito.cglib.core.*;
import org.mockito.asm.Label;
import org.mockito.asm.Type;

class LazyLoaderGenerator implements CallbackGenerator {
    public static final LazyLoaderGenerator INSTANCE = new LazyLoaderGenerator();

    private static final Signature LOAD_OBJECT = 
      TypeUtils.parseSignature("Object loadObject()");
    private static final Type LAZY_LOADER =
      TypeUtils.parseType("org.mockito.cglib.proxy.LazyLoader");

    public void generate(ClassEmitter ce, Context context, List methods) {
        Set indexes = new HashSet();
        for (Iterator it = methods.iterator(); it.hasNext();) {
            MethodInfo method = (MethodInfo)it.next();
            if (TypeUtils.isProtected(method.getModifiers())) {
                // ignore protected methods
            } else {
                int index = context.getIndex(method);
                indexes.add(new Integer(index));
                CodeEmitter e = context.beginMethod(ce, method);
                e.load_this();
                e.dup();
                e.invoke_virtual_this(loadMethod(index));
                e.checkcast(method.getClassInfo().getType());
                e.load_args();
                e.invoke(method);
                e.return_value();
                e.end_method();
            }
        }

        for (Iterator it = indexes.iterator(); it.hasNext();) {
            int index = ((Integer)it.next()).intValue();

            String delegate = "CGLIB$LAZY_LOADER_" + index;
            ce.declare_field(Constants.ACC_PRIVATE, delegate, Constants.TYPE_OBJECT, null);

            CodeEmitter e = ce.begin_method(Constants.ACC_PRIVATE |
                                            Constants.ACC_SYNCHRONIZED |
                                            Constants.ACC_FINAL,
                                            loadMethod(index),
                                            null);
            e.load_this();
            e.getfield(delegate);
            e.dup();
            Label end = e.make_label();
            e.ifnonnull(end);
            e.pop();
            e.load_this();
            context.emitCallback(e, index);
            e.invoke_interface(LAZY_LOADER, LOAD_OBJECT);
            e.dup_x1();
            e.putfield(delegate);
            e.mark(end);
            e.return_value();
            e.end_method();
            
        }
    }

    private Signature loadMethod(int index) {
        return new Signature("CGLIB$LOAD_PRIVATE_" + index,
                             Constants.TYPE_OBJECT,
                             Constants.TYPES_EMPTY);
    }

    public void generateStatic(CodeEmitter e, Context context, List methods) { }
}
