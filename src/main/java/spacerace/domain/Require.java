package spacerace.domain;

public class Require {

    public static void notNull(final Object o) {
        notNull(o, "Argument was null");
    }

    public static void notNull(final Object o, final String message) {
        if (o == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(final Object... o) {
        final int i = 0;
        for (final Object object : o) {
            if (object == null) {
                throw new IllegalArgumentException("Argument " + i + " was null");
            }
        }
    }

    public static void that(final boolean b) {
        if (!b) {
            throw new IllegalArgumentException("Argument must be true");
        }
    }

    public static void that(final boolean b, final String message) {
        if (!b) {
            throw new IllegalArgumentException(message);
        }
    }
}
