package univ_angers.virtuose.rendu;

import java.io.File;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class Extract {
	private static String res_id;
	private static String card;
	public static String extract( String OrgCard, String Id ) throws Exception{
		String d1 = null;
		res_id = Id;
		card = OrgCard;
		try{
			DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();
			fabrique.setValidating(true);
			File xml = new File(OrgCard);
			DocumentBuilder constructeur = fabrique.newDocumentBuilder();
			constructeur.setErrorHandler(null);
			
			Document document = constructeur.parse(xml);
			Element racine = document.getDocumentElement();
			NodeList liste = racine.getElementsByTagName("node");
			Element e = null;
			
			for(int i=0; i<liste.getLength(); i++){
				Element e1 = (Element)liste.item(i);
				if(e1.getAttribute("ID").equals(Id)){
					e = (Element) liste.item(i);
				}
			}
			if(e.hasAttribute("TEXT")){
				String s1 = displayChilds(e);
				//String s2 = displayParents((Element) e.getParentNode(),racine,s1 );
				//System.out.println(s2+"\n\n");
				d1 = displayParents((Element) e.getParentNode(), racine, s1);
				return d1;
			}
		}catch(Exception e){
			throw e;
		}
		return d1;
	}

	private static int foundInNodeList(Node n , NodeList nl){
		int ret = -1;
		for(int i = 0; i< nl.getLength(); ++i){
			if (n.equals(nl.item(i))){
				ret=i;
			}
		}
		return ret;
	}
	private static String displayChilds(Element e) {
		String ret = "";
		NodeList nodeChilds = e.getElementsByTagName("node");
		NodeList childs = e.getChildNodes();
		String attributes = "ID=\""+e.getAttribute("ID")+"\" TEXT=\""+e.getAttribute("TEXT")+"\"";
		if(e.getAttribute("ID").equals(res_id)){
			attributes +=" class=\"result\"";
			//attributes +=" BACKGROUND_COLOR=\"#F0FFF0\"";
		}
		ret +="<node "+attributes+">\n";
		if(childs.getLength()>0){
			for(int i =0; i<childs.getLength(); ++i){
				Node tmp = childs.item(i);
				int pos = foundInNodeList(tmp, nodeChilds);
				if(pos!=-1){
					Element ce = (Element) nodeChilds.item(pos);
					ret += displayChilds(ce);
				}
				
			}
		}
		ret +="</node>\n";
		return ret;
	}
	
	private static String displayParents(Element e, Element r, String s) {
		String ret=s;
		Element pe= (Element) e.getParentNode();
		if (!e.equals(r)){
			ret="<node ID=\""+e.getAttribute("ID")+"\" TEXT=\""+e.getAttribute("TEXT")+"\">\n"+s+"</node>\n";
		}
		if(pe.equals(r)){
			return ret;
		}else{
			return displayParents(pe, r, ret);
		}
		
	}
}