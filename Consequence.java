import java.util.ArrayList;

public class Consequence {

   public Move move; //the move this consequence is resultant of
   public int distance; //number of turns in the future this consequence is
   public Player player; //player who's persiective is to be evaluated
   public int allegiance;

   public Consequence(Move aMove, int aDistance, Player aPlayer) {
      move = aMove;
      distance = aDistance;
      player = aPlayer;
      allegiance = (move.player.getTeam() == player.getTeam())? 1 : -1;
   }
}
