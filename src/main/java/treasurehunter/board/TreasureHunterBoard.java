package treasurehunter.board;

import java.util.Random;

import commons.gameengine.board.Board;
import static treasurehunter.board.Tile.TileState;

import commons.gameengine.board.PlayerColor;
import commons.gameengine.board.Coordinate;
import treasurehunter.GameResult;
import treasurehunter.domain.Move;
import treasurehunter.domain.Orientation;

public class TreasureHunterBoard implements Board<Tile> {
    private int numberOfColumns = 18;
    private int numberOfRows = 16;

    private int totalTreasures;

    private Tile[][] board;

    @Override
    public Tile[][] getCells() {
        return board;
    }

    public TreasureHunterBoard() {
        this(new Random().nextInt(10) * 2 + 5);
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

    public void movePlayer(PlayerColor player, Move playerMove) {
        Coordinate  playerTile = findPlayer(player);

        int x = playerTile.getX();
        int y = playerTile.getY();

        Orientation playerDirection = board[x][y].getOrientation();

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
                            board[x + playerDirection.xDirection()][y + playerDirection.yDirection()].setState(TileState.valueOf(player.toString()));
                            board[x + playerDirection.xDirection()][y + playerDirection.yDirection()].setOrientation(board[x][y].getOrientation());
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
                return;

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
                return;

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
                return;

            default:
                throw new RuntimeException("Undefined player move! - " + playerMove.toString());
        }
    }

    private Coordinate findPlayer(PlayerColor player) {
        for (int i = 0; i < numberOfColumns; i++) {
            for (int j = 0; j < numberOfRows; j++) {
                if (board[i][j].getState().toString().equals(player.toString())) {
                    return board[i][j].getCoordinates();
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

        board[0][0].setState(TileState.RED);
        board[0][0].setOrientation(Orientation.DOWN);

        board[numberOfColumns - 1][numberOfRows - 1].setState(TileState.YELLOW);
        board[numberOfColumns - 1][numberOfRows - 1].setOrientation(Orientation.UP);

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
