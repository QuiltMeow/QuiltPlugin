package ew.quilt.util.reflection.resolver;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ResolverAbstract<T> {

    protected final Map<ResolverQuery, T> resolvedObjects = new ConcurrentHashMap<>();

    protected T resolveSilent(ResolverQuery... queries) {
        try {
            return (T) resolve(queries);
        } catch (Exception ex) {
        }
        return null;
    }

    protected T resolve(ResolverQuery... queries) throws ReflectiveOperationException {
        if (queries == null || queries.length <= 0) {
            throw new IllegalArgumentException("查詢參數為空");
        }
        for (ResolverQuery query : queries) {
            if (resolvedObjects.containsKey(query)) {
                return (T) resolvedObjects.get(query);
            }
            try {
                T resolved = resolveObject(query);

                resolvedObjects.put(query, resolved);
                return resolved;
            } catch (ReflectiveOperationException ex) {
            }
        }
        throw notFoundException(Arrays.asList(queries).toString());
    }

    protected abstract T resolveObject(ResolverQuery paramResolverQuery) throws ReflectiveOperationException;

    protected ReflectiveOperationException notFoundException(String joinedNames) {
        return new ReflectiveOperationException("無法解析物件 " + joinedNames);
    }
}
