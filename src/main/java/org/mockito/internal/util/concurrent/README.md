This is a miniature implementation of a concurrent, lock-free (as in lock-less) hash map with weak keys where keys respect reference equality. Such a hash map removes entries containing collected keys by either:

1. Inline removal (entries that contain to collected keys are removed as a result of interaction with the map).
2. Implicit concurrent removal (entries that contain collected keys are removed by an external thread).
3. Explicit concurrent removal (explicit interaction with the map's reference queue).

As a wrapper around this `WeakConcurrentMap`, this package also contains a `DetachedThreadLocal` which describes a weak concurrent map where the current thread serves as a key of the map. Also, this package delivers a `WeakConcurrentSet` as a wrapper around a weak concurrent map.

This map does not implement the `java.util.Map` interface to simplify the implementation. Writes to the map might cause blocking if many threads write to the map concurrently (this is implied by the maps backing by a `ConcurrentHashMap` and a `ReferenceQueue`), the performance of the map is however significantly better than using a synchronized wrapper around a concurrent hash map.

The library is hosted on *Maven Central* and *JCenter*:

```xml
<dependency>
  <groupId>com.blogspot.mydailyjava</groupId>
  <artifactId>weak-lock-free</artifactId>
  <version>0.11</version>
</dependency>
```
