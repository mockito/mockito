/*
 * Copyright 2003 The Apache Software Foundation
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
package org.mockito.cglib.transform;

import org.mockito.cglib.core.ClassGenerator;
import org.mockito.asm.Attribute;
import org.mockito.asm.ClassReader;
import org.mockito.asm.ClassVisitor;

public class ClassReaderGenerator implements ClassGenerator {
    private final ClassReader r;
    private final Attribute[] attrs;
    private final int flags;
    
    public ClassReaderGenerator(ClassReader r, int flags) {
        this(r, null, flags);
    }

    public ClassReaderGenerator(ClassReader r, Attribute[] attrs, int flags) {
        this.r = r;
        this.attrs = (attrs != null) ? attrs : new Attribute[0];
        this.flags = flags;
    }
    
    public void generateClass(ClassVisitor v) {
        r.accept(v, attrs, flags);
    }
}
