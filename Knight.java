import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public class Knight extends Piece {
   private final static String[] IMGS = {"images/whites/knight.png", "images/blacks/knight.png"};

   public Knight(Board board, Player player, int xTile, int yTile) {
      super(board, player, xTile, yTile);
      setImg(IMGS[getTeam()]);
   }

   //returns all the possible locations the piece can move to.
   public ArrayList<Point> validTiles() {
      ArrayList<Point> valids = new ArrayList<Point>();

      Point p = new Point(getTile().x + 1, getTile().y + 2);
      if(onBoard(p) && pieceAt(p) == null) valids.add(p);
      p = new Point(getTile().x + 2, getTile().y + 1);
      if(onBoard(p) && pieceAt(p) == null) valids.add(p);
      p = new Point(getTile().x + 2, getTile().y - 1);
      if(onBoard(p) && pieceAt(p) == null) valids.add(p);
      p = new Point(getTile().x + 1, getTile().y - 2);
      if(onBoard(p) && pieceAt(p) == null) valids.add(p);
      p = new Point(getTile().x - 1, getTile().y - 2);
      if(onBoard(p) && pieceAt(p) == null) valids.add(p);
      p = new Point(getTile().x - 2, getTile().y - 1);
      if(onBoard(p) && pieceAt(p) == null) valids.add(p);
      p = new Point(getTile().x - 2, getTile().y + 1);
      if(onBoard(p) && pieceAt(p) == null) valids.add(p);
      p = new Point(getTile().x - 1, getTile().y + 2);
      if(onBoard(p) && pieceAt(p) == null) valids.add(p);

      return valids;
   }
}
