package ew.quilt.util.reflection.resolver;

import ew.quilt.util.reflection.wrapper.FieldWrapper;
import java.lang.reflect.Field;

public class FieldResolver extends MemberResolver<Field> {

    public FieldResolver(Class<?> clazz) {
        super(clazz);
    }

    public FieldResolver(String className) throws ClassNotFoundException {
        super(className);
    }

    @Override
    public Field resolveIndex(int index) throws IndexOutOfBoundsException, ReflectiveOperationException {
        return AccessUtil.setAccessible(clazz.getDeclaredFields()[index]);
    }

    @Override
    public Field resolveIndexSilent(int index) {
        try {
            return resolveIndex(index);
        } catch (IndexOutOfBoundsException | ReflectiveOperationException ignored) {
        }
        return null;
    }

    @Override
    public FieldWrapper resolveIndexWrapper(int index) {
        return new FieldWrapper(resolveIndexSilent(index));
    }

    public FieldWrapper resolveWrapper(String... names) {
        return new FieldWrapper(resolveSilent(names));
    }

    public Field resolveSilent(String... names) {
        try {
            return resolve(names);
        } catch (Exception ex) {
        }
        return null;
    }

    public Field resolve(String... names) throws NoSuchFieldException {
        ResolverQuery.Builder builder = ResolverQuery.builder();
        for (String name : names) {
            builder.with(name);
        }
        try {
            return (Field) super.resolve(builder.build());
        } catch (ReflectiveOperationException ex) {
            throw ((NoSuchFieldException) ex);
        }
    }

    @Override
    public Field resolveSilent(ResolverQuery... queries) {
        try {
            return resolve(queries);
        } catch (Exception ex) {
        }
        return null;
    }

    @Override
    public Field resolve(ResolverQuery... queries) throws NoSuchFieldException {
        try {
            return (Field) super.resolve(queries);
        } catch (ReflectiveOperationException ex) {
            throw ((NoSuchFieldException) ex);
        }
    }

    @Override
    protected Field resolveObject(ResolverQuery query) throws ReflectiveOperationException {
        if (query.getTypes() == null || query.getTypes().length == 0) {
            return AccessUtil.setAccessible(clazz.getDeclaredField(query.getName()));
        }
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getName().equals(query.getName())) {
                for (Class type : query.getTypes()) {
                    if (field.getType().equals(type)) {
                        return field;
                    }
                }
            }
        }
        return null;
    }

    public Field resolveByFirstType(Class<?> type) throws ReflectiveOperationException {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType().equals(type)) {
                return AccessUtil.setAccessible(field);
            }
        }
        throw new NoSuchFieldException("無法解析欄位類型 " + type.toString() + " 於類別 " + clazz);
    }

    public Field resolveByFirstTypeSilent(Class<?> type) {
        try {
            return resolveByFirstType(type);
        } catch (Exception ex) {
        }
        return null;
    }

    public Field resolveByLastType(Class<?> type) throws ReflectiveOperationException {
        Field field = null;
        for (Field field1 : clazz.getDeclaredFields()) {
            if (field1.getType().equals(type)) {
                field = field1;
            }
        }
        if (field == null) {
            throw new NoSuchFieldException("無法解析欄位類型 " + type.toString() + " 於類別 " + clazz);
        }
        return AccessUtil.setAccessible(field);
    }

    public Field resolveByLastTypeSilent(Class<?> type) {
        try {
            return resolveByLastType(type);
        } catch (Exception ex) {
        }
        return null;
    }

    @Override
    protected NoSuchFieldException notFoundException(String joinedNames) {
        return new NoSuchFieldException("無法解析欄位 " + joinedNames + " 於類別 " + clazz);
    }
}
