package com.crawler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import net.sf.json.JSONObject;

import com.redis.util.JedisUtil;
import com.tool.DBUtil;


public class DBtask implements Runnable{
	public final static byte[] redisKey_wemedia2 = "quene_tender_db".getBytes();//¶ÓÁÐÃû³Æ
	JedisUtil ju=new JedisUtil();
	DBUtil dbu=new DBUtil();
	@Override
	public void run() {
		for(int i=0;;i++)
		{
			byte[] bytes = ju.rpop(redisKey_wemedia2);
			if(bytes!=null)
			{
				String msg = new String(bytes);
				System.out.println(msg);
				JSONObject json=JSONObject.fromObject(msg);
				insertDB(json);
			}else
			{
				System.out.println("quene_36kr_db null");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public void insertDB(JSONObject wb)
	{
		String source=wb.getString("source");
		String channel=wb.getString("channel");
		String we_media_summary=wb.getString("we_media_summary");
		String we_media_name=wb.getString("we_media_name");
		String we_media_image=wb.getString("we_media_image");
		String we_media_url=wb.getString("we_media_url");
		String click_num=wb.getString("click_num");
		String fans_num=wb.getString("fans_num");
		String art_num=wb.getString("art_num");
		String last_upload_time=wb.getString("last_upload_time");
		String sql="INSERT INTO wemedia_list (source, channel, we_media_summary, we_media_name, we_media_image, we_media_url, click_num, fans_num, art_num, last_upload_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		Connection cnn=this.dbu.Conn;
		try {
			PreparedStatement preStmt = cnn.prepareStatement(sql);
			int i=1;
			preStmt.setString(i++, source);
			preStmt.setString(i++,channel);
			preStmt.setString(i++, we_media_summary);
			preStmt.setString(i++, we_media_name);
			preStmt.setString(i++, we_media_image);
			preStmt.setString(i++, we_media_url);
			preStmt.setString(i++, click_num);
			preStmt.setString(i++, fans_num);
			preStmt.setString(i++, art_num);
			preStmt.setString(i++, last_upload_time);
			dbu.Conn.prepareStatement("set names utf8mb4").executeQuery();
			preStmt.executeUpdate();
		} catch (SQLException e) {
			try {
				Thread.sleep(100000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.exit(-1);
			e.printStackTrace();
		}
			
	}
}
