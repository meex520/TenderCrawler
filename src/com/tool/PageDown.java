package com.tool;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.mozilla.universalchardet.UniversalDetector;



public class PageDown {
	public CloseableHttpClient pagemain(String url)
	{
		 CloseableHttpClient httpclient = HttpClients.createDefault();
		 HttpGet httpget = new HttpGet(url);
		 httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.93 Safari/537.36");
		// httpget.setHeader("Connection","keep-alive");
		// httpget.setHeader("Cookie","__jsluid=87fa212f7f5f16ca38d0a006c9fc3512; JSESSIONID=00006GmDlQVrNr1MTONQlUNpvHg:-1; __jsl_clearance=1482315044.048|0|43T%2BS8dGhWHYHtRkksn0qPkmAl4%3D; _gscu_1997809816=8117765721o7fw15; _gscs_1997809816=t823150465p0bmu13|pv:2; _gscbrs_1997809816=1");
		// httpget.setHeader("Host","www.cbrc.gov.cn");
		// httpget.setHeader("Pragma","no-cache");
		// httpget.setHeader("Referer","http://www.cbrc.gov.cn/zhuanti/xzcf/getPcjgXZCFDocListDividePage/chongqing.html");
		// httpget.setHeader("Upgrade-Insecure-Requests","1");
		
		 System.out.println("Executing request " + httpget.getURI());//�?�?
		 String html="";
	     try {
			CloseableHttpResponse response = httpclient.execute(httpget);
			 HttpEntity entity = response.getEntity();  
	         html=EntityUtils.toString(entity, "utf-8");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	     System.out.println(html);
	     return httpclient;
	}
	public final String[] getHtml(String url) throws Exception {
		CloseableHttpClient httpclient = HttpClients.custom()
				.setUserAgent("Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31")
				.build();
		try {
			HttpContext httpContext = new BasicHttpContext();
			HttpGet httpget = new HttpGet(url);
			int timeout = 100000;
			// System.out.println("Executing request " +
			// httpget.getRequestLine());
			RequestConfig config = RequestConfig.custom().setCookieSpec(CookieSpecs.BEST_MATCH)
					 .setExpectContinueEnabled(true).setStaleConnectionCheckEnabled(true)
					 .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM,
					 AuthSchemes.DIGEST))
					 .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).setRedirectsEnabled(true)
					.setRelativeRedirectsAllowed(true).setSocketTimeout(timeout).setConnectTimeout(timeout)
					.setConnectionRequestTimeout(timeout).setStaleConnectionCheckEnabled(true).build();
			httpget.setConfig(config);
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

