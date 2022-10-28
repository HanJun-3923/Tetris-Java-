public class InGame {
    public static final int BAG = 7; // 한 가방 속에 존재하는 블록의 수
    static class Position { // 좌표를 표현하기 위한 클래스(행렬 기준 좌표)
        int r;
        int c;
        public Position(int r, int c) {
            this.r = r;
            this.c = c;
        }
    };
    static enum Direction { // 방향에 대한 열거형
        LEFT, RIGHT, DOWN, UP
    };
    public static Position pos; // 블록의 좌표 (행렬)
    public static int rotation = 0; // 회전된 수
    public static final int INITIAL_POS_C = 3; // 새로운 블록의 열
    public static final int INITIAL_POS_R = 0; // 새로운 블록의 행

    public static int numOfUsedBlocks = 0; // 놓아진 모든 블록의 수

    public static BlockShape[] nextBlocks = new BlockShape[BAG * 2];
    public static BlockShape crntBlockShape = BlockShape.NONE;
    public static GameBoard.Table[][] table = new GameBoard.Table[GameBoard.TABLE_HEIGHT][GameBoard.TABLE_WIDTH];
    public static BlockShape[][] crntBlock = new BlockShape[4][4];

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
    public static void setCrntBlockShape() {
        crntBlockShape = nextBlocks[numOfUsedBlocks % 7];
    }
    public static void initPosition() {
        if(pos == null) pos = new Position(0, 0); // NullPointerException 처리
        pos.c = INITIAL_POS_C;
        pos.r = INITIAL_POS_R;
    }
    public static void setCrntBlock() {
        int[][] temp = new int[4][4];
        temp = BlockData.fetch(nextBlocks[numOfUsedBlocks % BAG]);
        for(int r = 0; r < 4; r++) {
            for(int c = 0; c < 4; c++) {
                crntBlock[r][c] = intToBlockShape(temp[r][c]);
            } 
        }
    }
    public static void uploadCrntBlock() {
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                if(crntBlock[i][j] != BlockShape.NONE) {
                    table[pos.r + i][pos.c + j].mino = crntBlock[i][j];
                    table[pos.r + i][pos.c + j].isVisible = true;
                }
            }
        }
    }
    public static void hardDrop() {
        
    }
    public static void initLiquidBlock() {
        // init Liquid Components
        for(int r = 0; r < GameBoard.TABLE_HEIGHT; r++) {
            for(int c = 0; c < GameBoard.TABLE_WIDTH; c++) {
                Position testPos = new Position(r, c);
                BlockShape testResult = solidOrLiquid(testPos);
                if(testResult == BlockShape.LIQUID) {
                    //init
                    table[r][c].mino = BlockShape.NONE;
                    table[r][c].isVisible = false;
                }
            }
        }
    }
    public static void move(Direction direction) {
        if(direction == Direction.LEFT) {
            pos.c--;
        } 
        else if(direction == Direction.RIGHT) {
            pos.c++;
        }
        else if(direction == Direction.DOWN) {
            pos.r++;
        }
        else { // direction == Direction.UP
            pos.r--;
        }
        initLiquidBlock();
        uploadCrntBlock();
    }
    public static boolean movable(Direction direction) {
        boolean boolMovable = true;
        if(direction == Direction.LEFT) {
            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 4; c++) {
                    if(crntBlock[r][c] == crntBlockShape) { // 4x4 행렬 속에서 블럭이 존재할 때
                        Position testPos = new Position(c + pos.c - 1, r + pos.r);
                        BlockShape testResult = solidOrLiquid(testPos);
                        if(testResult == BlockShape.SOLID) boolMovable = false;
                    }
                }
            }
        }
        else if(direction == Direction.RIGHT) {
            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 4; c++) {
                    if(crntBlock[r][c] == crntBlockShape) { // 4x4 행렬 속에서 블럭이 존재할 때
                        Position testPos = new Position(c + pos.c + 1, r + pos.r);
                        BlockShape testResult = solidOrLiquid(testPos);
                        if(testResult == BlockShape.SOLID) boolMovable = false;
                    }
                }
            }
        }
        else if(direction == Direction.DOWN) {
            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 4; c++) {
                    if(crntBlock[r][c] == crntBlockShape) { // 4x4 행렬 속에서 블럭이 존재할 때
                        Position testPos = new Position(c + pos.c, r + pos.r + 1);
                        BlockShape testResult = solidOrLiquid(testPos);
                        if(testResult == BlockShape.SOLID) boolMovable = false;
                    }
                }
            }
        }
        else { // if(direction == Direction.UP)
            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 4; c++) {
                    if(crntBlock[r][c] == crntBlockShape) { // 4x4 행렬 속에서 블럭이 존재할 때
                        Position testPos = new Position(c + pos.c, r + pos.r - 1);
                        BlockShape testResult = solidOrLiquid(testPos);
                        if(testResult == BlockShape.SOLID) boolMovable = false;
                    }
                }
            }
        }
        return boolMovable;
    }
    private static BlockShape solidOrLiquid(Position testPos) {
        // 테트리스 화면 밖 모든 블럭은 Solid 블록이다.
        if(testPos.c < 0 || testPos.c >= GameBoard.TABLE_WIDTH || testPos.r >= GameBoard.TABLE_HEIGHT || testPos.r < 0) 
            return BlockShape.SOLID; 
        BlockShape mino = table[testPos.r][testPos.c].mino;
        if (mino == BlockShape.NONE) return BlockShape.NONE;
        else if(mino == BlockShape.SLD_I || mino == BlockShape.SLD_J || mino == BlockShape.SLD_L || mino == BlockShape.SLD_O || mino == BlockShape.SLD_S || mino == BlockShape.SLD_J || mino == BlockShape.SLD_T || mino == BlockShape.SLD_Z)
            return BlockShape.SOLID;
        else 
            return BlockShape.LIQUID;
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
