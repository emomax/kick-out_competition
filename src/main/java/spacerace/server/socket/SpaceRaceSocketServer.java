package spacerace.server.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.springframework.stereotype.Component;

import spacerace.server.SpaceRaceRestController;
import spacerace.server.response.ServerResponse;

@Component
public class SpaceRaceSocketServer {

    public static final int                     PORT           = 9090;
    private final       SpaceRaceRestController restController = new SpaceRaceRestController();

    public SpaceRaceSocketServer() {
        // We need to run this async so that server start completes (otherwise we hog the main startup thread).
        // Probably a better way to do this with Spring functionality...
        new Thread(this::startListeningOnClientConnectAttempts).start();
    }

    private void startListeningOnClientConnectAttempts() {
        System.out.println("Socket server is running on port " + PORT);

        int clientNumber = 0;
        try (ServerSocket listener = new ServerSocket(PORT)) {
            while (true) {
                final Socket clientSocket = listener.accept();
                clientNumber++;
                final ClientConnection clientConnection = new ClientConnection(clientSocket, clientNumber, restController);
                new Thread(clientConnection).start();
            }
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientConnection implements Runnable {
        private final Socket                  socket;
        private final int                     clientNumber;
        private final SpaceRaceRestController restController;

        private ClientConnection(final Socket socket, final int clientNumber, final SpaceRaceRestController restController) {
            this.socket = socket;
            this.clientNumber = clientNumber;
            this.restController = restController;
        }

        public void run() {
            try (
                    final ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                    final ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream())
            ) {
                System.out.println("Client " + clientNumber + " connected at " + socket);

                SocketRequest request;
                while ((request = (SocketRequest) inputStream.readObject()) != null) {
                    final ServerResponse response = getServerResponse(request);
                    outputStream.writeObject(response);

                    // When we have returned the game result, we should close the socket so that we dont recieve an EOF exception
                }
            }
            catch (final IOException e) {
                System.out.println("Error handling client " + clientNumber + ": " + e);
                e.printStackTrace();
            }
            catch (final ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                    System.out.println("Connection to client " + clientNumber + " closed");
                }
                catch (final IOException e) {
                    System.out.println("Failed to close socket to client " + clientNumber);
                    e.printStackTrace();
                }
            }
        }

        private ServerResponse getServerResponse(final SocketRequest request) {
            if (request.getType() == SocketRequest.Type.REGISTER_PLAYER) {
                return restController.registerPlayer(request.getGameName(),
                                                     request.getPlayerName(),
                                                     request.getLevelNumber());
            }
            else if (request.getType() == SocketRequest.Type.GET_GAME_STATE) {
                return restController.getGameState(request.getGameName());
            }
            else if (request.getType() == SocketRequest.Type.POST_ACTION) {
                return restController.action(request.getGameName(),
                                             request.getPlayerName(),
                                             request.getAccelerationX(),
                                             request.getAccelerationY(),
                                             request.isStabilize());
            }
            else if (request.getType() == SocketRequest.Type.SEND_START_COMMAND) {
                return restController.startGame(request.getGameName());
            }
            else if (request.getType() == SocketRequest.Type.GET_GAME_RESULT) {
                return restController.getGameResult(request.getGameName());
            }
            else {
                // Should be testing
                final ServerResponse testResponse = new ServerResponse();
                testResponse.setMessage("Still working!");
                return testResponse;
            }
        }
    }
}
