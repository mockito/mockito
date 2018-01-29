/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.junit;

import org.mockito.internal.invocation.finder.AllInvocationsFinder;
import org.mockito.internal.stubbing.UnusedStubbingReporting;
import org.mockito.internal.util.collections.ListUtil.Filter;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.Stubbing;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.internal.util.collections.ListUtil.filter;

/**
 * Finds unused stubbings
 */
public class UnusedStubbingsFinder {

    /**
     * Gets all unused stubbings for given set of mock objects, in order.
     * Stubbings explicitily marked as LENIENT are not included.
     */
    public UnusedStubbings getUnusedStubbings(Iterable<Object> mocks) {
        Set<Stubbing> stubbings = AllInvocationsFinder.findStubbings(mocks);

        List<Stubbing> unused = filter(stubbings, new Filter<Stubbing>() {
            public boolean isOut(Stubbing s) {
                return !UnusedStubbingReporting.shouldBeReported(s);
            }
        });

        return new UnusedStubbings(unused);
    }

    /**
     * Gets unused stubbings per location. This method is less accurate than {@link #getUnusedStubbings(Iterable)}.
     * It considers that stubbings with the same location (e.g. ClassFile + line number) are the same.
     * This is not completely accurate because a stubbing declared in a setup or constructor
     * is created per each test method. Because those are different test methods,
     * different mocks are created, different 'Invocation' instance is backing the 'Stubbing' instance.
     * In certain scenarios (detecting unused stubbings by JUnit runner), we need this exact level of accuracy.
     * Stubbing declared in constructor but realized in % of test methods is considered as 'used' stubbing.
     * There are high level unit tests that demonstrate this scenario.
     */
    public Collection<Invocation> getUnusedStubbingsByLocation(Iterable<Object> mocks) {
        Set<Stubbing> stubbings = AllInvocationsFinder.findStubbings(mocks);

        //1st pass, collect all the locations of the stubbings that were used
        //note that those are _not_ locations where the stubbings was used
        Set<String> locationsOfUsedStubbings = new HashSet<String>();
        for (Stubbing s : stubbings) {
            if (!UnusedStubbingReporting.shouldBeReported(s)) {
                String location = s.getInvocation().getLocation().toString();
                locationsOfUsedStubbings.add(location);
            }
        }

        //2nd pass, collect unused stubbings by location
        //If the location matches we assume the stubbing was used in at least one test method
        //Also, using map to deduplicate reported unused stubbings
        // if unused stubbing appear in the setup method / constructor we don't want to report it per each test case
        Map<String, Invocation> out = new LinkedHashMap<String, Invocation>();
        for (Stubbing s : stubbings) {
            String location = s.getInvocation().getLocation().toString();
            if (!locationsOfUsedStubbings.contains(location)) {
                out.put(location, s.getInvocation());
            }
        }

        return out.values();
    }
}
