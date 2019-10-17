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
               //TODO: do the short castle and move the castle itself pls
            }
         }
      }

      return valids;
   }

   private boolean isValid(Point p) {
      return (onBoard(p) && pieceAt(p) == null);
   }
}
