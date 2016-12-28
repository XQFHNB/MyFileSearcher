package com.xqf.xxx.frame;

import com.xqf.xxx.congfig.Config;
import com.xqf.xxx.db.DBHelper;
import com.xqf.xxx.widget.MyProgressBar;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Timer;
import java.util.Vector;

/**
 * Created by XQF on 2016/12/27.
 */
public class FileEditor extends JFrame implements ActionListener {

    //定义一个私有的文件的绝对路径文本域对象
    private JTextField selectField;
    private JTable table;

    //定义一个私有的编辑区对象
    private JTextArea editArea;

    //定义一个私有的“保存”按钮对象
    private JButton saveBtn;

    //定义一个私有的“浏览”按钮对象
    private JButton openFileBtn;
    private JMenuItem menuItem1;
    private JMenuItem menuItem2;
    private JMenuItem menuItem3;


    private String openUrl;

    //定义一个私有的记录目录层次数，其初始值为0
    private int level = 0;


    private Timer timer;

    public FileEditor(Vector vector, String textString, boolean openFile) {

        boolean isFirstOpen = isFirstOpen();
        if (isFirstOpen) {
            //定时后台线程更新部分，。，单位是毫秒，我也没有计算究竟是多少时间
            timer = new Timer();
            timer.schedule(new MyTimerTask(), 2000000000, 200000000);

            //新开线程往数据库里插入数据部分
            Thread t = new Thread(new MyTaskOfGetFiles());
            t.start();
            MyProgressBar progressBar = new MyProgressBar();
            if (!t.isAlive()) {
                progressBar.setVisible(false);
                JOptionPane.showMessageDialog(null, "初始化成功", "提示", JOptionPane.CLOSED_OPTION);
            }
        }

        //设置标题为 Editor
        this.setTitle("Editor");

        //设置组件的大小
        this.setBounds(300, 50, 600, 650);

        /*
         * 面板的北边，即路径输入域、浏览按钮
         */

        //创建一个选择框对象
        selectField = new JTextField(40);
        selectField.setText(textString);

        //创建一个按钮对象
        openFileBtn = new JButton("Browse");

        openFileBtn.addActionListener(this);
        //为刚创建的按钮添加监听事件

        //新建一个流布局，并且左对齐的面板
        JPanel upPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        //设置画板的背景颜色
        upPanel.setBackground(Color.CYAN);

        //将选择框加入画板中
        upPanel.add(selectField);

        //将按钮加入画板中
        upPanel.add(openFileBtn);

        //将面板放在北边区域
        this.add(upPanel, BorderLayout.NORTH);

        /*
         * 创建文本编辑区，并加入到整个布局的中间区域
         */
//        editArea = new JTextArea();

        JPopupMenu popupMenu = new JPopupMenu();
        menuItem1 = new JMenuItem("打开");
        menuItem2 = new JMenuItem("复制");
        menuItem3 = new JMenuItem("粘贴");

        menuItem1.addActionListener(this);
        menuItem2.addActionListener(this);
        menuItem3.addActionListener(this);
        popupMenu.add(menuItem1);
        popupMenu.add(menuItem2);
        popupMenu.add(menuItem3);

        ScrollPane scollPanel = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
        if (!openFile) {
            Vector headVector = new Vector();
            headVector.add("结果");
            table = new JTable(vector, headVector);
            scollPanel.add(table);

            //原来中间的组件需要绑定
            table.setComponentPopupMenu(popupMenu);


            table.setCellSelectionEnabled(true);//设置此表是否允许同时存在行选择和列选择。
            //返回用来维持行选择状态的 ListSelectionModel。
            //此接口表示任何组件的当前选择状态，该组件显示一个具有稳定索引的值列表。
            ListSelectionModel cellSelectionModel = table.getSelectionModel();
            //单选,只能选择一个单元格
            cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            cellSelectionModel.addListSelectionListener(new ListSelectionListener() {

                public void valueChanged(ListSelectionEvent e) {//单元格值变动事件
                    String selectedData = null;
                    int[] selectedRow = table.getSelectedRows();//被选择的行
                    int[] selectedColumns = table.getSelectedColumns();//被选择的列

                    for (int i = 0; i < selectedRow.length; i++) {//循环取出
                        for (int j = 0; j < selectedColumns.length; j++) {
                            selectedData = (String) table.getValueAt(selectedRow[i], selectedColumns[j]);
                        }
                    }
                    System.out.println("选择的：" + selectedData);
                    openUrl = selectedData;
                }
            });
        } else {
            editArea = new JTextArea();
            scollPanel.add(editArea);
            File file = new File(textString);
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String str = null;
                while ((str = br.readLine()) != null) {
                    editArea.append(str + "\r\n");
                }
                br.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        this.add(scollPanel, BorderLayout.CENTER);


        /*
         * 创建保存按钮，并为按钮添加监听事件
         */
        saveBtn = new JButton("Save");
        saveBtn.addActionListener(this);


        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.green);
        southPanel.add(saveBtn);
        this.add(southPanel, BorderLayout.SOUTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);


        Container container = getContentPane();
        container.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });


    }


    //帮助文件看应用是不是初始化过
    private boolean isFirstOpen() {
        boolean result = false;
        try {
            RandomAccessFile rFile = new RandomAccessFile(Config.ISOPENFILEHELPER, "rw");
            if (rFile.length() == 0) {
                rFile.writeUTF("1");
                result = true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object event = e.getSource();
        Vector contentVector = null;
        if (event == openFileBtn) {
            FileEditor.this.level = 0;
            String oldstring = selectField.getText().trim();
            StringBuffer sb = new StringBuffer();
//            String path = oldstring.replaceAll("\\\\", "\\\\\\\\");
            String path = oldstring;
            System.out.println("最后的：" + path);
            contentVector = DBHelper.getDbHelper().selectFromDB(path);

            //  table = new JTable(contentVector, headVector);
            this.setVisible(false);
            new FileEditor(contentVector, oldstring, false);
        }
        //打开
        if (event == menuItem1) {
            this.setVisible(false);
            new FileEditor(null, openUrl, true);
        }
        //复制
        if (event == menuItem2) {
            copyFileContentToClipboard();
            JOptionPane.showMessageDialog(null, "已经成功复制至剪贴板", "小熊提示", JOptionPane.CLOSED_OPTION);
        }
        if (event == menuItem3) {
            // TODO: 2016/12/28 清空剪贴板 
            emptyClipboard();
            JOptionPane.showMessageDialog(null, "剪贴板删除成功", "小熊提示", JOptionPane.CLOSED_OPTION);

        }
        if (event == saveBtn) {
            saveFile();

        }
    }

    private void emptyClipboard() {
        try {
            FileWriter fw = new FileWriter(new File(Config.CLIPBOARD));
            fw.write("");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void copyFileContentToClipboard() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(openUrl)));
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(Config.CLIPBOARD)));
            StringBuffer sb = new StringBuffer();
            String string;
            while ((string = br.readLine()) != null) {
                sb.append(string);
            }
            pw.println(sb.toString());
            br.close();
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * 保存文件
     */
    private void saveFile() {
        FileDialog fd = new FileDialog(this, "Save File");

        //设置需要保存文件的后缀
        fd.setFile("untitled.txt");

        //设置为“保存”模式
        fd.setMode(FileDialog.SAVE);
        fd.setVisible(true);
        //获取文件名
        String fileName = fd.getFile();

        //获取对话框的当前目录
        String dir = fd.getDirectory();

        //根据目录名、文件名创建一个文件，即要保存的目标文件
        File newFile = new File(dir + File.separator + fileName);
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(
                    newFile)));

            String str = editArea.getText();
            pw.println(str);
            pw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            pw.close();
        }
    }


    public static void main(String[] args) {
        new FileEditor(null, "", false);
    }
}
