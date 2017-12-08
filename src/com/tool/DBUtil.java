package com.tool;




import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBUtil {
    public static void main(String[] args)
    {
        DBUtil dbu=new DBUtil();
        String sql="select user_access_key from weibo_account where platform_id='1900' and related_user_id>99";
        try {
            ResultSet rs=dbu.Execute(sql);
            while(rs.next())
            {
                String ss=rs.getString("user_access_key");
                System.out.println(ss);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    String Connstr = "";
    public Connection Conn = null;
    Statement stmt = null;
    //初始化数据库连接???
    public DBUtil(){
        try{
            init();
        }catch(Exception e){
            System.out.print("DBUtil:" + e.getMessage());
        }
    }
    public void init() throws Exception {
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String url=com.util.PropertyUtil.getvalue("dburl");
            //Connstr = "jdbc:mysql@57553b7280b19.gz.cdb.myqcloud.com:3625";
            Conn = DriverManager.getConnection(url);
            Conn.setAutoCommit(true);
            stmt = Conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
        }catch(Exception e){
            System.out.print("DBUtil Init:" + e.getMessage());
        }
    }
    //查询返回结果???
    public ResultSet Execute(String sql) throws Exception{
        ResultSet rsC = null;
        try{
            rsC = stmt.executeQuery(sql);
        }catch(Exception e){
            System.out.println("DBUtil->Execute:" + e.getMessage());
        }
        return rsC;
    }
    //执行SQL语句返回影响行数
    public int Execute(String sql,int iR) throws Exception{
        iR = stmt.executeUpdate(sql);
        return iR;
    }
    //执行插入
    public int Insert(String sql) throws Exception{
        int intTemp = 0 ;
        intTemp = stmt.executeUpdate(sql);
        return intTemp;
    }
    //执行更新
    public int Update(String sql) throws Exception{
        int intTemp = 0 ;
        intTemp = stmt.executeUpdate(sql);
        return intTemp;
    }
    //?????
    public void destroy() {
        try{
            stmt.close();
            Conn.close();
        }catch(Exception e){
            System.out.print("DBUtil destroy:" + e.getMessage());
        }
    }

}

