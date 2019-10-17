import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Point;
import java.util.ArrayList;
import javax.swing.*;

public abstract class Piece {

   private static final double TRACKING_MULTIPLIER = 1.15; //relative size of a selected piece

   private final Board board; //stores the board reference
   private final int team; // stores the pieces team. 0 == white, 1 == black

   private Image img; //the sized image of the piece
   private boolean currentlyTracking = false; //if the piece is tracking the mouse
   private Point mouse; //last known coordinate of the mouse
   private Point tile; //location of the piece
   private Point pos; //x,y position of the top left corner of the piece
   private int w; //width and height of the piece
   private int h;


   public Piece(Board aBoard, int aTeam, int xTile, int yTile) {
      board = aBoard;
      team = aTeam;
      tile = new Point(xTile, yTile);
      pos = new Point();
      mouse = new Point();
   }


   public void draw(Graphics2D g2) {
      if(currentlyTracking) {
         pos.x = (int)(mouse.x - (board.getTileWidth() * TRACKING_MULTIPLIER) / 2);
         pos.y = (int)(mouse.y - (board.getTileWidth() * TRACKING_MULTIPLIER) / 2);
      } else {
         pos.x = (int)(board.getPos().x + (tile.x * board.getTileWidth()));
         pos.y = (int)(board.getPos().y + (tile.y * board.getTileHeight()));
      }
      g2.drawImage(img, pos.x, pos.y, null);
   }

   public void resize() {
      //TODO: activate the tracking multiplier. also request a resize of tracked piece after begin or end tracking. make sure this isn't too laggy. hope.
      if(currentlyTracking) {
         w = (int)(board.getTileWidth() * TRACKING_MULTIPLIER);
         h = (int)(board.getTileHeight() * TRACKING_MULTIPLIER);
      } else {
         w = (int)board.getTileWidth();
         h = (int)board.getTileHeight();
      }
      img = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
   }

   public void setImg(String url) {
      try{
         img = ImageIO.read(new File(url));
      } catch(IOException e){
         //TODO: is this really what i want in here?
         e.printStackTrace();
      }
   }

   //setter methods
   public void setCurrentlyTracking(boolean setTo) {
      currentlyTracking = setTo;
   }
   public void setPosition(Point aTile) {
      tile = aTile;
   }
   public void setMouse(Point aMouse) {
      mouse = aMouse;
   }

   //getter methods
   public Point getTile() {
      return tile;
   }
   public int getTeam() {
      return team;
   }

   public abstract ArrayList<Point> validTiles();
}
