package spacerace.client.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import spacerace.domain.Action;
import spacerace.server.communication.response.ServerResponse;
import spacerace.server.communication.socket.SocketRequest;
import spacerace.server.communication.socket.SocketRequestType;
import spacerace.server.communication.socket.SpaceRaceSocketServer;

public class SocketServerAdapter implements ServerAdapter {

    private final String         playerName;
    private final String         gameName;
    private final Integer        levelNumber;
    private       Socket         socket;
    private       BufferedReader in;
    private       PrintWriter    out;

    public SocketServerAdapter(final String serverIP, final String playerName, final String gameName) {
        this(serverIP, playerName, gameName, null);
    }

    public SocketServerAdapter(final String serverIP, final String playerName, final String gameName, final Integer levelNumber) {
        this.playerName = playerName;
        this.gameName = gameName;
        this.levelNumber = levelNumber;

        connectToServer(serverIP);
    }

    private void connectToServer(final String serverIP) {
        try {
            socket = new Socket(serverIP, SpaceRaceSocketServer.PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        }
        catch (final IOException e) {
            throw new IllegalArgumentException("Exception when connecting to " + serverIP, e);
        }
    }

    private void disconnect() {
        try {
            socket.close();
            System.out.println("Disconnected from server");
        }
        catch (final IOException e) {
            throw new IllegalArgumentException("Exception when disconnecting from server", e);
        }
    }

    @Override
    public ServerResponse registerPlayer() {
        final SocketRequest request = new SocketRequest();
        request.setType(SocketRequestType.REGISTER_PLAYER);
        request.setGameName(gameName);
        request.setPlayerName(playerName);
        request.setLevelNumber(levelNumber);
        return sendRequest(request);
    }

    @Override
    public ServerResponse getGameState() {
        final SocketRequest request = new SocketRequest();
        request.setType(SocketRequestType.GET_GAME_STATE);
        request.setGameName(gameName);
        return sendRequest(request);
    }

    @Override
    public ServerResponse getGameStateForViewing() {
        final SocketRequest request = new SocketRequest();
        request.setType(SocketRequestType.GET_GAME_STATE_FOR_VIEWING);
        request.setGameName(gameName);
        return sendRequest(request);
    }

    @Override
    public ServerResponse postActionToServer(final Action action) {
        final SocketRequest request = new SocketRequest();
        request.setType(SocketRequestType.POST_ACTION);
        request.setGameName(gameName);
        request.setPlayerName(playerName);
        request.setAccelerationX(action.getAccelerationX());
        request.setAccelerationY(action.getAccelerationY());
        request.setStabilize(action.isStabilize());
        return sendRequest(request);
    }

    @Override
    public ServerResponse sendStartCommand() {
        final SocketRequest request = new SocketRequest();
        request.setType(SocketRequestType.SEND_START_COMMAND);
        request.setGameName(gameName);
        return sendRequest(request);
    }

    @Override
    public ServerResponse getGameResult() {
        final SocketRequest request = new SocketRequest();
        request.setType(SocketRequestType.GET_GAME_RESULT);
        request.setGameName(gameName);
        final ServerResponse response = sendRequest(request);

        // Not too pretty to have here but what the heck...
        System.out.println("Game result received, disconnecting from server... ");
        disconnect();
        return response;
    }

    private ServerResponse sendRequest(final SocketRequest request) {
        final ObjectMapper objectMapper = new ObjectMapper();
        sendRequest(request, objectMapper);
        return getResponse(objectMapper);
    }

    private void sendRequest(final SocketRequest request, final ObjectMapper objectMapper) {
        final String requestString;
        try {
            requestString = objectMapper.writeValueAsString(request);
        }
        catch (final JsonProcessingException e) {
            throw new IllegalArgumentException("Unable to map object to JSON", e);
        }
        out.println(requestString);
    }

    private ServerResponse getResponse(final ObjectMapper objectMapper) {
        try {
            final String response = in.readLine();
            if (response == null) {
                throw new IllegalArgumentException("Server response was null");
            }
            return objectMapper.readValue(response, ServerResponse.class);
        }
        catch (final IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
