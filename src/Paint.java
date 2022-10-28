import javax.swing.*;

import java.awt.*;


public class Paint extends JPanel {

    @Override
    public void paint(Graphics g) {
        super.paintComponents(g);
        paintBlock(g); // paint Blocks according to Table's Data.
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
        g.drawRect(GameBoard.X, GameBoard.Y, GameBoard.WIDTH, GameBoard.HEIGHT); 
        
        // Grid
        for(int i = 1; i < 10; i++) {
            g.drawLine(GameBoard.X + i * (GameBoard.WIDTH / 10), GameBoard.Y, GameBoard.X + i * (GameBoard.WIDTH / 10), GameBoard.Y + GameBoard.HEIGHT);
        }
        for(int i = 1; i < 20; i++) {
            g.drawLine(GameBoard.X, GameBoard.Y + i * (GameBoard.HEIGHT / 20), GameBoard.X + GameBoard.WIDTH, GameBoard.Y + i * (GameBoard.HEIGHT / 20));
        }
    }

    // paint Blocks according to Table's Data
    private void paintBlock(Graphics g) {
        for (int r = 0; r < GameBoard.TABLE_HEIGHT; r++) {
            for (int c = 0; c < GameBoard.TABLE_WIDTH; c++) {
                if(InGame.table[r][c].isVisible) { // 블럭이 존재한다면
                    setColorAccordingToMino(g, InGame.table[r][c].mino); // Set Graphcis Color according to Table's mino
                    g.fillRect(GameBoard.X + GameBoard.BLOCK_SIZE * c, GameBoard.Y + GameBoard.BLOCK_SIZE * r, GameBoard.BLOCK_SIZE, GameBoard.BLOCK_SIZE);
                    continue;
                }
            }
        }
    }

    // Set Graphcis Color according to Table's mino
    private void setColorAccordingToMino(Graphics g, BlockShape mino) {
        // Color Profile from Jstris

        if(mino == BlockShape.I) {
            g.setColor(new Color(71, 153, 210)); // RGB Color SkyBlue
        } else if (mino == BlockShape.T) { 
            g.setColor(new Color(161, 53, 134)); // RGB Color Purple
        } else if (mino == BlockShape.O) { 
            g.setColor(new Color(217, 162, 55)); // RGB Color Yellow
        } else if (mino == BlockShape.S) { 
            g.setColor(new Color(111, 175, 52)); // RGB Color Green
        } else if (mino == BlockShape.Z) { 
            g.setColor(new Color(144, 34, 44)); // RGB Color Red
        } else if (mino == BlockShape.L) { 
            g.setColor(new Color(211, 100, 40)); // RGB Color Orange
        } else if (mino == BlockShape.J) { 
            g.setColor(new Color(29, 45, 133)); // RGB Color Blue
        } else {
            g.setColor(Color.black);
        }

    }
}
