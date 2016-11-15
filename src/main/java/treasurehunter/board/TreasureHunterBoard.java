package treasurehunter.board;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import commons.gameengine.board.Board;
import static treasurehunter.board.Tile.TileState;

import commons.gameengine.board.PlayerColor;
import commons.gameengine.board.Coordinate;
import treasurehunter.GameResult;
import treasurehunter.domain.Move;
import treasurehunter.domain.Orientation;

public class TreasureHunterBoard implements Board<Tile> {

    private float wallDensity = 0.5f;
    private int numberOfColumns = 28;
    private int numberOfRows = 16;

    private int totalTreasures;

    private Tile[][] board;

    @Override
    public Tile[][] getCells() {
        return board;
    }

    public TreasureHunterBoard() {
        this(new Random().nextInt(20) * 2 + 20);
    }

    public TreasureHunterBoard(int amountOfTreasures) {
        this.board = new Tile[numberOfColumns][numberOfRows];
        this.totalTreasures = amountOfTreasures;

        generateMap(amountOfTreasures);
    }

    public TreasureHunterBoard(final Tile[][] board) {
        this.board = board;
        this.totalTreasures = calcTreasuresLeft();
    }

    private int calcTreasuresLeft() {
        int treasures = 0;
        for (int x = 0; x < numberOfColumns; x++) {
            for (int y = 0; y < numberOfRows; y++) {
                if (board[x][y].getState() == TileState.TREASURE) {
                    treasures++;
                }
            }
        }
        return treasures;
    }

    public Tile getTile(final int x, final int y) {
        return board[x][y];
    }

    public BoardStateUpdate movePlayer(PlayerColor player, Move playerMove) {
        final BoardStateUpdate boardStateUpdate = new BoardStateUpdate();
        final Tile playerTile = findPlayer(player);

        int x = playerTile.getCoordinates().getX();
        int y = playerTile.getCoordinates().getY();

        Orientation playerDirection = playerTile.getOrientation();

        switch (playerMove) {
            case MOVE_FORWARD:
                int newX = x + playerDirection.xDirection();
                int newY = y + playerDirection.yDirection();

                Coordinate possibleNextTile = new Coordinate(newX, newY);

                if (!isOutsideBoard(possibleNextTile)) {
                    Tile nextTile = board[newX][newY];

                    switch (nextTile.getState()) {
                        case TREASURE:
                            GameResult.playerCollected(player);
                            // Deliberate fallthrough
                        case EMPTY:
                            board[x][y].setState(TileState.EMPTY);
                            board[newX][newY].setState(TileState.valueOf(player.toString()));
                            board[newX][newY].setOrientation(board[x][y].getOrientation());
                            boardStateUpdate.addChangedTile(board[x][y], board[newX][newY]);
                            break;
                        case WALL:
                        case RED:
                        case YELLOW:
                            // Can't move here, do nothing
                            break;
                    }
                }
                else {
                    // Player moved against outer wall
                }
                break;

            case ROTATE_LEFT:
                if (playerDirection == Orientation.UP) {
                    board[x][y].setOrientation(Orientation.LEFT);
                }
                else if (playerDirection == Orientation.DOWN) {
                    board[x][y].setOrientation(Orientation.RIGHT);
                }
                else if (playerDirection == Orientation.LEFT) {
                    board[x][y].setOrientation(Orientation.DOWN);
                }
                else if (playerDirection == Orientation.RIGHT) {
                    board[x][y].setOrientation(Orientation.UP);
                }
                boardStateUpdate.addChangedTile(board[x][y]);
                break;

            case ROTATE_RIGHT:
                if (playerDirection == Orientation.UP) {
                    board[x][y].setOrientation(Orientation.RIGHT);
                }
                else if (playerDirection == Orientation.DOWN) {
                    board[x][y].setOrientation(Orientation.LEFT);
                }
                else if (playerDirection == Orientation.LEFT) {
                    board[x][y].setOrientation(Orientation.UP);
                }
                else if (playerDirection == Orientation.RIGHT) {
                    board[x][y].setOrientation(Orientation.DOWN);
                }
                boardStateUpdate.addChangedTile(board[x][y]);
                break;

            default:
                throw new RuntimeException("Undefined player move! - " + playerMove.toString());
        }

        return boardStateUpdate;
    }

    private Tile findPlayer(PlayerColor player) {
        for (int i = 0; i < numberOfColumns; i++) {
            for (int j = 0; j < numberOfRows; j++) {
                if (board[i][j].getState().toString().equals(player.toString())) {
                    return board[i][j];
                }
            }
        }

        throw new RuntimeException("Player was not found!");
    }

    @Override
    public int getNumberOfRows() {
        return numberOfRows;
    }

    @Override
    public int getNumberOfCols() {
        return numberOfColumns;
    }

    public int getTotalTreasures() {
        return totalTreasures;
    }

    @Override
    public boolean isOutsideBoard(Coordinate coordinate) {
        final int x = coordinate.getX();
        final int y = coordinate.getY();

        return (x < 0) ||
               (y < 0) ||
               (x >= getNumberOfCols()) ||
               (y >= getNumberOfRows());
    }

    public void reset() {
        generateMap(totalTreasures);
    }

