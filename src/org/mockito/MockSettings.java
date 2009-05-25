/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import org.mockito.stubbing.Answer;

/**
 * @author SG0897539
 *
 */
public interface MockSettings {
    //TODO: validate javadoc
    
    /**
     * Specifies extra interfaces the mock should implement. Might be useful for legacy code or some corner cases.
     * For background, see issue 51 <a href="http://code.google.com/p/mockito/issues/detail?id=51">here</a>  
     * <p>
     * This mysterious feature should be used very occasionally. 
     * The object under test should know exactly its collaborators & dependencies.
     * If you happen to use it often than please make sure you are really producing simple, clean & readable code.
     * <p>   
     * Examples:
     * <pre>
     *   Foo foo = mock(Foo.class, withSettings().extraInterfaces(Bar.class, Baz.class));
     *   
     *   //now, the mock implements extra interfaces, so following casting is possible:
     *   Bar bar = (Bar) foo;
     *   Baz baz = (Baz) foo;
     * </pre>
     * 
     * 
     * @param interfaces extra interfaces the should implement.
     * @return settings instance so that you can fluently specify other settings 
     */
    MockSettings extraInterfaces(Class<?>... interfaces);

    /**
     * Specifies mock name. Naming mocks can be helpful for debugging - the name is used in all verification errors. 
     * <p>
     * Beware that naming mocks is not a solution for complex code which uses too many mocks or collaborators. 
     * <b>If you have too many mocks then refactor the code</b> so that it's easy to test/debug without necessity of naming mocks.
     * <p>
     * <b>If you use &#064;Mock annotation then you've got naming mocks for free!</b> &#064;Mock uses field name as mock name. {@link Mock Read more.}
     * <p>
     * Examples:
     * <pre>
     *   Foo foo = mock(Foo.class, withSettings().name("foo"));
     *   
     *   //Below does exactly the same:
     *   Foo foo = mock(Foo.class, "foo");
     * </pre>
     * @param name the name of the mock, later used in all verification errors
     * @return settings instance so that you can fluently specify other settings
     */
    MockSettings name(String name);

    MockSettings spiedInstance(Object object);

    @SuppressWarnings("unchecked")
    //it's ok to supress it because having raw Answer here it makes nicer for clients 
    MockSettings defaultAnswer(Answer defaultAnswer);
    
}