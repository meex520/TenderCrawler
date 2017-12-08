package com.bean;

import java.util.ArrayList;

/**
 * 正文信息
 * @author admin
 *
 */
public class ArtInfoBean {
	String url="";
	String area="";   //地区
	String department=""; //部门
	String part=""; //栏目
	String arturl=""; //文章链接
	String title=""; //标题
	String content=""; //正文
	String pubtime=""; //发布时间
	String crawlertime="";//抓取时间
	ArrayList<String> table=new ArrayList<String>();//文章中的表格内容
	ArrayList<FileBean> filelist=new ArrayList<FileBean>();
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getPart() {
		return part;
	}
	public void setPart(String part) {
		this.part = part;
	}
	public String getArturl() {
		return arturl;
	}
	public void setArturl(String arturl) {
		this.arturl = arturl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPubtime() {
		return pubtime;
	}
	public void setPubtime(String pubtime) {
		this.pubtime = pubtime;
	}
	public String getCrawlertime() {
		return crawlertime;
	}
	public void setCrawlertime(String crawlertime) {
		this.crawlertime = crawlertime;
	}
	public ArrayList<FileBean> getFilelist() {
		return filelist;
	}
	public void setFilelist(ArrayList<FileBean> filelist) {
		this.filelist = filelist;
	}
	public ArrayList<String> getTable() {
		return table;
	}
	public void setTable(ArrayList<String> table) {
		this.table = table;
	}
	
	
}