				public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					// System.out.println(response.getStatusLine());
					if (status >= 200 && status < 300) {
						HttpEntity entity = response.getEntity();

						if (entity == null) {
							return null;
						}

						if (entity.getContentLength() > 100000) {
							return null;
						}

						if (!EntityUtils.getContentMimeType(entity).contains("text/html")) {
							return null;
						}

						InputStream myStream = entity.getContent();
						if (myStream == null) {
							return null;
						}
						try {
							int i = (int) entity.getContentLength();
							if (i < 0) {
								i = 4096;
							}
							final ByteArrayBuffer buffer = new ByteArrayBuffer(i);
							final byte[] tmp = new byte[4096];
							int l;
							UniversalDetector detector = new UniversalDetector(null);
							try {
								while ((l = myStream.read(tmp)) != -1) {
									buffer.append(tmp, 0, l);
									if (!detector.isDone()) {
										detector.handleData(tmp, 0, l);
									}
								}
							} catch (Exception e) {

							}
							detector.dataEnd();
							String encoding = detector.getDetectedCharset();
							// System.out.println(encoding);
							detector.reset();

							if (encoding != null) {
								return new String(buffer.toByteArray(), encoding);

							} else {
								return new String(buffer.toByteArray());
							}

						} finally {
							myStream.close();

						}

					} else {
						throw new ClientProtocolException("Unexpected response status: " + status);
					}
				}

			};
			String responseBody = httpclient.execute(httpget, responseHandler, httpContext);
			String currentUrl = url;
			try {
				HttpHost currentHost = (HttpHost) httpContext.getAttribute(HttpCoreContext.HTTP_TARGET_HOST);
				HttpUriRequest req = (HttpUriRequest) httpContext.getAttribute(HttpCoreContext.HTTP_REQUEST);
				currentUrl = (req.getURI().isAbsolute()) ? req.getURI().toString()
						: (currentHost.toURI() + req.getURI());
			} catch (Exception e) {
			}
			String[] result = new String[2];
			result[0] = responseBody;
			result[1] = currentUrl;
			return result;
		} finally {

			httpclient.close();
		}
	}

	public String[] downLoadHtml(String url, String downloader) {
		String[] result = null;
		try {
			if (downloader.trim().equalsIgnoreCase("1.0")) {
				result = getHtml(url);
			} else if (downloader.trim().equalsIgnoreCase("0.0")) {

				String html = null;
				try {
					html = Jsoup.parse(new URL(url), 10000).html();
				} catch (MalformedURLException e) {

				} catch (IOException e) {

				}
				if (html != null) {
					result = new String[2];
					result[0] = html;
					result[1] = url;
				}
			} else {
				result = getHtml(url);
			}
		} catch (Exception e) {
			// e.printStackTrace();
			result = null;
		}

		return result;
	}
	/**
	 * 
	 * @param tmpHref url链接
	 * @param downloader 下载方式 1.0 使用httpclient下载�?0.0 使用Jsoup下载
	 * @return
	 * @throws Exception
	 */
	public String[] getDocAndHrefmCurrent(String tmpHref, String downloader) throws Exception {
		String[] docAndHref = null;
		try {
			String[] tmpResult = downLoadHtml(tmpHref, downloader);
			if (tmpResult == null) {
				System.out.println("error tmpHref not downloader:"+tmpHref);
				//throw new Exception();
				return null;
			}
			if (tmpResult.length == 2) {
				docAndHref = new String[2];
				String tmpHtml = tmpResult[0];
				String tmpHrefCurrent = tmpResult[1];
				docAndHref[0] = tmpHtml;
				docAndHref[1] = tmpHrefCurrent;
			}

		} catch (Exception e) {
			throw e;
		}
		return docAndHref;
	}
	public  String getHtml1(String url) throws Exception {
		System.out.println(url);
		final String fc=url;
		CloseableHttpClient httpclient = HttpClients.custom()
				.setUserAgent(
						"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31")
				.build();
		try {
			HttpContext httpContext = new BasicHttpContext();
			HttpGet httpget = new HttpGet(url);
			int timeout = 5000000;
			HttpHost myProxy = new HttpHost("127.0.0.1", 1984);

			RequestConfig config = RequestConfig.custom().setCookieSpec("best-match").setExpectContinueEnabled(true)
					.setStaleConnectionCheckEnabled(true)
					.setTargetPreferredAuthSchemes(Arrays.asList(new String[] { "NTLM", "Digest" }))
					.setProxyPreferredAuthSchemes(Arrays.asList(new String[] { "Basic" })).setRedirectsEnabled(true)
					.setRelativeRedirectsAllowed(true).setSocketTimeout(timeout).setConnectTimeout(timeout)
					.setConnectionRequestTimeout(timeout).setStaleConnectionCheckEnabled(true).build();
			httpget.setConfig(config);

			ResponseHandler responseHandler = new ResponseHandler() {
				public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();

					if ((status >= 200) && (status < 300)) {
						HttpEntity entity = response.getEntity();

						if (entity == null) {
							return null;
						}

						if (entity.getContentLength() > 100000L) {
							return null;
						}

						if (!EntityUtils.getContentMimeType(entity).contains("text/html")) {
							return null;
						}

						InputStream myStream = entity.getContent();
						if (myStream == null) {
							return null;
						}

						try {
							int i = (int) entity.getContentLength();
							if (i < 0) {
								i = 4096;
							}
							ByteArrayBuffer buffer = new ByteArrayBuffer(i);

							byte[] tmp = new byte[4096];

							UniversalDetector detector = new UniversalDetector(null);
							try {
								int l;
								while ((l = myStream.read(tmp)) != -1) {
									buffer.append(tmp, 0, l);
									if (!detector.isDone())
										detector.handleData(tmp, 0, l);
								}
							} catch (Exception localException) {
							}
							detector.dataEnd();
							String encoding = detector.getDetectedCharset();
							System.out.println("============="+encoding);
							detector.reset();
							String str1;
							if (encoding != null) {
								String html=new String(buffer.toByteArray(), encoding);
								
								return html;
							}
							String html=new String(buffer.toByteArray());
							
							return html;
						} finally {
							myStream.close();
						}

					}

					throw new ClientProtocolException("Unexpected response status: " + status);
				}
			};
			String responseBody = (String) httpclient.execute(httpget, responseHandler, httpContext);
			String currentUrl = url;
			HttpUriRequest req;
			try {
				HttpHost currentHost = (HttpHost) httpContext.getAttribute("http.target_host");

				req = (HttpUriRequest) httpContext.getAttribute("http.request");

				currentUrl = currentHost.toURI() + req.getURI();
			} catch (Exception localException) {
			}
			String[] result = new String[2];
			System.out.println("currentUrl :" + currentUrl);
			return responseBody;
		} finally {
			httpclient.close();
		}
	}
	public  String getHtmltest(String url) throws Exception {
		final String fc=url;
		CloseableHttpClient httpclient = HttpClients.custom()
				.setUserAgent(
						"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.64 Safari/537.31")
				.build();
		try {
			HttpContext httpContext = new BasicHttpContext();
			HttpGet httpget = new HttpGet(url);
			int timeout = 5000000;
			

			RequestConfig config = RequestConfig.custom().setCookieSpec("best-match").setExpectContinueEnabled(true)
					.setStaleConnectionCheckEnabled(true)
					.setTargetPreferredAuthSchemes(Arrays.asList(new String[] { "NTLM", "Digest" }))
					.setProxyPreferredAuthSchemes(Arrays.asList(new String[] { "Basic" })).setRedirectsEnabled(true)
					.setRelativeRedirectsAllowed(true).setSocketTimeout(timeout).setConnectTimeout(timeout)
					.setConnectionRequestTimeout(timeout).setStaleConnectionCheckEnabled(true).build();
			httpget.setConfig(config);

			ResponseHandler responseHandler = new ResponseHandler() {
				public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();

					if ((status >= 200) && (status < 300)) {
						HttpEntity entity = response.getEntity();

						if (entity == null) {
							return null;
						}

						if (entity.getContentLength() > 100000L) {
							return null;
						}

						if (!EntityUtils.getContentMimeType(entity).contains("text/html")) {
							return null;
						}

						InputStream myStream = entity.getContent();
						if (myStream == null) {
							return null;
						}

						try {
							int i = (int) entity.getContentLength();
							if (i < 0) {
								i = 4096;
							}
							ByteArrayBuffer buffer = new ByteArrayBuffer(i);

							byte[] tmp = new byte[4096];

							UniversalDetector detector = new UniversalDetector(null);
							try {
								int l;
								while ((l = myStream.read(tmp)) != -1) {
									buffer.append(tmp, 0, l);
									if (!detector.isDone())
										detector.handleData(tmp, 0, l);
								}
							} catch (Exception localException) {
							}
							detector.dataEnd();
							String encoding = detector.getDetectedCharset();

							detector.reset();
							String str1;
							String encodehtml=new String(buffer.toByteArray());
							String encodinghtml=getencode(encodehtml);
							if(!"".equals(encodinghtml))
							{
								encoding=encodinghtml;
							}
							if (encoding != null) {
								String html=new String(buffer.toByteArray(), encoding);
								
								return html;
							}
							String html=new String(buffer.toByteArray());
							
							return html;
						} finally {
							myStream.close();
						}

					}

					throw new ClientProtocolException("Unexpected response status: " + status);
				}
			};
			String responseBody = (String) httpclient.execute(httpget, responseHandler, httpContext);
			String currentUrl = url;
			HttpUriRequest req;
			try {
				HttpHost currentHost = (HttpHost) httpContext.getAttribute("http.target_host");

				req = (HttpUriRequest) httpContext.getAttribute("http.request");

				currentUrl = currentHost.toURI() + req.getURI();
			} catch (Exception localException) {
			}
			String[] result = new String[2];
			System.out.println("currentUrl :" + currentUrl);
			return responseBody;
		} finally {
			httpclient.close();
		}
	}
	public String getencode(String html)
	{
		Elements es=Jsoup.parse(html).getElementsByTag("meta");
		for(int i=0;i<es.size();i++)
		{
			String content=es.get(i).attr("content");
			if(content.indexOf("charset=")!=-1)
			{
				int start=content.indexOf("charset=")+8;
				content=content.substring(start);
				if(content.indexOf(";")==-1)
					return content;
				else{
					String[] strs=content.split(";");
					return strs[0];
				}
							
			}
		}
		return "";
	}
	public static void main(String[] args)
	{
		PageDown d=new PageDown();
	
		try {
			String[] ss=d.getHtml("http://www.hljdep.gov.cn/docweb/docList.action?channelId=4850&parentChannelId=-1");
			System.out.println(ss[0]);
			System.out.println(Jsoup.parse(ss[0]).select("title").text());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
