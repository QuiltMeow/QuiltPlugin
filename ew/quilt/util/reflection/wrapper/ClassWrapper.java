package ew.quilt.util.reflection.wrapper;

public class ClassWrapper<R> extends WrapperAbstract {

    private final Class<R> classs;

    public ClassWrapper(Class<R> clazz) {
        this.classs = clazz;
    }

    @Override
    public boolean exists() {
        return classs != null;
    }

    public Class<R> getClazz() {
        return classs;
    }

    public String getName() {
        return classs.getName();
    }

    public R newInstance() {
        try {
            return (R) classs.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public R newInstanceSilent() {
        try {
            return (R) classs.newInstance();
        } catch (Exception ex) {
        }
        return null;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        ClassWrapper<?> that = (ClassWrapper) object;

        return that.classs == null ? true : classs != null ? classs.equals(that.classs) : false;
    }

    @Override
    public int hashCode() {
        return classs != null ? classs.hashCode() : 0;
    }
}
