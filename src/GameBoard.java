import javax.swing.*;

enum BlockShape {
    NONE, 
    Z, S, T, J, L, I, O,
    SLD_Z, SLD_S, SLD_T, SLD_J, SLD_L, SLD_I, SLD_O
};



public class GameBoard extends JPanel {
    public static final int WIDTH = 300; // 300px in Screen
    public static final int HEIGHT = 600; // 600px in Screen

    public static final int X = (Window.SCREEN_WIDTH / 2) - (WIDTH / 2); // Center in Screen
    public static final int Y = 30; // 30px in Screen

    public static final int BLOCK_SIZE = WIDTH / 10;

    public static final int TABLE_WIDTH = 10; // GameBoard(Table) Width
    public static final int TABLE_HEIGHT = 20; // GameBoard(Table) Height
    
    // Main Game Board
    static class Table {
        boolean isVisible;
        BlockShape mino;
        Table(boolean isVisible, BlockShape mino) {
            this.isVisible = isVisible;
            this.mino = mino;
        }
        
    }   
    
 
    
    public GameBoard() {
        // 다차원 배열 할당
        for(int r = 0; r < TABLE_HEIGHT; r++) {
            for(int c = 0; c < TABLE_WIDTH; c++) {
                InGame.table[r][c] = new Table(false, BlockShape.NONE);
            }
        } 
        for(int i = 0; i < InGame.BAG * 2; i++) {
            InGame.nextBlocks[i] = BlockShape.NONE;
        }

        //게임 시작
        InGame.setNextBlocks();
        InGame.setCrntBlock();
        InGame.initPosition();
        InGame.uploadCrntBlock();
        repaint();
        

        // put nowBlock's Data into Table
        // Paint
    }
    public void setBlockData(int r, int c, int data) {
        BlockShape blockShape = InGame.intToBlockShape(data);
        InGame.table[r][c].mino = blockShape; // put BlockShape data
        InGame.table[r][c].isVisible = true; // set visible true
    }
}
