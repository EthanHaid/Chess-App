import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

public class AI extends Player implements Runnable{

   private static final int SEARCH_TIME = 5000; //time to search for best move, in ms
   private static final int PREDICTION_CONFIDENCE = 4; //confidence in predicting future turns

   private ArrayList<Move> possibles;
   private LinkedList<Consequence> consequences;

   public AI(Board board, int startPos, int team) { //AI constructor
      super(board, startPos, team);
   }

   public void makeMove() throws InterruptedException { //function called when a move is required

      possibles = new ArrayList<Move>(); //add all possible moves to the array TODO: include variations of pawn upgrades
      consequences = new LinkedList<Consequence>();
      for(Piece piece : getAlives()) {
         for(Point point : piece.validTiles()) {
            Move move = new Move(this, piece, point);
            possibles.add(move);
            addToConsequences(move, 0, this, piece, point);
         }
      }

      //TODO: add support for no valid moves

      Thread t = new Thread(this); //begin the search
      t.start();
      Thread.sleep(SEARCH_TIME); //TODO: add functionality to check if thread finished early

      if(t.isAlive()) {
         t.interrupt();
         t.join();
      }

      Move best = possibles.get(0);

      for(Move m : possibles) {
         if(m.score > best.score) {
            best = m;
         }
      }

      executeMove(best.piece, best.point); //TODO: upgrading a pawn support
   }

   public void run() { //run the search for best move
      while(!Thread.interrupted() && consequences.size()>0){
         //pop a consequence off the queue to examine
         Consequence cons = consequences.pop();
         //add to or subtract from the score of the move the consequence is affiliated with. weighted based on # of turns into the future
         if(cons.distance == 0) {
            cons.move.score = utility(cons);
         } else {
            cons.move.score += (int) (cons.allegiance * utility(cons) * (1 / (1.0 / PREDICTION_CONFIDENCE) * cons.distance + 1));
         }
         //add all the valid retaliation elements to the queue
         for(Piece piece : cons.player.getOpponent().getAlives()) {
            for(Point point : piece.validTiles()) {
               addToConsequences(cons.move, cons.distance+1, cons.player.getOpponent(), piece, point);
            }
         }
      }
   }

   //takes consequence, returns a situation utility score
   private int utility(Consequence c) {
      int sum=0;
      for(Piece p : c.player.getAlives()) {
         sum += p.getValue(); //TODO: relative to number of pieces you have? also the opponent has?
      }
      for(Piece p : c.player.getOpponent().getAlives()) {
         sum -= p.getValue();
      }
      return sum;
   }

   private void addToConsequences(Move move, int distance, Player player, Piece piece, Point point) {
      Player newPlayer = new Simulation(player.getBoard(), player.getStartPos(), player.getTeam());

      //TODO: set opponent (remember remove taken pieces) ps i think this is done now? it's giving errors tho
      Player newOpponent = new Simulation(player.getOpponent().getBoard(), player.getOpponent().getStartPos(), player.getOpponent().getTeam());
      ArrayList<Piece> newOpponentPieces = new ArrayList<Piece>();
      for(Piece p : player.getOpponent().getAlives()) {
         if(!p.getTile().equals(point)) {
            Piece newOpponentPiece = p.copy();
            newOpponentPiece.setPlayer(newOpponent);
            newOpponentPieces.add(newOpponentPiece);
         }
      }
      newOpponent.setAlives(newOpponentPieces);
      newPlayer.setOpponent(newOpponent);
      newOpponent.setOpponent(newPlayer);

      ArrayList<Piece> newPieces = new ArrayList<Piece>();
      for(Piece p : player.getAlives()) {
         Piece newPiece = p.copy();
         if(p == piece) { //if this is the piece that is getting moved
            newPiece.setPosition(point);
         }
         //TODO: castling and pawn upgrade logic
         newPiece.setPlayer(newPlayer);
         newPieces.add(newPiece);
      }
      newPlayer.setAlives(newPieces);
      consequences.add(new Consequence(move, distance, newPlayer));
   }
}
