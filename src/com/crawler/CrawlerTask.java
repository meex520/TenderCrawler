package com.crawler;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bean.ArtInfoBean;
import com.bean.FileBean;
import com.redis.util.JedisUtil;
import com.tool.DBUtil;
import com.tool.Md5Util;
import com.tool.PageDown;
import com.tool.Phantomjs;
import com.util.HttpDownload;
import com.util.PropertyUtil;


public class CrawlerTask implements Runnable{
	public final static byte[] redisKey_wemedia = "quene_tender".getBytes();
	public final static byte[] redisKey_wemedia2 = "quene_tender_db".getBytes();
	JedisUtil ju=new JedisUtil();
	private final static Logger logger = LoggerFactory.getLogger(CrawlerTask.class);
	PageDown pd=new PageDown();
	long sptime=1000*60*1;
	DBUtil dbu=new DBUtil();
	public static void main(String[] args)
	{
		CrawlerTask ct=new CrawlerTask();
		ArrayList<String> list=ct.getNewUrl("http://www.fjwater.gov.cn/jhtml/cn/836");//获取新产生的链接
		for(int j=0;j<list.size();j++)
		{
			String aurl=list.get(j);
			System.out.println(aurl);
		}
	}
	@Override
	public void run() {
		for(int i=0;;i++)
		{
			byte[] bytes = ju.rpop(redisKey_wemedia);
			if(bytes!=null)
			{
				String msg = new String(bytes);
				JSONObject json=JSONObject.fromObject(msg);
				if(json.has("aurl")) //此分支是针对内容页
				{
					String aurl=json.getString("aurl");
					if(aurl.startsWith("ftp")||aurl.endsWith("doc")||aurl.endsWith("docx")||aurl.endsWith("pdf")||aurl.endsWith("xsl")||aurl.endsWith("xslx"))
					{
						String filename=downloadFile(aurl);
					}else
					{
						ArtGetInfo(json);
					}
				}else  //此分支是针对列表页
				{
					String url=json.getString("url");
					ArrayList<String> list=getNewUrl(url);//获取新产生的链接
					if(list.size()<15)
					{
						ArrayList<String> listajax=getNewUrlByPhantomjs(url);
						list=mergeArrayList(list, listajax);
					}
					/*String sql="INSERT INTO badlist (url, num) VALUES ('"+url+"', "+list.size()+")";
					try {
						dbu.Insert(sql);
					} catch (Exception e) {
						e.printStackTrace();
					}*/
					for(int j=0;j<list.size();j++)
					{
						String aurl=list.get(j);
						json.put("aurl", aurl);
						ju.rpush(redisKey_wemedia, json.toString().getBytes());//反向存储
					}
				}
			}else
			{
				System.out.println("task null");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public ArrayList<String> mergeArrayList(ArrayList<String> list1,ArrayList<String> list2)
	{
		HashMap<String, String> hm=new HashMap<String, String>();
		for(int i=0;i<list1.size();i++)
		{
			String url=list1.get(i);
			hm.put(url, "");
		}
		for(int i=0;i<list2.size();i++)
		{
			String url=list2.get(i);
			if(!hm.containsKey(url))
			{
				list1.add(url);
			}
		}
		hm.clear();
		return list1;
	}
	/**
	 * 对新链接进行请求与解析哦
	 * @param url
	 */
	public ArtInfoBean ArtGetInfo(JSONObject json)
	{
		ArtInfoBean ab=new ArtInfoBean();
		ArrayList<FileBean> filelist=new ArrayList<FileBean>();
		String area=json.getString("area");
		ab.setArea(area);
		String department=json.getString("department");
		ab.setDepartment(department);
		String part=json.getString("part");
		ab.setPart(part);
		String url=json.getString("url");
		ab.setUrl(url);
		String aurl=json.getString("aurl");
		ab.setArturl(aurl);
		try {
			String[] strs=pd.getHtml(aurl);
			if(strs[0].length()>0)
			{
				Document doc=Jsoup.parse(strs[0],aurl);
				Elements es=doc.getElementsByTag("a");
				for(int i=0;i<es.size();i++)  //下载内容页的文件
				{
					Element e=es.get(i);
					String fileurl=e.absUrl("href");
					String filename=downloadFile(fileurl);
					if(!"".equals(filename))
					{
						FileBean fb=new FileBean();
						fb.setFileurl(fileurl);
						fb.setDownname(filename);
						fb.setFilename(e.text());
						filelist.add(fb);
					}
				}
				ab.setFilelist(filelist);
				//对正文的代码中的表格进行
			}
		} catch (Exception e) {
			logger.error(aurl+"\n内容页网络请求数据失败！\n"+e.toString());
		}
		return ab;
	}
	public String downloadFile(String fileurl)
	{
		FileBean fb=new FileBean();
		if(fileurl.startsWith("ftp")||fileurl.endsWith("doc")||fileurl.endsWith("docx")||fileurl.endsWith("pdf")||fileurl.endsWith("xsl")||fileurl.endsWith("xslx"))
		{
			System.out.println(fileurl);
			String filepath = PropertyUtil.getvalue("docpath");
			String tag="notag";
			if(fileurl.endsWith("doc"))
			{
				tag="doc";
			}
			if(fileurl.endsWith("docx"))
			{
				tag="docx";
			}
			if(fileurl.endsWith("pdf"))
			{
				tag="pdf";
			}
			if(fileurl.endsWith("xsl"))
			{
				tag="xsl";
			}
			if(fileurl.endsWith("xslx"))
			{
				tag="xslx";
			}
			String name=Md5Util.getMd5(fileurl);
			
			HttpDownload.download(fileurl, filepath+name+"."+tag);
			return name+"."+tag;
		}
		return "";
	}
	/**
	 * 从list列表获得新的连接
	 * @param url
	 * @return
	 */
	public ArrayList<String> getNewUrl(String url)
	{
		ArrayList<String> list=new ArrayList<String>();
		try {
			String[] strs=pd.getHtml(url);
			if(strs[0].length()>0)
			{
				Document doc=Jsoup.parse(strs[0],url);
				Elements es=doc.getElementsByTag("a");
				for(int i=0;i<es.size();i++)
				{
					Element e=es.get(i);
					String aurl=e.absUrl("href");
					if(aurl.startsWith("http"))
					{
						list.add(aurl);
					}
				}
			}
		} catch (Exception e) {
			logger.error(url+"\n列表页网络请求数据失败！\n"+e.toString());
		}
		return list;
	}
	/**
	 * getNewUrl 方法得到的链接小于15的时候，使用Phantomjs浏览器进行进一步的抓取，防止部分链接使用异步加载
	 * @param url
	 * @return
	 */
	public ArrayList<String> getNewUrlByPhantomjs(String url)
	{
		ArrayList<String> list=new ArrayList<String>();
		String html=Phantomjs.getAjaxPage(url);
		if(html!=null&&html.length()>0)
		{
			Document doc=Jsoup.parse(html,url);
			Elements es=doc.getElementsByTag("a");
			for(int i=0;i<es.size();i++)
			{
				Element e=es.get(i);
				String aurl=e.absUrl("href");
				if(aurl.startsWith("http"))
				{
					list.add(aurl);
				}
			}
		}
		return list;
	}
	/**
	 * 对正文部分的html中的表格数据抽取
	 * @param e
	 * @return
	 */
	public ArrayList<String> GetTableList(Element e)
	{
		ArrayList<String> list=new ArrayList<String>();
		Elements es=e.getElementsByTag("table");
		for(int i=0;i<es.size();i++)
		{
			Element table=es.get(i);
			list.add(table.html());
		}
		return list;
	}
}
