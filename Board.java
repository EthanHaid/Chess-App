import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Line2D;
import java.awt.Point;
import java.awt.Color;
import java.util.ArrayList;

public class Board {
   private static final int NUM_TILES = 8; //number of tiles per edge
   private static final double MAX_BOARD_FILL = (double) 9 / 10; //max fraction of frame used
   private static final Color FILL_COLOR = Color.GRAY; //color of the dark tiles
   private static final Color HIGHLIGHT_COLOR = Color.YELLOW; // color of the highlighted tiles
   private static final Color BORDER_COLOR = Color.BLACK; //color of the outlines

   private ArrayList<Point> validTiles; //tiles to be highlighted when drawn
   private Point pos; //pos of the upper left corner
   //width and height of the board
   private double width;
   private double height;
   //width and height of each tile
   private double tileWidth;
   private double tileHeight;

   public Board() {
      pos = new Point();
      resize(0, 0);
   }

   public void draw(Graphics2D g2) {
      //draws outline of board
      Rectangle2D.Double board = new Rectangle2D.Double(pos.x, pos.y, width, height);
      g2.setColor(BORDER_COLOR);
      g2.draw(board);

      //draws each tile
      Rectangle2D.Double tile;
      double xDraw;
      double yDraw = pos.y;
      boolean isValid;
      for(int i = 0; i < NUM_TILES; i++) {
         xDraw = pos.x;
         for(int j = 0; j < NUM_TILES; j++) { //for every tile on the board
            isValid = false;
            if(validTiles != null) {
               for(Point p : validTiles) { //check if the tile is on the list of valid tiles
                  if(p.x == j && p.y == i) isValid = true;
               }
            }
            if(isValid) {
               tile = new Rectangle2D.Double(xDraw, yDraw, tileWidth, tileHeight);
               g2.setColor(HIGHLIGHT_COLOR);
               g2.fill(tile);
            } else if(i % 2 != j % 2) {
               tile = new Rectangle2D.Double(xDraw, yDraw, tileWidth, tileHeight);
               g2.setColor(FILL_COLOR);
               g2.fill(tile);
            }
            xDraw += tileWidth;
         }
         yDraw += tileHeight;
      }
   }

   public void resize(int frameWidth, int frameHeight) {
      int shortSide = (frameWidth < frameHeight)? frameWidth : frameHeight;
      width = shortSide * MAX_BOARD_FILL;
      height = width;

      pos.x = (int)(((double)frameWidth / 2) - (width / 2));
      pos.y = (int)(((double)frameHeight / 2) - (height / 2));

      tileWidth = width / NUM_TILES;
      tileHeight = tileWidth;
   }

   public void setValidTiles(ArrayList<Point> valids) {
      validTiles = valids;
   }

   //getter methods
   public int getNumTiles() {
      return NUM_TILES;
   }
   public Point getPos() {
      return pos;
   }
   public double getTileWidth() {
      return tileWidth;
   }
   public double getTileHeight() {
      return tileHeight;
   }
   public double getWidth() {
      return width;
   }
   public double getHeight() {
      return height;
   }
}
