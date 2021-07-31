package com.brady;

import com.sun.rowset.internal.Row;
import javafx.beans.property.ReadOnlyBooleanWrapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class SaoLei implements ActionListener
{
    JFrame frame = new JFrame();
    ImageIcon bannerIcon = new ImageIcon("banner2.png");
    ImageIcon guessIcon = new ImageIcon("guess.png");
    ImageIcon bombIcon = new ImageIcon("bomb.png");
    ImageIcon failIcon = new ImageIcon("fail.png");
    ImageIcon winIcon = new ImageIcon("win.png");
    ImageIcon winFlagIcon = new ImageIcon("win_flag.png");
    JButton bannerBtn = new JButton(bannerIcon);
    int second = 0;
    Timer timer = new Timer(1000,this);


    // 数据结构
    int ROW = 20;
    int COL = 20;
    int[][] data = new int[ROW][COL];
    JButton[][] btns = new JButton[ROW][COL];
    int LEICOUT = 3; // 雷的总数
    int LEICODE = -1;
    int unopend = ROW*COL;
    int opend = 0;

    JLabel lable1 = new JLabel("待开: "+ unopend);
    JLabel lable2 = new JLabel("已开: "+ 0);
    JLabel lable3 = new JLabel("用时: "+ second + "s");


    public SaoLei()
    {
        frame.setSize(600,700);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLayout(new BorderLayout());
        setHeader();
        addLei();
        setBtns();
        timer.start();

        frame.setVisible(true);

    }

    private void addLei() {
        Random random = new Random();
        for (int i = 0; i < LEICOUT; ) {
            int r = random.nextInt(ROW);
            int c = random.nextInt(COL);
            if(data[r][c] != LEICODE) {
                data[r][c] = LEICODE;
                i++;
            }
        }

        //计算周边雷的数量
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if(data[i][j] == LEICODE){
                    continue;
                }
                int tempCnt = 0;
                // 越界
                if(i>0  && j>0  && data[i-1][j-1]==LEICODE) tempCnt++;
                if(i>0  && 		   data[i-1][j  ]==LEICODE) tempCnt++;
                if(i>0  && j<COL-1 && data[i-1][j+1]==LEICODE) tempCnt++;
                if( 	   j>0  && data[i  ][j-1]==LEICODE) tempCnt++;
                if(		   j<COL-1 && data[i  ][j+1]==LEICODE) tempCnt++;
                if(i<ROW-1 && j>0  && data[i+1][j-1]==LEICODE) tempCnt++;
                if(i<ROW-1  		&& data[i+1][j  ]==LEICODE) tempCnt++;
                if(i<ROW-1 && j<COL-1 && data[i+1][j+1]==LEICODE) tempCnt++;
                data[i][j] = tempCnt;

            }
        }
    }

    public  void setBtns()
    {
        Container con = new Container();
        con.setLayout(new GridLayout(ROW,COL));
        frame.add(con,BorderLayout.CENTER);
        for (int i=0;i<ROW;i++){
            for (int j = 0; j < COL; j++) {
                //JButton button = new JButton(data[i][j]+"");
                JButton button = new JButton(guessIcon);
                button.setOpaque(true);
                button.setBackground(new Color(213, 195, 10));
                button.addActionListener(this);
                con.add(button);
                btns[i][j] = button;
            }
        }
    }
    private void setHeader(){
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c1 = new GridBagConstraints(0,0,3,1,1.0,1.0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
        panel.add(bannerBtn,c1);

        bannerBtn.addActionListener(this);

        lable1.setOpaque(true);
        lable1.setBackground(Color.white);
        lable1.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        lable2.setOpaque(true);
        lable2.setBackground(Color.white);
        lable2.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        lable3.setOpaque(true);
        lable3.setBackground(Color.white);
        lable3.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));


        GridBagConstraints c2 = new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
        panel.add(lable1,c2);

        GridBagConstraints c3 = new GridBagConstraints(1,1,1,1,1.0,1.0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
        panel.add(lable2,c3);

        GridBagConstraints c4 = new GridBagConstraints(2,1,1,1,1.0,1.0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
        panel.add(lable3,c4);



        frame.add(panel,BorderLayout.NORTH);
    }



    public static void main(String[] args)
    {
        new SaoLei();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Timer){
            second++;
            lable3.setText("用时: "+second+"s");
            timer.start();
            return ;
        }
        JButton btn = (JButton)e.getSource();
        if(btn.equals(bannerBtn)){
            restartGame();
            return ;
        }

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if(btn.equals(btns[i][j])){
                    if(data[i][j] == LEICODE){
                        lose();
                    } else{
                        openCell(i,j);
                        checkWin();
                    }

                    return ;
                }
            }
        }
    }

    private void restartGame() {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                data[i][j] = 0;
                JButton button = btns[i][j];

                button.setEnabled(true);
                button.setText("");
                button.setBackground(new Color(213, 195, 10));
                button.setIcon(guessIcon);
            }
        }
        unopend = ROW*COL;
        opend = 0;
        second = 0;

        lable1.setText("待开: "+ unopend);
        lable2.setText("已开: "+ 0);
        lable3.setText("用时: "+ second + "s");

        addLei();
        timer.start();
    }

    private void checkWin() {
        int count = 0;
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if(btns[i][j].isEnabled()) count++;
            }
        }

        if(count == LEICOUT) {
            timer.stop();
            for (int i = 0; i < ROW; i++) {
                for (int j = 0; j < COL; j++) {
                    if(btns[i][j].isEnabled()){
                        btns[i][j].setIcon(winFlagIcon);
                    }
                }
            }

            JOptionPane.showMessageDialog(frame,"你赢了! \n 点击 banner重新开始","赢了",JOptionPane.PLAIN_MESSAGE);

        }
    }

    private void lose() {
        timer.stop();
        bannerBtn.setIcon(failIcon);
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if(btns[i][j].isEnabled()){
                    JButton btn = btns[i][j];
                    if(data[i][j] == LEICODE) {
                        btn.setEnabled(false);
                        btn.setIcon(bombIcon);
                        btn.setDisabledIcon(bombIcon);

                    } else {
                        btn.setIcon(null);
                        btn.setEnabled(false);
                        btn.setOpaque(true);
                        btn.setText(data[i][j]+"");
                    }
                }
            }
        }

        JOptionPane.showMessageDialog(frame,"踩到雷了!\n 点击 banner重新开始","暴雷了",JOptionPane.PLAIN_MESSAGE);

    }

    // 打开格子
    private void openCell(int i, int j){
        JButton btn = btns[i][j];
        if(!btn.isEnabled()) return ;
        btn.setIcon(null);
        btn.setEnabled(false);
        addOpenCount();
        btn.setOpaque(true);
        btn.setBackground(Color.GREEN);
        btn.setText(data[i][j]+"");
        if(data[i][j] == 0) {
            if(i>0  && j>0  && data[i-1][j-1]==0) openCell(i-1,j-1);
            if(i>0  && 		   data[i-1][j  ]==0) openCell(i-1,j);
            if(i>0  && j<COL-1 && data[i-1][j+1]==0) openCell(i-1,j+1);
            if( 	   j>0  && data[i  ][j-1]==0) openCell(i,j-1);
            if(		   j<COL-1 && data[i  ][j+1]==0) openCell(i,j+1);
            if(i<ROW-1 && j>0  && data[i+1][j-1]==0) openCell(i+1,j-1);
            if(i<ROW-1  		&& data[i+1][j  ]==0)  openCell(i+1,j);
            if(i<ROW-1 && j<COL-1 && data[i+1][j+1]==0) openCell(i+1,j+1);
        }

    }

    private void addOpenCount() {
        unopend--;
        opend++;
        lable1.setText("待开: "+ unopend);
        lable2.setText("已开: "+ opend);
    }
}
