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

import univ_angers.virtuose.utils.HandleFiles;

 
public class Writer {
 
   public static int countFile = 1;
   //public static String sufName;
   public static String prefName;
   //public static HandleFiles h;
   public static ArrayList<String> parcours;
   public static Stack<String> id_parent;
   public static int nbnoeud_o;
   public static int profondeur;
   
   public static ArrayList<Document> docs = new ArrayList<Document>();
		   
   public static void proceed(String filePath) {
	   String parsedFile = filePath;
	   //(new HandleFiles("d",docPath)).handle();
	   
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
					   	// racine
					   	if(profondeur == 0){
					   		id_p = "NULL";
					   	}
					   	else id_p = id_parent.peek();					   
					   	id_parent.push(attributes.getValue("ID"));
					   	
					
			   	   		String text = "<node ID=\""+attributes.getValue("ID")+"\" ID_PARENT=\""+ id_p +"\" TEXT=\""+attributes.getValue("TEXT")+"\" PROFONDEUR=\""+profondeur+"\">";
			   	   		parcours.add(text);  
			   	   		profondeur++;
				   }
 
			   }
 
			   public void endElement(String uri, String localName,
					   String qName) throws SAXException {
 
				   if (qName.equalsIgnoreCase("node") ){
					   parcours.add("</node>");	
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
	   
	   /*****************************
	    *  Création des fichiers	*
	    *****************************/
	   //System.out.println(parcours.toString());
	   parcours.remove(parcours.size()-1);
	   parcours.remove(0);
	   //System.out.println(parcours);
	   int nbcount = 0;
	   
	   
	   while(!parcours.isEmpty()){
		   nbnoeud_o = 1;
		   int ind_end=0;
		   String content= "";
		   
		   ArrayList<String> tmp = new ArrayList<String>(parcours);
		   //h = new HandleFiles("f","/home/etudiant/cardsPagination2/"+prefName+nbcount+".xml");
		   //h.handle();
		   Document doc = new Document();
		   doc.add(new TextField("title", prefName+nbcount+".xml", Field.Store.YES));
		   
		   
		   while((nbnoeud_o != 0) && (!parcours.isEmpty()) ) {
			  String noeud = parcours.get(0);
			  parcours.remove(0);
			  if(noeud.equalsIgnoreCase("</node>")){
				  
				  nbnoeud_o--; 
			  }
			  else{
				  nbnoeud_o++;
			  }
			  if(nbnoeud_o == 1) nbnoeud_o = 0;
			  content += noeud;
			  ind_end++;
		   }

		   content = content.replace("\n", "");
		   //h.addText(content);
		   
	       int i = content.indexOf("PROFONDEUR");
	       String prof = content.substring(i,i+15);
	       String p = prof.substring(prof.indexOf("\"")+1,prof.lastIndexOf("\""));
	       TextField f = new TextField("content", content, Field.Store.YES);
	       f.setBoost(Float.parseFloat(p)*100);
		   doc.add(f);
		   docs.add(doc);
		   parcours = tmp;
		   parcours.remove(ind_end-1);
		   parcours.remove(0);
 		   nbcount++;		   
	   } 
   }
 
}