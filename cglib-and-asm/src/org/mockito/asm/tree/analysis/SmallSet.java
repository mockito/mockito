/***
 * ASM: a very small and fast Java bytecode manipulation framework
 * Copyright (c) 2000-2007 INRIA, France Telecom
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holders nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mockito.asm.tree.analysis;

import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A set of at most two elements.
 * 
 * @author Eric Bruneton
 */
class SmallSet extends AbstractSet implements Iterator {

    // if e1 is null, e2 must be null; otherwise e2 must be different from e1

    Object e1, e2;

    static final Set EMPTY_SET = new SmallSet(null, null);

    SmallSet(final Object e1, final Object e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    // -------------------------------------------------------------------------
    // Implementation of inherited abstract methods
    // -------------------------------------------------------------------------

    public Iterator iterator() {
        return new SmallSet(e1, e2);
    }

    public int size() {
        return e1 == null ? 0 : (e2 == null ? 1 : 2);
    }

    // -------------------------------------------------------------------------
    // Implementation of the Iterator interface
    // -------------------------------------------------------------------------

    public boolean hasNext() {
        return e1 != null;
    }

    public Object next() {
        Object e = e1;
        e1 = e2;
        e2 = null;
        return e;
    }

    public void remove() {
    }

    // -------------------------------------------------------------------------
    // Utility methods
    // -------------------------------------------------------------------------

    Set union(final SmallSet s) {
        if ((s.e1 == e1 && s.e2 == e2) || (s.e1 == e2 && s.e2 == e1)) {
            return this; // if the two sets are equal, return this
        }
        if (s.e1 == null) {
            return this; // if s is empty, return this
        }
        if (e1 == null) {
            return s; // if this is empty, return s
        }
        if (s.e2 == null) { // s contains exactly one element
            if (e2 == null) {
                return new SmallSet(e1, s.e1); // necessarily e1 != s.e1
            } else if (s.e1 == e1 || s.e1 == e2) { // s is included in this
                return this;
            }
        }
        if (e2 == null) { // this contains exactly one element
            // if (s.e2 == null) { // cannot happen
            // return new SmallSet(e1, s.e1); // necessarily e1 != s.e1
            // } else
            if (e1 == s.e1 || e1 == s.e2) { // this in included in s
                return s;
            }
        }
        // here we know that there are at least 3 distinct elements
        HashSet r = new HashSet(4);
        r.add(e1);
        if (e2 != null) {
            r.add(e2);
        }
        r.add(s.e1);
        if (s.e2 != null) {
            r.add(s.e2);
        }
        return r;
    }
}
