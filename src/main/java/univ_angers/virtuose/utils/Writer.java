package univ_angers.virtuose.utils;

import java.io.File;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import univ_angers.virtuose.utils.HandleFiles;

 
public class Writer {
 
   public static int count =0;
   public static int countFile = 1;
   //public static String sufName;
   public static String prefName;
   public static HandleFiles h;
		   
   public static void proceed(String docPath, String filePath) {
	   String parsedFile = filePath;
	   (new HandleFiles("d",docPath)).handle();
	   
	   prefName = (new File(parsedFile)).getName().replace('.', '_');
	   
	   try {
 
		   SAXParserFactory factory = SAXParserFactory.newInstance();
		   SAXParser saxParser = factory.newSAXParser();
		   
		   DefaultHandler handler = new DefaultHandler() {
		   	   
		   	   public void startDocument() throws SAXException{
		   	   		count = -1;
		   	   }
			   public void startElement(String uri, String localName,String qName, 
					   Attributes attributes) throws SAXException {
				   if (qName.equalsIgnoreCase("node") ){
				   	   if (count == 0){
				   	   		//sufName = attributes.getValue("ID");
				   	   		h = new HandleFiles("f","/home/etudiant/cardsPagination/"+prefName+countFile+".xml");
				   	   		h.handle();
				   	   		countFile++;
				   	   }
				   	   	if(count>=0){
				   	   		String text = "<node ID=\""+attributes.getValue("ID")+"\" TEXT=\""+attributes.getValue("TEXT")+"\">";
				   	   		h.addText(text);
				   	   	}
				   	   		
					   count++;
				   }
 
			   }
 
			   public void endElement(String uri, String localName,
					   String qName) throws SAXException {
 
				   if (qName.equalsIgnoreCase("node") ){
				   	   if (count != 0){
				   		   h.addText("</node>");
				   	   }
					   count--;
				   }
 
			   }
 
			   public void characters(char ch[], int start, int length) throws SAXException {
 
			   }
 
		   };
 
		   saxParser.parse(parsedFile, handler);
 
	   } catch (Exception e) {
		   e.printStackTrace();
     }
 
   }
 
}