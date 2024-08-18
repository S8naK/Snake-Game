import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener{
    int boardWidth;
    int boardHeight;
    int tileSize = 25; //object size in game
    
    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    
    Tile snakeHead;
    ArrayList<Tile> snakeBody;
    Tile apple;
    Random random;

    Timer timer;
    int velocityX;
    int velocityY;

    boolean gameOver = false;

    GamePanel(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.setPreferredSize(new Dimension(boardWidth,boardHeight));
        this.setBackground(Color.black);
        
        this.addKeyListener(this);
        this.setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<>();
        apple = new Tile(10, 15);
        random = new Random();
        newApple();

        velocityX = 0;
        velocityY = 0;

        timer = new Timer(110, this);
        timer.start();

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    
    public void draw(Graphics g) {
        /*for(int i=0; i<boardHeight/tileSize; i++) {
            g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
            g.drawLine(0, i*tileSize, boardWidth, i*tileSize);
        }*/

        g.setColor(Color.green);
        g.fillOval(apple.x*tileSize, apple.y*tileSize, tileSize, tileSize);
        
        g.setColor(new Color(123,50,250));
        g.fill3DRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize, true);

        for(int i=0; i<snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            //g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
            g.fill3DRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize, true);
        }

        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if(gameOver) {
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.PLAIN, 60));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Game Over: " + String.valueOf(snakeBody.size()), (boardWidth-metrics.stringWidth("Game Over: "))/2, boardHeight/2);
            g.setFont(new Font("Arial", Font.PLAIN, 30));
            g.drawString("Press Space to Restart", 160, 340);
        }
        else {
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize-16, tileSize);
        }
    }

    public void newApple() {
        apple.x = random.nextInt(boardWidth/tileSize);
        apple.y = random.nextInt(boardHeight/tileSize);
    }

    public void move() {
        if(checkCollision(snakeHead, apple)) {
            snakeBody.add(new Tile(apple.x, apple.y));
            newApple();
        }
        
        for(int i=snakeBody.size()-1; i>=0; i--) {
            Tile snakePart = snakeBody.get(i);
            if(i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else {
                Tile prevsnakePart = snakeBody.get(i-1);
                snakePart.x = prevsnakePart.x;
                snakePart.y = prevsnakePart.y;
            }
        }

        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        for(int i=0; i<snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            if(checkCollision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }
        if(snakeHead.x*tileSize < 0 || snakeHead.x*tileSize > boardWidth-tileSize || 
           snakeHead.y*tileSize < 0 || snakeHead.y*tileSize > boardHeight-tileSize) {
            gameOver = true;
        }
    }

    public boolean checkCollision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver) {
            timer.stop();
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }

        else if(e.getKeyCode() == KeyEvent.VK_SPACE && gameOver) {
            gameOver = false;
            snakeBody.clear();
            snakeHead = new Tile(5, 5);
            velocityX = 0;
            velocityY = 0;
            timer.start();
            repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyReleased(KeyEvent e) {
    }
    
}
