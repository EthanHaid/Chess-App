import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public class Rook extends Piece {
   private final static String[] IMGS = {"images/whites/rook.png", "images/blacks/rook.png"};

   private boolean hasMoved = false; //if the piece has moved since the start of the game

   public Rook(Board board, Player player, int xTile, int yTile) {
      super(board, player, xTile, yTile);
      setImg(IMGS[getTeam()]);
   }

   public void setPosition(Point tile) { //overrides piece method to include a hasMoved status
      super.setPosition(tile);
      hasMoved = true;
   }

   //returns all the possible locations the piece can move to.
   public ArrayList<Point> validTiles() {
      ArrayList<Point> valids = new ArrayList<Point>();
      valids.addAll(searchDirection(new Point(getTile()), new Point(1, 0)));
      valids.addAll(searchDirection(new Point(getTile()), new Point(-1, 0)));
      valids.addAll(searchDirection(new Point(getTile()), new Point(0, 1)));
      valids.addAll(searchDirection(new Point(getTile()), new Point(0, -1)));
      return valids;
   }

   private ArrayList<Point> searchDirection(Point spot, Point dir) { //finds all the valid spots in a direction
      ArrayList<Point> valids = new ArrayList<Point>();
      boolean done = false;
      do{
         spot.x += dir.x;
         spot.y += dir.y;
         if(onBoard(spot) && pieceAt(spot) == null) {
            valids.add(new Point(spot));
         } else {
            done = true;
         }
         if(getOpponent().pieceAt(spot) != null) {
            done = true;
         }
      } while(done == false);
      return valids;
   }

   public boolean hasMoved() {
      return hasMoved;
   }
}
