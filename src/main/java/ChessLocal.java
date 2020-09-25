// 保存每一步棋的位置 javabean
public class ChessLocal {
    private int locali,localj;

    //构造函数
    public ChessLocal(){};
    public ChessLocal(int locali, int localj) {
        this.locali = locali;
        this.localj = localj;
    }

    public int getLocali() {
        return locali;
    }

    public void setLocali(int locali) {
        this.locali = locali;
    }

    public int getLocalj() {
        return localj;
    }

    public void setLocalj(int localj) {
        this.localj = localj;
    }
}
