public class InGame {
    public static final int BAG = 7;
    static class Position {
        int r;
        int c;
        public Position(int r, int c) {
            this.r = r;
            this.c = c;
        }
    };
    public static Position pos;
    public static int rotation = 0;
    public static final int INITIAL_POS_C = 3;
    public static final int INITIAL_POS_R = 0;

    public static int numOfUsedBlocks = 0;

    public static BlockShape[] nextBlocks = new BlockShape[BAG * 2];
    public static GameBoard.Table[][] table = new GameBoard.Table[GameBoard.TABLE_HEIGHT][GameBoard.TABLE_WIDTH];
    public static int[][] crntBlock;

    public static void setNextBlocks() {
        if(nextBlocks[0] == BlockShape.NONE) { // set nextBlocks Initially
            for(int i = 0; i < BAG * 2; i++) { 
                boolean again;
                do {
                    again = false;
                    nextBlocks[i] = intToBlockShape((int)(Math.random() * 10) % 7 + 1);
                    //if i is in first BAG
                    if(i < BAG) {
                        for(int j = 0; j < i; j++)
                        // if any next has same BlockShape with another in its bag, it goes again.
                            if(nextBlocks[j] == nextBlocks[i]) again = true; 
                    }
                    else { // if i is in second BAG
                        for(int j = 7; j < i; j++) {
                            // if any next has same BlockShape with another in its bag, it goes again.
                            if(nextBlocks[j] == nextBlocks[i]) again = true; 
                        }
                        
                    }
                } while(again);
            }
        }
        else { // put into first bag next blocks in second bag, and make new second bag.
            for(int i = 0; i < BAG; i++) {
                int j = i + BAG;
                nextBlocks[i] = nextBlocks[j];
            }
            for(int i = 7; i < 14; i++) {
                boolean again;
                do {
                    again = false;
                    nextBlocks[i] = intToBlockShape((int)(Math.random() * 10) % 7 + 1);
                    for(int j = 7; j < i; j++) {
                        if(nextBlocks[j] == nextBlocks[i]) again = true;
                    }
                } while(again);
            }
        }
    }
    public static void initPosition() {
        pos = new Position(0, 0);
        pos.c = INITIAL_POS_C;
        pos.r = INITIAL_POS_R;
    }
    public static void setCrntBlock() {
        crntBlock = BlockData.fetch(nextBlocks[numOfUsedBlocks % BAG]);
    }
    public static void uploadCrntBlock() {
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                if(crntBlock[i][j] != 0) {
                    table[pos.r + i][pos.c + j].mino = intToBlockShape(crntBlock[i][j]);
                    table[pos.r + i][pos.c + j].isVisible = true;
                }
            }
        }
    }
    public static void hardDrop() {

    }
    public static boolean movable() {
        boolean canMove = true;
        if(pos.c < 0 || pos.c >= 10)
        return canMove;
    }


    public static BlockShape intToBlockShape(int randNum) {
        switch(randNum) {
            case 1:
                return BlockShape.I;
            case 2:
                return BlockShape.T;
            case 3:
                return BlockShape.O;
            case 4:
                return BlockShape.S;
            case 5:
                return BlockShape.Z;
            case 6:
                return BlockShape.L;
            case 7:
                return BlockShape.J;
            default:
                return BlockShape.NONE;
        }
    }
}
