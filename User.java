import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public class User extends Player {

   private Point mousePos; //stores the coordinates of the mouse
   private boolean mouseDown = false; //stores if the mouse button is pressed or not
   private Piece tracking = null; //the piece currently tracking the mouse

   public User(Board board, int startPos, int team) { //player constructor
      super(board, startPos, team);
   }

   public void draw(Graphics2D g2) { //draws all the pieces except for the tracking one
      for(Piece p : getAlives()) {
         if(p != tracking) {
            p.draw(g2);
         }
      }
      for(Piece p : getDeads()) {
         p.draw(g2);
      }
   }

   public void makeMove() { //requesting a move from the user
      if(mousePos != null) {
         if(tracking == null && mouseDown && onBoard(tileAt(mousePos))) {
            pickUpPiece();
         } else if(tracking != null && mouseDown == false) {
            dropPiece();
         }
         if(tracking != null) {
            tracking.setMousePos(mousePos);
         }
      }
   }

   private void pickUpPiece() { //make a piece begin tracking the mouse
      tracking = pieceAt(tileAt(mousePos));
      if(tracking != null) {
         tracking.setCurrentlyTracking(true);
         tracking.resize();
         getBoard().setValidTiles(tracking.validTiles());
      }
   }

   private void dropPiece() { //ends a piece's tracking of the mouse
      tracking.setCurrentlyTracking(false);
      tracking.resize();
      getBoard().setValidTiles(null);

      boolean isValid = false;
      for(Point p : tracking.validTiles()) {
         if(p.equals(tileAt(mousePos))) isValid = true; //asserts that the destination is valid
      }
      if(onBoard(tileAt(mousePos)) && pieceAt(tileAt(mousePos)) == null && isValid) {
         executeMove(tracking, tileAt(mousePos)); //move the piece to its new location
      }
      tracking = null;
   }

   public void setMousePos(Point aMousePos) {
      mousePos = aMousePos;
   }
   public void setMouseDown(boolean setTo) {
      mouseDown = setTo;
   }

   public Piece getTracking() {
      return tracking;
   }
}
