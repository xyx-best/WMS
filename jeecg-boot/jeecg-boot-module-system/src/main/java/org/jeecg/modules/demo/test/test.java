package org.jeecg.modules.demo.test;

import java.sql.*;

public class test {
    public static void main(String[] args) {
        Connection conn = null;
        ResultSet rs = null;
        Statement statement = null;
        try {
            Class.forName("org.sqlite.JDBC");//驱动
//            conn = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Administrator\\AppData\\Roaming\\Microsoft\\Windows\\Network Shortcuts\\c (CHINA-EA36)\\test3");
            conn = DriverManager.getConnection("jdbc:sqlite:E:\\jeecgboot\\jeecg-boot-master\\jeecg-boot-master\\jeecg-boot\\jeecg-boot-module-system\\src\\main\\resources\\db\\test3");
            statement = conn.createStatement();
//            rs = statement.executeQuery("SELECT * FROM test");
            statement.execute("INSERT INTO BETrace VALUES('1111WQWEQ','323423','32423445','4SFDSF')");
            rs = statement.executeQuery("SELECT * FROM BETrace");
            while (rs.next()){
                System.out.println("--------------------");
                System.out.print("id:"+rs.getString("id"));
//                System.out.print("id:"+rs.getString("id"));
//                System.out.print("    name:"+rs.getString("name"));
//                System.out.println("    age:"+rs.getString("age"));

            }

        }catch (ClassNotFoundException ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();

        }
        catch (SQLException sqlex){
            System.out.println(sqlex.getMessage());
            sqlex.printStackTrace();
        }
        finally {
            try {
                rs.close();
                statement.close();
                conn.close();
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
}