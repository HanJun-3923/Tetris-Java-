import javax.swing.*;

enum BlockShape {
    NONE, 
    Z, S, T, J, L, I, O,
    SLD_Z, SLD_S, SLD_T, SLD_J, SLD_L, SLD_I, SLD_O,
    SOLID, LIQUID
};



public class GameBoard extends JPanel {
    static class Coordinate {
        int PIXEL_WIDTH;
        int PIXEL_HEIGHT;
        int INT_WIDTH;
        int INT_HEIGHT;
        int COORD_X;
        int COORD_Y;
        Coordinate(int PIXEL_WIDTH, int PIXEL_HEIGHT, int INT_WIDTH, int INT_HEIGHT, int COORD_X, int COORD_Y) {
            this.PIXEL_WIDTH = PIXEL_WIDTH;
            this.PIXEL_HEIGHT = PIXEL_HEIGHT;
            this.INT_WIDTH = INT_WIDTH;
            this.INT_HEIGHT = INT_HEIGHT;
            this.COORD_X = COORD_X;
            this.COORD_Y = COORD_Y;
        }
    };
    public static final Coordinate MAIN_BOARD = new Coordinate(300, 600, 10, 20, (Window.SCREEN_WIDTH / 2) - (300 / 2), 30);
    public static final int BLOCK_SIZE = MAIN_BOARD.PIXEL_WIDTH / 10;
    public static final int NEXT_BLOCKS_BOARD_HEIGHT_PER_VISION = 3;
    public static final int VISION_OF_NEXT_BLOCKS = 5; // 볼 수 있는 넥스트 블럭의 개수
    public static final Coordinate NEXT_BLOCKS_BOARD = new Coordinate(BLOCK_SIZE * 4, BLOCK_SIZE * VISION_OF_NEXT_BLOCKS * NEXT_BLOCKS_BOARD_HEIGHT_PER_VISION, 4, NEXT_BLOCKS_BOARD_HEIGHT_PER_VISION * VISION_OF_NEXT_BLOCKS, MAIN_BOARD.COORD_X + MAIN_BOARD.PIXEL_WIDTH + 20, MAIN_BOARD.COORD_Y);


    public static InGame player1 = new InGame(); // first player
 
    
 
    
    public GameBoard() {

        player1.gameStart();
        repaint();
        

        // put nowBlock's Data into Table
        // Paint
    }
}
