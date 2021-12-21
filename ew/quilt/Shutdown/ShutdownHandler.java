package ew.quilt.Shutdown;

public class ShutdownHandler extends Thread {

    @Override
    public void run() {
        System.out.println("Quilt Plugin 最終檢查 ...");
        System.out.println("Quilt Plugin 已完全關閉");
    }
}
