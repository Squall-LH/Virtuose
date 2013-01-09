package univ_angers.virtuose;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.queryparser.classic.ParseException;
import univ_angers.virtuose.search.Search;

/**
 * Permet de tester les classes sans passer par l'interface Web
 * 
 * Utilisation :
 * -index .mm to index
 * -search keyword to search for
 */
public class App {
	private static Logger log = Logger.getLogger(App.class);
	private static String param_index = "-cut";
	private static String param_search = "-search";
	private static String folderPath = "/home/etudiant/lucene_docs_src/";
	
	public static void main(String[] args) {
		if(args.length <= 1) { 
			args = new String[3];
			args[1] = "-search";
			args[2] = "Affaire";
			//args[1] = "-cut";
			//args[2] = "src/main/resources/Manceau-alain-rai-UIPL.mm";
		}
		
		log.info("request type: " + args[1]);
		log.info("request param: " + args[2]);
		/*
		if (args.length > 1 && args[1].equals(param_index)) {
			try {
				log.info("on index le fichier " + args[2]);
				Cut.start(args[2], folderPath);
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		} else if(args.length > 1 && args[1].equals(param_search)){
			try {
				log.info("recherche de " + args[2]);
				Search.start(args[2], folderPath);
			} catch (IOException e) {
				log.error("errrorIO: " + e.getMessage());
			} catch (ParseException e) {
				log.error("ParseError:" + e.getMessage());
			}
		}*/
	}
}
