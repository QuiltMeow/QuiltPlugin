package ew.quilt.util.reflection.resolver;

import ew.quilt.util.reflection.wrapper.ClassWrapper;

public class ClassResolver extends ResolverAbstract<Class> {

    public ClassWrapper resolveWrapper(String... names) {
        return new ClassWrapper(resolveSilent(names));
    }

    public Class resolveSilent(String... names) {
        try {
            return resolve(names);
        } catch (Exception ex) {
        }
        return null;
    }

    public Class resolve(String... names) throws ClassNotFoundException {
        ResolverQuery.Builder builder = ResolverQuery.builder();
        for (String name : names) {
            builder.with(name);
        }
        try {
            return (Class) super.resolve(builder.build());
        } catch (ReflectiveOperationException ex) {
            throw ((ClassNotFoundException) ex);
        }
    }

    @Override
    protected Class resolveObject(ResolverQuery query) throws ReflectiveOperationException {
        return Class.forName(query.getName());
    }

    @Override
    protected ClassNotFoundException notFoundException(String joinedNames) {
        return new ClassNotFoundException("無法解析類別 " + joinedNames);
    }
}
