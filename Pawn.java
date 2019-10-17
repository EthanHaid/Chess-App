import java.util.ArrayList;

public class Pawn extends Piece {

   private final static String IMG =
      "images/queen.jpeg";

   public Pawn(Board boardA, String colorA, int xTileA, int yTileA){
      super(boardA, colorA, xTileA, yTileA, IMG);
      //TODO: assign IMG to either images/whites/pawn.png or images/blacks/pawn.png depending on color.
   }
}
