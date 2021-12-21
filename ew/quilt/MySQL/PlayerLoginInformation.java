package ew.quilt.MySQL;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.UUID;

public class PlayerLoginInformation {

    private final String uuid;
    private final long id;
    private final String name, ip;
    private final Timestamp lastLogin, lastLogout;

    public PlayerLoginInformation(long id, String uuid, String name, String ip, Timestamp lastLogin, Timestamp lastLogout) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.ip = ip;
        this.lastLogin = lastLogin;
        this.lastLogout = lastLogout;
    }

    public long getLastLogin() {
        return lastLogin.getTime();
    }

    public long getLastLogout() {
        if (lastLogout == null) {
            return -1;
        }
        return lastLogout.getTime();
    }

    public long getLastOnlineTime() {
        if (lastLogout == null) {
            return -1;
        }
        return getLastLogout() - getLastLogin();
    }

    public long getId() {
        return id;
    }

    public String getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getIPAddress() {
        return ip;
    }

    public UUID getUUIDObject() {
        return UUID.fromString(uuid);
    }

    public InetAddress getIPAddressObject() {
        try {
            return InetAddress.getByName(ip);
        } catch (UnknownHostException ex) {
            return null;
        }
    }
}
