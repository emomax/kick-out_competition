package treasurehunter.server.response;

import commons.gameengine.board.BoardState;
import commons.network.server.Response.ServerResponseBase;
import treasurehunter.board.Tile;

public class ServerResponse extends ServerResponseBase {

    private BoardState<Tile> boardState;//BoardState<Tile> boardState;

    public BoardState<Tile> getBoardState() {
        return boardState;
    }

    public void setBoardState(final BoardState<Tile> boardState) {
        this.boardState = boardState;
    }
}
