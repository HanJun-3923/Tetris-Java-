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
            if(InGame.movable(InGame.Direction.RIGHT))
                InGame.move(InGame.Direction.RIGHT);
        } 
        else if(e.getKeyCode() == KeyEvent.VK_LEFT) { // Left Arrow
            if(InGame.movable(InGame.Direction.LEFT))
                InGame.move(InGame.Direction.LEFT);
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN) { // Down Arrow
            if(InGame.movable(InGame.Direction.DOWN))
                InGame.move(InGame.Direction.DOWN);
        }
        else if(e.getKeyCode() == KeyEvent.VK_UP) {
            if(InGame.movable(InGame.Direction.UP))
                InGame.move(InGame.Direction.UP);
        }
        else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            InGame.hardDrop();
        }
        System.out.println(e.getKeyCode());
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // repaint(); <- 해야할지 말지 잘 모르겠음
    }
    
}
