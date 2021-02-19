package github.geooo.extension;

/**
 * @author zhaoqi.wang
 * @date 2021/2/19
 */
public class Holder<T> {

    private volatile T value;

    public T get() {
        return value;
    }

    public void set(T val) {
        this.value = val;
    }
}
