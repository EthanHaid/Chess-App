import java.awt.Graphics2D;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;

public class Pawn extends Piece {
   private final static String[] IMGS = {"images/whites/pawn.png", "images/blacks/pawn.png"};
   private final static int PIECE_VALUE = 100;

   private final int dir; //the direction of "foreward movement": 1 for down, -1 for up

   private boolean hasMoved = false; //if the piece has moved since the start of the game

   public Pawn(Board board, Player player, int xTile, int yTile) {
      super(board, player, xTile, yTile);
      dir = (getStartPos() == 0)? 1 : -1;
   }

   public void setPosition(Point tile) { //overrides piece method to include a hasMoved status
      super.setPosition(tile);
      hasMoved = true;
   }

   //returns all the possible locations the piece can move to.
   public ArrayList<Point> validTiles() {
      ArrayList<Point> valids = new ArrayList<Point>();

      Point p = new Point(getTile().x, getTile().y + dir);
      if(onBoard(p) && pieceAt(p) == null &&
         getOpponent().pieceAt(p) == null) {
            valids.add(p); //adds the tile directly in front of the piece.
      }
      if(hasMoved == false && valids.isEmpty() == false) {
         p = new Point(getTile().x, getTile().y + (2 * dir));
         if(onBoard(p) && pieceAt(p) == null && getOpponent().pieceAt(p) == null) {
               valids.add(p); //adds the tile 2 spaces in front of the piece.
         }
      }

      p = new Point(getTile().x + 1, getTile().y + dir);
      if(onBoard(p) && getOpponent().pieceAt(p) != null) {
            valids.add(p); //adds a diagonal piece take
      }
      p = new Point(getTile().x - 1, getTile().y + dir);
      if(onBoard(p) && getOpponent().pieceAt(p) != null) {
            valids.add(p); //adds a diagonal piece take
      }
      //TODO: include en-passant if you want
      return valids;
   }

   //set the image to the appropriate file import
   public void setImg() {
      try{
         super.fullSizeImg = ImageIO.read(new File(IMGS[getTeam()]));
      } catch(IOException e){
         //TODO: is this really what i want in here?
         e.printStackTrace();
      }
   }

   public Piece copy() { //returns a copy of this piece
      Pawn p = new Pawn(getBoard(), getPlayer(), getTile().x, getTile().y);
      p.setHasMoved(hasMoved);
      return p;
   }

   public void setHasMoved(boolean setTo) {
      hasMoved = setTo; //TODO you are here
   }

   public int getValue() {
      return PIECE_VALUE;
   }
}
