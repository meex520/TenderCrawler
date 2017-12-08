package com.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;

public class PdfReader {
	public static void readImage(){

        // ������PDF
        File pdfFile = new File("/Users/xiaolong/Downloads/test.pdf");      
        // �հ�PDF
        File pdfFile_out = new File("/Users/xiaolong/Downloads/testout.pdf");

        PDDocument document = null;  
        PDDocument document_out = null;  
        try {  
            document = PDDocument.load(pdfFile);  
            document_out = PDDocument.load(pdfFile_out);  
        } catch (IOException e) {  
            e.printStackTrace();
        }  

        int pages_size = document.getNumberOfPages();

        System.out.println("getAllPages==============="+pages_size);  
        int j=0;

        for(int i=0;i<pages_size;i++) {  
            PDPage page = document.getPage(i);
            PDPage page1 = document_out.getPage(0);
            PDResources resources = page.getResources();  
            Iterable xobjects = resources.getXObjectNames();

            if (xobjects != null) {  
                Iterator imageIter = xobjects.iterator();  
                while (imageIter.hasNext()) {  
                    COSName key = (COSName) imageIter.next();  
                    if(resources.isImageXObject(key)){              
                        try {
                            PDImageXObject image = (PDImageXObject) resources.getXObject(key);

                            // ��ʽһ����PDF�ĵ��е�ͼƬ �ֱ�浽һ���հ�PDF�С�
                            PDPageContentStream contentStream = new PDPageContentStream(document_out,page1,AppendMode.APPEND,true);

                            float scale = 1f;
                            contentStream.drawImage(image, 20,20,image.getWidth()*scale,image.getHeight()*scale);
                            contentStream.close();
                            document_out.save("/Users/xiaolong/Downloads/123"+j+".pdf");

                            System.out.println(image.getSuffix() + ","+image.getHeight() +"," + image.getWidth());

                            /**
                            // ��ʽ������PDF�ĵ��е�ͼƬ �ֱ����ΪͼƬ��
                            File file = new File("/Users/xiaolong/Downloads/123"+j+".png");
                            FileOutputStream out = new FileOutputStream(file);

                            InputStream input = image.createInputStream();                   

                            int byteCount = 0;
                            byte[] bytes = new byte[1024];

                            while ((byteCount = input.read(bytes)) > 0)
                            {                       
                                out.write(bytes,0,byteCount);       
                            }

                            out.close();
                            input.close();
                            **/

                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } 
                        //image count
                        j++;  
                    }                 
                }  
            } 
        } 

        System.out.println(j);
    }  
    public static void main(String[] args){

        File pdfFile = new File("D:\\test2.pdf");
        PDDocument document = null;
        try
        {
            // ��ʽһ��
            /**
            InputStream input = null;
            input = new FileInputStream( pdfFile );
            //���� pdf �ĵ�
            PDFParser parser = new PDFParser(new RandomAccessBuffer(input));
            parser.parse();
            document = parser.getPDDocument();
            **/

            // ��ʽ����
            document=PDDocument.load(pdfFile);

            // ��ȡҳ��
            int pages = document.getNumberOfPages();

            // ���ı�����
            PDFTextStripper stripper=new PDFTextStripper();
            // ���ð�˳�����
            stripper.setSortByPosition(true);
            stripper.setStartPage(1);
            stripper.setEndPage(pages);
            String content = stripper.getText(document);
            System.out.println(content);     
        }
        catch(Exception e)
        {
            System.out.println(e);
        }

    }

}