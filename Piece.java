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
   private final Player player; //stores the player reference

   private Image fullSizeImg; //the unscaled image of the piece
   private Image img; //the sized image of the piece
   private boolean currentlyTracking = false; //if the piece is tracking the mouse
   private boolean isAlive = true;
   private Point mouse; //last known coordinate of the mouse
   private Point tile; //location of the piece
   private Point pos; //x,y position of the top left corner of the piece
   private int w; //width and height of the piece
   private int h;


   public Piece(Board aBoard, Player aPlayer, int xTile, int yTile) {
      board = aBoard;
      player = aPlayer;
      tile = new Point(xTile, yTile);
      pos = new Point();
      mouse = new Point();
   }

   public void draw(Graphics2D g2) {
      if(isAlive) {
         if(currentlyTracking) {
            pos.x = (int)(mouse.x - (board.getTileWidth() * TRACKING_MULTIPLIER) / 2);
            pos.y = (int)(mouse.y - (board.getTileWidth() * TRACKING_MULTIPLIER) / 2);
         } else {
            pos.x = (int)(board.getPos().x + (tile.x * board.getTileWidth()));
            pos.y = (int)(board.getPos().y + (tile.y * board.getTileHeight()));
         }
         g2.drawImage(img, pos.x, pos.y, null);
      } else {
         //TODO: if you want to make a graveyard, draw the piece's location here. move the g2.drawImage (from above) down below the else.
      }

   }

   public void resize() {
      if(currentlyTracking) {
         w = (int)(board.getTileWidth() * TRACKING_MULTIPLIER);
         h = (int)(board.getTileHeight() * TRACKING_MULTIPLIER);
      } else {
         w = (int)board.getTileWidth();
         h = (int)board.getTileHeight();
      }
      img = fullSizeImg.getScaledInstance(w, h, Image.SCALE_SMOOTH);
   }

   public void setImg(String url) {
      try{
         fullSizeImg = ImageIO.read(new File(url));
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
   public void setMousePos(Point aMouse) {
      mouse = aMouse;
   }
   public void setIsAlive(boolean setTo) {
      isAlive = setTo;
   }

   //getter methods
   public Point getTile() {
      return tile;
   }
   public Board getBoard() {
      return board;
   }
   public Player getPlayer() {
      return player;
   }
   public Player getOpponent() {
      return player.getOpponent();
   }
   public int getTeam() {
      return player.getTeam();
   }
   public int getStartPos() {
      return player.getStartPos();
   }
   public boolean onBoard(Point p) {
      return player.onBoard(p);
   }
   public Piece pieceAt(Point p) {
      return player.pieceAt(p);
   }

   public abstract ArrayList<Point> validTiles();
}
