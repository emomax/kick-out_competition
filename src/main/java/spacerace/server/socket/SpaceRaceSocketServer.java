package spacerace.server.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import spacerace.server.SpaceRaceRestController;
import spacerace.server.response.ServerResponse;

@Component
public class SpaceRaceSocketServer {

    public static final int PORT = 9898;

    private final SpaceRaceRestController restController = new SpaceRaceRestController();

    public SpaceRaceSocketServer() throws IOException {
        System.out.println("Socket server running at port " + PORT);

        // We need to run this async so that server start completes (otherwise we hog the main startup thread).
        // Probably a better way to do this with Spring functionality...
        new Thread(this::startListeningToClientConnectRequests).start();
    }

    private void startListeningToClientConnectRequests() {
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
            System.out.println("Client " + clientNumber + " connected at " + socket);
        }

        public void run() {
            try {
                final BufferedReader in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                final PrintWriter    out = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    // Read request
                    final ObjectMapper objectMapper  = new ObjectMapper();
                    final String       requestString = in.readLine();
                    if (requestString == null) {
                        break;
                    }
                    final SocketRequest request = objectMapper.readValue(requestString, SocketRequest.class);

                    // Get and send response
                    final ServerResponse response       = getServerResponse(request);
                    final String         responseString = objectMapper.writeValueAsString(response);
                    out.println(responseString);
                }
            }
            catch (final IOException e) {
                System.out.println("Error handling client# " + clientNumber + ": " + e);
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
            if (request.getType() == SocketRequestType.REGISTER_PLAYER) {
                return restController.registerPlayer(request.getGameName(),
                                                     request.getPlayerName(),
                                                     request.getLevelNumber());
            }
            else if (request.getType() == SocketRequestType.GET_GAME_STATE) {
                return restController.getGameState(request.getGameName());
            }
            else if (request.getType() == SocketRequestType.POST_ACTION) {
                return restController.action(request.getGameName(),
                                             request.getPlayerName(),
                                             request.getAccelerationX(),
                                             request.getAccelerationY(),
                                             request.isStabilize());
            }
            else if (request.getType() == SocketRequestType.SEND_START_COMMAND) {
                return restController.startGame(request.getGameName());
            }
            else if (request.getType() == SocketRequestType.GET_GAME_RESULT) {
                return restController.getGameResult(request.getGameName());
            }
            else if (request.getType() == SocketRequestType.TEST) {
                final ServerResponse testResponse = new ServerResponse();
                testResponse.setMessage("Still working!");
                return testResponse;
            }
            else {
                final ServerResponse errorResponse = new ServerResponse();
                errorResponse.setErrorMessage("Unknown SocketRequestType");
                return errorResponse;
            }
        }
    }
}
