package univ_angers.virtuose.utils;

import java.io.*;

import org.apache.log4j.Logger;
import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Stack;

public class XmlToHtml {
	private static Logger log = Logger.getLogger(Writer.class);

	static Document document;
	static Element racine;
	static String mapFolder = "src/main/resources/";
	static String mapName = "map.xml";
	static String mapPath = mapFolder + mapName;
	static String xml_folder = "/home/etudiant/html";
	static String htmlPath = "/home/etudiant/html/page.html";
	static int depth = 0;
	
	public static void main(String[] args) {
		SAXBuilder sxb = new SAXBuilder();
		try {
			document = sxb.build(new File(mapPath));
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		// On initialise un nouvel élément racine avec l'élément racine du
		// document. aka map
		racine = document.getRootElement();
		afficheALL(document, racine);
	}

	static void afficheALL(Document doc, Element node) {
		List<Element> lNode = new ArrayList<Element>();
		lNode.add(node);
		elemToHtml(node);
		
		Stack<Element> stack = new Stack<Element>();
		Iterator<Element> i = node.getChildren("node").iterator();
		while (i.hasNext()) {
			stack.push(i.next());
		}

		while (!stack.empty()) {
			Element e = stack.pop();
			
			
			int current_depth = getDepth(e);
			//log.debug("DEPTH: " + depth);
			//log.debug("CURRENT_DEPTH: " + current_depth);
			if(depth < current_depth) {	
				Element parent = e.getParentElement();
				if(!parent.getName().equals("ul")) {
					List<Element> content = parent.removeContent();
					Element ul = new Element("ul");
					parent.addContent(ul);
					ul.addContent(content);
				}
			}
			depth = current_depth;
			
			elemToHtml(e);
			
			Iterator<Element> e_i = e.getChildren("node").iterator();
			while (e_i.hasNext()) {
				Element eChild = (Element) e_i.next();
				if (!lNode.contains(eChild)) {
					lNode.add(eChild);
					stack.push(eChild);
				}
			}
		}

		save_xml(doc, htmlPath);
	}
	
	static int getDepth(Element e) {
		int depth = 0;
		while(e.getParentElement() != null) {
			e = e.getParentElement();
			depth++;
		}
		return depth;
	}

	static void elemToHtml(Element e) {
		if(e.getName().equals("node")) {
			String text = e.getAttributeValue("TEXT");
			log.debug(text);
			Element a = new Element("a");
			a.setText(text);
			e.addContent(0, a);
			e.removeAttribute("TEXT");
			//e.removeAttribute("PROFONDEUR");
			e.removeAttribute("ID");
			e.removeAttribute("ID_PARENT");
			e.setName("li");
		}
	}
	
	static void save_xml(Document doc, String filePath) {
		try {
			XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
			sortie.output(doc, new FileOutputStream(filePath));
			log.info("html page saved at: " + filePath);
		} catch (java.io.IOException e) {
			log.error(e.getMessage());
		}
	}
}
