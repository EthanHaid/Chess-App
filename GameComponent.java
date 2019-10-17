import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Timer;

public class GameComponent extends JComponent implements MouseListener {

   private static final int REFRESH_RATE = 30; //number of ms per screen refresh

   private final Board board; //playing board
   private final Player[] players; //the two players playing

   private int playerTurn;
   private Point mousePos;
   private boolean mouseDown = false;
   //stores previous frame dimensions
   private int cachedFrameWidth = 0;
   private int cachedFrameHeight = 0;

   public GameComponent() { //constructor

      //creates a new timer, update() every REFRESH_RATE ms
      java.util.Timer timer = new java.util.Timer();
      timer.scheduleAtFixedRate(new update(), REFRESH_RATE, REFRESH_RATE);

      addMouseListener(this); //mouse click listener

      board = new Board(); //creates new board
      //TODO: create a graveyard

      Player player1 = new User(board, 1, 0); //creates Player(board, starting position, team color)
      //Player player2 = new User(board, 0, 1);
      Player player2 = new User(board, 0, 1);

      playerTurn = (player1.getTeam() == 0)? 0 : 1; //sets the white team to have first turn.
      player1.setOpponent(player2);
      player2.setOpponent(player1);
      players = new Player[] {player1, player2};
      for(Player p : players) {
         p.createPieces();
      }
   }

   public void paint(Graphics g) {
      final Graphics2D g2 = (Graphics2D) g;
      final int frameWidth = getWidth();
      final int frameHeight = getHeight();
      mousePos = getMousePosition();

      g2.clearRect(0, 0, frameWidth, frameHeight); //clears the background for fresh drawing

      if(frameChanged(frameWidth, frameHeight)) { //if the frame changed sizes, resize models.
         board.resize(frameWidth, frameHeight);
         for(Player p : players) {
            p.resize();
         }
      }

      //here is where the take-a-turn code is implimented
      players[playerTurn].setMoveWasMade(false);
      if(players[playerTurn] instanceof User) { //update User with the mouse data
         ((User)players[playerTurn]).setMousePos(mousePos);
         ((User)players[playerTurn]).setMouseDown(mouseDown);
      }
      try {
         players[playerTurn].makeMove(); //player make its move
      } catch(InterruptedException e) {
         System.out.println("THREADS ARE CAUSING ISSUES");
      }
      if(players[playerTurn].getMoveWasMade()) { //next player's turn
         playerTurn = (playerTurn == 0)? 1 : 0;
      }

      board.draw(g2); //draws the board
      for(Player p : players) { //draws the pieces
         p.draw(g2);
      }
      if(players[playerTurn] instanceof User) {
         if(((User)players[playerTurn]).getTracking() != null) {
            ((User)players[playerTurn]).getTracking().draw(g2);
         }
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
