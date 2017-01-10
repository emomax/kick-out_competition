package spacerace.client;

import spacerace.domain.Action;
import spacerace.server.response.ServerResponse;

public interface ServerAdapter {

    ServerResponse registerPlayer();

    ServerResponse getGameState();

    ServerResponse postActionToServer(final Action action);

    ServerResponse sendStartCommand();
}
