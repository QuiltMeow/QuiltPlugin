package ew.quilt.util.reflection.resolver;

import ew.quilt.util.reflection.wrapper.WrapperAbstract;
import java.lang.reflect.Member;

public abstract class MemberResolver<T extends Member> extends ResolverAbstract<T> {

    protected Class<?> clazz;

    public MemberResolver(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("類別不能為空");
        }
        this.clazz = clazz;
    }

    public MemberResolver(String className) throws ClassNotFoundException {
        this(new ClassResolver().resolve(new String[]{className}));
    }

    public abstract T resolveIndex(int paramInt) throws IndexOutOfBoundsException, ReflectiveOperationException;

    public abstract T resolveIndexSilent(int paramInt);

    public abstract WrapperAbstract resolveIndexWrapper(int paramInt);
}
