package com.crawler;


import java.sql.ResultSet;
import java.util.ArrayList;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redis.util.JedisUtil;
import com.tool.DBUtil;


public class CrawlSysScheduler implements Runnable{
	private final static Logger logger = LoggerFactory.getLogger(CrawlSysScheduler.class);
	public final static byte[] redisKey_wemedia = "quene_tender".getBytes();//队列名称
	public final static byte[] redisKey_wemedia2 = "quene_tender_db".getBytes();//队列名称
	JedisUtil ju=new JedisUtil();
	DBUtil dbu=new DBUtil();
	public static void main(String[] args)
	{
		CrawlSysScheduler cs=new CrawlSysScheduler();
		cs.core();
	}
	public void run() {
		core();
	}
	/**
	 * 初始化
	 */
	public void core()
	{
		for(int i=0;i<1;i++)
		{
			byte[] task=ju.rpop(redisKey_wemedia);
			if(task!=null)
			{
				ju.lpush(redisKey_wemedia, task);
			}else
			{
				ArrayList<JSONObject> list=initTask();
				for(int j=0;j<list.size();j++)
				{
					JSONObject json=list.get(j);
					ju.lpush(redisKey_wemedia, json.toString().getBytes());//写入递归队列
				}
			}
			try {
				Thread.sleep(50000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	public ArrayList<JSONObject> initTask()
	{
		ArrayList<JSONObject> list=new ArrayList<JSONObject>();
		String sql="select area,department,part,url from taskinfo";
		try {
			ResultSet rs=dbu.Execute(sql);
			while(rs.next())
			{
				JSONObject json=new JSONObject();
				String area=rs.getString("area");
				String department=rs.getString("department");
				String part=rs.getString("part");
				String url=rs.getString("url");
				json.put("area", area);
				json.put("department", department);
				json.put("part", part);
				json.put("url", url);
				list.add(json);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}
