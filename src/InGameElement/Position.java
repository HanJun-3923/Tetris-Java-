package InGameElement;

public class Position { // 좌표를 표현하기 위한 클래스(행렬 기준 좌표)
    public static final int INITIAL_POS_C = 3; // 초기 열 위치
    public static final int INITIAL_POS_R = 0; // 초기 행 위치
    
    public int r;
    public int c;
    
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
