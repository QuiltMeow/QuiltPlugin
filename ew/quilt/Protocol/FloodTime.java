package ew.quilt.Protocol;

public class FloodTime implements Runnable {

    public int time;

    public FloodTime(int time) {
        this.time = time;
        Flood.setFlooding(true);
    }

    @Override
    public void run() {
        try {
            Thread.sleep(time * 1000);
        } catch (Exception ex) {
        }
        Flood.setFlooding(false);
    }
}
