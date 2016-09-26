package fourinarowbot.server.response;

import fourinarowbot.board.BoardState;

public class GetBoardResponse {

    private BoardState boardState;
    private String     message;

    public static GetBoardResponse responseWithBoardState(final BoardState boardState) {
        final GetBoardResponse serverResponse = new GetBoardResponse();
        serverResponse.setBoardState(boardState);
        return serverResponse;
    }

    public static GetBoardResponse responseWithMessage(final String message) {
        final GetBoardResponse serverResponse = new GetBoardResponse();
        serverResponse.setMessage(message);
        return serverResponse;
    }

    public static GetBoardResponse emptyResponse() {
        return new GetBoardResponse();
    }

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
}
