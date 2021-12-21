package ew.quilt.Protocol;

public abstract class Flood implements Runnable {

    public static final int THREAD_COUNT = 5;

    private static boolean isFlooding = false;

    public static final boolean isFlooding() {
        return isFlooding;
    }

    public static final void setFlooding(boolean flooding) {
        isFlooding = flooding;
    }

    private final String target;
    private final int port;

    public Flood(String target, int port) {
        this.target = target;
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public String getTarget() {
        return target;
    }

    public static final void begin(int second, Flood flood) {
        new Thread(new FloodTime(second)).start();
        for (int i = 0; i < THREAD_COUNT; ++i) {
            new Thread(flood).start();
        }
    }
}
