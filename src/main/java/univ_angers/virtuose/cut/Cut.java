package univ_angers.virtuose.cut;
import univ_angers.virtuose.utils.Writer;


public class Cut {

	public static void main(String[] args) {
		String file = "/home/etudiant/Documents/projet_annuel/Manceau-alain-rai-UIPL.mm";
		String docs = "/home/etudiant/cardsPagination/";
		
		Writer.proceed(docs, file);

	}

}
