import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;

// 对右边按钮组的监听操作处理
public class ButtonListener implements SmallGoBangConfig, ActionListener {
    private SmallGoBangframe sgf;
    private JComboBox box;

    public ButtonListener(SmallGoBangframe sgf,JComboBox box) {
        // 获取左边部分
        this.sgf = sgf;
        //获取选择框对象
        this.box = box;
    }
    //当界面发生操作时进行处理
    public void actionPerformed(ActionEvent e) {
        //获取当前被点击按钮的内容，判断是不是"开始"这个按钮
        if(e.getActionCommand().equals("开始")) {
            //重绘棋盘
            for(int i=0;i< sgf.local.length;i++)
                for(int j=0;j< sgf.local[i].length;j++)
                    sgf.local[i][j]=0;
            sgf.repaint();
            //如果是开始新游戏的按钮，再为左半部分设置监听方法
            sgf.flag=1;
        }
        //判断当前点击的按钮是不是悔棋
        else if(e.getActionCommand().equals("悔棋")) {
            if(sgf.ChessLocalList.size()>1) {
                //把棋子数组相应的位置置为0；
                ChessLocal l=new ChessLocal();
                //获取最后一个棋子的对象信息
                l=sgf.ChessLocalList.remove(sgf.ChessLocalList.size()-1);
                //把相应的数组位置置为0
                sgf.local[l.getLocali()][l.getLocalj()]=0;
                //把玩家还原为上一步的玩家
                if(sgf.flag==1) sgf.flag++;
                else sgf.flag--;

                //直接调用sgf的重绘方法，重绘方法的画笔应该是在棋盘页面还没生成的时候就要获取
                //调用repaint会自动调用paint方法，而且不用给参数
                sgf.repaint();
            }
            else {
                System.out.println("不能悔棋!");
            }
        }
        //认输
        else if(e.getActionCommand().equals("认输")) {
            if(sgf.flag==1) System.out.println("白方赢");
            else System.out.println("黑方赢");
            //重新把棋盘设置为不可操作
            sgf.flag=0;
        }
        // 人机对战模式
        else if(box.getSelectedItem().equals("人机对战")) {
            sgf.BattleType=1;
            sgf.flag=0;
        }
        //人人对战模式
        else if(box.getSelectedItem().equals("人人对战")){
            sgf.BattleType=0;
            sgf.flag=0;
        }
    }
}
