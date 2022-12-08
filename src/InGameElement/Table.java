package InGameElement;

// Main Game Board
public class Table {
    public boolean isVisible;
    public BlockShape mino;
    
    public Table(boolean isVisible, BlockShape mino) {
        this.isVisible = isVisible;
        this.mino = mino;
    }
}  
