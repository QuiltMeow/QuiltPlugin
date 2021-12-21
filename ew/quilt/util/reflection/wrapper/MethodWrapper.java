package ew.quilt.util.reflection.wrapper;

import java.lang.reflect.Method;

public class MethodWrapper<R> extends WrapperAbstract {

    private final Method method;

    public MethodWrapper(Method method) {
        this.method = method;
    }

    @Override
    public boolean exists() {
        return method != null;
    }

    public String getName() {
        return method.getName();
    }

    public R invoke(Object object, Object... args) {
        try {
            return (R) method.invoke(object, args);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public R invokeSilent(Object object, Object... args) {
        try {
            return (R) method.invoke(object, args);
        } catch (Exception ex) {
        }
        return null;
    }

    public Method getMethod() {
        return method;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        MethodWrapper<?> that = (MethodWrapper) object;

        return that.method == null ? true : method != null ? method.equals(that.method) : false;
    }

    @Override
    public int hashCode() {
        return method != null ? method.hashCode() : 0;
    }
}
