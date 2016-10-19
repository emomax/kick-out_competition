package commons.gameengine;

import commons.network.server.Parameterable;

public interface Action<T extends Parameterable> {
    T get();
    String getMethod();
}
