enum Direction { // 방향에 대한 열거형
    NONE, LEFT, RIGHT, DOWN, UP, CLOCKWISE, COUNTER_CLOCKWISE
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
        public void set(int r, int c) {
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
    class Rotation {
        int rotation;
        public void plus(int n) {
            rotation += n;
            while(rotation >= 4) rotation -= 4;
            setCrntBlock();
        }
        public void minus(int n) {
            rotation -= n;
            while(rotation < 0) rotation += 4;
            setCrntBlock();
        }
        public void set(int n) {
            rotation = n;
            setCrntBlock();
        }
        public Rotation(int n) {
            rotation = n;
        }
    }
    public Position pos; // 블록의 좌표 (행렬)
    public Rotation rotation = new Rotation(0); // 회전된 수
    private final int INITIAL_POS_C = 3; // 새로운 블록의 열
    public final int INITIAL_POS_R = 0; // 새로운 블록의 행

    public int numOfUsedBlocks = 0; // 놓아진 모든 블록의 수

    public BlockShape[] nextBlocks = new BlockShape[BAG * 2];
    public BlockShape crntBlockShape = BlockShape.NONE;
    public Table[][] table = new Table[GameBoard.MAIN_BOARD.INT_HEIGHT][GameBoard.MAIN_BOARD.INT_WIDTH];
    public BlockShape[][] crntBlock = new BlockShape[4][4];
    public Table[][] nextBlocksTable = new Table[GameBoard.NEXT_BLOCKS_BOARD_HEIGHT_PER_VISION * GameBoard.VISION_OF_NEXT_BLOCKS][GameBoard.NEXT_BLOCKS_BOARD.INT_WIDTH];


    public InGame() {
        // 다차원 배열 할당
        for(int r = 0; r < GameBoard.MAIN_BOARD.INT_HEIGHT; r++) {
            for(int c = 0; c < GameBoard.MAIN_BOARD.INT_WIDTH; c++) {
                table[r][c] = new Table(false, BlockShape.NONE);
            }
        } 
        for(int r = 0; r < GameBoard.NEXT_BLOCKS_BOARD_HEIGHT_PER_VISION * GameBoard.VISION_OF_NEXT_BLOCKS; r++) {
            for(int c = 0; c < GameBoard.NEXT_BLOCKS_BOARD.INT_WIDTH; c++) {
                nextBlocksTable[r][c] = new Table(false, BlockShape.NONE);
            }
        }
        for(int i = 0; i < BAG * 2; i++) {
            nextBlocks[i] = BlockShape.NONE;
        }
    }

    //public
    public void hardDrop() {
        while(movable(Direction.DOWN)) {
            move(Direction.DOWN);
        }
        solidification();
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
    public void setNewBlock() {
        rotation.set(0);
        setCrntBlock();
        setCrntBlockShape();
        initPosition();
        uploadCrntBlock();
        setNextBlocksTable();
        if(numOfUsedBlocks % 7 == 6) setNextBlocks();
        
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
        else if(direction == Direction.UP) {
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
        else { // direction == Direction.NONE
            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 4; c++) {
                    if(crntBlock[r][c] == crntBlockShape) { // 4x4 행렬 속에서 블럭이 존재할 때
                        Position testPos = new Position(r + pos.r, c + pos.c);
                        BlockShape testResult = solidOrLiquid(testPos);
                        if(testResult == BlockShape.SOLID) return false;
                    }
                }
            }
        }

        // 모든 조건 통과
        return true;
    }
    public void rotation(Direction direction) {
        if(direction == Direction.CLOCKWISE) {
            rotation.plus(1);
            if(!movable(Direction.NONE)) wallKick(Direction.CLOCKWISE);
        }
        else {
            rotation.minus(1);
            if(!movable(Direction.NONE)) wallKick(Direction.COUNTER_CLOCKWISE);
        }
        setCrntBlock();
        uploadCrntBlock();
    }
    public void uploadCrntBlock() {
        initLiquidBlock();
        for(int r = 0; r < 4; r++) {
            for(int c = 0; c < 4; c++) {
                if(crntBlock[r][c] != BlockShape.NONE) {
                    table[pos.r + r][pos.c + c].mino = crntBlock[r][c];
                    table[pos.r + r][pos.c + c].isVisible = true;
                }
            }
        }
    }


    //private
    private void setCrntBlockShape() {
        crntBlockShape = nextBlocks[numOfUsedBlocks % 7];
    }
    private void initPosition() {
        if(pos == null) pos = new Position(0, 0); // NullPointerException 처리
        pos.c = INITIAL_POS_C;
        pos.r = INITIAL_POS_R;
    }
    private void setCrntBlock() {
        int[][] temp = new int[4][4];
        temp = BlockData.fetch(nextBlocks[numOfUsedBlocks % BAG]);
        for(int r = 0; r < 4; r++) {
            for(int c = 0; c < 4; c++) {
                crntBlock[r][c] = intToBlockShape(temp[r][c]);
            } 
        }
    }
    private void solidification() {
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
        uploadCrntBlock(); // upload solid crntBlock
        numOfUsedBlocks++;
        lineClear();
        setNewBlock();
    }
    private void initLiquidBlock() {
        // init Liquid Components
        for(int r = 0; r < GameBoard.MAIN_BOARD.INT_HEIGHT; r++) {
            for(int c = 0; c < GameBoard.MAIN_BOARD.INT_WIDTH; c++) {
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
    private BlockShape solidOrLiquid(Position testPos) {
        // 테트리스 화면 밖 모든 블럭은 Solid 블록이다.

        // Array Index Out Of Bounds Exception
        if(testPos.c < 0 || testPos.c >= GameBoard.MAIN_BOARD.INT_WIDTH || testPos.r >= GameBoard.MAIN_BOARD.INT_HEIGHT || testPos.r < 0) 
            return BlockShape.SOLID; 
        
        BlockShape mino = table[testPos.r][testPos.c].mino;
        if (mino == BlockShape.NONE) return BlockShape.NONE;
        else if(mino == BlockShape.SLD_I || mino == BlockShape.SLD_J || mino == BlockShape.SLD_L || mino == BlockShape.SLD_O || mino == BlockShape.SLD_S || mino == BlockShape.SLD_J || mino == BlockShape.SLD_T || mino == BlockShape.SLD_Z)
            return BlockShape.SOLID;
        else 
            return BlockShape.LIQUID;
    }
    private BlockShape intToBlockShape(int randNum) {
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
    private void wallKick(Direction direction) {
        // reference: https://tetris.fandom.com/wiki/SRS
        Position crntPos = new Position(pos.r, pos.c);


        // J, L, T, S, Z Tetromino Wall Kick Data
        if(direction == Direction.CLOCKWISE) {
            if(crntBlockShape == BlockShape.J || crntBlockShape == BlockShape.L || crntBlockShape == BlockShape.T || crntBlockShape == BlockShape.S || crntBlockShape == BlockShape.Z) {
                // Test 1
                // (-1, 0) (1, 0) (1, 0) (-1, 0)
                if(rotation.rotation == 1) move(Direction.LEFT);
                else if(rotation.rotation == 2) move(Direction.RIGHT);
                else if(rotation.rotation == 3) move(Direction.RIGHT);
                else move(Direction.LEFT);


                if(movable(Direction.NONE)) return;

                // Test 2
                // (-1, 1) (1, -1) (1, 1) (-1, -1)
                if(rotation.rotation == 1) move(Direction.UP);
                else if(rotation.rotation == 2) move(Direction.DOWN);
                else if(rotation.rotation == 3) move(Direction.UP);
                else move(Direction.DOWN);

                if(movable(Direction.NONE)) return;
                else pos.set(crntPos.r, crntPos.c);

                // Test 3
                // (0. -2) (0, 2) (0, -2) (0, 2)
                if(rotation.rotation == 1) { move(Direction.DOWN); move(Direction.DOWN); }
                else if(rotation.rotation == 2) { move(Direction.UP); move(Direction.UP); }
                else if(rotation.rotation == 3) { move(Direction.DOWN); move(Direction.DOWN); }
                else { move(Direction.UP); move(Direction.UP); }
            
                if(movable(Direction.NONE)) return;

                // Test 4
                // (-1, -2) (1, 2) (1, -2) (-1, 2)
                if(rotation.rotation == 1) move(Direction.LEFT);
                else if(rotation.rotation == 2) move(Direction.RIGHT);
                else if(rotation.rotation == 3) move(Direction.RIGHT);
                else move(Direction.LEFT);

                if(movable(Direction.NONE)) return;
                else { pos.set(crntPos.r, crntPos.c); rotation.minus(1); } // 모든 테스트 실패 => 원위치
            } 
            else if(crntBlockShape == BlockShape.I) {
                // Test 1
                // (-2, 0) (-1, 0) (2, 0) (1, 0)
                if(rotation.rotation == 1) { move(Direction.LEFT); move(Direction.LEFT); }
                else if(rotation.rotation == 2) move(Direction.LEFT);
                else if(rotation.rotation == 3) { move(Direction.RIGHT); move(Direction.RIGHT); }
                else move(Direction.RIGHT);

                if(movable(Direction.NONE)) return;
                else pos.set(crntPos.r, crntPos.c);

                // Test 2
                // (1, 0) (2, 0) (-1, 0) (-2, 0)
                if(rotation.rotation == 1) move(Direction.RIGHT);
                else if(rotation.rotation == 2) { move(Direction.RIGHT); move(Direction.RIGHT); }
                else if(rotation.rotation == 3) move(Direction.LEFT);
                else { move(Direction.LEFT); move(Direction.LEFT); }

                if(movable(Direction.NONE)) return;
                else pos.set(crntPos.r, crntPos.c);

                // Test 3
                // (-2, -1) (-1, 2) (2, 1) (1, -2)
                if(rotation.rotation == 1) { move(Direction.LEFT); move(Direction.LEFT); move(Direction.DOWN); }
                else if(rotation.rotation == 2) { move(Direction.LEFT); move(Direction.UP); move(Direction.UP); }
                else if(rotation.rotation == 3) { move(Direction.RIGHT); move(Direction.RIGHT); move(Direction.UP); }
                else { move(Direction.RIGHT); move(Direction.DOWN); move(Direction.DOWN); }
            
                if(movable(Direction.NONE)) return;

                // Test 4
                // (1, 2) (2, -1) (-1, -2) (-2, 1)
                if(rotation.rotation == 1) { move(Direction.RIGHT); move(Direction.UP); move(Direction.UP); }
                else if(rotation.rotation == 2) { move(Direction.RIGHT); move(Direction.RIGHT); move(Direction.DOWN); }
                else if(rotation.rotation == 3) { move(Direction.LEFT); move(Direction.DOWN); move(Direction.DOWN); }
                else { move(Direction.LEFT); move(Direction.LEFT); move(Direction.UP); }

                if(movable(Direction.NONE)) return;
                else { pos.set(crntPos.r, crntPos.c); rotation.minus(1); } // 모든 테스트 실패 => 원위치
            }
        }
        else { // Direction.COUNTER_CLOCKWISE
            if(crntBlockShape == BlockShape.J || crntBlockShape == BlockShape.L || crntBlockShape == BlockShape.T || crntBlockShape == BlockShape.S || crntBlockShape == BlockShape.Z) {
                // Test 1
                // (1, 0) (-1, 0) (-1, 0) (1, 0)
                if(rotation.rotation == 0) move(Direction.RIGHT);
                else if(rotation.rotation == 1) move(Direction.LEFT);
                else if(rotation.rotation == 2) move(Direction.LEFT);
                else move(Direction.RIGHT);


                if(movable(Direction.NONE)) return;

                // Test 2
                // (1, -1) (-1, 1) (-1, -1) (1, 1)
                if(rotation.rotation == 0) move(Direction.DOWN);
                else if(rotation.rotation == 1) move(Direction.UP);
                else if(rotation.rotation == 2) move(Direction.DOWN);
                else move(Direction.UP);

                if(movable(Direction.NONE)) return;
                else pos.set(crntPos.r, crntPos.c);

                // Test 3
                // (0. 2) (0, -2) (0, 2) (0, -2)
                if(rotation.rotation == 0) { move(Direction.UP); move(Direction.UP); }
                else if(rotation.rotation == 1) { move(Direction.DOWN); move(Direction.DOWN); }
                else if(rotation.rotation == 2) { move(Direction.UP); move(Direction.UP); }
                else { move(Direction.DOWN); move(Direction.DOWN); }
            
                if(movable(Direction.NONE)) return;

                // Test 4
                // (1, 2) (-1, -2) (-1, 2) (1, -2)
                if(rotation.rotation == 0) move(Direction.RIGHT);
                else if(rotation.rotation == 1) move(Direction.LEFT);
                else if(rotation.rotation == 2) move(Direction.LEFT);
                else move(Direction.RIGHT);

                if(movable(Direction.NONE)) return;
                else { pos.set(crntPos.r, crntPos.c); rotation.minus(1); } // 모든 테스트 실패 => 원위치
            }
            else if(crntBlockShape == BlockShape.I) {
                // Test 1
                // (2, 0) (1, 0) (-2, 0) (-1, 0)
                if(rotation.rotation == 1) { move(Direction.RIGHT); move(Direction.RIGHT); }
                else if(rotation.rotation == 2) move(Direction.RIGHT);
                else if(rotation.rotation == 3) { move(Direction.LEFT); move(Direction.LEFT); }
                else move(Direction.LEFT);

                if(movable(Direction.NONE)) return;
                else pos.set(crntPos.r, crntPos.c);

                // Test 2
                // (-1, 0) (-2, 0) (1, 0) (2, 0)
                if(rotation.rotation == 1) move(Direction.LEFT);
                else if(rotation.rotation == 2) { move(Direction.LEFT); move(Direction.LEFT); }
                else if(rotation.rotation == 3) move(Direction.RIGHT);
                else { move(Direction.RIGHT); move(Direction.RIGHT); }

                if(movable(Direction.NONE)) return;
                else pos.set(crntPos.r, crntPos.c);

                // Test 3
                // (2, 1) (1, -2) (-2, -1) (-1, 2)
                if(rotation.rotation == 1) { move(Direction.RIGHT); move(Direction.RIGHT); move(Direction.UP); }
                else if(rotation.rotation == 2) { move(Direction.RIGHT); move(Direction.DOWN); move(Direction.DOWN); }
                else if(rotation.rotation == 3) { move(Direction.LEFT); move(Direction.LEFT); move(Direction.DOWN); }
                else { move(Direction.LEFT); move(Direction.UP); move(Direction.UP); }
            
                if(movable(Direction.NONE)) return;

                // Test 4
                // (-1, -2) (-2, 1) (1, 2) (2, -1)
                if(rotation.rotation == 1) { move(Direction.LEFT); move(Direction.DOWN); move(Direction.DOWN); }
                else if(rotation.rotation == 2) { move(Direction.LEFT); move(Direction.LEFT); move(Direction.UP); }
                else if(rotation.rotation == 3) { move(Direction.RIGHT); move(Direction.UP); move(Direction.UP); }
                else { move(Direction.RIGHT); move(Direction.RIGHT); move(Direction.DOWN); }

                if(movable(Direction.NONE)) return;
                else { pos.set(crntPos.r, crntPos.c); rotation.minus(1); } // 모든 테스트 실패 => 원위치
            }
        }
    }
    private void lineClear() {
        for(int r = 0; r < GameBoard.MAIN_BOARD.INT_HEIGHT; r++) {
            if(isLineFull(r)) { // 라인이 다 찼다면
                for(int upperRow = r - 1; upperRow >= 0; upperRow--) { // upperRow + 1 로 인해 MAIN_BOARD.INT_HEIGHT - 1 까지 반복
                    for(int c = 0; c < GameBoard.MAIN_BOARD.INT_WIDTH; c++) {
                        table[upperRow + 1][c].mino = table[upperRow][c].mino;
                        table[upperRow + 1][c].isVisible = table[upperRow][c].isVisible;
                        if(upperRow == 0) {
                            table[upperRow][c].mino = BlockShape.NONE;
                            table[upperRow][c].isVisible = false;
                        }
                    }
                }
            }
        }
    }
    private boolean isLineFull(int r) {
        for(int c = 0; c < GameBoard.MAIN_BOARD.INT_WIDTH; c++) {
            // 하나라도 블럭이 비었다면 return false
            if(table[r][c].isVisible == false) return false; 
        }
        // 모두 블럭이 차있으므로 return true
        return true;
    }
    private void setNextBlocksTable() {
        /*
            [] [] [] []
            [] [] [] []
            [] [] [] []

            [] [] [] []
            [] [] [] []
            [] [] [] []

            [] [] [] []
            [] [] [] []
            [] [] [] []
        */
        if(numOfUsedBlocks == 0) { // INITIAL SETTING
            for(int i = 0; i < GameBoard.VISION_OF_NEXT_BLOCKS; i++) {
                // nextBlock 을 만든 다음 이를 nextBlocksTable의 적절한 위치에 넣는다.
                BlockShape[][] nextBlock = new BlockShape[GameBoard.NEXT_BLOCKS_BOARD_HEIGHT_PER_VISION][GameBoard.NEXT_BLOCKS_BOARD.INT_WIDTH];
                int[][] temp = new int[4][4];
                temp = BlockData.fetch(nextBlocks[numOfUsedBlocks % BAG + (i + 1)]); // i번째 넥스트 블럭을 가져와 temp에 int형으로 저장
                for(int r = 0; r < GameBoard.NEXT_BLOCKS_BOARD_HEIGHT_PER_VISION; r++) { // int -> BlockShape 형변환
                    for(int c = 0; c < GameBoard.NEXT_BLOCKS_BOARD.INT_WIDTH; c++) {
                        nextBlock[r][c] = intToBlockShape(temp[r][c]);
                    }
                }
                for(int r = 0; r < GameBoard.NEXT_BLOCKS_BOARD_HEIGHT_PER_VISION; r++) {
                    for(int c = 0; c < GameBoard.NEXT_BLOCKS_BOARD.INT_WIDTH; c++) {
                        if(nextBlock[r][c] != BlockShape.NONE) {
                            nextBlocksTable[i * 3 + r][c].mino = nextBlock[r][c];
                            nextBlocksTable[i * 3 + r][c].isVisible = true;
                        } else {
                            nextBlocksTable[i * 3 + r][c].mino = BlockShape.NONE;
                            nextBlocksTable[i * 3 + r][c].isVisible = false;
                        }
                        
                    }
                }
            }
        }
        else { // REPEATING SETTING
            for(int r = 3; r < GameBoard.NEXT_BLOCKS_BOARD_HEIGHT_PER_VISION * GameBoard.VISION_OF_NEXT_BLOCKS; r++) {
                for(int c = 0; c < GameBoard.NEXT_BLOCKS_BOARD.INT_WIDTH; c++) {
                    nextBlocksTable[r - 3][c].mino = nextBlocksTable[r][c].mino; 
                    nextBlocksTable[r - 3][c].isVisible = nextBlocksTable[r][c].isVisible; 
                }
            }
            BlockShape[][] nextBlock = new BlockShape[GameBoard.NEXT_BLOCKS_BOARD_HEIGHT_PER_VISION][GameBoard.NEXT_BLOCKS_BOARD.INT_WIDTH];
            int[][] temp = new int[4][4];
            
            // 5번째 넥스트 블럭을 가져와 temp에 int형으로 저장
            temp = BlockData.fetch(nextBlocks[numOfUsedBlocks % BAG + 5]); 
            
            for(int r = 0; r < GameBoard.NEXT_BLOCKS_BOARD_HEIGHT_PER_VISION; r++) { // int -> BlockShape 형변환
                for(int c = 0; c < GameBoard.NEXT_BLOCKS_BOARD.INT_WIDTH; c++) {
                    nextBlock[r][c] = intToBlockShape(temp[r][c]);
                }
            }
            for(int r = 0; r < GameBoard.NEXT_BLOCKS_BOARD_HEIGHT_PER_VISION; r++) {
                for(int c = 0; c < GameBoard.NEXT_BLOCKS_BOARD.INT_WIDTH; c++) {
                    if(nextBlock[r][c] != BlockShape.NONE) {
                        nextBlocksTable[12 + r][c].mino = nextBlock[r][c];
                        nextBlocksTable[12 + r][c].isVisible = true;
                    } else {
                        nextBlocksTable[12 + r][c].mino = BlockShape.NONE;
                        nextBlocksTable[12 + r][c].isVisible = false;
                    }
                    
                }
            }
        }
    }
}




/*
Wall Kick GuideLine (S Z L J T)

0>>1   (-1, 0) (-1, 1) ( 0,-2) (-1,-2)
1>>2   ( 1, 0) ( 1,-1) ( 0, 2) ( 1, 2)
2>>3   ( 1, 0) ( 1, 1) ( 0,-2) ( 1,-2)
3>>0   (-1, 0) (-1,-1) ( 0, 2) (-1, 2)

1>>0   ( 1, 0) ( 1,-1) ( 0, 2) ( 1, 2)
2>>1   (-1, 0) (-1, 1) ( 0,-2) (-1,-2)
3>>2   (-1, 0) (-1,-1) ( 0, 2) (-1, 2)
0>>3   ( 1, 0) ( 1, 1) ( 0,-2) ( 1,-2)

*/

/*
블록을 새로 꺼낼 때 -> setNewBlock
블록을 이동했을 때(업로드) -> uploadBlock
블록을 놓을 때 -> solidification (사용된 블록 수 +1) -> 라인 클리어
블록을 다시 새로 꺼낼 때 -> setNewBlock
*/
