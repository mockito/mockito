package org.mockito.release.util

import spock.lang.Specification

class MultiMapTest extends Specification {

    def "empty multi map"() {
        def map = new MultiMap<String, String>()

        expect:
        map.size() == 0
        map.keySet().empty
    }

    def "contains multiple values per key"() {
        def map = new MultiMap<String, String>();
        map.put("1", "x")
        map.put("1", "y")
        map.put("2", "z")

        expect:
        map.get("1") == ["x", "y"] as Set
        map.get("2") == ["z"] as Set
        map.size() == 2
        map.keySet() == ["1", "2"] as Set

        //questionable but I decided to keep the impl simple:
        map.get("3") == null
    }
}
