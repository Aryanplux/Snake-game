import javax.swing.*;

public class app {
    public static void main(String[] args) {
        int boardWidth = 600;
        int boardHeight = boardWidth;

        JFrame frame = new JFrame("Snake Game");
        SnakeGame snakeGame = new SnakeGame(boardWidth, boardHeight);
        frame.add(snakeGame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        snakeGame.requestFocus();
    }
}