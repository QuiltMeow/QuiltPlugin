package ew.quilt.util.reflection.wrapper;

import java.lang.reflect.Constructor;

public class ConstructorWrapper<R> extends WrapperAbstract {

    private final Constructor<R> constructor;

    public ConstructorWrapper(Constructor<R> constructor) {
        this.constructor = constructor;
    }

    @Override
    public boolean exists() {
        return constructor != null;
    }

    public R newInstance(Object... args) {
        try {
            return (R) constructor.newInstance(args);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public R newInstanceSilent(Object... args) {
        try {
            return (R) constructor.newInstance(args);
        } catch (Exception ex) {
        }
        return null;
    }

    public Class<?>[] getParameterTypes() {
        return constructor.getParameterTypes();
    }

    public Constructor<R> getConstructor() {
        return constructor;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        ConstructorWrapper<?> that = (ConstructorWrapper) object;

        return that.constructor == null ? true : constructor != null ? constructor.equals(that.constructor) : false;
    }

    @Override
    public int hashCode() {
        return constructor != null ? constructor.hashCode() : 0;
    }
}
