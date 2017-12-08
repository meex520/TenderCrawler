package com.crawler;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class core {
	public static void main(String[] args)
	{
		int slaveNum =3;
		int slaveNumDB = 0;
		ArrayList<Thread> allThreadsList = new ArrayList<Thread>();
		ArrayList<CrawlerTask> allSimpleCrawlerList = new ArrayList<CrawlerTask>();
		for (int i = 0; i < slaveNum; i++) {
			CrawlerTask tmpSimpleCrawler = new CrawlerTask();
			allSimpleCrawlerList.add(tmpSimpleCrawler);
			allThreadsList.add(new Thread(tmpSimpleCrawler));
		}
		for (int i = 0; i < slaveNum; i++) {
			allThreadsList.get(i).start();
		}
		
		ArrayList<Thread> allThreadsListDB = new ArrayList<Thread>();
		ArrayList<DBtask> allSimpleDBList = new ArrayList<DBtask>();
		for (int i = 0; i < slaveNumDB; i++) {
			DBtask tmpSimpleCrawler = new DBtask();
			allSimpleDBList.add(tmpSimpleCrawler);
			allThreadsListDB.add(new Thread(tmpSimpleCrawler));
		}
		for (int i = 0; i < slaveNumDB; i++) {
			allThreadsListDB.get(i).start();
		}
		
		while (true) {
			try {
				TimeUnit.SECONDS.sleep(10);
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (int i = 0; i < slaveNum; i++) {

				if (!allThreadsList.get(i).isAlive()) {
 
					allThreadsList.remove(i);
					allSimpleCrawlerList.remove(i);
					CrawlerTask tmpSimpleCrawler = new CrawlerTask();
					allSimpleCrawlerList.add(i, tmpSimpleCrawler);
					Thread tmpThread = new Thread(tmpSimpleCrawler);
					tmpThread.start();
					allThreadsList.add(i, tmpThread);// 若没有启动线程 则无限循环
				}
			}
			for (int i = 0; i < slaveNumDB; i++) {

				if (!allThreadsListDB.get(i).isAlive()) {
 
					allThreadsListDB.remove(i);
					allSimpleDBList.remove(i);
					DBtask tmpSimpleCrawler = new DBtask();
					allSimpleDBList.add(i, tmpSimpleCrawler);
					Thread tmpThread = new Thread(tmpSimpleCrawler);
					tmpThread.start();
					allThreadsListDB.add(i, tmpThread);// 若没有启动线程 则无限循环
				}
			}
		}
	}
}
