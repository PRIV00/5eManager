package main.assets;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GuiTools {
    public static String trim(String s) {
        return s.replaceAll("\\*\\*$", "").replaceAll("^\\*\\*", "");
    }

    public static String getCurrentTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        return dtf.format(LocalDateTime.now());
    }
}
