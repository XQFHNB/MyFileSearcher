package com.xqf.xxx.frame;

import com.xqf.xxx.db.DBHelper;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by XQF on 2016/12/27.
 */
public class GetFilesOfPc {
    private int counter = 0;
    private DBHelper helper;

    public GetFilesOfPc() {

        FileSystemView fsv = FileSystemView.getFileSystemView();
        File[] fs = File.listRoots();
        helper = DBHelper.getDbHelper();
        for (int i = 0; i < fs.length - 1; i++) {
            String dispalyName = fsv.getSystemDisplayName(fs[i]);

            //盘符
            String destinateName = getRoots(dispalyName);

            System.out.println(i + " 测试：" + destinateName);

            File file = new File(destinateName);// 指定文件目录
            File[] files = file.listFiles();
            for (int j = 0; j < files.length; j++) {

                method(file);
            }
        }

        System.out.println("总文件数：" + counter);
    }

    public void method(File file) {
        File[] fs = file.listFiles();// 得到File数组

        if (fs != null) {// 判断fs是否为null
            for (File f : fs) {
                if (f.isFile()) {// 如果是文件直接输出
//                    System.out.println(f.getName() + "     ");

                    String filePath = f.getAbsolutePath();
                    String fileName = f.getName();
                    String filePostfix = getExtensionName(fileName);
                    // TODO: 2016/12/27 将信息插入数据库
                    helper.insertIntoDB(filePath, fileName, filePostfix);
                    //       System.out.println(filePath + "    " + fileName + "    " + filePostfix);
                    counter++;

                } else {
                    method(f);// 否则递归调用
                }
            }
        }
    }


    //获取盘符
    private String getRoots(String str) {

        //这个正则表达式是获取括号中的数据的，
        Pattern pattern = Pattern.compile("(?<=\\()(.+?)(?=\\))");
        Matcher mat = pattern.matcher(str);
        String result = null;
        if (mat.find()) {
            result = mat.group(1) + "\\";
            System.out.println(result);
        }
        return result;
    }

    //获取文件的后缀名
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot);
            }
        }
        return "." + filename;
    }

    public static void main(String[] args) {
        new GetFilesOfPc();
    }
}
