import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public class Pawn extends Piece {

   private final static String[] IMGS = {"images/whites/pawn.png", "images/blacks/pawn.png"};

   private final int direction; //the direction the piece is facing.

   private boolean hasMoved = false; //if the piece has moved since the start of the game
   private ArrayList<ArrayList<Piece>> pieces; //all the pieces on the board
   private ArrayList<Point> valids; //the tiles which the piece can move to

   public Pawn(Board board, int team, int xTile, int yTile) {
      super(board, team, xTile, yTile);
      super.setImg(IMGS[team]);
      direction = (team == 0)? -1 : 1;
   }

   public ArrayList<Point> validTiles() {
      pieces = new ArrayList<ArrayList<Piece>>(); TODO: //link this to pieces properly
      pieces.add(new ArrayList<Piece>());
      pieces.add(new ArrayList<Piece>());
      valids = new ArrayList<Point>();
      add(super.getTile().x, super.getTile().y + direction);
      if(!hasMoved) add(super.getTile().x, super.getTile().y + (2 * direction));
      //TODO: impliment
      return valids;
   }

   public void setPosition(Point tile) {
      hasMoved = true;
      super.setPosition(tile);
   }


   private void add(int x, int y) { //checks if occupied by friendly piece, if not, adds piece
      Point point = new Point(x, y);
      if(!pieceAt(super.getTeam(), point)) valids.add(point);
      //TODO: THIS WON'T WORK BECAUSE ONLY THE HORSE CAN JUMP. THIS ENTIRE IDEA IS TRASH SO LIKE PLEASE THROW IT OUT. THANKS. RIP.
   }

   //returns if there is a piece of a given team at a given tile
   private boolean pieceAt(int team, Point tile) {
      if(tile == null) return false;
      for(Piece p : pieces.get(team)) {
         if(tile.equals(p.getTile())) {
            return true;
         }
      }
      return false;
   }
}
