package univ_angers.virtuose.utils;

import java.io.StringReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/** - Fusionne les chemins presque identique. 
 */
public class Fuse_Tree {
	private static Logger log = Logger.getLogger(Fuse_Tree.class);

	private List<String> maps;
	
	public Fuse_Tree(List<String> maps) {
		this.maps = maps;
	}
	
	public List<String> fuse() {
		List<String> fused_maps = new ArrayList<String>();
		
		for(int i = 0; i < maps.size(); i++) {
			for(int j = i+1; j < maps.size(); j++) {
				if(toFuse(i,j)) {
					log.debug("map: " + i + " et map: " + j + " sont proches.");
					//fused_map.add(merge(i,j));
				}
			}
		}
		
		return fused_maps;
	}
	
	/** Vrai si les deux chemins sont semblables */
	public boolean toFuse(Integer id1, Integer id2) {
		String map1 = maps.get(id1);
		String map2 = maps.get(id2);
		Document document1;
		Element racine1;
		Document document2;
		Element racine2;
		
		boolean fuse = false;
		
		SAXBuilder sxb = new SAXBuilder();
		try {
			document1 = sxb.build(new StringReader(map1));
			document2 = sxb.build(new StringReader(map2));
			
			List<Integer> listId1 = getListId(document1);
			List<Integer> listId2 = getListId(document2);
			
			return match(listId1, listId2);
			
		} catch (Exception e) {
			log.error(e.getMessage());
		}		
		return fuse;
	}
	
	/** Retourne la liste des Id contenus dans le doc */
	public List<Integer> getListId(Document doc) {
		Element node = doc.getRootElement();
		List<Integer> listId = new ArrayList<Integer>();
		
		List<Element> lNode = new ArrayList<Element>();
		lNode.add(node);
		
		addId(node, listId);
		Queue<Element> stack = new ArrayDeque<Element>();

		Iterator<Element> i = node.getChildren().iterator();
		while (i.hasNext()) {
			stack.add(i.next());
		}

		while (stack.peek() != null) {
			Element e = stack.poll();
			
			addId(e, listId);
			Iterator<Element> e_i = e.getChildren().iterator();
			while (e_i.hasNext()) {
				Element eChild = (Element) e_i.next();
				if (!lNode.contains(eChild)) {
					lNode.add(eChild);
					stack.add(eChild);
				}
			}
		}
		
		return listId;
	}
	
	public void addId(Element node, List<Integer> listId) {
		String sid = node.getAttributeValue("ID");
		if(sid != null) {
			Integer id = Integer.getInteger(sid);
			listId.add(id);
		}
	}
	
	/** Vrai si les deux listes d'id sont suffisemment proche */
	public boolean match(List<Integer> l1, List<Integer> l2) {
		// On compte le nombre d'id identique entre l1 et l2.
		// Si le nombre d'id identique est proche de la taille de la plus petite liste entre l1 et l2, il y a match
		
		int nb_matchs = 0;
		int min_size = (l1.size() < l2.size()) ? l1.size() : l2.size();
		
		for(Integer i : l1) {
			if(l2.contains(i)) {
				nb_matchs++;
			}
		}
		
		return (min_size - nb_matchs) < 2;
	}
}
