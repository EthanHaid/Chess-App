import javax.swing.JComponent;
import java.util.Timer;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.event.*;

public class BoardComponent extends JComponent implements MouseListener {

   private static final int REFRESH_RATE = 30;
   private static final String P1_COLOR = "black";
   private static final String P2_COLOR = "white";

   private final Board board;
   private final ArrayList<ArrayList<Piece>> pieces;

   private boolean mouseDown = false;
   private Piece tracking = null;
   private Piece dead = null;

   //BoardComponent constructor
   public BoardComponent(){
      //creates a new timer, update() every 30 ms
      java.util.Timer timer = new java.util.Timer();
      timer.scheduleAtFixedRate(new update(), REFRESH_RATE, REFRESH_RATE);

      //mouse click listener
      addMouseListener(this);

      //creates board and pieces array
      board = new Board();
      pieces = new ArrayList<ArrayList<Piece>>();
      pieces.add(new ArrayList<Piece>());
      pieces.add(new ArrayList<Piece>());

      //ArrayList of white pieces
      //TODO: CREATE A BUNCH OF STATIC CLASSES FOR EACH TYPE OF PIECE. PASS THE CLASS FOR REFERENCE BY PIECE OBJECTS HERE
      /*
      pieces.get(0).add(new Piece(board, P1_COLOR, 0, 0));
      pieces.get(0).add(new Piece(board, P1_COLOR, 1, 0));
      pieces.get(0).add(new Piece(board, P1_COLOR, 2, 0));
      pieces.get(0).add(new Piece(board, P1_COLOR, 3, 0));
      pieces.get(0).add(new Piece(board, P1_COLOR, 4, 0));
      pieces.get(0).add(new Piece(board, P1_COLOR, 5, 0));
      pieces.get(0).add(new Piece(board, P1_COLOR, 6, 0));
      pieces.get(0).add(new Piece(board, P1_COLOR, 7, 0));
      */
      for(int i=0; i<board.getNumTiles(); i++){
         pieces.get(0).add(new Pawn(board, P1_COLOR, i, 1));
      }
      //ArrayList of black pieces
      /*
      pieces.get(1).add(new Piece(board, P2_COLOR, 0, 7));
      pieces.get(1).add(new Piece(board, P2_COLOR, 1, 7));
      pieces.get(1).add(new Piece(board, P2_COLOR, 2, 7));
      pieces.get(1).add(new Piece(board, P2_COLOR, 3, 7));
      pieces.get(1).add(new Piece(board, P2_COLOR, 4, 7));
      pieces.get(1).add(new Piece(board, P2_COLOR, 5, 7));
      pieces.get(1).add(new Piece(board, P2_COLOR, 6, 7));
      pieces.get(1).add(new Piece(board, P2_COLOR, 7, 7));
      */
      for(int i=0; i<board.getNumTiles(); i++){
         pieces.get(1).add(new Pawn(board, P2_COLOR, i, 6));
      }
   }

   public void paint(Graphics g) {
      final Graphics2D g2 = (Graphics2D) g;
      // the frame's dimensions
      final int frameWidth = getWidth();
      final int frameHeight = getHeight();

      //script for anti-aliasing later if i need to add it in
      //g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.Value_ANTIALIAS_ON);

      //clears the background for fresh drawing
      g2.clearRect(0, 0, frameWidth, frameHeight);

      //draws the chess board
      board.draw(g2, frameWidth, frameHeight);

      if(getMousePosition() != null){
         int mouseX = getMousePosition().x;
         int mouseY = getMousePosition().y;
         //begin tracking a piece
         if(tracking == null && mouseDown && onBoard(mouseX, mouseY)){
            for(ArrayList<Piece> a : pieces){
               for(Piece p : a){
                  if(xTileAt(mouseX) == p.getXTile() && yTileAt(mouseY) == p.getYTile()){
                     //pickup script
                     p.setCurrentlyTracking(true);
                     tracking = p;
                  }
               }
            }
         //end tracking a piece
         }else if(mouseDown == false && tracking != null){
            tracking.setCurrentlyTracking(false);
            if(onBoard(mouseX, mouseY)){
              int i;
              if(tracking.getColor().equals(P1_COLOR)){
                 i=0;
              } else{
                 i=1;
              }
              //search for friendly pieces
              boolean friendlyPiece = false;
              for(Piece p : pieces.get(i)){
                 if(xTileAt(mouseX) == p.getXTile() && yTileAt(mouseY) == p.getYTile()){
                    //found a friendly piece
                    friendlyPiece = true;
                 }
              }
              if(friendlyPiece == false){
                 tracking.setPosition(xTileAt(mouseX), yTileAt(mouseY));
                 //search for enimy pieces
                 for(Piece p : pieces.get((i == 0)? 1 : 0)){
                    if(xTileAt(mouseX) == p.getXTile() && yTileAt(mouseY) == p.getYTile()){
                       //take enimy piece
                       //dead is set to p, to be removed from pieces after the for loop is complete
                       dead = p;
                    }
                 }
                 pieces.get((i == 0)? 1 : 0).remove(dead);
                 dead = null;
              }
            }
            tracking = null;
         }
      }

      //draw all static pieces
      for(ArrayList<Piece> a : pieces){
         for(Piece p : a){
            if(p != tracking){
               p.draw(g2);
            }
         }
      }

      //update position of tracked piece
      if(tracking !=null) {
         if(getMousePosition() != null){
            tracking.setMouseX(getMousePosition().x);
            tracking.setMouseY(getMousePosition().y);
         }
         tracking.draw(g2);
      }
   }

   public static String getP1Color(){
      return P1_COLOR;
   }

   //returns the tile which the x, y is hovering over
   private int xTileAt(double x){
      if(x < board.getX()) return -1;
      return (int)(x - board.getX()) / (int) board.getTileWidth();
   }
   private int yTileAt(double y){
      if( y < board.getY()) return -1;
      return (int)(y - board.getY()) / (int) board.getTileHeight();
   }
   //returns true if x, y coords are on the board
   private boolean onBoard(double x, double y){
     if(xTileAt(x) < 0 || yTileAt(y) < 0 || xTileAt(x) >= board.getNumTiles() || yTileAt(y) >= board.getNumTiles()){
       return false;
     }
     return true;
   }

   //asks for board to be redrawn
   private class update extends java.util.TimerTask {
      public void run() {
         repaint();
      }
   }

   //detection for when mouse button pressed and released
   public void mousePressed (MouseEvent mouseEvent) {
      mouseDown = true;
   }
   public void mouseReleased (MouseEvent mouseEvent) {
      mouseDown = false;
   }
   public void mouseClicked (MouseEvent mouseEvent) {}
   public void mouseEntered (MouseEvent mouseEvent) {}
   public void mouseExited (MouseEvent mouseEvent) {}
}
