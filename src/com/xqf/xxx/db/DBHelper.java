package com.xqf.xxx.db;


import com.xqf.xxx.frame.GetFilesOfPc;

import java.sql.*;
import java.util.Vector;

/**
 * Created by XQF on 2016/12/27.
 */
public class DBHelper {

    Connection connection;
    String driverString = "com.mysql.jdbc.Driver";
    String urlString = "jdbc:mysql://localhost:3306/javaDb?useSSL=false";
    String userName = "root";
    String password = "125880";

    private DBHelper() {
        try {
            Class.forName(driverString);
            connection = DriverManager.getConnection(urlString, userName, password);

            // TODO: 2016/12/28 建表语句是否需要

            System.out.println("数据库连接成功");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("驱动加载失败");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("数据库获取连接失败！");
        }
    }


    public static DBHelper getDbHelper() {
        return new DBHelper();
    }


    //获取连接，这是这个类最有用的方法
    public Connection getConnection() {
        return connection;
    }


    //关闭连接，单例模式严格使用，不严格的话获取到connection就可以关闭了，这个方法没用
    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("数据库关闭失败！");
        }
    }

    public void insertIntoDB(String filePath, String fileName, String filePostfix) {
        String sqlString = "insert into files values(?,?,?)";
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(sqlString);
            pstmt.setString(1, filePath);
            pstmt.setString(2, fileName);
            pstmt.setString(3, filePostfix);
            pstmt.execute();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public Vector selectFromDB(String string) {
        String sqlString = null;


        if (string.indexOf(':') < 0 && string.indexOf('*') < 0 && string.indexOf('\\') < 0) {

            string = "%" + string + "%";
            String newString = '\'' + string + '\'';
            sqlString = "select filePath from files where filePath like " + newString;
        } else if (string.indexOf(':') >= 0 && string.indexOf('\\') >= 0) {
            string = string.replaceAll("\\\\", "\\\\\\\\");
            String newString = '\'' + string + '\'';
            sqlString = "select filePath from files where filepath=" + newString;
        } else {
            String[] strs = string.split("\\*");
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < strs.length; i++) {
                sb.append(strs[i].trim());
            }
            string = sb.toString();
            string = "%" + string + "%";
            String newString = '\'' + string + '\'';
            sqlString = "select filePath from files where filePath like " + newString;
        }

        System.out.println("String:" + string);
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        Vector allVector = new Vector<>();
        try {
            pstmt = connection.prepareStatement(sqlString);
            resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                Vector vector = new Vector();
                System.out.println(resultSet.getString(1));
                vector.add(resultSet.getString(1));
                allVector.add(vector);
            }
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return allVector;
    }


    public void updateDB() {
        String sqlString = "delete from files";
        new GetFilesOfPc();
    }


    public static void main(String[] args) {
        Connection connection = DBHelper.getDbHelper().getConnection();
    }
}
