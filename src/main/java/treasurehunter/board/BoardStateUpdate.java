package treasurehunter.board;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class BoardStateUpdate implements Serializable {

    private List<Tile> changedTiles = new ArrayList<>();

    public BoardStateUpdate() {
    }

    public void addChangedTile(final Tile... tiles) {
        for (final Tile tile : tiles) {
            changedTiles.add(tile);
        }
    }

    public String toJSON() {
        return new Gson().toJson(this);
    }
}
