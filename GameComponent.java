import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
//TODO: optimize this import:
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Timer;

public class GameComponent extends JComponent implements MouseListener {

   private static final int REFRESH_RATE = 30; //number of ms per screen refresh

   private final Board board; //playing board
   private final ArrayList<ArrayList<Piece>> pieces; //array of all game pieces

   private int playerTurn = 0; //stores which player's turn it is. Either 0 or 1
   private boolean mouseDown = false; //stores if the mouse button is pressed or not
   private Piece tracking = null; //the piece currently tracking the mouse
   private ArrayList<Point> validTiles; //tiles to highlight on the board
   //stores previous frame dimensions
   private int cachedFrameWidth = 0;
   private int cachedFrameHeight = 0;

   //GameComponent constructor
   public GameComponent() {
      //creates a new timer, update() every REFRESH_RATE ms
      java.util.Timer timer = new java.util.Timer();
      timer.scheduleAtFixedRate(new update(), REFRESH_RATE, REFRESH_RATE);

      addMouseListener(this); //mouse click listener

      //creates new board and pieces
      board = new Board();
      pieces = new ArrayList<ArrayList<Piece>>();
      pieces.add(new ArrayList<Piece>());
      pieces.add(new ArrayList<Piece>());

      //for each team, create the main pieces
      for(int i=0; i<2; i++) {
         //TODO: the pieces created here will not be pawns
         //pieces.get(i).add(new Pawn(board, i, 0, i * (board.getNumTiles() - 1)));
      }
      //create the pawns
      for(int i=0; i<board.getNumTiles(); i++) {
         pieces.get(0).add(new Pawn(board, 1, i, 1));
      }
      for(int i=0; i<board.getNumTiles(); i++) {
         pieces.get(1).add(new Pawn(board, 0, i, board.getNumTiles() - 2));
      }
   }


   public void paint(Graphics g) {
      final Graphics2D g2 = (Graphics2D) g;
      final int frameWidth = getWidth();
      final int frameHeight = getHeight();

      g2.clearRect(0, 0, frameWidth, frameHeight); //clears the background for fresh drawing

      if(frameChanged(frameWidth, frameHeight)) { //if the frame changed sizes, resize models.
         board.resize(frameWidth, frameHeight);
         for(ArrayList<Piece> a : pieces) {
            for(Piece p : a) {
               p.resize();
            }
         }
      }

      board.draw(g2, validTiles); //draws the board

      Point mouse = getMousePosition();
      if(mouse != null) { //TODO: top down design the SHIT out of this
         if(tracking == null && mouseDown && onBoard(mouse)) { //pickup a piece
            tracking = pieceAt(playerTurn, mouse);
            if(tracking != null) {
               tracking.setCurrentlyTracking(true);
               tracking.resize();
               validTiles = tracking.validTiles();
            }
         } else if(tracking != null && mouseDown == false) { //drop a piece
            tracking.setCurrentlyTracking(false);
            tracking.resize();
            validTiles = null;
            if(onBoard(mouse) && pieceAt(playerTurn, mouse) == null) { //TODO: one of these conditions has to include the "valid" location param of Piece
               tracking.setPosition(tileAt(mouse)); //drop piece new coordinate
               tracking = pieceAt(((playerTurn == 0)? 1 : 0), mouse); //detect enimy piece
               if(tracking != null) { //if enimy piece at location, remove
                  pieces.get((playerTurn == 0)? 1 : 0).remove(tracking);
               }
               playerTurn = (playerTurn == 0)? 1 : 0;
            }
            tracking = null; //stop watching the tracking piece
         }
      }

      //draw the pieces which arent tracking the mouse
      for(ArrayList<Piece> a : pieces){
         for(Piece p : a){
            if(p != tracking){
               p.draw(g2);
            }
         }
      }

      //draw the piece tracking the mouse
      if(tracking !=null) {
         if(mouse != null){
            tracking.setMouse(mouse);
         }
         tracking.draw(g2);
      }
   }

   //takes new frame dimensions, and checks if they differ from the old dimensions.
   //if they have changed, update the stored dimensions and return true, else false
   private boolean frameChanged(int newFrameWidth, int newFrameHeight) {
      if(newFrameWidth == cachedFrameWidth && newFrameHeight == cachedFrameHeight) {
         return false;
      }
      cachedFrameWidth = newFrameWidth;
      cachedFrameHeight = newFrameHeight;
      return true;
   }

   //returns the piece at the given tileX, tileY coordinates
   private Piece pieceAt(int team, Point point) {
      if(point == null) return null;
      point = tileAt(point);
      for(Piece p : pieces.get(team)) {
         if(point.equals(p.getTile())) {
            return p;
         }
      }
      return null;
   }

   //returns the  tile which the coordinate is upon
   private Point tileAt(Point point) {
      if(point == null || point.x < board.getPos().x || point.y < board.getPos().y) {
         return new Point(-1, -1);
      }
      return new Point(((int)(point.x - board.getPos().x) / (int) board.getTileWidth()),
         ((int)(point.y - board.getPos().y) / (int) board.getTileHeight()));
   }

   //returns true if the coordinates are on the playing board, else false
   private boolean onBoard(Point point) {
      if(point == null) return false;
      point = tileAt(point);
      if(point.x < 0 || point.y < 0 ||
         point.x >= board.getNumTiles() || point.y >= board.getNumTiles()) {
         return false;
      }
      return true;
   }

   //updates the game image, called every REFRESH_RATE ms.
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
