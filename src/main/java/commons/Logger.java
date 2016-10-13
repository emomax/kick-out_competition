package commons;

public class Logger {

    private static boolean isDebugLogOn = false;

    public static synchronized void setDebugLogOn(final boolean debugLogOn) {
        isDebugLogOn = debugLogOn;
    }

    public static void log(final String s) {
        System.out.println(s);
    }

    public static void logDebug(final String s) {
        if (isDebugLogOn) {
            System.out.println(s);
        }
    }
}
