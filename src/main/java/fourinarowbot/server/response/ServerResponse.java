package fourinarowbot.server.response;

import fourinarowbot.board.BoardState;

public class ServerResponse {

    private BoardState     boardState;
    private String         message;
    private GameStatistics gameStatistics;
    private String         redPlayerName;
    private String         yellowPlayerName;

    public BoardState getBoardState() {
        return boardState;
    }

    public void setBoardState(final BoardState boardState) {
        this.boardState = boardState;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public GameStatistics getGameStatistics() {
        return gameStatistics;
    }

    public void setGameStatistics(final GameStatistics gameStatistics) {
        this.gameStatistics = gameStatistics;
    }

    public String getRedPlayerName() {
        return redPlayerName;
    }

    public void setRedPlayerName(final String redPlayerName) {
        this.redPlayerName = redPlayerName;
    }

    public String getYellowPlayerName() {
        return yellowPlayerName;
    }

    public void setYellowPlayerName(final String yellowPlayerName) {
        this.yellowPlayerName = yellowPlayerName;
    }
}
