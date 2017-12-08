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
//		System.setProperty("phantomjs.binary.path",PropertyUtil.getvalue("phantomjs"));// ָ������·��
//		WebDriver dr = new PhantomJSDriver();
//		dr.get(url); // ����ҳ
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
//			logger.error("phantomjs���������������⣺"+url);
//		}
//		return html;
//	}
//	public static void main(String[] args)
//	{
//		String url="http://edu.hainan.gov.cn/news/11";
//		System.out.println(Phantomjs.getAjaxPage(url));
//	}
//}
