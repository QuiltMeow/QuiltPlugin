package ew.quilt.util.reflection.resolver;

import ew.quilt.util.reflection.wrapper.ConstructorWrapper;
import java.lang.reflect.Constructor;

public class ConstructorResolver extends MemberResolver<Constructor> {

    public ConstructorResolver(Class<?> clazz) {
        super(clazz);
    }

    public ConstructorResolver(String className) throws ClassNotFoundException {
        super(className);
    }

    @Override
    public Constructor resolveIndex(int index) throws IndexOutOfBoundsException, ReflectiveOperationException {
        return AccessUtil.setAccessible(clazz.getDeclaredConstructors()[index]);
    }

    @Override
    public Constructor resolveIndexSilent(int index) {
        try {
            return resolveIndex(index);
        } catch (IndexOutOfBoundsException | ReflectiveOperationException ignored) {
        }
        return null;
    }

    @Override
    public ConstructorWrapper resolveIndexWrapper(int index) {
        return new ConstructorWrapper(resolveIndexSilent(index));
    }

    public ConstructorWrapper resolveWrapper(Class<?>[]... types) {
        return new ConstructorWrapper(resolveSilent(types));
    }

    public Constructor resolveSilent(Class<?>[]... types) {
        try {
            return resolve(types);
        } catch (Exception ex) {
        }
        return null;
    }

    public Constructor resolve(Class<?>[]... types) throws NoSuchMethodException {
        ResolverQuery.Builder builder = ResolverQuery.builder();
        for (Class<?>[] type : types) {
            builder.with(type);
        }
        try {
            return (Constructor) super.resolve(builder.build());
        } catch (ReflectiveOperationException ex) {
            throw ((NoSuchMethodException) ex);
        }
    }

    @Override
    protected Constructor resolveObject(ResolverQuery query) throws ReflectiveOperationException {
        return AccessUtil.setAccessible(clazz.getDeclaredConstructor(query.getTypes()));
    }

    public Constructor resolveFirstConstructor() throws ReflectiveOperationException {
        Constructor[] arrayOfConstructor = clazz.getDeclaredConstructors();
        int i = arrayOfConstructor.length;
        int j = 0;
        if (j < i) {
            Constructor constructor = arrayOfConstructor[j];
            return AccessUtil.setAccessible(constructor);
        }
        return null;
    }

    public Constructor resolveFirstConstructorSilent() {
        try {
            return resolveFirstConstructor();
        } catch (Exception ex) {
        }
        return null;
    }

    public Constructor resolveLastConstructor() throws ReflectiveOperationException {
        Constructor constructor = null;
        for (Constructor constructor1 : clazz.getDeclaredConstructors()) {
            constructor = constructor1;
        }
        if (constructor != null) {
            return AccessUtil.setAccessible(constructor);
        }
        return null;
    }

    public Constructor resolveLastConstructorSilent() {
        try {
            return resolveLastConstructor();
        } catch (Exception ex) {
        }
        return null;
    }

    @Override
    protected NoSuchMethodException notFoundException(String joinedNames) {
        return new NoSuchMethodException("無法解析建構子 " + joinedNames + " 於類別 " + clazz);
    }
}
