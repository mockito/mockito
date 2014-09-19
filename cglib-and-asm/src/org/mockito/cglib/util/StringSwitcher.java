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
package org.mockito.cglib.util;

import java.util.*;
import org.mockito.cglib.core.*;
import org.mockito.asm.ClassVisitor;
import org.mockito.asm.Label;
import org.mockito.asm.Type;

/**
 * This class implements a simple String->int mapping for a fixed set of keys.
 */
abstract public class StringSwitcher {
    private static final Type STRING_SWITCHER =
      TypeUtils.parseType("org.mockito.cglib.util.StringSwitcher");
    private static final Signature INT_VALUE =
      TypeUtils.parseSignature("int intValue(String)");
    private static final StringSwitcherKey KEY_FACTORY =
      (StringSwitcherKey)KeyFactory.create(StringSwitcherKey.class);

    interface StringSwitcherKey {
        public Object newInstance(String[] strings, int[] ints, boolean fixedInput);
    }

    /**
     * Helper method to create a StringSwitcher.
     * For finer control over the generated instance, use a new instance of StringSwitcher.Generator
     * instead of this static method.
     * @param strings the array of String keys; must be the same length as the value array
     * @param ints the array of integer results; must be the same length as the key array
     * @param fixedInput if false, an unknown key will be returned from {@link #intValue} as <code>-1</code>; if true,
     * the result will be undefined, and the resulting code will be faster
     */
    public static StringSwitcher create(String[] strings, int[] ints, boolean fixedInput) {
        Generator gen = new Generator();
        gen.setStrings(strings);
        gen.setInts(ints);
        gen.setFixedInput(fixedInput);
        return gen.create();
    }

    protected StringSwitcher() {
    }

    /**
     * Return the integer associated with the given key.
     * @param s the key
     * @return the associated integer value, or <code>-1</code> if the key is unknown (unless
     * <code>fixedInput</code> was specified when this <code>StringSwitcher</code> was created,
     * in which case the return value for an unknown key is undefined)
     */
    abstract public int intValue(String s);

    public static class Generator extends AbstractClassGenerator {
        private static final Source SOURCE = new Source(StringSwitcher.class.getName());

        private String[] strings;
        private int[] ints;
        private boolean fixedInput;
        
        public Generator() {
            super(SOURCE);
        }

        /**
         * Set the array of recognized Strings.
         * @param strings the array of String keys; must be the same length as the value array
         * @see #setInts
         */
        public void setStrings(String[] strings) {
            this.strings = strings;
        }

        /**
         * Set the array of integer results.
         * @param ints the array of integer results; must be the same length as the key array
         * @see #setStrings
         */
        public void setInts(int[] ints) {
            this.ints = ints;
        }

        /**
         * Configure how unknown String keys will be handled.
         * @param fixedInput if false, an unknown key will be returned from {@link #intValue} as <code>-1</code>; if true,
         * the result will be undefined, and the resulting code will be faster
         */
        public void setFixedInput(boolean fixedInput) {
            this.fixedInput = fixedInput;
        }

        protected ClassLoader getDefaultClassLoader() {
            return getClass().getClassLoader();
        }

        /**
         * Generate the <code>StringSwitcher</code>.
         */
        public StringSwitcher create() {
            setNamePrefix(StringSwitcher.class.getName());
            Object key = KEY_FACTORY.newInstance(strings, ints, fixedInput);
            return (StringSwitcher)super.create(key);
        }

        public void generateClass(ClassVisitor v) throws Exception {
            ClassEmitter ce = new ClassEmitter(v);
            ce.begin_class(Constants.V1_2,
                           Constants.ACC_PUBLIC,
                           getClassName(),
                           STRING_SWITCHER,
                           null,
                           Constants.SOURCE_FILE);
            EmitUtils.null_constructor(ce);
            final CodeEmitter e = ce.begin_method(Constants.ACC_PUBLIC, INT_VALUE, null);
            e.load_arg(0);
            final List stringList = Arrays.asList(strings);
            int style = fixedInput ? Constants.SWITCH_STYLE_HASHONLY : Constants.SWITCH_STYLE_HASH;
            EmitUtils.string_switch(e, strings, style, new ObjectSwitchCallback() {
                public void processCase(Object key, Label end) {
                    e.push(ints[stringList.indexOf(key)]);
                    e.return_value();
                }
                public void processDefault() {
                    e.push(-1);
                    e.return_value();
                }
            });
            e.end_method();
            ce.end_class();
        }

        protected Object firstInstance(Class type) {
            return (StringSwitcher)ReflectUtils.newInstance(type);
        }

        protected Object nextInstance(Object instance) {
            return instance;
        }
    }
}
