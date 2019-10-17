import javax.swing.JFrame;

public class ChessFrame {

   private final static int WIDTH = 1500;
   private final static int HEIGHT = 1000;

   public static void main(String[] args) {
      JFrame frame = new JFrame();

      frame.setSize(WIDTH, HEIGHT);
      frame.setTitle("Chess");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      frame.add(new GameComponent());

      frame.setVisible(true);
   }
}
