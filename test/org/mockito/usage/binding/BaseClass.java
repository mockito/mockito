package org.mockito.usage.binding;

public class BaseClass<M extends BaseMessage, D extends IBaseInteface<M>> {

	private final D object;

	BaseClass(D object) {
		this.object = object;
	}

	public void print(M message) {
		object.print(message);
	}
}
