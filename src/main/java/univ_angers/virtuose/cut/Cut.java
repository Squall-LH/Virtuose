package univ_angers.virtuose.cut;
import univ_angers.virtuose.utils.Writer;


public class Cut {

	public static void start(String[] args) {
		
		String file = "src/main/resources/Manceau-alain-rai-UIPL.mm";
		String docs = "/home/etudiant/cardsPagination/";
		
		Writer.proceed(docs, file);
	}

}
