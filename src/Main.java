import java.io.*;
import org.jdom2.*;
import org.jdom2.input.*;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Stack;

/**
 * On explore en profondeur la carte. En fonction de certains pramètre par exemple le nombre de lettre dans la branche explorée jusqu'à présent par 
 * rapport au nombre de lettre total de la carte, le nombre de node exploré et d'autre param, on crée ensuite une page xml avec pour contenu les node exploré
 * jusqu'à présent. Il faudra couper uniquement lorsque l'on a atteind une feuille...
 * Chaque page xml sera nommé avec le nom de la carte + _numero_de_page + .xml
 * 
 * 
 * @author etudiant
 *
 */

public class Main
{
   static org.jdom2.Document document;
   static Element racine;
   static org.jdom2.Document document_xml;
   static Element racine_xml;
   static String mapFolder = "/home/etudiant/Virtuose/maps/";
   static String mapName = "Manceau-alain-rai-UIPL.mm";
   static String mapPath = mapFolder + mapName;
   static String xml_folder = "/home/etudiant/Virtuose/xml/";
   
   public static void main(String[] args)
   {
      SAXBuilder sxb = new SAXBuilder();
      try
      {
    	  document = sxb.build(new File(mapPath));
      }
      catch(Exception e){}

      //On initialise un nouvel élément racine avec l'élément racine du document. aka map
      racine = document.getRootElement();
      List<Element> lNode = racine.getChildren("node");
      Element node = (Element)lNode.get(0);
      afficheALL(node);
   }
   
   static void afficheALL(Element node)
   {
	   boolean hadNext = true;
	   int nbChar = 0;
	   int nbPage = 1;
	  // xml 
	  // Racine de chaque xml créé à partir d'une map.
	  racine_xml = new Element("node");
	  document_xml = new Document(racine_xml);
	  Element e_xml = new Element("node");
	  e_xml.setText("racine");
	  racine_xml.addContent(e_xml);
	  // end xml
	  
	  List<Element> lNode = new ArrayList<Element>();
	  lNode.add(node);  
	  System.out.println(node.getAttributeValue("TEXT"));
	  
	  Stack<Element> stack = new Stack<Element>();
	  Iterator<Element> i = node.getChildren("node").iterator();
      while(i.hasNext())	
      {
         stack.push(i.next());
      }
      
      Element e_xml_old = null;
      while(!stack.empty()) {
    	  Element e = stack.pop();
    	  String text = e.getAttributeValue("TEXT");
    	  System.out.println(text);
    	  nbChar += text.length();
    	  // xml
    	  if(hadNext) { // On n'était pas sur une feuille.
			  Element e_xml_child = new Element("node");
			  e_xml_child.setText(e.getAttributeValue("TEXT"));
			  e_xml.addContent(e_xml_child);
			  e_xml = e_xml_child;
    	  }
    	  else {  // on était sur une feuille.
    		  
    		  if(nbChar > 200) {
    			  System.out.println("nbChar: " + nbChar);
    			  
    			  save_xml(Integer.toString(nbPage));
    			  nbChar -= e_xml_old.getText().length();
    			  nbPage++;
    			  document_xml.removeContent(e_xml_old);
    		  }
        	  e_xml = new Element("node");
        	  e_xml.setText(e.getAttributeValue("TEXT"));
        	  racine_xml.addContent(e_xml);
    	  }
		  // end xml
    	  
    	  Iterator<Element> e_i = e.getChildren("node").iterator();
    	  hadNext = false;
    	  e_xml_old = e_xml;
    	  while(e_i.hasNext()) {
    		  hadNext = true;
    		  Element eChild = (Element)e_i.next();
    		  if(!lNode.contains(eChild)) {
    			  lNode.add(eChild);
    			  stack.push(eChild);
    		  }
    	  }
      }
      
      save_xml(Integer.toString(nbPage));
   }
   
   static void save_xml(String pageNumber)
   {
      try
      {
         //On utilise ici un affichage classique avec getPrettyFormat()
         XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
         //Remarquez qu'il suffit simplement de créer une instance de FileOutputStream
         //avec en argument le nom du fichier pour effectuer la sérialisation.
         sortie.output(document_xml, new FileOutputStream(xml_folder + mapName + "_" + pageNumber + ".xml"));
      }
      catch (java.io.IOException e){}
   }
}

