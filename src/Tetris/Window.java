package Tetris;
import javax.swing.*;

import InGameElement.Direction;

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
        int key = e.getKeyCode();
        if(key == KeyEvent.VK_RIGHT) { // Right Arrow
            if(GameBoard.player1.crntBlockMovable(Direction.RIGHT))
                GameBoard.player1.move(Direction.RIGHT);
        } 
        else if(key == KeyEvent.VK_LEFT) { // Left Arrow
            if(GameBoard.player1.crntBlockMovable(Direction.LEFT))
                GameBoard.player1.move(Direction.LEFT);
        }
        else if(key == KeyEvent.VK_DOWN) { // Down Arrow
            if(GameBoard.player1.crntBlockMovable(Direction.DOWN))
                GameBoard.player1.move(Direction.DOWN);
        }
        else if(key == KeyEvent.VK_UP) { // Up Arrow
            if(GameBoard.player1.crntBlockMovable(Direction.UP))
                GameBoard.player1.move(Direction.UP);
        }
        else if(key == KeyEvent.VK_SPACE) { // SpaceBar
            GameBoard.player1.hardDrop();
        }
        else if(key == KeyEvent.VK_D || key == KeyEvent.VK_C) { // Key D, C
            GameBoard.player1.rotation(Direction.CLOCKWISE);
        }
        else if(key == KeyEvent.VK_S || key == KeyEvent.VK_X) { // Key S, X
            GameBoard.player1.rotation(Direction.COUNTER_CLOCKWISE);
        }
        else if(key == KeyEvent.VK_A || key == KeyEvent.VK_SHIFT) { // Key A, Shift
            GameBoard.player1.hold();
        }
        GameBoard.player1.updateWithEveryMove();
        repaint();
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
