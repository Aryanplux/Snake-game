package Snake;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.ArrayList;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int boardheight;
    int boardwidth;
    int tilesize = 15;

    // Snake
    ArrayList<Tile> snakeBody;
    Tile snakeHead;

    // Food
    Tile food;
    Random random;

    // Game logic
    Timer gameLoop;
    int velocityX = 0; // Horizontal velocity
    int velocityY = 0; // Vertical velocity

    SnakeGame(int boardwidth, int boardheight) {
        this.boardwidth = boardwidth;
        this.boardheight = boardheight;
        setPreferredSize(new Dimension(this.boardwidth, this.boardheight));
        setBackground(Color.BLACK);
        addKeyListener(this); // Add KeyListener to handle keyboard input
        setFocusable(true); // Allow JPanel to receive key events

        // Initialize snake
        snakeHead = new Tile(2, 3);
        snakeBody = new ArrayList<>();

        // Initialize food
        food = new Tile(5, 5);
        random = new Random();
        placeFood();

        // Initialize game loop
        gameLoop = new Timer(100, this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // Draw grid
        for (int i = 0; i < boardwidth / tilesize; i++) {
            g.drawLine(i * tilesize, 0, i * tilesize, boardheight);
            g.drawLine(0, i * tilesize, boardwidth, i * tilesize);
        }

        // Draw food
        g.setColor(Color.red);
        g.fillRect(food.x * tilesize, food.y * tilesize, tilesize, tilesize);

        // Draw snake head
        g.setColor(Color.green);
        g.fillRect(snakeHead.x * tilesize, snakeHead.y * tilesize, tilesize, tilesize);

        // Draw snake body
        for (Tile bodyPart : snakeBody) {
            g.fillRect(bodyPart.x * tilesize, bodyPart.y * tilesize, tilesize, tilesize);
        }
    }

    public void placeFood() {
        food.x = random.nextInt(boardwidth / tilesize); // Random x position
        food.y = random.nextInt(boardheight / tilesize); // Random y position
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move() {
        // Eat food
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y)); // Grow snake
            placeFood(); // Place new food
        }

        // Move snake body
        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Tile bodyPart = snakeBody.get(i);
            if (i == 0) {
                bodyPart.x = snakeHead.x;
                bodyPart.y = snakeHead.y;
            } else {
                Tile prevBodyPart = snakeBody.get(i - 1);
                bodyPart.x = prevBodyPart.x;
                bodyPart.y = prevBodyPart.y;
            }
        }

        // Move snake head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        // Game over conditions
        for (Tile bodyPart : snakeBody) {
            if (collision(snakeHead, bodyPart)) {
                gameLoop.stop(); // Stop game if snake collides with itself
            }
        }

        if (snakeHead.x < 0 || snakeHead.x >= boardwidth / tilesize ||
            snakeHead.y < 0 || snakeHead.y >= boardheight / tilesize) {
            gameLoop.stop(); // Stop game if snake goes out of bounds
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        } else if (keyCode == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        } else if (keyCode == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        } else if (keyCode == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame(500, 500);
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}