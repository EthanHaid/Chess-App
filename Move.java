import java.awt.Point;

public class Move {

   public Player player;
   public Piece piece;
   public Point point;
   public int score;
   //TODO: add upgrade string for pawns

   public Move(Player aPlayer, Piece aPiece, Point aPoint) {
      player = aPlayer;
      piece = aPiece;
      point = aPoint;
      score = 0;
   }

   public void updateScore(int val, int distance) {

   }
}
