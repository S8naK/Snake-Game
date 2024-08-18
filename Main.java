import javax.swing.*;

public class Main {
    static final int boardWidth = 600;
    static final int boardHeight = 600;
    public static void main(String[] args) throws Exception {
        
        JFrame frame = new JFrame("Snake");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        GamePanel game = new GamePanel(boardWidth, boardHeight);
        frame.add(game);
        ImageIcon image = new ImageIcon("snake.png");
        frame.setIconImage(image.getImage());
        frame.pack();

        frame.setVisible(true);
        game.requestFocus();

    }
}
