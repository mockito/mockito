package org.mockito.stubbing;

public class Times {
	private int numberOfInvocations;

	private Times(int numberOfInvocations) {
		this.numberOfInvocations = numberOfInvocations;
	}
	
	public static Times times(int numberOfInvocations){
		return new Times(numberOfInvocations);
	}

	public int getNumberOfInvocations() {
		return numberOfInvocations;
	}
}
