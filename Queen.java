import java.awt.Graphics2D;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;

public class Queen extends Piece {
   private final static String[] IMGS = {"images/whites/queen.png", "images/blacks/queen.png"};
   private final static int PIECE_VALUE = 500;

   public Queen(Board board, Player player, int xTile, int yTile) {
      super(board, player, xTile, yTile);
   }

   //returns all the possible locations the piece can move to.
   public ArrayList<Point> validTiles() {
      ArrayList<Point> valids = new ArrayList<Point>();
      valids.addAll(searchDirection(new Point(getTile()), new Point(1, 1)));
      valids.addAll(searchDirection(new Point(getTile()), new Point(1, -1)));
      valids.addAll(searchDirection(new Point(getTile()), new Point(-1, 1)));
      valids.addAll(searchDirection(new Point(getTile()), new Point(-1, -1)));
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
      return new Queen(getBoard(), getPlayer(), getTile().x, getTile().y);
   }

   public int getValue() {
      return PIECE_VALUE;
   }
}
