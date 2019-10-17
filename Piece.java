import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
//import javax.swing.*;

public class Piece {

   protected static final Color OUTLINE_COLOR = Color.BLACK;
   //multiplier for size of piece while tracking the mouse
   protected static final double TRACKING_MULTIPLIER = 1.15;
   protected static final String WHITE = "white";

   protected final Board board;
   protected final Color fillColor;
   protected final String color;
   //url of the image
   private final String imgURL;
   private BufferedImage img;

   //number of the tile the piece is on
   protected int xTile;
   protected int yTile;
   //the x, y position of the piece
   protected double x;
   protected double y;
   private double w;
   private double h;
   //if the mouse has selected this piece
   protected boolean currentlyTracking = false;
   //if the piece has moved since the start of the game
   protected boolean hasMoved = false;
   //the x, y position of the mouse, for use if tracking the mouse
   protected int mouseX = 0;
   protected int mouseY = 0;

   //constructor
   //(playing board, color of piece, x tile piece is on, y tile piece is on)
   public Piece(Board boardA, String colorA, int xTileA, int yTileA, String imgURLA) {
      board = boardA;
      color = colorA;
      if(color.equals(WHITE)){
         fillColor = Color.WHITE;
      } else{
         fillColor = Color.BLACK;
      }
      xTile = xTileA;
      yTile = yTileA;
      imgURL = imgURLA;
      try{
         img = ImageIO.read(new File(imgURL));
      } catch(IOException e){
         //TODO: is this really what i want in here?
         e.printStackTrace();
      }
   }

   //draw method
   public void draw(Graphics2D g2) {
      if(currentlyTracking){
         x = mouseX - (board.getTileWidth() * TRACKING_MULTIPLIER) / 2;
         y = mouseY - (board.getTileWidth() * TRACKING_MULTIPLIER) / 2;
         w = board.getTileWidth() * TRACKING_MULTIPLIER;
         h = board.getTileHeight() * TRACKING_MULTIPLIER;
      }else{
         x = board.getX() + (xTile * board.getTileWidth());
         y = board.getY() + (yTile * board.getTileHeight());
         w = board.getTileWidth();
         h = board.getTileHeight();
      }
      //TODO: this is the line that's gotta be avoided at all costs plis
      g2.drawImage(img.getScaledInstance((int)w, (int)h, Image.SCALE_SMOOTH), (int)x, (int)y, null);
   }

   public void setCurrentlyTracking(boolean setTo){
      currentlyTracking = setTo;
   }

   public void setMouseX(int x){
      mouseX = x;
   }
   public void setMouseY(int y){
      mouseY = y;
   }
   public void setPosition(int xTileA, int yTileA){
      xTile = xTileA;
      yTile = yTileA;
   }

   public int getXTile(){
      return xTile;
   }
   public int getYTile(){
      return yTile;
   }
   public String getColor(){
      return color;
   }

   public boolean isCurrentlyTracking(){
      return currentlyTracking;
   }
}
