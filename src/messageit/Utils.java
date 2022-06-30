package messageit;

import java.util.regex.Pattern;

public class Utils {
    
    public static boolean validIp(String ip) {
        // https://stackoverflow.com/questions/5284147/validating-ipv4-addresses-with-regexp
        return ip.equals("") || ip.equals(LOCALHOST) || Pattern.compile(
            "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"
        ).matcher(ip).find();
    }
    
    public static boolean validPort(int port) {
        return port > -1 && port < 10000;
    }
    
    public static boolean validName(String name) {
        return !name.equals("");
    }
    
    public static int randomPort() {
        return (int)(Math.random() * 10000);
    }
    
    public static final String LOCALHOST = "localhost";
}
