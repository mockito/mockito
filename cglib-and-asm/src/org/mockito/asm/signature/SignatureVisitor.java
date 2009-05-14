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
package org.mockito.asm.signature;

/**
 * A visitor to visit a generic signature. The methods of this interface must be
 * called in one of the three following orders (the last one is the only valid
 * order for a {@link SignatureVisitor} that is returned by a method of this
 * interface): <ul> <li><i>ClassSignature</i> = (
 * <tt>visitFormalTypeParameter</tt> 
 *   <tt>visitClassBound</tt>?
 * <tt>visitInterfaceBound</tt>* )* ( <tt>visitSuperClass</tt> 
 *   <tt>visitInterface</tt>* )</li>
 * <li><i>MethodSignature</i> = ( <tt>visitFormalTypeParameter</tt> 
 *   <tt>visitClassBound</tt>?
 * <tt>visitInterfaceBound</tt>* )* ( <tt>visitParameterType</tt>*
 * <tt>visitReturnType</tt> 
 *   <tt>visitExceptionType</tt>* )</li> <li><i>TypeSignature</i> =
 * <tt>visitBaseType</tt> | <tt>visitTypeVariable</tt> |
 * <tt>visitArrayType</tt> | (
 * <tt>visitClassType</tt> <tt>visitTypeArgument</tt>* (
 * <tt>visitInnerClassType</tt> <tt>visitTypeArgument</tt>* )*
 * <tt>visitEnd</tt> ) )</li> </ul>
 * 
 * @author Thomas Hallgren
 * @author Eric Bruneton
 */
public interface SignatureVisitor {

    /**
     * Wildcard for an "extends" type argument.
     */
    char EXTENDS = '+';

    /**
     * Wildcard for a "super" type argument.
     */
    char SUPER = '-';

    /**
     * Wildcard for a normal type argument.
     */
    char INSTANCEOF = '=';

    /**
     * Visits a formal type parameter.
     * 
     * @param name the name of the formal parameter.
     */
    void visitFormalTypeParameter(String name);

    /**
     * Visits the class bound of the last visited formal type parameter.
     * 
     * @return a non null visitor to visit the signature of the class bound.
     */
    SignatureVisitor visitClassBound();

    /**
     * Visits an interface bound of the last visited formal type parameter.
     * 
     * @return a non null visitor to visit the signature of the interface bound.
     */
    SignatureVisitor visitInterfaceBound();

    /**
     * Visits the type of the super class.
     * 
     * @return a non null visitor to visit the signature of the super class
     *         type.
     */
    SignatureVisitor visitSuperclass();

    /**
     * Visits the type of an interface implemented by the class.
     * 
     * @return a non null visitor to visit the signature of the interface type.
     */
    SignatureVisitor visitInterface();

    /**
     * Visits the type of a method parameter.
     * 
     * @return a non null visitor to visit the signature of the parameter type.
     */
    SignatureVisitor visitParameterType();

    /**
     * Visits the return type of the method.
     * 
     * @return a non null visitor to visit the signature of the return type.
     */
    SignatureVisitor visitReturnType();

    /**
     * Visits the type of a method exception.
     * 
     * @return a non null visitor to visit the signature of the exception type.
     */
    SignatureVisitor visitExceptionType();

    /**
     * Visits a signature corresponding to a primitive type.
     * 
     * @param descriptor the descriptor of the primitive type, or 'V' for
     *        <tt>void</tt>.
     */
    void visitBaseType(char descriptor);

    /**
     * Visits a signature corresponding to a type variable.
     * 
     * @param name the name of the type variable.
     */
    void visitTypeVariable(String name);

    /**
     * Visits a signature corresponding to an array type.
     * 
     * @return a non null visitor to visit the signature of the array element
     *         type.
     */
    SignatureVisitor visitArrayType();

    /**
     * Starts the visit of a signature corresponding to a class or interface
     * type.
     * 
     * @param name the internal name of the class or interface.
     */
    void visitClassType(String name);

    /**
     * Visits an inner class.
     * 
     * @param name the local name of the inner class in its enclosing class.
     */
    void visitInnerClassType(String name);

    /**
     * Visits an unbounded type argument of the last visited class or inner
     * class type.
     */
    void visitTypeArgument();

    /**
     * Visits a type argument of the last visited class or inner class type.
     * 
     * @param wildcard '+', '-' or '='.
     * @return a non null visitor to visit the signature of the type argument.
     */
    SignatureVisitor visitTypeArgument(char wildcard);

    /**
     * Ends the visit of a signature corresponding to a class or interface type.
     */
    void visitEnd();
}
