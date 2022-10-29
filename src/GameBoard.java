import javax.swing.*;

enum BlockShape {
    NONE, 
    Z, S, T, J, L, I, O,
    SLD_Z, SLD_S, SLD_T, SLD_J, SLD_L, SLD_I, SLD_O,
    SOLID, LIQUID
};



public class GameBoard extends JPanel {
    public static final int WIDTH = 300; // 300px in Screen
    public static final int HEIGHT = 600; // 600px in Screen

    public static final int X = (Window.SCREEN_WIDTH / 2) - (WIDTH / 2); // Center in Screen
    public static final int Y = 30; // 30px in Screen

    public static final int BLOCK_SIZE = WIDTH / 10;

    public static final int TABLE_WIDTH = 10; // GameBoard(Table) Width
    public static final int TABLE_HEIGHT = 20; // GameBoard(Table) Height
    
    public static InGame player1 = new InGame(); // first player
 
    
 
    
    public GameBoard() {

        player1.setNextBlocks(); // 게임 시작시 한 번 호출
        //게임 시작
        player1.setNewBlock();
        repaint();
        

        // put nowBlock's Data into Table
        // Paint
    }
}
