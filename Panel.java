package tetris;

import javax.swing.*;
import java.awt.*;

/**
 * The class is responsible for displaying the game grid and handling things for the game board.
 */
public class Panel extends JPanel {
    private static final long serialVersionUID = 0;
    public static final int columns = 10;
    private static final int linesOnDesk = 20;
    private static final int linesOfTheDesk = 2;
    public static final int lines = linesOnDesk + linesOfTheDesk;
    public static final int blockSize = 24;
    public static final int panelWidth = columns * blockSize + 10;
    public static final int panelHeight = linesOnDesk * blockSize + 10;
    private TetrisGame tetris;
    private Types[][] tiles;

    /**
     * Crates a new gameBoard.
     *
     * @param tetris The tetris will use.
     */
    public Panel(TetrisGame tetris) {
        this.tetris = tetris;
        this.tiles = new Types[lines][columns];
        setPreferredSize(new Dimension(250, 500));
        setBackground(Color.darkGray);
    }

    /**
     * Resets the board and clears away any tiles.
     */
    public void clear() {
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < columns; j++) {
                tiles[i][j] = null;
            }
        }
    }

    /**
     * Determines whether or not a piece can be placed at the coordinates.
     *
     * @param type   The type of piece to use.
     * @param x      The x coordinate of the piece.
     * @param y      The y coordinate of the piece.
     * @param rotate The rotation of the piece.
     * @return Whether or not the position is valid.
     */
    public boolean isEmpty(Types type, int x, int y, int rotate) {
        if (x < -type.getLeftInset(rotate)) {
            return false;
        }
        if (x + type.getDimension() - type.getRightInset(rotate) >= columns) {
            return false;
        }
        if (y < -type.getTopInset(rotate)) {
            return false;
        }
        if (y + type.getDimension() - type.getBottomInset(rotate) >= lines) {
            return false;
        }
        for (int col = 0; col < type.getDimension(); col++) {
            for (int row = 0; row < type.getDimension(); row++) {
                if (type.kind_of_type(col, row, rotate) && isBusy(x + col, y + row)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Adds a piece to the game board.
     *
     * @param type     The type of piece to place.
     * @param x        The x coordinate of the piece.
     * @param y        The y coordinate of the piece.
     * @param rotate The rotation of the piece.
     */
    public void addPiece(Types type, int x, int y, int rotate) {
        for (int col = 0; col < type.getDimension(); col++) {
            for (int row = 0; row < type.getDimension(); row++) {
                if (type.kind_of_type(col, row, rotate)) {
                    setTile(col + x, row + y, type);
                }
            }
        }
    }

    /**
     * Checks the board to see if any lines have been cleared, and removes them.
     *
     * @return The number of lines that were cleared.
     */
    public int checkLines() {
        int completed = 0;
        for (int row = 0; row < lines; row++) {
            if (checkLine(row)) {
                completed++;
            }
        }
        return completed;
    }

    /**
     * Checks whether or not row is full.
     *
     * @param line The row to check.
     * @return Whether or not this row is full.
     */
    private boolean checkLine(int line) {
        for (int col = 0; col < columns; col++) {
            if (!isBusy(col, line)) {
                return false;
            }
        }

        for (int row = line - 1; row >= 0; row--) {
            for (int col = 0; col < columns; col++) {
                setTile(col, row + 1, getTile(col, row));
            }
        }
        return true;
    }

    /**
     * Checks to see if the tile is already occupied.
     *
     * @param x The column.
     * @param y The row.
     * @return Whether or not the tile is occupied.
     */
    public boolean isBusy(int x, int y) {
        return tiles[y][x] != null;
    }

    public boolean isBusy1(int x, int y) {
        return tiles[y - 1][x] != null;
    }

    public boolean isBusy2(int x, int y) {
        return tiles[y - 2][x] != null;
    }

    /**
     * Sets a tile located at the desired column and row.
     *
     * @param x    The column.
     * @param y    The row.
     * @param type The value to set to the tile to.
     */
    private void setTile(int x, int y, Types type) {
        tiles[y][x] = type;
    }

    /**
     * Gets a tile by it's column and row.
     *
     * @param x The column.
     * @param y The row.
     * @return The tile.
     */
    private Types getTile(int x, int y) {
        return tiles[y][x];
    }

    /**
     * Draw the current tile and grid for best display. Also write a text after the game or before it.
     *
     * @param g The graphics object.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (tetris.newGame() || tetris.gameOver()) {
            g.setColor(Color.WHITE);
            String msg = tetris.newGame() ? "TETRIS" : "GAME OVER";
            g.drawString(msg, 90, 150);
        } else {
            for (int x = 0; x < columns; x++) {
                for (int y = linesOfTheDesk; y < lines; y++) {
                    Types tile = getTile(x, y);
                    if (tile != null) {
                        drawTile(tile, x * blockSize, (y - linesOfTheDesk) * blockSize, g);
                    }
                }
            }
            Types type = tetris.getPieceType();
            int pieceCol = tetris.getPieceCol();
            int pieceRow = tetris.getPieceRow();
            int rotate = tetris.getPieceRotate();
            for (int col = 0; col < type.getDimension(); col++) {
                for (int row = 0; row < type.getDimension(); row++) {
                    if (pieceRow + row >= 2 && type.kind_of_type(col, row, rotate)) {
                        drawTile(type, (pieceCol + col) * blockSize,
                                (pieceRow + row - linesOfTheDesk) * blockSize, g);
                    }
                }
            }
            g.setColor(Color.GRAY);
            for (int x = 0; x < columns; x++) {
                for (int y = 0; y < linesOnDesk; y++) {
                    g.drawLine(0, y * blockSize, columns * blockSize, y * blockSize);
                    g.drawLine(x * blockSize, 0, x * blockSize, linesOnDesk * blockSize);
                }
            }
        }
        g.setColor(Color.WHITE);
        g.drawRect(0, 0, blockSize * columns, blockSize * linesOnDesk);
    }

    /**
     * Draws a tile onto the board.
     *
     * @param type The type of tile to draw.
     * @param x    The column.
     * @param y    The row.
     * @param g    The graphics object.
     */
    private void drawTile(Types type, int x, int y, Graphics g) {
        drawTile(type.getBaseColor(), type.getLightColor(), type.getDarkColor(), x, y, g);
    }

    /**
     * Draws a rectangle onto the board.
     *
     * @param base  The base color of rectangle.
     * @param light The light color.
     * @param dark  The dark color.
     * @param x     The column.
     * @param y     The row.
     * @param g     The graphics object.
     */
    private void drawTile(Color base, Color light, Color dark, int x, int y, Graphics g) {
        g.setColor(base);
        g.fillRect(x, y, blockSize, blockSize);
    }

}
