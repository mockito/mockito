/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration.injection.filter;

import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilter implements MockCandidateFilter {
	private final MockCandidateFilter next;
	private final MockUtil mockUtil = new MockUtil();

	public NameBasedCandidateFilter(MockCandidateFilter next) {
		this.next = next;
	}

	public OngoingInjecter filterCandidate(Collection<Object> mocks,
			Field field, List<Field> fields, Object fieldInstance) {
		List<Object> mockNameMatches = new ArrayList<Object>();
		if (mocks.size() > 1) {
			for (Object mock : mocks) {
				if (field.getName().equals(mockUtil.getMockName(mock).toString())) {
					mockNameMatches.add(mock);
				}
			}
			return next.filterCandidate(mockNameMatches, field, fields,
					fieldInstance);
			/*
			 * In this case we have to check whether we have conflicting naming
			 * fields. E.g. 2 fields of the same type, but we have to make sure
			 * we match on the correct name.
			 * 
			 * Therefore we have to go through all other fields and make sure
			 * whenever we find a field that does match its name with the mock
			 * name, we should take that field instead.
			 */
		} else if (mocks.size() == 1) {
			String mockName = mockUtil.getMockName(mocks.iterator().next())
					.toString();

			for (Field otherField : fields) {
				if (!otherField.equals(field)
						&& otherField.getType().equals(field.getType())
						&& otherField.getName().equals(mockName)) {

					return new OngoingInjecter() {
						public Object thenInject() {
							return null;
						}
					};
				}
			}
		}
		return next.filterCandidate(mocks, field, fields, fieldInstance);
	}
}
