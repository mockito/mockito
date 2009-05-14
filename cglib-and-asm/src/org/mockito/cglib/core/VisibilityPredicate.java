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
package org.mockito.cglib.core;

import java.lang.reflect.*;

import org.mockito.asm.Type;

public class VisibilityPredicate implements Predicate {
    private boolean protectedOk;
    private String pkg;

    public VisibilityPredicate(Class source, boolean protectedOk) {
        this.protectedOk = protectedOk;
        pkg = TypeUtils.getPackageName(Type.getType(source));
    }

    public boolean evaluate(Object arg) {
        int mod = (arg instanceof Member) ? ((Member)arg).getModifiers() : ((Integer)arg).intValue();
        if (Modifier.isPrivate(mod)) {
            return false;
        } else if (Modifier.isPublic(mod)) {
            return true;
        } else if (Modifier.isProtected(mod)) {
            return protectedOk;
        } else {
            return pkg.equals(TypeUtils.getPackageName(Type.getType(((Member)arg).getDeclaringClass())));
        }
    }
}

