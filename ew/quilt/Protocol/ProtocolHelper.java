package ew.quilt.Protocol;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProtocolHelper {

    public static boolean isIPv4(String ipAddress) {
        String ip = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }
}
