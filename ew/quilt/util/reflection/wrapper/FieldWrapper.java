package ew.quilt.util.reflection.wrapper;

import java.lang.reflect.Field;

public class FieldWrapper<R> extends WrapperAbstract {

    private final Field field;

    public FieldWrapper(Field field) {
        this.field = field;
    }

    @Override
    public boolean exists() {
        return field != null;
    }

    public String getName() {
        return field.getName();
    }

    public R get(Object object) {
        try {
            return (R) field.get(object);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public R getSilent(Object object) {
        try {
            return (R) field.get(object);
        } catch (Exception ex) {
        }
        return null;
    }

    public void set(Object object, R value) {
        try {
            field.set(object, value);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void setSilent(Object object, R value) {
        try {
            field.set(object, value);
        } catch (Exception ex) {
        }
    }

    public Field getField() {
        return field;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        FieldWrapper<?> that = (FieldWrapper) object;
        if (field != null ? !field.equals(that.field) : that.field != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return field != null ? field.hashCode() : 0;
    }
}
