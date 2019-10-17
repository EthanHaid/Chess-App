import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Line2D;
import java.awt.Color;

public class Board {

   //number of tiles per side
   private static final int NUM_TILES = 8;
   //maximum fraction of frame to be filled by the board
   private static final double MAX_BOARD_FILL = (double) 9 / 10;
   //color of the dark tiles
   private static final Color FILL_COLOR = Color.GRAY;
   //color of the outlines
   private static final Color BORDER_COLOR = Color.BLACK;

   //coords of the upper left corner
   private double x;
   private double y;
   //width and height of the board
   private double width;
   private double height;
   //width and height of each tile
   private double tileWidth;
   private double tileHeight;

   public void draw(Graphics2D g2, int frameWidth, int frameHeight) {
      //initializes board's positioning
      int shortSide = (frameWidth < frameHeight)? frameWidth : frameHeight;
      width = shortSide * MAX_BOARD_FILL;
      height = width;

      x = (frameWidth / 2) - (width / 2);
      y = (frameHeight / 2) - (height / 2);

      tileWidth = width / NUM_TILES;
      tileHeight = tileWidth;

      //draws outline of board
      Rectangle2D.Double board = new Rectangle2D.Double(x, y, width, height);
      g2.setColor(BORDER_COLOR);
      g2.draw(board);

      //draws each tile
      Rectangle2D.Double tile;
      double xDraw;
      double yDraw = y;
      for(int i = 0; i < NUM_TILES; i++){
         xDraw = x;
         for(int j = 0; j < NUM_TILES; j++){
            if(i % 2 != j % 2){
               tile = new Rectangle2D.Double(xDraw, yDraw, tileWidth, tileHeight);
               g2.setColor(BORDER_COLOR);
               g2.draw(tile);
               g2.setColor(FILL_COLOR);
               g2.fill(tile);
            }
            xDraw += tileWidth;
         }
         yDraw += tileHeight;
      }
   }

   //getter methods
   public int getNumTiles(){
      return NUM_TILES;
   }
   public double getX(){
      return x;
   }
   public double getY(){
      return y;
   }
   public double getWidth(){
      return width;
   }
   public double getHeight(){
      return height;
   }
   public double getTileWidth(){
      return tileWidth;
   }
   public double getTileHeight(){
      return tileHeight;
   }
}
