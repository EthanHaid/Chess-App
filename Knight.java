import java.awt.Graphics2D;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;

public class Knight extends Piece {
   private final static String[] IMGS = {"images/whites/knight.png", "images/blacks/knight.png"};
   private final static int PIECE_VALUE = 350;

   public Knight(Board board, Player player, int xTile, int yTile) {
      super(board, player, xTile, yTile);
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
      return new Knight(getBoard(), getPlayer(), getTile().x, getTile().y);
   }

   public int getValue() {
      return PIECE_VALUE;
   }
}
