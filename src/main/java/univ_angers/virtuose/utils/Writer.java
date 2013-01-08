package univ_angers.virtuose.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

 
public class Writer {
 
   public int countFile = 1;
   public String prefName;
   public ArrayList<String> parcours;
   public Stack<String> id_parent;
   public int nbnoeud_o;
   public int profondeur;
   public int  nbcount = 0;
   
   
   public ArrayList<Document> docs = new ArrayList<Document>();
		   
   public void proceed(String filePath) {
	   final String parsedFile = filePath;
	   
	   prefName = (new File(parsedFile)).getName().replace('.', '_');
	   parcours = new ArrayList<String>();
	   id_parent = new Stack<String>();
	   profondeur = 0;
	   
	   
	   try {
 
		   SAXParserFactory factory = SAXParserFactory.newInstance();
		   SAXParser saxParser = factory.newSAXParser();
		   
		   DefaultHandler handler = new DefaultHandler() {
		   	   
		   	   public void startDocument() throws SAXException{
		   	  
		   	   }
		   	   
			   public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
				   if (qName.equalsIgnoreCase("node") ){
					   	String id_p = "";
					   	String contenu;
					   	// racine
					   	if(profondeur == 0){
					   		id_p = "NULL";
					   		contenu = "";
					   		
					   	}
					   	else{
					   		id_p = id_parent.peek();	
					   		contenu = attributes.getValue("TEXT");
					   	}
					   	id_parent.push(attributes.getValue("ID"));
					   	String id = attributes.getValue("ID");
					   						   	
					   	Document doc = new Document();
						doc.add(new TextField("title", prefName+nbcount+".xml", Field.Store.YES));
						doc.add(new TextField("document", parsedFile, Field.Store.YES));
						doc.add(new TextField("id", id, Field.Store.YES));
						doc.add(new TextField("id_parent", id_p, Field.Store.YES));
						doc.add(new TextField("profondeur",String.valueOf(profondeur), Field.Store.YES));
						TextField f = new TextField("content", contenu, Field.Store.YES);
					    f.setBoost(Float.parseFloat(String.valueOf(profondeur))*100);
						doc.add(f);
						docs.add(doc);
 
			   	   		profondeur++;
			   	   		nbcount++;
				   }
 
			   }
 
			   public void endElement(String uri, String localName,
					   String qName) throws SAXException {
 
				   if (qName.equalsIgnoreCase("node") ){
					   //parcours.add("</node>");	
					   profondeur--;
					   id_parent.pop();
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