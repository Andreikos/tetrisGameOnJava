package tetris;

import javax.swing.*;
import java.awt.*;

/**
 * The class is responsible for displaying various information.
 */
public class Panels extends JPanel {

  private static final long serialVersionUID = 0;

  private TetrisGame tetris;

  /**
   * Creates a new panel and sets it's display properties.
   *
   * @param tetris The tetris use.
   */
  public Panels(TetrisGame tetris) {
    int shape = 200;
    this.tetris = tetris;
    setPreferredSize(new Dimension(shape, shape));
    setBackground(Color.darkGray);
  }

  /**
   * Display information such as the next piece, the score and current level.
   *
   * @param g The graphics object.
   */
  @Override
  public void paintComponent(Graphics g) {
    int rect = 80;
    int x1 = 20;
    int y1 = 200, y2 = 230, y3 = 70;
    super.paintComponent(g);
    g.setColor(Color.ORANGE);
    g.drawString("Level:        " + tetris.getLevel(), x1, y1);
    g.drawString("Score:       " + tetris.getScore(), x1, y2);
    g.drawString("Next:", x1, y3);
    g.drawRect(rect, rect, rect, rect);
    Types type = tetris.getNextPieceType();
    if (!tetris.gameOver() && type != null) {
      int dimension = type.getDimension();
      int x = 100;
      int y = 105;
      int pixel = 12;
      int top = type.getTopInset(0);
      int left = type.getLeftInset(0);
      for (int row = 0; row < dimension; row++) {
        for (int col = 0; col < dimension; col++) {
          if (type.kind_of_type(col, row, 0)) {
            drawTile(type, x + ((col - left) * pixel), y + ((row - top) * pixel), g);
          }
        }
      }
    }
  }

  /**
   * Draws a tile onto the preview window.
   *
   * @param type The type of tile to draw.
   * @param x The x coordinate of the tile.
   * @param y The y coordinate of the tile.
   * @param g The graphics object.
   */
  private void drawTile(Types type, int x, int y, Graphics g) {
    int height = 15, width = 15;
    g.setColor(type.getBaseColor());
    g.fillRect(x, y, width, height);
  }
}
