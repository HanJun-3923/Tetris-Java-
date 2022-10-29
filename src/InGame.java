enum Direction { // 방향에 대한 열거형
    LEFT, RIGHT, DOWN, UP
};

public class InGame {
    private final int BAG = 7; // 한 가방 속에 존재하는 블록의 수
    class Position { // 좌표를 표현하기 위한 클래스(행렬 기준 좌표)
        int r;
        int c;
        public Position(int r, int c) {
            this.r = r;
            this.c = c;
        }
    };
    // Main Game Board
    class Table {
        boolean isVisible;
        BlockShape mino;
        public Table(boolean isVisible, BlockShape mino) {
            this.isVisible = isVisible;
            this.mino = mino;
        }
        
    }  
    public Position pos; // 블록의 좌표 (행렬)
    public int rotation = 0; // 회전된 수
    private final int INITIAL_POS_C = 3; // 새로운 블록의 열
    public final int INITIAL_POS_R = 0; // 새로운 블록의 행

    public int numOfUsedBlocks = 0; // 놓아진 모든 블록의 수

    public BlockShape[] nextBlocks = new BlockShape[BAG * 2];
    public BlockShape crntBlockShape = BlockShape.NONE;
    public Table[][] table = new Table[GameBoard.TABLE_HEIGHT][GameBoard.TABLE_WIDTH];
    public BlockShape[][] crntBlock = new BlockShape[4][4];

    public InGame() {
        // 다차원 배열 할당
        for(int r = 0; r < GameBoard.TABLE_HEIGHT; r++) {
            for(int c = 0; c < GameBoard.TABLE_WIDTH; c++) {
                table[r][c] = new Table(false, BlockShape.NONE);
            }
        } 
        for(int i = 0; i < BAG * 2; i++) {
            nextBlocks[i] = BlockShape.NONE;
        }
    }


    public void setNextBlocks() { 
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
                        for(int j = BAG; j < i; j++) {
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
            for(int i = BAG; i < 14; i++) {
                boolean again;
                do {
                    again = false;
                    nextBlocks[i] = intToBlockShape((int)(Math.random() * 10) % 7 + 1);
                    for(int j = BAG; j < i; j++) {
                        if(nextBlocks[j] == nextBlocks[i]) again = true;
                    }
                } while(again);
            }
        }
    }
    public void setCrntBlockShape() {
        crntBlockShape = nextBlocks[numOfUsedBlocks % 7];
    }
    public void initPosition() {
        if(pos == null) pos = new Position(0, 0); // NullPointerException 처리
        pos.c = INITIAL_POS_C;
        pos.r = INITIAL_POS_R;
    }
    public void setCrntBlock() {
        int[][] temp = new int[4][4];
        temp = BlockData.fetch(nextBlocks[numOfUsedBlocks % BAG]);
        for(int r = 0; r < 4; r++) {
            for(int c = 0; c < 4; c++) {
                crntBlock[r][c] = intToBlockShape(temp[r][c]);
            } 
        }
    }
    public void uploadCrntBlock() {
        for(int r = 0; r < 4; r++) {
            for(int c = 0; c < 4; c++) {
                if(crntBlock[r][c] != BlockShape.NONE) {
                    table[pos.r + r][pos.c + c].mino = crntBlock[r][c];
                    table[pos.r + r][pos.c + c].isVisible = true;
                }
            }
        }
    }
    public void hardDrop() {
        while(movable(Direction.DOWN)) {
            move(Direction.DOWN);
        }
        solidification();
    }
    public void solidification() {
        // change liquid blocks into solid blocks
        BlockShape solidBlockShape; // 변경될 solid 블록
        if(crntBlockShape == BlockShape.I) solidBlockShape = BlockShape.SLD_I;
        else if(crntBlockShape == BlockShape.T) solidBlockShape = BlockShape.SLD_T;
        else if(crntBlockShape == BlockShape.O) solidBlockShape = BlockShape.SLD_O;
        else if(crntBlockShape == BlockShape.S) solidBlockShape = BlockShape.SLD_S;
        else if(crntBlockShape == BlockShape.Z) solidBlockShape = BlockShape.SLD_Z;
        else if(crntBlockShape == BlockShape.L) solidBlockShape = BlockShape.SLD_L;
        else solidBlockShape = BlockShape.SLD_J;

        // solidification
        for(int r = 0; r < 4; r++) {
            for(int c = 0; c < 4; c++) {
                if(crntBlock[r][c] == crntBlockShape) {
                    crntBlock[r][c] = solidBlockShape;
                }
            }
        }
        uploadCrntBlock();
        numOfUsedBlocks++;
    }
    public void setNewBlock() {
        setCrntBlock();
        setCrntBlockShape();
        initPosition();
        uploadCrntBlock();
    }
    public void initLiquidBlock() {
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
    public void move(Direction direction) {
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
    public boolean movable(Direction direction) {
        if(direction == Direction.LEFT) {
            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 4; c++) {
                    if(crntBlock[r][c] == crntBlockShape) { // 4x4 행렬 속에서 블럭이 존재할 때
                        Position testPos = new Position(r + pos.r, c + pos.c - 1);
                        BlockShape testResult = solidOrLiquid(testPos);
                        if(testResult == BlockShape.SOLID) return false;
                    }
                }
            }
        }
        else if(direction == Direction.RIGHT) {
            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 4; c++) {
                    if(crntBlock[r][c] == crntBlockShape) { // 4x4 행렬 속에서 블럭이 존재할 때
                        Position testPos = new Position(r + pos.r, c + pos.c + 1);
                        BlockShape testResult = solidOrLiquid(testPos);
                        if(testResult == BlockShape.SOLID) return false;
                    }
                }
            }
        }
        else if(direction == Direction.DOWN) {
            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 4; c++) {
                    if(crntBlock[r][c] == crntBlockShape) { // 4x4 행렬 속에서 블럭이 존재할 때
                        Position testPos = new Position(r + pos.r + 1, c + pos.c);
                        BlockShape testResult = solidOrLiquid(testPos);
                        if(testResult == BlockShape.SOLID) return false;
                    }
                }
            }
        }
        else { // if(direction == Direction.UP)
            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 4; c++) {
                    if(crntBlock[r][c] == crntBlockShape) { // 4x4 행렬 속에서 블럭이 존재할 때
                        Position testPos = new Position(r + pos.r - 1, c + pos.c);
                        BlockShape testResult = solidOrLiquid(testPos);
                        if(testResult == BlockShape.SOLID) return false;
                    }
                }
            }
        }

        // 모든 조건 통과
        return true;
    }
    private BlockShape solidOrLiquid(Position testPos) {
        // 테트리스 화면 밖 모든 블럭은 Solid 블록이다.

        // Array Index Out Of Bounds Exception
        if(testPos.c < 0 || testPos.c >= GameBoard.TABLE_WIDTH || testPos.r >= GameBoard.TABLE_HEIGHT || testPos.r < 0) 
            return BlockShape.SOLID; 
        
        BlockShape mino = table[testPos.r][testPos.c].mino;
        if (mino == BlockShape.NONE) return BlockShape.NONE;
        else if(mino == BlockShape.SLD_I || mino == BlockShape.SLD_J || mino == BlockShape.SLD_L || mino == BlockShape.SLD_O || mino == BlockShape.SLD_S || mino == BlockShape.SLD_J || mino == BlockShape.SLD_T || mino == BlockShape.SLD_Z)
            return BlockShape.SOLID;
        else 
            return BlockShape.LIQUID;
    }
    public BlockShape intToBlockShape(int randNum) {
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
