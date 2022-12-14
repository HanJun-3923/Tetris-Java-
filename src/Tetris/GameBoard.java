package Tetris;
import javax.swing.*;

import InGame.InGame;

public class GameBoard extends JPanel {
    public static class Coordinate {
        public int INT_WIDTH;
        public int INT_HEIGHT;
        public int PIXEL_WIDTH;
        public int PIXEL_HEIGHT;
        public int COORD_X;
        public int COORD_Y;
    };
    public static final Coordinate MAIN_BOARD = new Coordinate();
    public static final Coordinate NEXT_BLOCKS_BOARD = new Coordinate();
    public static final Coordinate HOLD_BLOCK_BOARD = new Coordinate();
    public static final int BLOCK_SIZE = 30;
    public static final int NEXT_BLOCKS_BOARD_HEIGHT_PER_VISION = 3;
    public static final int VISION_OF_NEXT_BLOCKS = 5; // 볼 수 있는 넥스트 블럭의 개수

    public static InGame player1; // first player
 
    
    public GameBoard() {
        // *** MAIN_BOARD SETTING *** 
        MAIN_BOARD.INT_WIDTH = 10;
        MAIN_BOARD.INT_HEIGHT = 20;
        MAIN_BOARD.PIXEL_WIDTH = BLOCK_SIZE * MAIN_BOARD.INT_WIDTH;
        MAIN_BOARD.PIXEL_HEIGHT = BLOCK_SIZE * MAIN_BOARD.INT_HEIGHT;
        MAIN_BOARD.COORD_X = (Window.SCREEN_WIDTH / 2) - (MAIN_BOARD.PIXEL_WIDTH / 2);
        MAIN_BOARD.COORD_Y = 30;

        // *** NEXT_BLOCKS_BOARD SETTING ***
        NEXT_BLOCKS_BOARD.INT_WIDTH = 4;
        NEXT_BLOCKS_BOARD.INT_HEIGHT = VISION_OF_NEXT_BLOCKS * NEXT_BLOCKS_BOARD_HEIGHT_PER_VISION;
        NEXT_BLOCKS_BOARD.PIXEL_WIDTH  = BLOCK_SIZE * NEXT_BLOCKS_BOARD.INT_WIDTH;
        NEXT_BLOCKS_BOARD.PIXEL_HEIGHT = BLOCK_SIZE * NEXT_BLOCKS_BOARD.INT_HEIGHT;
        NEXT_BLOCKS_BOARD.COORD_X = MAIN_BOARD.COORD_X + MAIN_BOARD.PIXEL_WIDTH + 20;
        NEXT_BLOCKS_BOARD.COORD_Y = MAIN_BOARD.COORD_Y;

        HOLD_BLOCK_BOARD.INT_WIDTH = 4;
        HOLD_BLOCK_BOARD.INT_HEIGHT = 3;
        HOLD_BLOCK_BOARD.PIXEL_WIDTH  = BLOCK_SIZE * HOLD_BLOCK_BOARD.INT_WIDTH;
        HOLD_BLOCK_BOARD.PIXEL_HEIGHT = BLOCK_SIZE * HOLD_BLOCK_BOARD.INT_HEIGHT;
        HOLD_BLOCK_BOARD.COORD_X = MAIN_BOARD.COORD_X - HOLD_BLOCK_BOARD.PIXEL_WIDTH - 20;
        HOLD_BLOCK_BOARD.COORD_Y = MAIN_BOARD.COORD_Y;

        player1 = new InGame();
        player1.gameStart();
        repaint();

    }
}
