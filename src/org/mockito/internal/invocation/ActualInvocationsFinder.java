/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.invocation;

import java.util.LinkedList;
import java.util.List;

import org.mockito.internal.progress.VerificationModeImpl;

public class ActualInvocationsFinder {

    private final Chunker chunker;
    
    public ActualInvocationsFinder() {
        this(new Chunker());
    }
    
    ActualInvocationsFinder(Chunker chunker) {
        this.chunker = chunker;
    }

    public List<Invocation> findInvocations(List<Invocation> invocations, InvocationMatcher wanted, VerificationModeImpl mode) {
        if (mode.strictMode()) {
            List<Invocation> unverified = new InvocationsAnalyzer().removeUntilLastStrictlyVerified(invocations);
            return strictlyMatching(unverified, wanted, mode); 
        } else {
            return nonStrictlyMatching(invocations, wanted);
        }
    }

    private List<Invocation> strictlyMatching(List<Invocation> invocations, InvocationMatcher wanted,
            VerificationModeImpl mode) {
        List<ObjectsChunk<Invocation>> chunks = chunker.chunk(invocations, new MatchesWantedSeer(wanted));
        List<Invocation> firstMatching = new LinkedList<Invocation>(); 
        for(ObjectsChunk<Invocation> chunk : chunks) {
            boolean wantedMatchesActual = wanted.matches(chunk.getObject());
            if (!wantedMatchesActual) {
                continue;
            }
            
            if (firstMatching.isEmpty()) {
                firstMatching.addAll(chunk.getObjects());
            }
                
            boolean chunkSizeMatches = mode.matchesActualCount(chunk.getSize());
            if (chunkSizeMatches) {
                return chunk.getObjects();
            }
        }
        return firstMatching;
    }

    private List<Invocation> nonStrictlyMatching(List<Invocation> invocations, InvocationMatcher wanted) {
        List<Invocation> actual = new LinkedList<Invocation>();
        for (Invocation i : invocations) {
            if (wanted.matches(i)) {
                actual.add(i);
            }
        }
        return actual;
    }
    
    private final class MatchesWantedSeer implements Chunker.ChunkSeer<Invocation> {
        private final InvocationMatcher wanted;

        public MatchesWantedSeer(InvocationMatcher wanted) {
            this.wanted = wanted;
        }
        
        public boolean isSameChunk(Invocation previous, Invocation current) {
            return wanted.matches(previous) && wanted.matches(current);
        }
    }
}