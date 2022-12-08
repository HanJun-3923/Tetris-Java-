package InGame;
import InGameElement.Table;
import InGameElement.BlockShape;
import InGameElement.BlockData;

public class Hold {
    public Table[][] table = new Table[3][4];
    protected BlockShape blockShape = BlockShape.NONE;
    protected boolean canHold = true;

    protected Hold() {
        // holdBlockTable 2차원 객체 배열 할당
        for(int r = 0; r < 3; r++) {
            for (int c = 0; c < 4; c++) {
                table[r][c] = new Table(false, BlockShape.NONE);
            }
        }
    }
    protected void setTable() {
        int[][] temp = new int[4][4];
        temp = BlockData.fetch(blockShape, 0); // rotationValue = 0
        for(int r = 0; r < 3; r ++) {
            for(int c = 0; c < 4; c++) {
                table[r][c].mino = BlockData.intToLiquidBlockShape(temp[r][c]);
                if(table[r][c].mino != BlockShape.NONE) 
                    table[r][c].isVisible = true;
                else
                    table[r][c].isVisible = false;
            }
        }
    }
    

}
