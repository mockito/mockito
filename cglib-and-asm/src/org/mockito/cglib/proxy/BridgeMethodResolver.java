/*
 * Copyright 2011 The Apache Software Foundation
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.mockito.cglib.core.Signature;

import org.mockito.asm.AnnotationVisitor;
import org.mockito.asm.Attribute;
import org.mockito.asm.ClassReader;
import org.mockito.asm.ClassVisitor;
import org.mockito.asm.FieldVisitor;
import org.mockito.asm.Label;
import org.mockito.asm.MethodVisitor;
import org.mockito.asm.Opcodes;

/**
 * Uses bytecode reflection to figure out the targets of all bridge methods
 * that use invokespecial, so that we can later rewrite them to use invokevirtual.
 * 
 * @author sberlin@gmail.com (Sam Berlin)
 */
class BridgeMethodResolver {

    private final Map/* <Class, Set<Signature> */declToBridge;

    public BridgeMethodResolver(Map declToBridge) {
        this.declToBridge = declToBridge;
    }

    /**
     * Finds all bridge methods that are being called with invokespecial &
     * returns them.
     */
    public Map/*<Signature, Signature>*/resolveAll() {
        Map resolved = new HashMap();
        for (Iterator entryIter = declToBridge.entrySet().iterator(); entryIter.hasNext(); ) {
            Map.Entry entry = (Map.Entry)entryIter.next();
            Class owner = (Class)entry.getKey();
            Set bridges = (Set)entry.getValue();
            try {
                new ClassReader(owner.getName())
                  .accept(new BridgedFinder(bridges, resolved),
                          ClassReader.SKIP_FRAMES | ClassReader.SKIP_DEBUG);
            } catch(IOException ignored) {}
        }
        return resolved;
    }

    private static class BridgedFinder extends ClassVisitor {
        private Map/*<Signature, Signature>*/ resolved;
        private Set/*<Signature>*/ eligableMethods;
        
        private Signature currentMethod = null;

        BridgedFinder(Set eligableMethods, Map resolved) {
            super(Opcodes.ASM4);
            this.resolved = resolved;
            this.eligableMethods = eligableMethods;
        }

        public void visit(int version, int access, String name,
                String signature, String superName, String[] interfaces) {
        }

        public MethodVisitor visitMethod(int access, String name, String desc,
                String signature, String[] exceptions) {
            Signature sig = new Signature(name, desc);
            if (eligableMethods.remove(sig)) {
                currentMethod = sig;
                return new MethodVisitor(Opcodes.ASM4) {
                    public void visitMethodInsn(int opcode, String owner, String name,
                                                String desc) {
                        if (opcode == Opcodes.INVOKESPECIAL && currentMethod != null) {
                            Signature target = new Signature(name, desc);
                            // If the target signature is the same as the current,
                            // we shouldn't change our bridge becaues invokespecial
                            // is the only way to make progress (otherwise we'll
                            // get infinite recursion).  This would typically
                            // only happen when a bridge method is created to widen
                            // the visibility of a superclass' method.
                            if (!target.equals(currentMethod)) {
                                resolved.put(currentMethod, target);
                            }
                            currentMethod = null;
                        }
                    }
                };
            } else {
                return null;
            }
        }
    }

}
