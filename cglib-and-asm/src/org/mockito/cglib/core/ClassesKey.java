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
package org.mockito.cglib.core;

public class ClassesKey {
    private static final Key FACTORY = (Key)KeyFactory.create(Key.class, KeyFactory.OBJECT_BY_CLASS);
    
    interface Key {
        Object newInstance(Object[] array);
    }

    private ClassesKey() {
    }

    public static Object create(Object[] array) {
        return FACTORY.newInstance(array);
    }
}
