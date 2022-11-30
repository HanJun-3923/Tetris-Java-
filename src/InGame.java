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
        public void init() {
            r = INITIAL_POS_R;
            c = INITIAL_POS_C;
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
        private int rotation;
        
        public void turnClockwise() {
            rotation += 1;
            while(rotation >= 4) rotation -= 4;
            setCrntBlockArray();
        }
        public void turnCounterClockwise() {
            rotation -= 1;
            while(rotation < 0) rotation += 4;
            setCrntBlockArray();
        }
        public void init() {
            rotation = 0;
            setCrntBlockArray();
        }
        public int getValue() {
            return rotation;
        }
        public Rotation() {
            rotation = 0;
        }
    }
    
    private final int INITIAL_POS_C = 3; // 초기 열 위치
    private final int INITIAL_POS_R = 0; // 초기 행 위치
    
    private Position position = new Position(INITIAL_POS_R, INITIAL_POS_C); // 블록의 좌표 (행렬)
    public Rotation rotation = new Rotation();
    private int numOfUsedBlocks = 0; // 놓아진 모든 블록의 수

    public Table[][] mainTable = new Table[GameBoard.MAIN_BOARD.INT_HEIGHT][GameBoard.MAIN_BOARD.INT_WIDTH];
    private BlockShape[][] crntBlockArray = new BlockShape[4][4];
    private BlockShape crntBlockShape = BlockShape.NONE;

    public Table[][] nextBlocksTable = new Table[GameBoard.NEXT_BLOCKS_BOARD_HEIGHT_PER_VISION * GameBoard.VISION_OF_NEXT_BLOCKS][GameBoard.NEXT_BLOCKS_BOARD.INT_WIDTH];
    private BlockShape[] nextBlocksArray = new BlockShape[BAG * 2];
    

    public InGame() {
        // mainTable 2차원 배열 할당
        for(int r = 0; r < GameBoard.MAIN_BOARD.INT_HEIGHT; r++) {
            for(int c = 0; c < GameBoard.MAIN_BOARD.INT_WIDTH; c++) {
                mainTable[r][c] = new Table(false, BlockShape.NONE);
            }
        } 
        // nextBlocksTable 2차원 객체 배열 할당
        for(int r = 0; r < GameBoard.NEXT_BLOCKS_BOARD_HEIGHT_PER_VISION * GameBoard.VISION_OF_NEXT_BLOCKS; r++) {
            for(int c = 0; c < GameBoard.NEXT_BLOCKS_BOARD.INT_WIDTH; c++) {
                nextBlocksTable[r][c] = new Table(false, BlockShape.NONE);
            }
        }
        // nextBlocksArray 1차원 배열 할당
        for(int i = 0; i < BAG * 2; i++) {
            nextBlocksArray[i] = BlockShape.NONE;
        }
        //position 객체 생성
    }
    
    // ***** 게임을 시작할 때 *****
    public void gameStart() {
        setNextArray();
        setNewBlock();
    }
    // ***** 블록을 놓을 때 *****
    public void putDownBlock() {
        solidification();
        numOfUsedBlocks++;
        lineClear();

        if(numOfUsedBlocks % BAG == 0) setNextArray();
        setNewBlock();
    }
    // ***** 새로운 블록을 불러올 때 *****
    public void setNewBlock() {
        rotation.init();
        setCrntBlockArray();
        setCrntBlockShape();
        position.init();
        uploadCrntBlockData();
        setNextBlocksTable();
        
    }
    // ***** 매 움직임에 따른 업데이트 ******
    public void updateWithEveryMove() {
        uploadCrntBlockData();
        uploadGhostBlockData();
    }

    public void hardDrop() {
        while(movable(Direction.DOWN)) {
            move(Direction.DOWN);
        }
        putDownBlock();
    }
    public void move(Direction direction) {
        if(direction == Direction.LEFT) {
            position.c--;
        } 
        else if(direction == Direction.RIGHT) {
            position.c++;
        }
        else if(direction == Direction.DOWN) {
            position.r++;
        }
        else { // direction == Direction.UP
            position.r--;
        }
    }
    public boolean movable(Direction direction) {
        if(direction == Direction.LEFT) {
            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 4; c++) {
                    if(crntBlockArray[r][c] == crntBlockShape) { // 4x4 행렬 속에서 블럭이 존재할 때
                        Position testPos = new Position(r + position.r, c + position.c - 1);
                        BlockShape testResult = solidOrLiquid(testPos);
                        if(testResult == BlockShape.SOLID) return false;
                    }
                }
            }
        }
        else if(direction == Direction.RIGHT) {
            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 4; c++) {
                    if(crntBlockArray[r][c] == crntBlockShape) { // 4x4 행렬 속에서 블럭이 존재할 때
                        Position testPos = new Position(r + position.r, c + position.c + 1);
                        BlockShape testResult = solidOrLiquid(testPos);
                        if(testResult == BlockShape.SOLID) return false;
                    }
                }
            }
        }
        else if(direction == Direction.DOWN) {
            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 4; c++) {
                    if(crntBlockArray[r][c] == crntBlockShape) { // 4x4 행렬 속에서 블럭이 존재할 때
                        Position testPos = new Position(r + position.r + 1, c + position.c);
                        BlockShape testResult = solidOrLiquid(testPos);
                        if(testResult == BlockShape.SOLID) return false;
                    }
                }
            }
        }
        else if(direction == Direction.UP) {
            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 4; c++) {
                    if(crntBlockArray[r][c] == crntBlockShape) { // 4x4 행렬 속에서 블럭이 존재할 때
                        Position testPos = new Position(r + position.r - 1, c + position.c);
                        BlockShape testResult = solidOrLiquid(testPos);
                        if(testResult == BlockShape.SOLID) return false;
                    }
                }
            }
        }
        else { // direction == Direction.NONE
            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 4; c++) {
                    if(crntBlockArray[r][c] == crntBlockShape) { // 4x4 행렬 속에서 블럭이 존재할 때
                        Position testPos = new Position(r + position.r, c + position.c);
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
            rotation.turnClockwise();
            if(!movable(Direction.NONE)) wallKick(Direction.CLOCKWISE);
        }
        else {
            rotation.turnCounterClockwise();
            if(!movable(Direction.NONE)) wallKick(Direction.COUNTER_CLOCKWISE);
        }
        setCrntBlockArray();
        uploadCrntBlockData();
    }

    //private
    private void uploadGhostBlockData() {
        
    }
    private void uploadCrntBlockData() {
        initLiquidBlock();
        for(int r = 0; r < 4; r++) {
            for(int c = 0; c < 4; c++) {
                if(crntBlockArray[r][c] != BlockShape.NONE) {
                    mainTable[position.r + r][position.c + c].mino = crntBlockArray[r][c];
                    mainTable[position.r + r][position.c + c].isVisible = true;
                }
            }
        }
    }
    private void setNextArray() { 
        if(nextBlocksArray[0] == BlockShape.NONE) { // set nextBlocksArray Initially
            for(int i = 0; i < BAG * 2; i++) { 
                boolean again;
                do {
                    again = false;
                    nextBlocksArray[i] = intToBlockShape((int)(Math.random() * 10) % 7 + 1);
                    //if i is in first BAG
                    if(i < BAG) {
                        for(int j = 0; j < i; j++)
                        // if any next has same BlockShape with another in its bag, it goes again.
                            if(nextBlocksArray[j] == nextBlocksArray[i]) again = true; 
                    }
                    else { // if i is in second BAG
                        for(int j = BAG; j < i; j++) {
                            // if any next has same BlockShape with another in its bag, it goes again.
                            if(nextBlocksArray[j] == nextBlocksArray[i]) again = true; 
                        }
                    }
                } while(again);
            }
        }
        else { // put into first bag next blocks in second bag, and make new second bag.
            for(int i = 0; i < BAG; i++) {
                int j = i + BAG;
                nextBlocksArray[i] = nextBlocksArray[j];
            }
            for(int i = BAG; i < 14; i++) {
                boolean again;
                do {
                    again = false;
                    nextBlocksArray[i] = intToBlockShape((int)(Math.random() * 10) % 7 + 1);
                    for(int j = BAG; j < i; j++) {
                        if(nextBlocksArray[j] == nextBlocksArray[i]) again = true;
                    }
                } while(again);
            }
        }
    }
    private void setCrntBlockShape() {
        crntBlockShape = nextBlocksArray[numOfUsedBlocks % 7];
    }
    private void setCrntBlockArray() {
        int[][] temp = new int[4][4];
        temp = BlockData.fetch(nextBlocksArray[numOfUsedBlocks % BAG]);
        for(int r = 0; r < 4; r++) {
            for(int c = 0; c < 4; c++) {
                crntBlockArray[r][c] = intToBlockShape(temp[r][c]);
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
                if(crntBlockArray[r][c] == crntBlockShape) {
                    crntBlockArray[r][c] = solidBlockShape;
                }
            }
        }
        uploadCrntBlockData(); // upload solid crntBlockArray
    }
    private void initLiquidBlock() {
        // init Liquid Components
        for(int r = 0; r < GameBoard.MAIN_BOARD.INT_HEIGHT; r++) {
            for(int c = 0; c < GameBoard.MAIN_BOARD.INT_WIDTH; c++) {
                Position testPos = new Position(r, c);
                BlockShape testResult = solidOrLiquid(testPos);
                if(testResult == BlockShape.LIQUID) {
                    //init
                    mainTable[r][c].mino = BlockShape.NONE;
                    mainTable[r][c].isVisible = false;
                }
            }
        }
    }
    private BlockShape solidOrLiquid(Position testPos) {
        // 테트리스 화면 밖 모든 블럭은 Solid 블록이다.

        // Array Index Out Of Bounds Exception
        if(testPos.c < 0 || testPos.c >= GameBoard.MAIN_BOARD.INT_WIDTH || testPos.r >= GameBoard.MAIN_BOARD.INT_HEIGHT || testPos.r < 0) 
            return BlockShape.SOLID; 
        
        BlockShape mino = mainTable[testPos.r][testPos.c].mino;
        if (mino == BlockShape.NONE) return BlockShape.NONE;
        else if(mino == BlockShape.SLD_I || mino == BlockShape.SLD_J || mino == BlockShape.SLD_L || mino == BlockShape.SLD_O || mino == BlockShape.SLD_S || mino == BlockShape.SLD_J || mino == BlockShape.SLD_T || mino == BlockShape.SLD_Z)
            return BlockShape.SOLID;
        else 
            return BlockShape.LIQUID;
    }
    private BlockShape intToBlockShape(int num) {
        switch(num) {
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
        Position crntPos = new Position(position.r, position.c);

        if(direction == Direction.CLOCKWISE) {
            // *** J, L, T, S, Z Tetromino Wall Kick Data ***
            if(crntBlockShape == BlockShape.J || crntBlockShape == BlockShape.L || crntBlockShape == BlockShape.T || crntBlockShape == BlockShape.S || crntBlockShape == BlockShape.Z) {
                // Test 1
                // (-1, 0) (1, 0) (1, 0) (-1, 0)
                if(rotation.getValue() == 1) move(Direction.LEFT);
                else if(rotation.getValue() == 2) move(Direction.RIGHT);
                else if(rotation.getValue() == 3) move(Direction.RIGHT);
                else move(Direction.LEFT);


                if(movable(Direction.NONE)) return;

                // Test 2
                // (-1, 1) (1, -1) (1, 1) (-1, -1)
                if(rotation.getValue() == 1) move(Direction.UP);
                else if(rotation.getValue() == 2) move(Direction.DOWN);
                else if(rotation.getValue() == 3) move(Direction.UP);
                else move(Direction.DOWN);

                if(movable(Direction.NONE)) return;
                else position.set(crntPos.r, crntPos.c);

                // Test 3
                // (0. -2) (0, 2) (0, -2) (0, 2)
                if(rotation.getValue() == 1) { move(Direction.DOWN); move(Direction.DOWN); }
                else if(rotation.getValue() == 2) { move(Direction.UP); move(Direction.UP); }
                else if(rotation.getValue() == 3) { move(Direction.DOWN); move(Direction.DOWN); }
                else { move(Direction.UP); move(Direction.UP); }
            
                if(movable(Direction.NONE)) return;

                // Test 4
                // (-1, -2) (1, 2) (1, -2) (-1, 2)
                if(rotation.getValue() == 1) move(Direction.LEFT);
                else if(rotation.getValue() == 2) move(Direction.RIGHT);
                else if(rotation.getValue() == 3) move(Direction.RIGHT);
                else move(Direction.LEFT);

                if(movable(Direction.NONE)) return;
                else { position.set(crntPos.r, crntPos.c); rotation.turnCounterClockwise(); } // 모든 테스트 실패 => 원위치
            }
            // *** I Tetromino Wall Kick Data ***
            else if(crntBlockShape == BlockShape.I) {
                // Test 1
                // (-2, 0) (-1, 0) (2, 0) (1, 0)
                if(rotation.getValue() == 1) { move(Direction.LEFT); move(Direction.LEFT); }
                else if(rotation.getValue() == 2) move(Direction.LEFT);
                else if(rotation.getValue() == 3) { move(Direction.RIGHT); move(Direction.RIGHT); }
                else move(Direction.RIGHT);

                if(movable(Direction.NONE)) return;
                else position.set(crntPos.r, crntPos.c);

                // Test 2
                // (1, 0) (2, 0) (-1, 0) (-2, 0)
                if(rotation.getValue() == 1) move(Direction.RIGHT);
                else if(rotation.getValue() == 2) { move(Direction.RIGHT); move(Direction.RIGHT); }
                else if(rotation.getValue() == 3) move(Direction.LEFT);
                else { move(Direction.LEFT); move(Direction.LEFT); }

                if(movable(Direction.NONE)) return;
                else position.set(crntPos.r, crntPos.c);

                // Test 3
                // (-2, -1) (-1, 2) (2, 1) (1, -2)
                if(rotation.getValue() == 1) { move(Direction.LEFT); move(Direction.LEFT); move(Direction.DOWN); }
                else if(rotation.getValue() == 2) { move(Direction.LEFT); move(Direction.UP); move(Direction.UP); }
                else if(rotation.getValue() == 3) { move(Direction.RIGHT); move(Direction.RIGHT); move(Direction.UP); }
                else { move(Direction.RIGHT); move(Direction.DOWN); move(Direction.DOWN); }
            
                if(movable(Direction.NONE)) return;

                // Test 4
                // (1, 2) (2, -1) (-1, -2) (-2, 1)
                if(rotation.getValue() == 1) { move(Direction.RIGHT); move(Direction.UP); move(Direction.UP); }
                else if(rotation.getValue() == 2) { move(Direction.RIGHT); move(Direction.RIGHT); move(Direction.DOWN); }
                else if(rotation.getValue() == 3) { move(Direction.LEFT); move(Direction.DOWN); move(Direction.DOWN); }
                else { move(Direction.LEFT); move(Direction.LEFT); move(Direction.UP); }

                if(movable(Direction.NONE)) return;
                else { position.set(crntPos.r, crntPos.c); rotation.turnCounterClockwise(); } // 모든 테스트 실패 => 원위치
            }
        }
        else { // Direction.COUNTER_CLOCKWISE
            // *** J, L, T, S, Z Tetromino Wall Kick Data ***
            if(crntBlockShape == BlockShape.J || crntBlockShape == BlockShape.L || crntBlockShape == BlockShape.T || crntBlockShape == BlockShape.S || crntBlockShape == BlockShape.Z) {
                // Test 1
                // (1, 0) (-1, 0) (-1, 0) (1, 0)
                if(rotation.getValue() == 0) move(Direction.RIGHT);
                else if(rotation.getValue() == 1) move(Direction.LEFT);
                else if(rotation.getValue() == 2) move(Direction.LEFT);
                else move(Direction.RIGHT);


                if(movable(Direction.NONE)) return;

                // Test 2
                // (1, -1) (-1, 1) (-1, -1) (1, 1)
                if(rotation.getValue() == 0) move(Direction.DOWN);
                else if(rotation.getValue() == 1) move(Direction.UP);
                else if(rotation.getValue() == 2) move(Direction.DOWN);
                else move(Direction.UP);

                if(movable(Direction.NONE)) return;
                else position.set(crntPos.r, crntPos.c);

                // Test 3
                // (0. 2) (0, -2) (0, 2) (0, -2)
                if(rotation.getValue() == 0) { move(Direction.UP); move(Direction.UP); }
                else if(rotation.getValue() == 1) { move(Direction.DOWN); move(Direction.DOWN); }
                else if(rotation.getValue() == 2) { move(Direction.UP); move(Direction.UP); }
                else { move(Direction.DOWN); move(Direction.DOWN); }
            
                if(movable(Direction.NONE)) return;

                // Test 4
                // (1, 2) (-1, -2) (-1, 2) (1, -2)
                if(rotation.getValue() == 0) move(Direction.RIGHT);
                else if(rotation.getValue() == 1) move(Direction.LEFT);
                else if(rotation.getValue() == 2) move(Direction.LEFT);
                else move(Direction.RIGHT);

                if(movable(Direction.NONE)) return;
                else { position.set(crntPos.r, crntPos.c); rotation.turnCounterClockwise(); } // 모든 테스트 실패 => 원위치
            }
            // *** I Tetromino Wall Kick Data ***
            else if(crntBlockShape == BlockShape.I) {
                // Test 1
                // (2, 0) (1, 0) (-2, 0) (-1, 0)
                if(rotation.getValue() == 1) { move(Direction.RIGHT); move(Direction.RIGHT); }
                else if(rotation.getValue() == 2) move(Direction.RIGHT);
                else if(rotation.getValue() == 3) { move(Direction.LEFT); move(Direction.LEFT); }
                else move(Direction.LEFT);

                if(movable(Direction.NONE)) return;
                else position.set(crntPos.r, crntPos.c);

                // Test 2
                // (-1, 0) (-2, 0) (1, 0) (2, 0)
                if(rotation.getValue() == 1) move(Direction.LEFT);
                else if(rotation.getValue() == 2) { move(Direction.LEFT); move(Direction.LEFT); }
                else if(rotation.getValue() == 3) move(Direction.RIGHT);
                else { move(Direction.RIGHT); move(Direction.RIGHT); }

                if(movable(Direction.NONE)) return;
                else position.set(crntPos.r, crntPos.c);

                // Test 3
                // (2, 1) (1, -2) (-2, -1) (-1, 2)
                if(rotation.getValue() == 1) { move(Direction.RIGHT); move(Direction.RIGHT); move(Direction.UP); }
                else if(rotation.getValue() == 2) { move(Direction.RIGHT); move(Direction.DOWN); move(Direction.DOWN); }
                else if(rotation.getValue() == 3) { move(Direction.LEFT); move(Direction.LEFT); move(Direction.DOWN); }
                else { move(Direction.LEFT); move(Direction.UP); move(Direction.UP); }
            
                if(movable(Direction.NONE)) return;

                // Test 4
                // (-1, -2) (-2, 1) (1, 2) (2, -1)
                if(rotation.getValue() == 1) { move(Direction.LEFT); move(Direction.DOWN); move(Direction.DOWN); }
                else if(rotation.getValue() == 2) { move(Direction.LEFT); move(Direction.LEFT); move(Direction.UP); }
                else if(rotation.getValue() == 3) { move(Direction.RIGHT); move(Direction.UP); move(Direction.UP); }
                else { move(Direction.RIGHT); move(Direction.RIGHT); move(Direction.DOWN); }

                if(movable(Direction.NONE)) return;
                else { position.set(crntPos.r, crntPos.c); rotation.turnCounterClockwise(); } // 모든 테스트 실패 => 원위치
            }
        }
    }
    private void lineClear() {
        for(int r = 0; r < GameBoard.MAIN_BOARD.INT_HEIGHT; r++) {
            if(isLineFull(r)) { // 라인이 다 찼다면
                for(int upperRow = r - 1; upperRow >= 0; upperRow--) { // upperRow + 1 로 인해 MAIN_BOARD.INT_HEIGHT - 1 까지 반복
                    for(int c = 0; c < GameBoard.MAIN_BOARD.INT_WIDTH; c++) {
                        mainTable[upperRow + 1][c].mino = mainTable[upperRow][c].mino;
                        mainTable[upperRow + 1][c].isVisible = mainTable[upperRow][c].isVisible;
                        if(upperRow == 0) {
                            mainTable[upperRow][c].mino = BlockShape.NONE;
                            mainTable[upperRow][c].isVisible = false;
                        }
                    }
                }
            }
        }
    }
    private boolean isLineFull(int r) {
        for(int c = 0; c < GameBoard.MAIN_BOARD.INT_WIDTH; c++) {
            // 하나라도 블럭이 비었다면 return false
            if(mainTable[r][c].isVisible == false) return false; 
        }
        // 모두 블럭이 차있으므로 return true
        return true;
    }
    private void setNextBlocksTable() {
        if(numOfUsedBlocks == 0) { // INITIAL SETTING
            for(int i = 0; i < GameBoard.VISION_OF_NEXT_BLOCKS; i++) {
                // nextBlock 을 만든 다음 이를 nextBlocksTable의 적절한 위치에 넣는다.
                BlockShape[][] nextBlock = new BlockShape[GameBoard.NEXT_BLOCKS_BOARD_HEIGHT_PER_VISION][GameBoard.NEXT_BLOCKS_BOARD.INT_WIDTH];
                int[][] temp = new int[4][4];
                temp = BlockData.fetch(nextBlocksArray[numOfUsedBlocks % BAG + (i + 1)]); // i번째 넥스트 블럭을 가져와 temp에 int형으로 저장
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
            temp = BlockData.fetch(nextBlocksArray[numOfUsedBlocks % BAG + 5]); 
            
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


