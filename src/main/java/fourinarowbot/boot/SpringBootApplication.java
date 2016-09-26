package fourinarowbot.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.client.RestTemplate;

import fourinarowbot.board.BoardImpl;
import fourinarowbot.domain.Coordinates;
import fourinarowbot.gameengine.GameEngine;
import fourinarowbot.gameengine.MyN00bGameEngine;
import fourinarowbot.server.response.GetBoardResponse;
import fourinarowbot.server.response.PlaceMarkerResponse;

@org.springframework.boot.autoconfigure.SpringBootApplication
public class SpringBootApplication {

    public static void main(final String[] args) {
        final ConfigurableApplicationContext applicationContext = SpringApplication.run(SpringBootApplication.class, args);
        final String                         arg1               = args[0];
        if (!arg1.equals("server")) {
            final String playerName = args[0];
            final String gamerName  = args[1];
            startGame(gamerName, playerName);
        }
    }

    private static void startGame(final String gameName, final String playerName) {
        final GameEngine myGameEngine = new MyN00bGameEngine();
        startPlaying(gameName, playerName, myGameEngine);
    }

    private static void startPlaying(final String gameName, final String playerName, final GameEngine playerEngine) {
        final String message;
        while (true) {
            final GetBoardResponse boardStateResponse = getBoardState(gameName, playerName);
            if (boardStateResponse.getMessage() != null) {
                message = boardStateResponse.getMessage();
                break;
            }

            final BoardImpl   board       = new BoardImpl(boardStateResponse.getBoardState().getMarkers());
            final Coordinates coordinates = playerEngine.getCoordinatesForNextMakerToPlace(board);

            final PlaceMarkerResponse placeMarkerResponse = placeMarker(gameName, playerName, coordinates);
            if (placeMarkerResponse.getMessage() != null) {
                board.print();
                message = placeMarkerResponse.getMessage();
                break;
            }
        }
        System.out.println("Game over: " + message);
    }

    private static GetBoardResponse getBoardState(final String gameName, final String playerName) {
        final RestTemplate restTemplate = new RestTemplate();
        final String       url          = "http://192.168.1.129/getBoard?gameName=" + gameName + "&playerName=" + playerName;
        return restTemplate.getForObject(url, GetBoardResponse.class);
    }

    private static PlaceMarkerResponse placeMarker(final String gameName, final String playerName, final Coordinates coordinates) {
        final RestTemplate restTemplate = new RestTemplate();
        final String       url          = "http://192.168.1.129/placeMarker?gameName=" + gameName + "&playerName=" + playerName + "&x=" + coordinates.getX() + "&y=" + coordinates.getY();
        return restTemplate.getForObject(url, PlaceMarkerResponse.class);
    }
}
