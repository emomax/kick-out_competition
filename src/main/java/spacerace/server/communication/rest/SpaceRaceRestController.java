package spacerace.server.communication.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import spacerace.domain.Acceleration;
import spacerace.server.communication.ServerCommunicationController;
import spacerace.server.communication.response.ServerResponse;

@RestController
public class SpaceRaceRestController {

    private final ServerCommunicationController communicationController = new ServerCommunicationController();

    @RequestMapping("/registerPlayer")
    public ServerResponse registerPlayer(
            @RequestParam(value = "gameName") final String gameName,
            @RequestParam(value = "playerName") final String playerName,
            @RequestParam(value = "levelNumber") final Integer levelNumber) {
        return communicationController.registerPlayer(gameName, playerName, levelNumber);
    }

    @RequestMapping("/getGameState")
    public ServerResponse getGameState(@RequestParam(value = "gameName") final String gameName) {
        return communicationController.getGameState(gameName);
    }

    @RequestMapping("/action")
    public ServerResponse action(
            @RequestParam(value = "gameName") final String gameName,
            @RequestParam(value = "playerName") final String playerName,
            @RequestParam(value = "accelerationX") final String accelerationX,
            @RequestParam(value = "accelerationY") final String accelerationY,
            @RequestParam(value = "stabilize") final boolean stabilize) {
        return communicationController.action(
                gameName,
                playerName,
                Acceleration.valueOf(accelerationX),
                Acceleration.valueOf(accelerationY),
                stabilize
        );
    }


    @RequestMapping("/startGame")
    public ServerResponse startGame(@RequestParam(value = "gameName") final String gameName) {
        return communicationController.startGame(gameName);
    }

    @RequestMapping("/getPlayerPositions")
    public ServerResponse getGameResult(@RequestParam(value = "gameName") final String gameName) {
        return communicationController.getGameResult(gameName);
    }

    @RequestMapping("/test")
    public String testing() {
        return "It still works!";
    }
}
