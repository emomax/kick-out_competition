package treasurehunter.board;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import commons.gameengine.board.Board;
import static treasurehunter.board.Tile.TileState;

import commons.gameengine.board.PlayerColor;
import commons.gameengine.board.Coordinate;
import treasurehunter.GameResult;
import treasurehunter.domain.Move;
import treasurehunter.domain.Orientation;

public class TreasureHunterBoard implements Board<Tile> {
    private int numberOfColumns = 28;
    private int numberOfRows = 16;

    private int totalTreasures;

    private Tile[][] board;

    @Override
    public Tile[][] getCells() {
        return board;
    }

    public TreasureHunterBoard() {
        this(new Random().nextInt(40) * 2 + 5);
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

        board[numberOfColumns / 2 - 1][numberOfRows / 2].setState(TileState.RED);
        board[numberOfColumns / 2 - 1][numberOfRows / 2].setOrientation(Orientation.LEFT);

        board[numberOfColumns / 2][numberOfRows / 2].setState(TileState.YELLOW);
        board[numberOfColumns / 2][numberOfRows / 2].setOrientation(Orientation.RIGHT);

        Random rng = new Random();

        int index = rng.nextInt(3);
        List<Coordinate> walls;

        if (index == 0) {
            walls = getCirclePit();
        }
        else if (index == 1) {
            walls = getJaggedStage();
        }
        else {
            walls = getJaggedStage();
        }

        walls.forEach(coord -> board[coord.getX()][coord.getY()].setState(TileState.WALL));

        Random rgn = new Random();

        for (int i = 0; i < amountOfTreasures; i++) {
            int x;
            int y;

            while (true) {
                x = rgn.nextInt(numberOfColumns);
                y = rgn.nextInt(numberOfRows);

                TileState state = board[x][y].getState();

                if (state != TileState.EMPTY) {
                    continue;
                }

                board[x][y].setState(TileState.TREASURE);
                break;
            }
        }

    }

    private List<Coordinate> getJaggedStage() {

        return Arrays.asList(
            new Coordinate(2, 1),
            new Coordinate(2, 2),
            new Coordinate(2, 3),
            new Coordinate(2, 4),
            new Coordinate(2, 5),
            new Coordinate(2, 6),
            new Coordinate(2, 7),
            new Coordinate(2, 8),
            new Coordinate(2, 9),
            new Coordinate(6, numberOfRows - 2),
            new Coordinate(6, numberOfRows - 3),
            new Coordinate(6, numberOfRows - 4),
            new Coordinate(6, numberOfRows - 5),
            new Coordinate(6, numberOfRows - 6),
            new Coordinate(6, numberOfRows - 7),
            new Coordinate(6, numberOfRows - 8),
            new Coordinate(6, numberOfRows - 9),
            new Coordinate(6, numberOfRows - 10),
            new Coordinate(11, 1),
            new Coordinate(11, 2),
            new Coordinate(11, 3),
            new Coordinate(11, 4),
            new Coordinate(11, 5),
            new Coordinate(11, 6),
            new Coordinate(11, 7),
            new Coordinate(11, 8),
            new Coordinate(11, 9),
            new Coordinate(numberOfColumns - 3, numberOfRows - 2),
            new Coordinate(numberOfColumns - 3, numberOfRows - 3),
            new Coordinate(numberOfColumns - 3, numberOfRows - 4),
            new Coordinate(numberOfColumns - 3, numberOfRows - 5),
            new Coordinate(numberOfColumns - 3, numberOfRows - 6),
            new Coordinate(numberOfColumns - 3, numberOfRows - 7),
            new Coordinate(numberOfColumns - 3, numberOfRows - 8),
            new Coordinate(numberOfColumns - 3, numberOfRows - 9),
            new Coordinate(numberOfColumns - 3, numberOfRows - 10),
            new Coordinate(numberOfColumns - 7, 1),
            new Coordinate(numberOfColumns - 7, 2),
            new Coordinate(numberOfColumns - 7, 3),
            new Coordinate(numberOfColumns - 7, 4),
            new Coordinate(numberOfColumns - 7, 5),
            new Coordinate(numberOfColumns - 7, 6),
            new Coordinate(numberOfColumns - 7, 7),
            new Coordinate(numberOfColumns - 7, 8),
            new Coordinate(numberOfColumns - 7, 9),
            new Coordinate(numberOfColumns - 12, numberOfRows - 2),
            new Coordinate(numberOfColumns - 12, numberOfRows - 3),
            new Coordinate(numberOfColumns - 12, numberOfRows - 4),
            new Coordinate(numberOfColumns - 12, numberOfRows - 5),
            new Coordinate(numberOfColumns - 12, numberOfRows - 6),
            new Coordinate(numberOfColumns - 12, numberOfRows - 7),
            new Coordinate(numberOfColumns - 12, numberOfRows - 8),
            new Coordinate(numberOfColumns - 12, numberOfRows - 9),
            new Coordinate(numberOfColumns - 12, numberOfRows - 10)
        );
    }

