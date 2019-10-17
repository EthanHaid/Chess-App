import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public abstract class Player {

   private final Board board; //playing board
   private final int startPos; // 0 for starting on top, 1 for starting on bottom
   private final int team; // 0 for white-team, or 1 for black-team

   private Player opponent; //the opposing player
   private ArrayList<Piece> alives; //currently alive pieces
   private ArrayList<Piece> deads; //currently dead pieces
   private boolean moveWasMade;

   public Player(Board aBoard, int aStartPos, int aTeam) { //player constructor
      board = aBoard;
      startPos = aStartPos;
      team = aTeam;
   }

   public void executeMove(Piece piece, Point point) { //moves a piece and kills the opposition
      moveWasMade = true;
      piece.setPosition(point);
      piece = getOpponent().pieceAt(point);
      if(piece != null) {
         piece.setIsAlive(false);
         getOpponent().getAlives().remove(piece);
         getOpponent().getDeads().add(piece);
      }
   }

   public void draw(Graphics2D g2) {
      for(Piece p : alives) {
         p.draw(g2);
      }
      for(Piece p : deads) {
         p.draw(g2);
      }
   }

   public void resize() {
      for(Piece p : alives) {
         p.resize();
      }
      for(Piece p : deads) {
         p.resize();
      }
   }

   public void createPieces() {
      alives = new ArrayList<Piece>();
      deads = new ArrayList<Piece>();

      //add the powerful pieces
      int i=0;
      alives.add(new Rook(board, team, i++, (startPos * (board.getNumTiles() - 1))));
      alives.add(new Knight(board, team, i++, (startPos * (board.getNumTiles() - 1))));
      alives.add(new Bishop(board, team, i++, (startPos * (board.getNumTiles() - 1))));
      alives.add(new Queen(board, team, i++, (startPos * (board.getNumTiles() - 1))));
      alives.add(new King(board, team, i++, (startPos * (board.getNumTiles() - 1))));
      alives.add(new Bishop(board, team, i++, (startPos * (board.getNumTiles() - 1))));
      alives.add(new Knight(board, team, i++, (startPos * (board.getNumTiles() - 1))));
      alives.add(new Rook(board, team, i++, (startPos * (board.getNumTiles() - 1))));

      for(i=0; i<board.getNumTiles(); i++) { //add the pawns
         alives.add(new Pawn(board, team, i, (startPos * (board.getNumTiles() - 3) + 1)));
      }
   }

   public Piece pieceAt(Point point) { //returns the piece at a given tile
      if(point == null) return null;
      for(Piece p : alives) {
         if(point.equals(p.getTile())) {
            return p;
         }
      }
      return null;
   }

   public Point tileAt(Point p) { //returns the  tile which the coordinate is upon
      if(p == null || p.x < board.getPos().x || p.y < board.getPos().y ||
         p.x > (board.getPos().x + board.getWidth()) || p.y > (board.getPos().y + board.getHeight())) {
         return new Point(-1, -1);
      }
      return new Point(((int)(p.x - board.getPos().x) / (int) board.getTileWidth()),
      ((int)(p.y - board.getPos().y) / (int) board.getTileHeight()));
   }

   public boolean onBoard(Point p) { //returns if a coordinate is hovering over the board
      if(p.x < 0 || p.y < 0) return false;
      return true;
   }

   //setter methods
   public void setOpponent(Player aOpponent) {
      opponent = aOpponent;
   }
   public void setMoveWasMade(boolean setTo) {
      moveWasMade = setTo;
   }

   //getter methods
   public int getTeam() {
      return team;
   }
   public int getStartPos() {
      return startPos;
   }
   public Board getBoard() {
      return board;
   }
   public ArrayList<Piece> getAlives() {
      return alives;
   }
   public ArrayList<Piece> getDeads() {
      return deads;
   }
   public Player getOpponent() {
      return opponent;
   }
   public boolean getMoveWasMade() {
      return moveWasMade;
   }

   public abstract void makeMove(); //players have to have makeMove()
}
