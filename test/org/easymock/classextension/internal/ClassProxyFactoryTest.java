package org.easymock.classextension.internal;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.lang.reflect.Method;

import org.easymock.internal.ClassProxyFactory;
import org.junit.*;
import org.mockito.MockAwareInvocationHandler;

public class ClassProxyFactoryTest {

	@Test public void shouldNotRewriteObjectMethodsForInterface() throws Exception {
		ClassProxyFactory<SomeInterface> factory = new ClassProxyFactory<SomeInterface>();
		SomeInterface proxy = null;
		try {
			proxy = factory.createProxy(SomeInterface.class, new MockAwareInvocationHandler() {
                public void setMock(Object mock) {}
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    return null;
                }});
		} catch (RuntimeException e) {
			fail("should not lookup Object methods since they are not available on interfaces");
		}
		
		assertThat(proxy, notNullValue());
	}
	
	private interface SomeInterface {};
}
