package tetris;

import java.awt.*;

/**
 * The enum class is responsible for the properties of the various pieces that can be used in the game.
 */
public enum Types {
    Type1(new Color(0, 220, 220), 4, new boolean[][]{
            {
                    false, false, false, false,
                    true, true, true, true,
                    false, false, false, false,
                    false, false, false, false,
            },
            {
                    false, false, true, false,
                    false, false, true, false,
                    false, false, true, false,
                    false, false, true, false,
            },
            {
                    false, false, false, false,
                    false, false, false, false,
                    true, true, true, true,
                    false, false, false, false,
            },
            {
                    false, true, false, false,
                    false, true, false, false,
                    false, true, false, false,
                    false, true, false, false,
            }
    }),
    Type2(new Color(50, 35, 220), 3, new boolean[][]{
            {
                    true, false, false,
                    true, true, true,
                    false, false, false,
            },
            {
                    false, true, true,
                    false, true, false,
                    false, true, false,
            },
            {
                    false, false, false,
                    true, true, true,
                    false, false, true,
            },
            {
                    false, true, false,
                    false, true, false,
                    true, true, false,
            }
    }),
    Type3(new Color(128, 255, 128), 3, new boolean[][]{
            {
                    false, false, true,
                    true, true, true,
                    false, false, false,
            },
            {
                    false, true, false,
                    false, true, false,
                    false, true, true,
            },
            {
                    false, false, false,
                    true, true, true,
                    true, false, false,
            },
            {
                    true, true, false,
                    false, true, false,
                    false, true, false,
            }
    }),
    Type4(new Color(35, 220, 35), 2, new boolean[][]{
            {
                    true, true,
                    true, true,
            },
            {
                    true, true,
                    true, true,
            },
            {
                    true, true,
                    true, true,
            },
            {
                    true, true,
                    true, true,
            }
    }),
    Type5(new Color(255, 0, 0), 3, new boolean[][]{
            {
                    false, true, true,
                    true, true, false,
                    false, false, false,
            },
            {
                    false, true, false,
                    false, true, true,
                    false, false, true,
            },
            {
                    false, false, false,
                    false, true, true,
                    true, true, false,
            },
            {
                    true, false, false,
                    true, true, false,
                    false, true, false,
            }
    }),
    Type6(new Color(255, 128, 128), 3, new boolean[][]{
            {
                    false, true, false,
                    true, true, true,
                    false, false, false,
            },
            {
                    false, true, false,
                    false, true, true,
                    false, true, false,
            },
            {
                    false, false, false,
                    true, true, true,
                    false, true, false,
            },
            {
                    false, true, false,
                    true, true, false,
                    false, true, false,
            }
    }),
    Type7(new Color(220, 35, 240), 3, new boolean[][]{
            {
                    true, true, false,
                    false, true, true,
                    false, false, false,
            },
            {
                    false, false, true,
                    false, true, true,
                    false, true, false,
            },
            {
                    false, false, false,
                    true, true, false,
                    false, true, true,
            },
            {
                    false, true, false,
                    true, true, false,
                    true, false, false,
            }
    });
    private Color baseColor;
    private Color lightColor;
    private Color darkColor;
    private int spawnCol;
    private int spawnRow;
    private int dimension;
    private boolean[][] tiles;

    /**
     * Creates a new TileType.
     *
     * @param color     The base color of the tile.
     * @param dimension The dimensions of the tiles array.
     * @param tiles     The tiles.
     */
    private Types(Color color, int dimension, boolean[][] tiles) {
        this.baseColor = color;
        this.lightColor = color.brighter();
        this.darkColor = color.darker();
        this.dimension = dimension;
        this.tiles = tiles;
        this.spawnCol = 5 - (dimension >> 1);
        this.spawnRow = getTopInset(0);
    }

    /**
     * Gets the base color of this type.
     *
     * @return The base color.
     */
    public Color getBaseColor() {
        return baseColor;
    }

    /**
     * Gets the light color of this type.
     *
     * @return The light color.
     */
    public Color getLightColor() {
        return lightColor;
    }

    /**
     * Gets the dark color of this type.
     *
     * @return The dark color.
     */
    public Color getDarkColor() {
        return darkColor;
    }

    /**
     * Gets the dimension of this type.
     *
     * @return The dimension.
     */
    public int getDimension() {
        return dimension;
    }

    /**
     * Gets the spawn column of this type.
     *
     * @return The spawn column.
     */
    public int getSpawnColumn() {
        return spawnCol;
    }

    /**
     * Gets the spawn row of this type.
     *
     * @return The spawn row.
     */
    public int getSpawnRow() {
        return spawnRow;
    }

    /**
     * Checks to see if the given coordinates and rotate contain a tile.
     *
     * @param x        The x coordinate of the tile.
     * @param y        The y coordinate of the tile.
     * @param rotate   To  rotate of our tile.
     * @return Regardless of whether or not there tiles.
     */
    public boolean kind_of_type(int x, int y, int rotate) {
        return tiles[rotate][y * dimension + x];
    }

    /**
     * The left inset is represented by the number of empty columns on the left side.
     *
     * @param rotate To rotate.
     * @return The left inset.
     */
    public int getLeftInset(int rotate) {
        for (int x = 0; x < dimension; x++) {
            for (int y = 0; y < dimension; y++) {
                if (kind_of_type(x, y, rotate))
                    return x;
            }
        }
        return -1;
    }

    /**
     * The right inset is represented by the number of empty columns on the right side.
     *
     * @param rotate To rotate.
     * @return The right inset.
     */
    public int getRightInset(int rotate) {
        for (int x = dimension - 1; x >= 0; x--) {
            for (int y = 0; y < dimension; y++) {
                if (kind_of_type(x, y, rotate))
                    return dimension - x;
            }
        }
        return -1;
    }

    /**
     * The top inset is represented by the number of empty rows on the top side.
     *
     * @param rotate To rotate.
     * @return The top inset.
     */
    public int getTopInset(int rotate) {
        for (int y = 0; y < dimension; y++) {
            for (int x = 0; x < dimension; x++) {
                if (kind_of_type(x, y, rotate))
                    return y;
            }
        }
        return -1;
    }

    /**
     * The botom inset is represented by the number of empty rows on the bottom side.
     *
     * @param rotate To rotate.
     * @return The bottom inset.
     */
    public int getBottomInset(int rotate) {
        for (int y = dimension - 1; y >= 0; y--) {
            for (int x = 0; x < dimension; x++) {
                if (kind_of_type(x, y, rotate))
                    return dimension - y;
            }
        }
        return -1;
    }
}
