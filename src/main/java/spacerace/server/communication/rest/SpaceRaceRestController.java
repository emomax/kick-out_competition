package spacerace.server.communication.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import spacerace.domain.Acceleration;
import spacerace.server.communication.SpaceRaceServerController;
import spacerace.server.communication.response.ServerResponse;

@RestController
public class SpaceRaceRestController {

    private final SpaceRaceServerController serverController;

    @Autowired
    public SpaceRaceRestController(final SpaceRaceServerController serverController) {
        this.serverController = serverController;
    }

    @RequestMapping("/registerPlayer")
    public ServerResponse registerPlayer(
            @RequestParam(value = "gameName") final String gameName,
            @RequestParam(value = "playerName") final String playerName,
            @RequestParam(value = "levelNumber") final Integer levelNumber) {
        return serverController.registerPlayer(gameName, playerName, levelNumber);
    }

    @RequestMapping("/getGameState")
    public ServerResponse getGameState(@RequestParam(value = "gameName") final String gameName) {
        return serverController.getGameState(gameName);
    }

    @RequestMapping("/getGameStateForViewing")
    public ServerResponse getGameStateForViewing(@RequestParam(value = "gameName") final String gameName) {
        return serverController.getGameStateForViewing(gameName);
    }

    @RequestMapping("/action")
    public ServerResponse action(
            @RequestParam(value = "gameName") final String gameName,
            @RequestParam(value = "playerName") final String playerName,
            @RequestParam(value = "accelerationX") final String accelerationX,
            @RequestParam(value = "accelerationY") final String accelerationY,
            @RequestParam(value = "stabilize") final boolean stabilize,
            @RequestParam(value = "missileAngle") final Double missileAngle) {
        return serverController.action(
                gameName,
                playerName,
                Acceleration.valueOf(accelerationX),
                Acceleration.valueOf(accelerationY),
                stabilize,
                missileAngle
        );
    }


    @RequestMapping("/startGame")
    public ServerResponse startGame(@RequestParam(value = "gameName") final String gameName) {
        return serverController.startGame(gameName);
    }

    @RequestMapping("/getPlayerPositions")
    public ServerResponse getGameResult(@RequestParam(value = "gameName") final String gameName) {
        return serverController.getGameResult(gameName);
    }

    @RequestMapping("/test")
    public String testing() {
        return "It still works!";
    }
}
