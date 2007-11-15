/*
 * Copyright (c) 2001-2007 OFFIS, Tammo Freese.
 * This program is made available under the terms of the MIT License.
 */
package org.easymock.internal;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Results {

    private int callCount;

    private LinkedList<Range> ranges = new LinkedList<Range>();

    private List<Result> results = new ArrayList<Result>();

    public void add(Result result, Range range) {
        if (!ranges.isEmpty()) {
            Range lastRange = ranges.getLast();
            if (!lastRange.hasFixedCount())
                throw new RuntimeExceptionWrapper(
                        new IllegalStateException(
                                "last method called on mock already has a non-fixed count set."));
        }
        ranges.add(range);
        results.add(result);
    }

    public Result next() {
        int currentPosition = 0;
        for (int i = 0; i < ranges.size(); i++) {
            Range interval = ranges.get(i);
            if (interval.hasOpenCount()) {
                callCount += 1;
                return results.get(i);
            }
            currentPosition += interval.getMaximum();
            if (currentPosition > callCount) {
                callCount += 1;
                return results.get(i);
            }
        }
        return null;
    }

    public boolean hasValidCallCount() {
        return getMainInterval().contains(getCallCount());
    }

    @Override
    public String toString() {
        return getMainInterval().expectedAndActual(getCallCount());
    }

    private Range getMainInterval() {
        int min = 0, max = 0;

        for (Range interval : ranges) {
            min += interval.getMinimum();
            if (interval.hasOpenCount() || max == Integer.MAX_VALUE) {
                max = Integer.MAX_VALUE;
            } else {
                max += interval.getMaximum();
            }
        }

        return new Range(min, max);
    }

    public int getCallCount() {
        return callCount;
    }
}
