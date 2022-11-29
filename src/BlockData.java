// 
public class BlockData {
    private static int[][][] ITetromino = { // 1
        {   
            {0, 0, 0, 0},
            {1, 1, 1, 1},
            {0, 0, 0, 0},
            {0, 0, 0, 0}    
        }, 
        {   
            {0, 0, 1, 0},
            {0, 0, 1, 0},
            {0, 0, 1, 0},
            {0, 0, 1, 0}
        }, 
        {
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {1, 1, 1, 1},
            {0, 0, 0, 0}
        }, 
        {
            {0, 1, 0, 0},
            {0, 1, 0, 0},
            {0, 1, 0, 0},
            {0, 1, 0, 0}
        }
    };
    private static int[][][] TTetromino = { // 2
        {
            {0, 2, 0, 0},
            {2, 2, 2, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
        }, 
        {
            {0, 2, 0, 0},
            {0, 2, 2, 0},
            {0, 2, 0, 0},
            {0, 0, 0, 0}
        }, 
        {
            {0, 0, 0, 0},
            {2, 2, 2, 0},
            {0, 2, 0, 0},
            {0, 0, 0, 0}
        }, 
        {
            {0, 2, 0, 0},
            {2, 2, 0, 0},
            {0, 2, 0, 0},
            {0, 0, 0, 0}
        }
    };
    private static int[][][] OTetromino = { // 3
        {
            {0, 3, 3, 0},
            {0, 3, 3, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
        }, 
        {
            {0, 3, 3, 0},
            {0, 3, 3, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
        }, 
        {
            {0, 3, 3, 0},
            {0, 3, 3, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
        }, 
        {
            {0, 3, 3, 0},
            {0, 3, 3, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
        }
    };
    private static int[][][] STetromino = { // 4
        {
            {0, 4, 4, 0},
            {4, 4, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
        }, 
        {
            {0, 4, 0, 0},
            {0, 4, 4, 0},
            {0, 0, 4, 0},
            {0, 0, 0, 0}
        }, 
        {
            {0, 0, 0, 0},
            {0, 4, 4, 0},
            {4, 4, 0, 0},
            {0, 0, 0, 0}
        }, 
        {
            {4, 0, 0, 0},
            {4, 4, 0, 0},
            {0, 4, 0, 0},
            {0, 0, 0, 0}
        }
    };
    public static int[][][] ZTetromino = { // BlockShape Z = int 5
        {
            {5, 5, 0, 0},
            {0, 5, 5, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
        }, 
        {
            {0, 0, 5, 0},
            {0, 5, 5, 0},
            {0, 5, 0, 0},
            {0, 0, 0, 0}
        }, 
        {
            {0, 0, 0, 0},
            {5, 5, 0, 0},
            {0, 5, 5, 0},
            {0, 0, 0, 0}
        }, 
        {
            {0, 5, 0, 0},
            {5, 5, 0, 0},
            {5, 0, 0, 0},
            {0, 0, 0, 0}
        }
    };
    private static int[][][] LTetromino = {
        {
            {0, 0, 6, 0},
            {6, 6, 6, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
        }, 
        {
            {0, 6, 0, 0},
            {0, 6, 0, 0},
            {0, 6, 6, 0},
            {0, 0, 0, 0}
        }, 
        {
            {0, 0, 0, 0},
            {6, 6, 6, 0},
            {6, 0, 0, 0},
            {0, 0, 0, 0}
        }, 
        {
            {6, 6, 0, 0},
            {0, 6, 0, 0},
            {0, 6, 0, 0},
            {0, 0, 0, 0}
        }
    };
    private static int[][][] JTetromino = {
        {
            {7, 0, 0, 0},
            {7, 7, 7, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
        }, 
        {
            {0, 7, 7, 0},
            {0, 7, 0, 0},
            {0, 7, 0, 0},
            {0, 0, 0, 0}
        }, 
        {
            {0, 0, 0, 0},
            {7, 7, 7, 0},
            {0, 0, 7, 0},
            {0, 0, 0, 0}
        }, 
        {
            {0, 7, 0, 0},
            {0, 7, 0, 0},
            {7, 7, 0, 0},
            {0, 0, 0, 0}
        }
    };
    public static int[][] fetch(BlockShape blockShape) {
        if(blockShape == BlockShape.I) {
            return ITetromino[GameBoard.player1.rotation.getValue()];
        } else if(blockShape == BlockShape.T) {
            return TTetromino[GameBoard.player1.rotation.getValue()];
        } else if(blockShape == BlockShape.O) {
            return OTetromino[GameBoard.player1.rotation.getValue()];
        } else if(blockShape == BlockShape.S) {
            return STetromino[GameBoard.player1.rotation.getValue()];
        } else if(blockShape == BlockShape.Z) {
            return ZTetromino[GameBoard.player1.rotation.getValue()];
        } else if(blockShape == BlockShape.L) {
            return LTetromino[GameBoard.player1.rotation.getValue()];
        } else {
            return JTetromino[GameBoard.player1.rotation.getValue()];
        }
    }
}