    private List<Coordinate> getCirclePit() {
        return Arrays.asList(
                    new Coordinate(1, 4),
                    new Coordinate(2, 4),
                    new Coordinate(3, 4),
                    new Coordinate(4, 4),
                    new Coordinate(5, 4),
                    new Coordinate(6, 4),
                    new Coordinate(1, numberOfRows - 5),
                    new Coordinate(2, numberOfRows - 5),
                    new Coordinate(3, numberOfRows - 5),
                    new Coordinate(4, numberOfRows - 5),
                    new Coordinate(5, numberOfRows - 5),
                    new Coordinate(6, numberOfRows - 5),
                    new Coordinate(numberOfColumns - 2, 4),
                    new Coordinate(numberOfColumns - 3, 4),
                    new Coordinate(numberOfColumns - 4, 4),
                    new Coordinate(numberOfColumns - 5, 4),
                    new Coordinate(numberOfColumns - 6, 4),
                    new Coordinate(numberOfColumns - 7, 4),
                    new Coordinate(numberOfColumns - 2, numberOfRows - 5),
                    new Coordinate(numberOfColumns - 3, numberOfRows - 5),
                    new Coordinate(numberOfColumns - 4, numberOfRows - 5),
                    new Coordinate(numberOfColumns - 5, numberOfRows - 5),
                    new Coordinate(numberOfColumns - 6, numberOfRows - 5),
                    new Coordinate(numberOfColumns - 7, numberOfRows - 5),


                    new Coordinate(numberOfColumns / 2 - 1, numberOfRows / 2 + 4),
                    new Coordinate(numberOfColumns / 2 - 2, numberOfRows / 2 + 3),
                    new Coordinate(numberOfColumns / 2 - 3, numberOfRows / 2 + 2),
                    new Coordinate(numberOfColumns / 2 - 4, numberOfRows / 2 + 1),

                    new Coordinate(numberOfColumns / 2 - 1, numberOfRows / 2 - 4),
                    new Coordinate(numberOfColumns / 2 - 2, numberOfRows / 2 - 3),
                    new Coordinate(numberOfColumns / 2 - 3, numberOfRows / 2 - 2),
                    new Coordinate(numberOfColumns / 2 - 4, numberOfRows / 2 - 1),

                    new Coordinate(numberOfColumns / 2 + 1, numberOfRows / 2 + 4),
                    new Coordinate(numberOfColumns / 2 + 2, numberOfRows / 2 + 3),
                    new Coordinate(numberOfColumns / 2 + 3, numberOfRows / 2 + 2),
                    new Coordinate(numberOfColumns / 2 + 4, numberOfRows / 2 + 1),

                    new Coordinate(numberOfColumns / 2 + 1, numberOfRows / 2 - 4),
                    new Coordinate(numberOfColumns / 2 + 2, numberOfRows / 2 - 3),
                    new Coordinate(numberOfColumns / 2 + 3, numberOfRows / 2 - 2),
                    new Coordinate(numberOfColumns / 2 + 4, numberOfRows / 2 - 1)
                    );
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
