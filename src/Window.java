import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Window extends JFrame implements KeyListener {
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;

    public Window() {
        setTitle("Tetris");
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setResizable(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JPanel gameBoard = new Paint();



        setContentPane(gameBoard);
        addKeyListener(this);
        new GameBoard();


        
        setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) { // Right Arrow
            if(GameBoard.player1.movable(Direction.RIGHT))
                GameBoard.player1.move(Direction.RIGHT);
        } 
        else if(e.getKeyCode() == KeyEvent.VK_LEFT) { // Left Arrow
            if(GameBoard.player1.movable(Direction.LEFT))
                GameBoard.player1.move(Direction.LEFT);
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN) { // Down Arrow
            if(GameBoard.player1.movable(Direction.DOWN))
                GameBoard.player1.move(Direction.DOWN);
        }
        else if(e.getKeyCode() == KeyEvent.VK_UP) {
            if(GameBoard.player1.movable(Direction.UP))
                GameBoard.player1.move(Direction.UP);
        }
        else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            GameBoard.player1.hardDrop();
            GameBoard.player1.setNewBlock();
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    
}
