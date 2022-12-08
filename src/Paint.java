import javax.swing.*;

import java.awt.*;


public class Paint extends JPanel {
    Color backgroundColor = new Color(238, 238, 238);

    @Override
    public void paint(Graphics g) {
        super.paintComponents(g);
        paintBlock(g, GameBoard.player1); // paint Blocks according to Table's Data.
        paintNextBlocks(g, GameBoard.player1);
        paintHoldBlocks(g, GameBoard.player1);
        
        paintGrid(g); // paint Border line and Grid of Game Board.
    }
    
    @Override
    public void update(Graphics g) {
        paint(g);
    }

    // paint Board and its Grid 
    private void paintGrid(Graphics g) { 
        g.setColor(Color.BLACK);

        // border line
        g.drawRect(GameBoard.MAIN_BOARD.COORD_X, GameBoard.MAIN_BOARD.COORD_Y, GameBoard.MAIN_BOARD.PIXEL_WIDTH, GameBoard.MAIN_BOARD.PIXEL_HEIGHT); 
        
        // Grid
        for(int i = 1; i < 10; i++) {
            g.drawLine(GameBoard.MAIN_BOARD.COORD_X + i * (GameBoard.MAIN_BOARD.PIXEL_WIDTH / 10), GameBoard.MAIN_BOARD.COORD_Y, GameBoard.MAIN_BOARD.COORD_X + i * (GameBoard.MAIN_BOARD.PIXEL_WIDTH / 10), GameBoard.MAIN_BOARD.COORD_Y + GameBoard.MAIN_BOARD.PIXEL_HEIGHT);
        }
        for(int i = 1; i < 20; i++) {
            g.drawLine(GameBoard.MAIN_BOARD.COORD_X, GameBoard.MAIN_BOARD.COORD_Y + i * (GameBoard.MAIN_BOARD.PIXEL_HEIGHT / 20), GameBoard.MAIN_BOARD.COORD_X + GameBoard.MAIN_BOARD.PIXEL_WIDTH, GameBoard.MAIN_BOARD.COORD_Y + i * (GameBoard.MAIN_BOARD.PIXEL_HEIGHT / 20));
        }
    }

    // paint Blocks according to Table's Data
    private void paintBlock(Graphics g, InGame player) {
        for (int r = 0; r < GameBoard.MAIN_BOARD.INT_HEIGHT; r++) {
            for (int c = 0; c < GameBoard.MAIN_BOARD.INT_WIDTH; c++) {
                if(player.mainTable[r][c].isVisible) { // 블럭이 존재한다면
                    setColorAccordingToMino(g, player.mainTable[r][c].mino); // Set Graphcis Color according to Table's mino
                } else { // 블럭이 존재하지 않는다면
                    g.setColor(Color.WHITE);
                }
                g.fillRect(GameBoard.MAIN_BOARD.COORD_X + GameBoard.BLOCK_SIZE * c, GameBoard.MAIN_BOARD.COORD_Y + GameBoard.BLOCK_SIZE * r, GameBoard.BLOCK_SIZE, GameBoard.BLOCK_SIZE);
            }
        }
    }

    // Set Graphcis Color according to Table's mino
    private void setColorAccordingToMino(Graphics g, BlockShape mino) {
        // Color Profile from Jstris

        //LIQUID or SOLID
        if(mino == BlockShape.I || mino == BlockShape.SLD_I) {
            g.setColor(new Color(71, 153, 210)); // RGB Color SkyBlue
        } else if (mino == BlockShape.T || mino == BlockShape.SLD_T) { 
            g.setColor(new Color(161, 53, 134)); // RGB Color Purple
        } else if (mino == BlockShape.O || mino == BlockShape.SLD_O) { 
            g.setColor(new Color(217, 162, 55)); // RGB Color Yellow
        } else if (mino == BlockShape.S || mino == BlockShape.SLD_S) { 
            g.setColor(new Color(111, 175, 52)); // RGB Color Green
        } else if (mino == BlockShape.Z || mino == BlockShape.SLD_Z) { 
            g.setColor(new Color(144, 34, 44)); // RGB Color Red
        } else if (mino == BlockShape.L || mino == BlockShape.SLD_L) { 
            g.setColor(new Color(211, 100, 40)); // RGB Color Orange
        } else if (mino == BlockShape.J || mino == BlockShape.SLD_J) {
            g.setColor(new Color(29, 45, 133)); // RGB Color Blue
        } 
        // GHOST
        else if(mino == BlockShape.GHST_I) {
            g.setColor(new Color(183, 183, 183)); // RGB Color SkyBlue
        } else if (mino == BlockShape.GHST_T) { 
            g.setColor(new Color(183, 183,183)); // RGB Color Purple
        } else if (mino == BlockShape.GHST_O) { 
            g.setColor(new Color(183, 183,183)); // RGB Color Yellow
        } else if (mino == BlockShape.GHST_S) { 
            g.setColor(new Color(183, 183,183)); // RGB Color Green
        } else if (mino == BlockShape.GHST_Z) { 
            g.setColor(new Color(183, 183,183)); // RGB Color Red
        } else if (mino == BlockShape.GHST_L) { 
            g.setColor(new Color(183, 183,183)); // RGB Color Orange
        } else if (mino == BlockShape.GHST_J) {
            g.setColor(new Color(183, 183,183)); // RGB Color Blue
        } 
    }

    // paint NextBlocks
    private void paintNextBlocks(Graphics g, InGame player) {
        //setNextBlockTable
        for(int r = 0; r < GameBoard.NEXT_BLOCKS_BOARD_HEIGHT_PER_VISION * GameBoard.VISION_OF_NEXT_BLOCKS; r++) {
            for(int c = 0; c < GameBoard.NEXT_BLOCKS_BOARD.INT_WIDTH; c++) {
                if(player.nextBlocksTable[r][c].isVisible) {
                    setColorAccordingToMino(g, player.nextBlocksTable[r][c].mino);
                } else { // 블럭이 존재하지 않는다면
                    g.setColor(backgroundColor);
                }
                g.fillRect(GameBoard.NEXT_BLOCKS_BOARD.COORD_X + GameBoard.BLOCK_SIZE * c, GameBoard.NEXT_BLOCKS_BOARD.COORD_Y + GameBoard.BLOCK_SIZE * r, GameBoard.BLOCK_SIZE, GameBoard.BLOCK_SIZE);
                
            }
        }
    }

    private void paintHoldBlocks(Graphics g, InGame player) {
        for(int r = 0; r < 3; r++) {
            for (int c = 0; c < 4; c++) {
                if(player.holdBlockTable[r][c].isVisible) {
                    setColorAccordingToMino(g, player.holdBlockTable[r][c].mino);
                } else {
                    g.setColor(backgroundColor);
                }
                g.fillRect(GameBoard.HOLD_BLOCK_BOARD.COORD_X + GameBoard.BLOCK_SIZE * c, GameBoard.HOLD_BLOCK_BOARD.COORD_Y + GameBoard.BLOCK_SIZE * r, GameBoard.BLOCK_SIZE, GameBoard.BLOCK_SIZE);
            }
        }
    }
}
