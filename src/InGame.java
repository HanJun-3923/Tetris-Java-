public class InGame {
    private static final int BAG = 7; // 한 가방 속에 존재하는 블록의 수
    
    private Position crntBlockPos = new Position(Position.INITIAL_POS_R, Position.INITIAL_POS_C); // 블록의 좌표 (행렬)
    private Position ghostBlockPos = new Position(Position.INITIAL_POS_R, Position.INITIAL_POS_C);
    public Rotation rotation = new Rotation();
    private int numOfUsedBlocks = 0; // 놓아진 모든 블록의 수

    public Table[][] mainTable = new Table[GameBoard.MAIN_BOARD.INT_HEIGHT][GameBoard.MAIN_BOARD.INT_WIDTH];
    private BlockShape[][] crntBlockArray = new BlockShape[4][4];
    private BlockShape crntBlockShape = BlockShape.NONE;
    private BlockShape[][] ghostBlockArray = new BlockShape[4][4];

    public Table[][] nextBlocksTable = new Table[GameBoard.NEXT_BLOCKS_BOARD_HEIGHT_PER_VISION * GameBoard.VISION_OF_NEXT_BLOCKS][GameBoard.NEXT_BLOCKS_BOARD.INT_WIDTH];
    private BlockShape[] nextBlocksArray = new BlockShape[BAG * 2];
    
    public Table[][] holdBlockTable = new Table[3][4];
    private BlockShape holdBlockShape = BlockShape.NONE;
    private boolean canHold = true;


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
        // holdBlockTable 2차원 객체 배열 할당
        for(int r = 0; r < 3; r++) {
            for (int c = 0; c < 4; c++) {
                holdBlockTable[r][c] = new Table(false, BlockShape.NONE);
            }
        }
        // nextBlocksArray 1차원 배열 할당
        for(int i = 0; i < BAG * 2; i++) {
            nextBlocksArray[i] = BlockShape.NONE;
        }  
        //crntBlockPos 객체 생성
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
        crntBlockPos.init();
        if(canHold == false) canHold = true;
        setCrntBlockArray();
        setCrntBlockShape();
        setGhostArray();
        setGhostPos();
        uploadToMainTable();

        setNextBlocksTable();
    }
    // ***** 강제로 블럭을 바꿀 때 *****
    private void changeCrntBlock(BlockShape blockShape) {
        nextBlocksArray[numOfUsedBlocks % BAG] = blockShape;
    }
    
    // ***** 매 움직임에 따른 업데이트 ******
    public void updateWithEveryMove() {
        setGhostArray();
        setGhostPos();
        uploadToMainTable();
    }
    // ***** 데이터 반영 ***
    public void uploadToMainTable() {
        uploadGhostBlockData();
        uploadCrntBlockData();
    }

    public void hardDrop() {
        while(crntBlockMovable(Direction.DOWN)) {
            move(Direction.DOWN);
        }
        putDownBlock();
    }
    public void move(Direction direction) {
        if(direction == Direction.LEFT) {
            crntBlockPos.c--;
        } 
        else if(direction == Direction.RIGHT) {
            crntBlockPos.c++;
        }
        else if(direction == Direction.DOWN) {
            crntBlockPos.r++;
        }
        else { // direction == Direction.UP
            crntBlockPos.r--;
        }
    }
    public boolean crntBlockMovable(Direction direction) {
        if(direction == Direction.LEFT) {
            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 4; c++) {
                    if(crntBlockArray[r][c] == crntBlockShape) { // 4x4 행렬 속에서 블럭이 존재할 때
                        Position testPos = new Position(r + crntBlockPos.r, c + crntBlockPos.c - 1);
                        BlockShape testResult = getMainTableBlockType(testPos);
                        if(testResult == BlockShape.SOLID) return false;
                    }
                }
            }
        }
        else if(direction == Direction.RIGHT) {
            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 4; c++) {
                    if(crntBlockArray[r][c] == crntBlockShape) { // 4x4 행렬 속에서 블럭이 존재할 때
                        Position testPos = new Position(r + crntBlockPos.r, c + crntBlockPos.c + 1);
                        BlockShape testResult = getMainTableBlockType(testPos);
                        if(testResult == BlockShape.SOLID) return false;
                    }
                }
            }
        }
        else if(direction == Direction.DOWN) {
            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 4; c++) {
                    if(crntBlockArray[r][c] == crntBlockShape) { // 4x4 행렬 속에서 블럭이 존재할 때
                        Position testPos = new Position(r + crntBlockPos.r + 1, c + crntBlockPos.c);
                        BlockShape testResult = getMainTableBlockType(testPos);
                        if(testResult == BlockShape.SOLID) return false;
                    }
                }
            }
        }
        else if(direction == Direction.UP) {
            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 4; c++) {
                    if(crntBlockArray[r][c] == crntBlockShape) { // 4x4 행렬 속에서 블럭이 존재할 때
                        Position testPos = new Position(r + crntBlockPos.r - 1, c + crntBlockPos.c);
                        BlockShape testResult = getMainTableBlockType(testPos);
                        if(testResult == BlockShape.SOLID) return false;
                    }
                }
            }
        }
        else { // direction == Direction.NONE
            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 4; c++) {
                    if(crntBlockArray[r][c] == crntBlockShape) { // 4x4 행렬 속에서 블럭이 존재할 때
                        Position testPos = new Position(r + crntBlockPos.r, c + crntBlockPos.c);
                        BlockShape testResult = getMainTableBlockType(testPos);
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
            setCrntBlockArray();
            if(!crntBlockMovable(Direction.NONE)) wallKick(Direction.CLOCKWISE);
        }
        else {
            rotation.turnCounterClockwise();
            setCrntBlockArray();
            if(!crntBlockMovable(Direction.NONE)) wallKick(Direction.COUNTER_CLOCKWISE);
        }
        setCrntBlockArray();
        uploadCrntBlockData();
    }
    public void hold() {
        if(canHold == false) return;

        if(holdBlockShape == BlockShape.NONE) {
            holdBlockShape = crntBlockShape;
            setHoldBlockTable();
            
            numOfUsedBlocks++;
            if(numOfUsedBlocks % BAG == 0) setNextArray();
            
            setNewBlock();
            canHold = false;
        }
        else {
            BlockShape temp = holdBlockShape;
            holdBlockShape = crntBlockShape;
            crntBlockShape = temp;

            setHoldBlockTable();
            changeCrntBlock(crntBlockShape);
            setNewBlock();
            canHold = false;
        }
    }

    // GhostBlock 관련
    private void uploadGhostBlockData() {
        intitGhostBlock();
        for(int r = 0; r < 4; r++) {
            for(int c = 0; c < 4; c++) {
                if(ghostBlockArray[r][c] != BlockShape.NONE) {
                    mainTable[ghostBlockPos.r + r][ghostBlockPos.c + c].mino = ghostBlockArray[r][c];
                    mainTable[ghostBlockPos.r + r][ghostBlockPos.c + c].isVisible = true;
                }
            }
        }
    }
    private void intitGhostBlock() {
        for(int r = 0; r < GameBoard.MAIN_BOARD.INT_HEIGHT; r++) {
            for(int c = 0; c < GameBoard.MAIN_BOARD.INT_WIDTH; c++) {

                if(getMainTableBlockType(new Position(r, c)) == BlockShape.GHOST) {
                    //init
                    mainTable[r][c].mino = BlockShape.NONE;
                    mainTable[r][c].isVisible = false;
                }
            }
        }
    }
    private void setGhostPos() {
        ghostBlockPos.r = crntBlockPos.r;
        ghostBlockPos.c = crntBlockPos.c;
    
        while(ghostBlockMovable()) {
            ghostBlockPos.r++;
        }
    }
    private boolean ghostBlockMovable() {
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                if(getBlockType(ghostBlockArray[r][c]) == BlockShape.GHOST) { // 4x4 행렬 속에서 블럭이 존재할 때
                    Position testPos = new Position(r + ghostBlockPos.r + 1, c + ghostBlockPos.c);
                    BlockShape testResult = getMainTableBlockType(testPos);
                    if(testResult == BlockShape.SOLID) return false;
                }
            }
        }
        return true;
    }
    private void setGhostArray() {
        int[][] temp = new int[4][4];
        temp = BlockData.fetch(crntBlockShape);
        for(int r = 0; r < 4; r++) {
            for(int c = 0; c < 4; c++) {
                if(intToGhostBlockShape(temp[r][c]) != BlockShape.NONE) {
                    ghostBlockArray[r][c] = intToGhostBlockShape(temp[r][c]);
                } else {
                    ghostBlockArray[r][c] = BlockShape.NONE;
                }
            } 
        }
    }
    
    private void uploadCrntBlockData() {
        initLiquidBlock();
        for(int r = 0; r < 4; r++) {
            for(int c = 0; c < 4; c++) {
                if(crntBlockArray[r][c] != BlockShape.NONE) {
                    mainTable[crntBlockPos.r + r][crntBlockPos.c + c].mino = crntBlockArray[r][c];
                    mainTable[crntBlockPos.r + r][crntBlockPos.c + c].isVisible = true;
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
                    nextBlocksArray[i] = intToLiquidBlockShape((int)(Math.random() * 10) % 7 + 1);
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
                    nextBlocksArray[i] = intToLiquidBlockShape((int)(Math.random() * 10) % 7 + 1);
                    for(int j = BAG; j < i; j++) {
                        if(nextBlocksArray[j] == nextBlocksArray[i]) again = true;
                    }
                } while(again);
            }
        }
    }
    private void setCrntBlockShape() {
        crntBlockShape = nextBlocksArray[numOfUsedBlocks % BAG];
    }
    public void setCrntBlockArray() {
        int[][] temp = new int[4][4];
        temp = BlockData.fetch(nextBlocksArray[numOfUsedBlocks % BAG]);
        for(int r = 0; r < 4; r++) {
            for(int c = 0; c < 4; c++) {
                crntBlockArray[r][c] = intToLiquidBlockShape(temp[r][c]);
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
                BlockShape testResult = getMainTableBlockType(testPos);
                if(testResult == BlockShape.LIQUID) {
                    //init
                    mainTable[r][c].mino = BlockShape.NONE;
                    mainTable[r][c].isVisible = false;
                }
            }
        }
    }
    private BlockShape getMainTableBlockType(Position testPos) {
        // 테트리스 화면 밖 모든 블럭은 Solid 블록이다.

        // Array Index Out Of Bounds Exception
        if(testPos.c < 0 || testPos.c >= GameBoard.MAIN_BOARD.INT_WIDTH || testPos.r >= GameBoard.MAIN_BOARD.INT_HEIGHT || testPos.r < 0) 
            return BlockShape.SOLID; 
        
        BlockShape mino = mainTable[testPos.r][testPos.c].mino;
        return getBlockType(mino);
    }
    private BlockShape getBlockType(BlockShape mino) {
        if (mino == BlockShape.NONE) return BlockShape.NONE;
        else if(mino == BlockShape.SLD_I || mino == BlockShape.SLD_J || mino == BlockShape.SLD_L || mino == BlockShape.SLD_O || mino == BlockShape.SLD_S || mino == BlockShape.SLD_J || mino == BlockShape.SLD_T || mino == BlockShape.SLD_Z)
            return BlockShape.SOLID;
        else if(mino == BlockShape.I || mino == BlockShape.J || mino == BlockShape.L || mino == BlockShape.O || mino == BlockShape.S || mino == BlockShape.J || mino == BlockShape.T || mino == BlockShape.Z)
            return BlockShape.LIQUID;
        else if(mino == BlockShape.GHST_I || mino == BlockShape.GHST_J || mino == BlockShape.GHST_L || mino == BlockShape.GHST_O || mino == BlockShape.GHST_S || mino == BlockShape.GHST_J || mino == BlockShape.GHST_T || mino == BlockShape.GHST_Z)
            return BlockShape.GHOST;
        else
            return BlockShape.NONE;
    }
    
    private BlockShape intToLiquidBlockShape(int num) {
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
    private BlockShape intToGhostBlockShape(int num) {
        switch(num) {
            case 1:
                return BlockShape.GHST_I;
            case 2:
                return BlockShape.GHST_T;
            case 3:
                return BlockShape.GHST_O;
            case 4:
                return BlockShape.GHST_S;
            case 5:
                return BlockShape.GHST_Z;
            case 6:
                return BlockShape.GHST_L;
            case 7:
                return BlockShape.GHST_J;
            default:
                return BlockShape.NONE;
        }
    }
    private void wallKick(Direction direction) {
        // reference: https://tetris.fandom.com/wiki/SRS
        Position crntPos = new Position(crntBlockPos.r, crntBlockPos.c);

        if(direction == Direction.CLOCKWISE) {
            // *** J, L, T, S, Z Tetromino Wall Kick Data ***
            if(crntBlockShape == BlockShape.J || crntBlockShape == BlockShape.L || crntBlockShape == BlockShape.T || crntBlockShape == BlockShape.S || crntBlockShape == BlockShape.Z) {
                // Test 1
                // (-1, 0) (1, 0) (1, 0) (-1, 0)
                if(rotation.getValue() == 1) move(Direction.LEFT);
                else if(rotation.getValue() == 2) move(Direction.RIGHT);
                else if(rotation.getValue() == 3) move(Direction.RIGHT);
                else move(Direction.LEFT);


                if(crntBlockMovable(Direction.NONE)) return;

                // Test 2
                // (-1, 1) (1, -1) (1, 1) (-1, -1)
                if(rotation.getValue() == 1) move(Direction.UP);
                else if(rotation.getValue() == 2) move(Direction.DOWN);
                else if(rotation.getValue() == 3) move(Direction.UP);
                else move(Direction.DOWN);

                if(crntBlockMovable(Direction.NONE)) return;
                else crntBlockPos.set(crntPos.r, crntPos.c);

                // Test 3
                // (0. -2) (0, 2) (0, -2) (0, 2)
                if(rotation.getValue() == 1) { move(Direction.DOWN); move(Direction.DOWN); }
                else if(rotation.getValue() == 2) { move(Direction.UP); move(Direction.UP); }
                else if(rotation.getValue() == 3) { move(Direction.DOWN); move(Direction.DOWN); }
                else { move(Direction.UP); move(Direction.UP); }
            
                if(crntBlockMovable(Direction.NONE)) return;

                // Test 4
                // (-1, -2) (1, 2) (1, -2) (-1, 2)
                if(rotation.getValue() == 1) move(Direction.LEFT);
                else if(rotation.getValue() == 2) move(Direction.RIGHT);
                else if(rotation.getValue() == 3) move(Direction.RIGHT);
                else move(Direction.LEFT);

                if(crntBlockMovable(Direction.NONE)) return;
                else { crntBlockPos.set(crntPos.r, crntPos.c); rotation.turnCounterClockwise(); } // 모든 테스트 실패 => 원위치
            }
            // *** I Tetromino Wall Kick Data ***
            else if(crntBlockShape == BlockShape.I) {
                // Test 1
                // (-2, 0) (-1, 0) (2, 0) (1, 0)
                if(rotation.getValue() == 1) { move(Direction.LEFT); move(Direction.LEFT); }
                else if(rotation.getValue() == 2) move(Direction.LEFT);
                else if(rotation.getValue() == 3) { move(Direction.RIGHT); move(Direction.RIGHT); }
                else move(Direction.RIGHT);

                if(crntBlockMovable(Direction.NONE)) return;
                else crntBlockPos.set(crntPos.r, crntPos.c);

                // Test 2
                // (1, 0) (2, 0) (-1, 0) (-2, 0)
                if(rotation.getValue() == 1) move(Direction.RIGHT);
                else if(rotation.getValue() == 2) { move(Direction.RIGHT); move(Direction.RIGHT); }
                else if(rotation.getValue() == 3) move(Direction.LEFT);
                else { move(Direction.LEFT); move(Direction.LEFT); }

                if(crntBlockMovable(Direction.NONE)) return;
                else crntBlockPos.set(crntPos.r, crntPos.c);

                // Test 3
                // (-2, -1) (-1, 2) (2, 1) (1, -2)
                if(rotation.getValue() == 1) { move(Direction.LEFT); move(Direction.LEFT); move(Direction.DOWN); }
                else if(rotation.getValue() == 2) { move(Direction.LEFT); move(Direction.UP); move(Direction.UP); }
                else if(rotation.getValue() == 3) { move(Direction.RIGHT); move(Direction.RIGHT); move(Direction.UP); }
                else { move(Direction.RIGHT); move(Direction.DOWN); move(Direction.DOWN); }
            
                if(crntBlockMovable(Direction.NONE)) return;

                // Test 4
                // (1, 2) (2, -1) (-1, -2) (-2, 1)
                if(rotation.getValue() == 1) { move(Direction.RIGHT); move(Direction.UP); move(Direction.UP); }
                else if(rotation.getValue() == 2) { move(Direction.RIGHT); move(Direction.RIGHT); move(Direction.DOWN); }
                else if(rotation.getValue() == 3) { move(Direction.LEFT); move(Direction.DOWN); move(Direction.DOWN); }
                else { move(Direction.LEFT); move(Direction.LEFT); move(Direction.UP); }

                if(crntBlockMovable(Direction.NONE)) return;
                else { // 모든 테스트 실패 => 원위치
                    crntBlockPos.set(crntPos.r, crntPos.c); 
                    rotation.turnCounterClockwise(); 
                }
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


                if(crntBlockMovable(Direction.NONE)) return;

                // Test 2
                // (1, -1) (-1, 1) (-1, -1) (1, 1)
                if(rotation.getValue() == 0) move(Direction.DOWN);
                else if(rotation.getValue() == 1) move(Direction.UP);
                else if(rotation.getValue() == 2) move(Direction.DOWN);
                else move(Direction.UP);

                if(crntBlockMovable(Direction.NONE)) return;
                else crntBlockPos.set(crntPos.r, crntPos.c);

                // Test 3
                // (0. 2) (0, -2) (0, 2) (0, -2)
                if(rotation.getValue() == 0) { move(Direction.UP); move(Direction.UP); }
                else if(rotation.getValue() == 1) { move(Direction.DOWN); move(Direction.DOWN); }
                else if(rotation.getValue() == 2) { move(Direction.UP); move(Direction.UP); }
                else { move(Direction.DOWN); move(Direction.DOWN); }
            
                if(crntBlockMovable(Direction.NONE)) return;

                // Test 4
                // (1, 2) (-1, -2) (-1, 2) (1, -2)
                if(rotation.getValue() == 0) move(Direction.RIGHT);
                else if(rotation.getValue() == 1) move(Direction.LEFT);
                else if(rotation.getValue() == 2) move(Direction.LEFT);
                else move(Direction.RIGHT);

                if(crntBlockMovable(Direction.NONE)) return;
                else { crntBlockPos.set(crntPos.r, crntPos.c); rotation.turnCounterClockwise(); } // 모든 테스트 실패 => 원위치
            }
            // *** I Tetromino Wall Kick Data ***
            else if(crntBlockShape == BlockShape.I) {
                // Test 1
                // (2, 0) (1, 0) (-2, 0) (-1, 0)
                if(rotation.getValue() == 1) { move(Direction.RIGHT); move(Direction.RIGHT); }
                else if(rotation.getValue() == 2) move(Direction.RIGHT);
                else if(rotation.getValue() == 3) { move(Direction.LEFT); move(Direction.LEFT); }
                else move(Direction.LEFT);

                if(crntBlockMovable(Direction.NONE)) return;
                else crntBlockPos.set(crntPos.r, crntPos.c);

                // Test 2
                // (-1, 0) (-2, 0) (1, 0) (2, 0)
                if(rotation.getValue() == 1) move(Direction.LEFT);
                else if(rotation.getValue() == 2) { move(Direction.LEFT); move(Direction.LEFT); }
                else if(rotation.getValue() == 3) move(Direction.RIGHT);
                else { move(Direction.RIGHT); move(Direction.RIGHT); }

                if(crntBlockMovable(Direction.NONE)) return;
                else crntBlockPos.set(crntPos.r, crntPos.c);

                // Test 3
                // (2, 1) (1, -2) (-2, -1) (-1, 2)
                if(rotation.getValue() == 1) { move(Direction.RIGHT); move(Direction.RIGHT); move(Direction.UP); }
                else if(rotation.getValue() == 2) { move(Direction.RIGHT); move(Direction.DOWN); move(Direction.DOWN); }
                else if(rotation.getValue() == 3) { move(Direction.LEFT); move(Direction.LEFT); move(Direction.DOWN); }
                else { move(Direction.LEFT); move(Direction.UP); move(Direction.UP); }
            
                if(crntBlockMovable(Direction.NONE)) return;

                // Test 4
                // (-1, -2) (-2, 1) (1, 2) (2, -1)
                if(rotation.getValue() == 1) { move(Direction.LEFT); move(Direction.DOWN); move(Direction.DOWN); }
                else if(rotation.getValue() == 2) { move(Direction.LEFT); move(Direction.LEFT); move(Direction.UP); }
                else if(rotation.getValue() == 3) { move(Direction.RIGHT); move(Direction.UP); move(Direction.UP); }
                else { move(Direction.RIGHT); move(Direction.RIGHT); move(Direction.DOWN); }

                if(crntBlockMovable(Direction.NONE)) return;
                else { // 모든 테스트 실패 => 원위치
                    crntBlockPos.set(crntPos.r, crntPos.c); 
                    rotation.turnCounterClockwise();
                }
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
        for(int i = 1; i <=GameBoard.VISION_OF_NEXT_BLOCKS; i++) {
            int[][] temp = new int[4][4];
            temp = BlockData.fetch(nextBlocksArray[numOfUsedBlocks % BAG + i]);
            for(int r = 3 * (i - 1), r2 = 0; r < 3 * i; r++, r2++) {
                for(int c = 0; c < 4; c++) {
                    nextBlocksTable[r][c].mino = intToLiquidBlockShape(temp[r2][c]);
                    if(nextBlocksTable[r][c].mino != BlockShape.NONE)
                        nextBlocksTable[r][c].isVisible = true;
                    else
                        nextBlocksTable[r][c].isVisible = false;
                }
            }
        }
    }
    
    private void setHoldBlockTable() {
        int[][] temp = new int[4][4];
        temp = BlockData.fetch(holdBlockShape);
        for(int r = 0; r < 3; r ++) {
            for(int c = 0; c < 4; c++) {
                holdBlockTable[r][c].mino = intToLiquidBlockShape(temp[r][c]);
                if(holdBlockTable[r][c].mino != BlockShape.NONE) 
                    holdBlockTable[r][c].isVisible = true;
                else
                    holdBlockTable[r][c].isVisible = false;
            }
        }
    }


}


