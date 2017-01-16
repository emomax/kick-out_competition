package spacerace.client.communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import spacerace.domain.Action;
import spacerace.server.response.ServerResponse;
import spacerace.server.socket.SocketRequest;

import static spacerace.server.socket.SpaceRaceSocketServer.PORT;

public class SocketServerAdapter implements ServerAdapter {

    private final String             playerName;
    private final String             gameName;
    private final int                levelNumber;
    private       Socket             socket;
    private       ObjectOutputStream outputStream;
    private       ObjectInputStream  inputStream;

    public SocketServerAdapter(final String serverIP, final String playerName, final String gameName, final int levelNumber) {
        this.playerName = playerName;
        this.gameName = gameName;
        this.levelNumber = levelNumber;

        connectToServer(serverIP);
    }

    private void connectToServer(final String serverIP) {
        try {
            socket = new Socket(serverIP, PORT);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        }
        catch (final IOException e) {
            throw new IllegalArgumentException("Exception when connecting to " + serverIP, e);
        }
    }

    public void disconnect() {
        try {
            socket.close();
        }
        catch (final IOException e) {
            throw new IllegalArgumentException("Exception when disconnecting from server", e);
        }
    }

    @Override
    public ServerResponse registerPlayer() {
        final SocketRequest request = new SocketRequest(SocketRequest.Type.REGISTER_PLAYER);
        request.setGameName(gameName);
        request.setPlayerName(playerName);
        request.setLevelNumber(levelNumber);
        return sendRequest(request);
    }

    @Override
    public ServerResponse getGameState() {
        final SocketRequest request = new SocketRequest(SocketRequest.Type.GET_GAME_STATE);
        request.setGameName(gameName);
        return sendRequest(request);
    }

    @Override
    public ServerResponse postActionToServer(final Action action) {
        final SocketRequest request = new SocketRequest(SocketRequest.Type.POST_ACTION);
        request.setGameName(gameName);
        request.setPlayerName(playerName);
        request.setAccelerationX(action.getAccelerationX().toString());
        request.setAccelerationY(action.getAccelerationY().toString());
        request.setStabilize(action.isStabilize());
        return sendRequest(request);
    }

    @Override
    public ServerResponse sendStartCommand() {
        final SocketRequest request = new SocketRequest(SocketRequest.Type.SEND_START_COMMAND);
        request.setGameName(gameName);
        return sendRequest(request);
    }

    @Override
    public ServerResponse getGameResult() {
        final SocketRequest request = new SocketRequest(SocketRequest.Type.GET_GAME_RESULT);
        request.setGameName(gameName);
        final ServerResponse response = sendRequest(request);

        // Not too pretty to have here but what the heck...
        disconnect();
        return response;
    }

    private ServerResponse sendRequest(final SocketRequest request) {
        try {
            outputStream.writeObject(request);
            return (ServerResponse) inputStream.readObject();
        }
        catch (final IOException | ClassNotFoundException ex) {
            disconnect();
            throw new IllegalStateException(ex);
        }
    }
}
