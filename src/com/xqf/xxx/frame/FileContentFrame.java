package com.xqf.xxx.frame;

import javax.swing.*;
import java.io.*;

/**
 * Created by XQF on 2016/12/27.
 */
public class FileContentFrame extends JFrame {

    private JTextArea editArea;


    public FileContentFrame(String urlString) {
        this.setVisible(true);
        this.setBounds(300, 50, 600, 650);
//        this.setSize(600, 450);


        editArea = new JTextArea();
        this.add(editArea);

        File file = new File(urlString);
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

        JScrollPane scrollPane = new JScrollPane();
//        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setViewportView(editArea);

        this.add(scrollPane);


    }

    public static void main(String[] args) {
        new FileContentFrame("C:\\Program Files\\IIS Express\\REDIST.TXT");
    }

}
