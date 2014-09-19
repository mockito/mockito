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
package org.mockito.cglib.transform.impl;

import java.lang.reflect.Method;

import org.mockito.cglib.core.CodeEmitter;
import org.mockito.cglib.core.Constants;
import org.mockito.cglib.core.MethodInfo;
import org.mockito.cglib.core.ReflectUtils;
import org.mockito.cglib.core.Signature;
import org.mockito.cglib.transform.ClassEmitterTransformer;

import org.mockito.asm.Attribute;
import org.mockito.asm.Type;

/**
 * @author	Mark Hobson
 */
public class AddInitTransformer extends ClassEmitterTransformer {
    private MethodInfo info;
    
    public AddInitTransformer(Method method) {
        info = ReflectUtils.getMethodInfo(method);
        
        Type[] types = info.getSignature().getArgumentTypes();
        if (types.length != 1 ||
        !types[0].equals(Constants.TYPE_OBJECT) ||
        !info.getSignature().getReturnType().equals(Type.VOID_TYPE)) {
            throw new IllegalArgumentException(method + " illegal signature");
        }
    }
    
    public CodeEmitter begin_method(int access, Signature sig, Type[] exceptions) {
        final CodeEmitter emitter = super.begin_method(access, sig, exceptions);
        if (sig.getName().equals(Constants.CONSTRUCTOR_NAME)) {
            return new CodeEmitter(emitter) {
                public void visitInsn(int opcode) {
                    if (opcode == Constants.RETURN) {
                        load_this();
                        invoke(info);
                    }
                    super.visitInsn(opcode);
                }
            };
        }
        return emitter;
    }
}

