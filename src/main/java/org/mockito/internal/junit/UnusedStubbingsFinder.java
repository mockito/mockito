package org.mockito.internal.junit;

import org.mockito.internal.invocation.Stubbing;
import org.mockito.internal.invocation.finder.AllInvocationsFinder;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.invocation.Invocation;

import java.util.*;

/**
 * Finds unused stubbings
 */
public class UnusedStubbingsFinder {
    public UnusedStubbings getUnusedStubbings(Iterable<Object> mocks) {
        Collection<Stubbing> stubbings = (Collection) AllInvocationsFinder.findStubbings(mocks);

        LinkedList<Stubbing> unused = ListUtil.filter(stubbings, new ListUtil.Filter<Stubbing>() {
            public boolean isOut(Stubbing s) {
                return s.wasUsed();
            }
        });

        return new UnusedStubbings(unused);
    }

    public Collection<Invocation> getUnusedStubbingsByLocation(Iterable<Object> mocks) {
        Collection<Stubbing> stubbings = (Collection) AllInvocationsFinder.findStubbings(mocks);

        //1st pass, collect all the locations of the stubbings that were used
        //note that those are _not_ locations where the stubbings was used
        Set<String> locationsOfUsedStubbings = new HashSet<String>();
        for (Stubbing s : stubbings) {
            if (s.wasUsed()) {
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
