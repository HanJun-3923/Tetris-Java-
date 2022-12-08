public class Rotation {
    private int rotation;
    
    public void turnClockwise() {
        rotation += 1;
        while(rotation >= 4) rotation -= 4;
    }
    public void turnCounterClockwise() {
        rotation -= 1;
        while(rotation < 0) rotation += 4;
    }
    public void init() {
        rotation = 0;
    }
    public int getValue() {
        return rotation;
    }
    public Rotation() {
        rotation = 0;
    }
};
