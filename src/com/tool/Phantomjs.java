//package com.tool;
//
//import java.util.concurrent.TimeUnit;
//
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.phantomjs.PhantomJSDriver;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.util.PropertyUtil;
//
//public class Phantomjs {
//	private final static Logger logger = LoggerFactory.getLogger(Phantomjs.class);
//	public static String getAjaxPage(String url)
//	{
//		System.setProperty("phantomjs.binary.path",PropertyUtil.getvalue("phantomjs"));// 指定驱动路径
//		WebDriver dr = new PhantomJSDriver();
//		dr.get(url); // 打开首页
//		dr.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		String html=dr.getPageSource();
//		dr.close();
//		if(html==null||html.length()<1)
//		{
//			logger.error("phantomjs浏览器请求出现问题："+url);
//		}
//		return html;
//	}
//	public static void main(String[] args)
//	{
//		String url="http://edu.hainan.gov.cn/news/11";
//		System.out.println(Phantomjs.getAjaxPage(url));
//	}
//}