    private void clear() {
        for (int x = 0; x < numberOfColumns; x++) {
            for (int y = 0; y < numberOfRows; y++) {
                board[x][y] = new Tile(new Coordinate(x, y));
            }
        }
    }

    private void generateMap(int amountOfTreasures) {
        clear();

        final Coordinate redStart = new Coordinate(numberOfColumns / 2 - 1, numberOfRows / 2);
        final Coordinate yellowStart = new Coordinate(numberOfColumns / 2, numberOfRows / 2);

        board[redStart.getX()][redStart.getY()].setState(TileState.RED);
        board[redStart.getX()][redStart.getY()].setOrientation(Orientation.LEFT);

        board[yellowStart.getX()][yellowStart.getY()].setState(TileState.YELLOW);
        board[yellowStart.getX()][yellowStart.getY()].setOrientation(Orientation.RIGHT);

        final Set<Coordinate> walkableTiles = generateMapPaths(yellowStart);
        // We add the coordinates surrounding the players to make sure they're not blocking each other at the start
        walkableTiles.addAll(getSurroundingTileCoords(redStart));
        walkableTiles.addAll(getSurroundingTileCoords(yellowStart));

        placeWalls(walkableTiles);
        placeTreasures(walkableTiles, amountOfTreasures);
    }

    // We generate paths by 'walking' randomly, and return the coordinates we've walked
    private Set<Coordinate> generateMapPaths(final Coordinate pathStart) {
        final Set<Coordinate> walkedCoordinates = new HashSet<>();

        final Random rng = new Random();

        int currX = pathStart.getX();
        int currY = pathStart.getY();

        final int targetAmountOfWalkableTiles = (int)((board.length * board[0].length) * (1f - wallDensity));

        while (walkedCoordinates.size() < targetAmountOfWalkableTiles) {
            final boolean moveHorizontally = rng.nextBoolean();
            final boolean moveTowardsOrigin = rng.nextBoolean();

            if (moveHorizontally) {
                currX += moveTowardsOrigin ? -1 : 1;
            }
            else {
                currY += moveTowardsOrigin ? -1 : 1;
            }

            if (isOutsideBoard(new Coordinate(currX, currY))) {
                // We moved out of bounds, go back to start of path
                currX = pathStart.getX();
                currY = pathStart.getY();
            }
            else {
                walkedCoordinates.add(new Coordinate(currX, currY));
            }
        }
        return walkedCoordinates;
    }

    private List<Coordinate> getSurroundingTileCoords(final Coordinate sourceCoordinate) {
        List<Coordinate> resultList = new ArrayList<>();

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                final Coordinate currCoord = new Coordinate(sourceCoordinate.getX() + x, sourceCoordinate.getY() + y);

                if (!isOutsideBoard(currCoord)) {
                    resultList.add(currCoord);
                }
            }
        }
        return resultList;
    }

    private void placeWalls(final Set<Coordinate> excludedTiles) {
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                if (!excludedTiles.contains(new Coordinate(x, y))) {
                    board[x][y].setState(TileState.WALL);
                }
            }
        }
    }

    private void placeTreasures(final Set<Coordinate> availableTilesSet, int amountOfTreasuresToPlace) {
        if (availableTilesSet.size() - 2 < amountOfTreasuresToPlace) {
            throw new RuntimeException("Trying to place more treasures than there are available spaces!");
        }

        final Random rng = new Random();
        final List<Coordinate> availableTiles = new ArrayList<>(availableTilesSet);

        while (amountOfTreasuresToPlace > 0) {
            final int index = rng.nextInt(availableTiles.size());
            final Coordinate coord = availableTiles.remove(index);

            if (board[coord.getX()][coord.getY()].getState() == TileState.YELLOW || board[coord.getX()][coord.getY()].getState() == TileState.RED) {
                continue;
            }

            board[coord.getX()][coord.getY()].setState(TileState.TREASURE);
            amountOfTreasuresToPlace--;
        }
    }

    @Override
    public void print() {
        //System.out.print((char) 27 + "[34;43m");

        System.out.println(new String(new char[numberOfColumns]).replace("\0", "-----------"));
        for (int row = 0; row < numberOfRows; row++) {
            String result = "";
            String lineSeparator = "";

            for (int col = 0; col < numberOfColumns; col++) {
                final Tile tile = board[col][row];
                String output = " ";

                if (tile.getState() == TileState.RED) {
                    output = "RED " + getOutputDirection(tile);
                }
                else if (tile.getState() == TileState.YELLOW) {
                    output = "YLW " + getOutputDirection(tile);
                }
                else if (tile.getState() != TileState.EMPTY) {
                    output = tile.getState().toString();
                }

                result += String.format("%1$" + 8 + "s", output);
                result += " | ";
                lineSeparator += "-----------";
            }
            System.out.println(result + "\n" + lineSeparator);
        }
        System.out.println("\n\n");
    }

    private String getOutputDirection(Tile tile) {
        switch (tile.getOrientation()) {
            case UP:
                return "^";
            case DOWN:
                return "v";
            case RIGHT:
                return ">";
            case LEFT:
                return "<";
        }

        throw new RuntimeException("Unexpected orientation: " + tile.getOrientation());
    }
}
