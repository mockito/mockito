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

import java.util.List;
import org.mockito.cglib.core.*;

interface CallbackGenerator
{
    void generate(ClassEmitter ce, Context context, List methods) throws Exception;
    void generateStatic(CodeEmitter e, Context context, List methods) throws Exception;

    interface Context
    {
        ClassLoader getClassLoader();
        CodeEmitter beginMethod(ClassEmitter ce, MethodInfo method);
        int getOriginalModifiers(MethodInfo method);
        int getIndex(MethodInfo method);
        void emitCallback(CodeEmitter ce, int index);
        Signature getImplSignature(MethodInfo method);
        void emitInvoke(CodeEmitter e, MethodInfo method);
    }
}
