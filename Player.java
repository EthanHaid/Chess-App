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

   public void executeMove(Piece piece, Point point) {
     executeMove(piece, point, null);
   }

   //TODO: update readme to include 3rd param option
   public void executeMove(Piece piece, Point point, String promoteTo) { //moves a piece and kills the opposition
      moveWasMade = true;

      if(piece instanceof King && ((King) piece).validCastles() != null) { //detect if castling
         for(Point p : ((King) piece).validCastles()) {
            if(p.equals(point)) { //script for castling TODO: fix for if there is no castle at that side???
               if(point.x < (board.getNumTiles() / 2)) { //castling with left piece
                  pieceAt(new Point(0, point.y))
                  .setPosition(new Point(point.x + 1, point.y));
               } else { //castling with right piece
                  pieceAt(new Point(board.getNumTiles() - 1, point.y))
                  .setPosition(new Point(point.x - 1, point.y));
               }
            }
         }
      }

      piece.setPosition(point); //move the piece

      if(piece instanceof Pawn && point.y == ((startPos == 0)? (board.getNumTiles() - 1) : 0)) {
        //TODO: it might be worth making cases pieces Pieces rather than Strings

        piece.setIsAlive(false); //remove the old piece
        alives.remove(piece);

        Piece newPiece;
        switch (promoteTo) {
          case "Rook":
            newPiece = new Rook(board, this, point.x, point.y);
            break;
          case "Knight":
            newPiece = new Knight(board, this, point.x, point.y);
            break;
          case "Bishop":
            newPiece = new Bishop(board, this, point.x, point.y);
            break;
          case "Queen":
            newPiece = new Queen(board, this, point.x, point.y);
            break;
          default:
            newPiece = piece;
            newPiece.setIsAlive(true);
            break;
        }
        alives.add(newPiece);
        newPiece.setImg();
        newPiece.resize();
      }

      piece = getOpponent().pieceAt(point); //takes any opponent piece
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

      for(int i=0; i<board.getNumTiles(); i++) { //add the pawns
         alives.add(new Pawn(board, this, i, (startPos * (board.getNumTiles() - 3) + 1)));
      }

      //add the powerful pieces
      int i=0;
      alives.add(new Rook(board, this, i++, (startPos * (board.getNumTiles() - 1))));
      alives.add(new Knight(board, this, i++, (startPos * (board.getNumTiles() - 1))));
      alives.add(new Bishop(board, this, i++, (startPos * (board.getNumTiles() - 1))));
      alives.add(new Queen(board, this, i++, (startPos * (board.getNumTiles() - 1))));
      alives.add(new King(board, this, i++, (startPos * (board.getNumTiles() - 1))));
      alives.add(new Bishop(board, this, i++, (startPos * (board.getNumTiles() - 1))));
      alives.add(new Knight(board, this, i++, (startPos * (board.getNumTiles() - 1))));
      alives.add(new Rook(board, this, i++, (startPos * (board.getNumTiles() - 1))));

      for(Piece p : alives) { //initalize piece images
         p.setImg();
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

   public boolean onBoard(Point p) { //returns if a tile is upon the board
      if(p.x < 0 || p.y < 0 || p.x >= board.getNumTiles() || p.y >= board.getNumTiles()) return false;
      return true;
   }

   //setter methods
   public void setOpponent(Player aOpponent) {
      opponent = aOpponent;
   }
   public void setMoveWasMade(boolean setTo) {
      moveWasMade = setTo;
   }
   public void setAlives(ArrayList<Piece> aAlives) {
      alives = aAlives;
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

   public abstract void makeMove() throws InterruptedException; //players have to have makeMove()
}
