package com.xqf.xxx.widget;

import javax.swing.*;
import java.awt.*;

/**
 * Created by XQF on 2016/12/28.
 */
//public class MyProgressBar extends JProgressBar {
//    public MyProgressBar() {
//        setBounds(100, 100, 350, 150);
//        this.setIndeterminate(true);
//        this.setVisible(true);
//    }
//
//    public static void main(String[] args) {
//        JFrame frame = new JFrame("正在初始化。。。。。");
//
//        frame.add(new MyProgressBar());
//        frame.setVisible(true);
//        frame.setSize(600, 80);
//    }
//
//}
public class MyProgressBar extends JFrame {
    public MyProgressBar() {
        this.setBounds(450, 100, 400, 130);
        this.setTitle("小熊提示：");
        this.setVisible(true);
        this.setSize(600, 80);
        Container container = this.getContentPane();
        JLabel label = new JLabel("正在初始化，请等候......");
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        container.add(label, BorderLayout.NORTH);
        container.add(progressBar);
    }

    public static void main(String[] args) {
        new MyProgressBar();
    }
}