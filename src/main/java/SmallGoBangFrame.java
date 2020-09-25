import java.util.ArrayList;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.util.HashMap;

// 绘制下棋界面
class SmallGoBangframe extends JPanel implements SmallGoBangConfig {
    //定义一支画笔
    public Graphics g;
    // 存储各个坐标点棋子的情况 1 黑棋，2 白棋
    public int[][] local = new int[column][row];
    //保存每一步棋的位置
    public ArrayList<ChessLocal> ChessLocalList = new ArrayList<ChessLocal> ();
    // 当前谁下的标志,0 未开始 ，1黑棋下，2白棋下
    int flag = 0;
    //下棋模式， 0表示人人对战，1表示人机对战，默认人人对战
    int BattleType = 0;

    //设置不同落子情况和相应权值的数组
    public static HashMap<String,Integer> map = new HashMap<String,Integer>();
    static {
        //被堵住的情况 0 空位， 1 黑棋 ，2 白棋 机器执白棋
        //在一个方向上被其他棋子堵住
        map.put("01", 17);
        map.put("02", 12);
        map.put("001", 17);
        map.put("002", 12);
        map.put("0001", 17);
        map.put("0002", 12);

        map.put("0102",17);
        map.put("0201",12);
        map.put("0012",15);
        map.put("0021",10);
        map.put("01002",19);
        map.put("02001",14);
        map.put("00102",17);
        map.put("00201",12);
        map.put("00012",15);
        map.put("00021",10);

        map.put("01000",21);
        map.put("02000",16);
        map.put("00100",19);
        map.put("00200",14);
        map.put("00010",17);
        map.put("00020",12);
        map.put("00001",15);
        map.put("00002",10);

        //被堵住
        map.put("0101",65);
        map.put("0202",60);
        map.put("0110",65);
        map.put("0220",60);
        map.put("011",65);
        map.put("022",60);
        map.put("0011",65);
        map.put("0022",60);

        map.put("01012",65);
        map.put("02021",60);
        map.put("01102",65);
        map.put("02201",60);
        map.put("00112",65);
        map.put("00221",60);

        map.put("01010",75);
        map.put("02020",70);
        map.put("01100",75);
        map.put("02200",70);
        map.put("00110",75);
        map.put("00220",70);
        map.put("00011",75);
        map.put("00022",70);

        //被堵住
        map.put("0111",150);
        map.put("0222",140);

        map.put("01112",150);
        map.put("02221",140);

        map.put("01101",1000);
        map.put("02202",800);
        map.put("01011",1000);
        map.put("02022",800);
        map.put("01110", 1000);
        map.put("02220", 800);

        map.put("01111",3000);
        map.put("02222",3500);
    }
    public int[][] weightArray=new int[column][row];//定义一个二维数组，保存各个点的权值

    //主函数入口
    public static void main(String[] args){
        //初始化界面对象
        SmallGoBangframe sgf = new SmallGoBangframe();
        //初始化棋盘界面
        sgf.initGUI();
    }

    private void initGUI(){
        //初始化一个界面,并设置标题大小
        JFrame jf=new JFrame();
        //设置标题
        jf.setTitle("小小五子棋");
        //设置窗口大小
        jf.setSize(800,650);
        //设置窗口相对于指定组件的位置,null 窗口将置于屏幕的中央
        jf.setLocationRelativeTo(null);
        //设置关闭窗口时要执行的操作
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //设置顶级容器JFrame为框架布局
        jf.setLayout(new BorderLayout());
        //设置右边部分的大小
        Dimension dim1=new Dimension(150,0);
        //设置右边按钮组件大小
        Dimension dim2=new Dimension(140,40);
        //设置左边棋盘部分大小
        Dimension dim3=new Dimension(550,0);
        //实现左边的界面，把SmallGoBangframe的对象添加到框架布局的中间部分
        //设置下棋界面的大小
        this.setPreferredSize(dim3);
        //设置下棋界面的颜色为灰色
        this.setBackground(Color.LIGHT_GRAY);
        //添加到框架布局的中间部分
        jf.add(this,BorderLayout.CENTER);
        //实现右边的JPanel容器界面
        JPanel jp=new JPanel();
        //设置JPanel的大小
        jp.setPreferredSize(dim1);
        //设置右边的界面颜色为白色
        jp.setBackground(Color.white);
        //添加到框架布局的东边部分
        jf.add(jp,BorderLayout.EAST);
        //设置JPanel为流式布局
        jp.setLayout(new FlowLayout());
        //把按钮等组件依次加到JPanel上面
        //设置按钮名称
        String[] butname = {"开始","悔棋","认输"};
        JButton[] button=new JButton[3];
        //依次把三个按钮组件加上去
        for(int i=0;i<butname.length;i++) {
            button[i]=new JButton(butname[i]);
            button[i].setPreferredSize(dim2);
            jp.add(button[i]);
        }
        //设置下拉框选项按钮
        String[] boxname= {"人人对战","人机对战"};
        JComboBox box=new JComboBox(boxname);
        jp.add(box);
        //设置按钮事件监听，获取一个按钮监听对象
        ButtonListener butListen=new ButtonListener(this,box);
        //将按钮事件监听对象挂载到按钮上
        for(int i=0;i<butname.length;i++) {
            //添加发生操作的监听处理对象
            button[i].addActionListener(butListen);
        }
        //对下拉框添加事件监听机制
        box.addActionListener(butListen);
        // 对下棋界面进行监听，处理下棋操作
        frameListener fl=new frameListener();
        //首先得获取画笔对象
        fl.setGraphics(this);
        //添加棋盘区域鼠标操作监听
        this.addMouseListener(fl);
        jf.setVisible(true);
    }

    //游戏结果弹窗
    public void PopUp(String result) {
        JOptionPane jo=new JOptionPane();
        JOptionPane.showMessageDialog(null, result, "游戏结果", JOptionPane.PLAIN_MESSAGE);
    }

    //重写重绘方法,重绘左边棋盘区域
    public void paint(Graphics g) {
        super.paint(g);
        //重绘出棋盘
        g.setColor(Color.black);
        for(int i=0;i<row;i++) {
            g.drawLine(x, y+size*i, x+size*(column-1), y+size*i);
        }
        for(int j=0;j<column;j++) {
            g.drawLine(x+size*j, y, x+size*j, y+size*(row-1));
        }
        //重绘出棋子
        for(int i=0;i<row;i++) {
            for(int j=0;j<column;j++) {
                if(local[i][j]==1) {
                    int countx=size*j+20;
                    int county=size*i+20;
                    g.setColor(Color.black);
                    g.fillOval(countx-size/2, county-size/2, size, size);
                }
                else if(local[i][j]==2) {
                    int countx=size*j+20;
                    int county=size*i+20;
                    g.setColor(Color.white);
                    g.fillOval(countx-size/2, county-size/2, size, size);
                }
            }
        }
    }

}
