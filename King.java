import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public class King extends Piece {
   private final static String[] IMGS = {"images/whites/king.png", "images/blacks/king.png"};

   private boolean hasMoved = false; //if the piece has moved since the start of the game

   public King(Board board, Player player, int xTile, int yTile) {
      super(board, player, xTile, yTile);
      setImg(IMGS[getTeam()]);
   }

   public void setPosition(Point tile) { //overrides piece method to include a hasMoved status
      super.setPosition(tile);
      hasMoved = true;
   }

   //returns all the possible locations the piece can move to.
   public ArrayList<Point> validTiles() { //TODO: check if king is in check
      ArrayList<Point> valids = new ArrayList<Point>();

      Point p = new Point(getTile().x + 1, getTile().y);
      if(isValid(p)) valids.add(p);
      p = new Point(getTile().x + 1, getTile().y + 1);
      if(isValid(p)) valids.add(p);
      p = new Point(getTile().x, getTile().y + 1);
      if(isValid(p)) valids.add(p);
      p = new Point(getTile().x - 1, getTile().y + 1);
      if(isValid(p)) valids.add(p);
      p = new Point(getTile().x - 1, getTile().y);
      if(isValid(p)) valids.add(p);
      p = new Point(getTile().x - 1, getTile().y - 1);
      if(isValid(p)) valids.add(p);
      p = new Point(getTile().x, getTile().y - 1);
      if(isValid(p)) valids.add(p);
      p = new Point(getTile().x + 1, getTile().y - 1);
      if(isValid(p)) valids.add(p);

      valids.addAll(validCastles());

      return valids;
   }

   public ArrayList<Point> validCastles() { //returns ArrayList of tiles to castle to
      ArrayList<Point> valids = new ArrayList<Point>();

      if(hasMoved == false) { //castling support
         Piece rook = pieceAt(new Point(0, getTile().y));
         if(rook instanceof Rook && ((Rook) rook).hasMoved() == false) {
            if(isValid(new Point(getTile().x - 1, getTile().y)) &&
            isValid(new Point(getTile().x - 2, getTile().y)) &&
            isValid(new Point(getTile().x - 3, getTile().y))) {
               valids.add(new Point(getTile().x - 2, getTile().y));
            }
            if(isValid(new Point(getTile().x + 1, getTile().y)) &&
            isValid(new Point(getTile().x + 2, getTile().y))) {
               valids.add(new Point(getTile().x + 2, getTile().y));
            }
         }
      }
      return valids;
   }

   private boolean isValid(Point p) {
      return (onBoard(p) && pieceAt(p) == null);
   }

   /* TODO: HERE'S THE PROBLEM, ALRIGHT
      in order to determine checkmate (win condition), i need to determine every possible move
      while this is easy now, my AI is going to need to project several turns ahead.
      this requires CLONING the players, and then modifying the clones.

      Point is, i need to make the players and pieces as lightweight as possible. Offload tasks
      to some idea of "piece". all the "onboard" and "pieceAt" (maybe not pieceAt idk) should be there
   */
   public boolean isInCheck() { //returns true if the king is in check, false otherwise
      for(Piece p : getOpponent().getAlives()) {
         for(Point v : p.validTiles()) {
            if(v.equals(getTile())) return true;
         }
      }
      return false;
   }
}
