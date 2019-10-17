import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public class Knight extends Piece {
   private final static String[] IMGS = {"images/whites/knight.png", "images/blacks/knight.png"};

   private boolean hasMoved = false; //if the piece has moved since the start of the game

   public Knight(Board board, int team, int xTile, int yTile) {
      super(board, team, xTile, yTile);
      super.setImg(IMGS[team]);
   }

   //this method would give too much away from the EthanAI, so i've left it out.
   //basically it just highlights all the possible locations a piece can move to.
   public ArrayList<Point> validTiles() {
      ArrayList<Point> valids = new ArrayList<Point>();
      //valids.add(new Point(4, 4));
      return valids;
   }
}
