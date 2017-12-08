package com.peopleyuqing.extractunion;
import org.jsoup.nodes.Element;
public class UnionTemplateTimeUtils {
	public static String extract(String template,Element myEle){
		String[] tmpTemplArr = template.split("\\|");
	 
		   for(int i =0;i<tmpTemplArr.length;i++){
			   String tmpItem = tmpTemplArr[i];
           	String tmpTagName = tmpItem.split(":")[0].trim();
           	String tmpTagIndex =  "";
           	String tmpSelector = null;
           	String tmpSelectorName = null;
           	if(tmpItem.contains(",")){
           		tmpTagIndex = tmpItem.split(":")[1].split(",")[0].trim();
           		tmpSelector = tmpItem.split(":")[1].split(",")[1].split("=")[0].trim();
           		tmpSelectorName = tmpItem.split(":")[1].split(",")[1].split("=")[1].replace("\"", "").trim();
           	}else{
           		tmpTagIndex = tmpItem.split(":")[1].trim();
           	}

           	int tmpCount = 0;
           	String tmpResult = "";
        	boolean bFound = false;
           	for(Element tmpEle:myEle.children()){
           		if(tmpTagIndex.equals("*")){
           			String tmpTemplate = "";
   					for(int j=i+1;j<tmpTemplArr.length;j++){
   						tmpTemplate = tmpTemplate+"|"+tmpTemplArr[j];
   					}
   					tmpTemplate = tmpTemplate.replaceFirst("\\|", "");
   					Element tmpElement = null;
           			if(tmpEle.tagName().equals(tmpTagName)){
               			if(tmpSelector!=null&&tmpSelectorName!=null){
               				if(tmpEle.attr(tmpSelector).trim().replace(" ", "").equalsIgnoreCase(tmpSelectorName.replace(" ", ""))){
               					tmpElement = tmpEle;
               				}
               			}else{
               				tmpElement = tmpEle;
               			}               			
               		}
           			
           			if(tmpElement!=null&&!tmpTemplate.equals("")){
           				String result = extract(tmpTemplate,tmpElement);
           				if(result !=null){
           					return result ;
           				}
           			}
           			if(tmpTemplate.equals("")&&tmpElement!=null){
           				if(tmpElement.text().trim().length()>0){
           				tmpResult = tmpResult+tmpElement.text()+"\r\n";
           				}
           			}
           		}else{
           		if(tmpEle.tagName().equals(tmpTagName)){
           			if(tmpCount==Integer.parseInt(tmpTagIndex)){
           				myEle = tmpEle;
           				bFound = true;
           				break;
           			}else{
           				tmpCount ++;
           			}
           		}
           	}
           		
           	}
            if(!tmpResult.equals("")){
          		 return tmpResult;
          	 }
           	if(!bFound){
       			return null;
       		}
           	
           	
           }
		   if(myEle!=null){
         		 if(myEle.text().length()>0){
         			// System.out.println(myEle.cssSelector());
         			 return myEle.text();
         			 
         		 }else{
         			 return null;
         		 }
         	 }else{
         		 return null;
         	 }
         		 
	}
}
