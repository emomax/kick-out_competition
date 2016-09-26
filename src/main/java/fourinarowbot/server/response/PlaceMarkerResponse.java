package fourinarowbot.server.response;

import fourinarowbot.board.BoardState;

public class PlaceMarkerResponse {

    private String     message;
    private BoardState boardState;

    public static PlaceMarkerResponse responseWithMessage(final String message, final BoardState boardState) {
        final PlaceMarkerResponse serverResponse = new PlaceMarkerResponse();
        serverResponse.setMessage(message);
        serverResponse.setBoardState(boardState);
        return serverResponse;
    }

    public static PlaceMarkerResponse emptyResponse() {
        return new PlaceMarkerResponse();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public BoardState getBoardState() {
        return boardState;
    }

    private void setBoardState(final BoardState boardState) {
        this.boardState = boardState;
    }
}
