package spacerace.client.communication;

import spacerace.domain.Action;
import spacerace.server.communication.response.ServerResponse;

public interface ServerAdapter {

    ServerResponse registerPlayer();

    ServerResponse getGameState();

    ServerResponse postActionToServer(final Action action);

    ServerResponse sendStartCommand();

    ServerResponse getGameResult();
}
