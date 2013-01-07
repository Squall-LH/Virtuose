package univ_angers.virtuose.search;

import univ_angers.virtuose.rendu.Extract;
import univ_angers.virtuose.utils.Writer;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.analysis.fr.FrenchAnalyzer;

public class Search {
	
	private static Logger log = Logger.getLogger(Search.class);

	public void index(String file){
		try{
			FrenchAnalyzer analyzer = new FrenchAnalyzer(Version.LUCENE_40);
			String indexpath = System.getProperty("user.dir")+"/src/ressources/index";
		    Directory index = FSDirectory.open(new File(indexpath));
		    IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
	
		    IndexWriter w = new IndexWriter(index, config);
			
		    Writer writer = new Writer();
			writer.proceed(file);
		    ArrayList<Document> documents = new ArrayList<Document> (writer.docs);
		    
		    for( Document d: documents){
		    	w.addDocument(d);
		    }
		    w.close();
		}
		catch( Exception e){}
		
	}
	
	/**
	 * 
	 * @param querystr requête au format String
	 * @return a list of all 10 documents that match the query result
	 */
	public ArrayList<Document> search( String querystr){
		// the "title" arg specifies the default field to use
	    // when no field is explicitly specified in the query.
		ArrayList<Document> founded=new ArrayList<Document>();
		try{
			FrenchAnalyzer analyzer = new FrenchAnalyzer(Version.LUCENE_40);
		    Query q = new QueryParser(Version.LUCENE_40, "content", analyzer).parse(querystr);
		    System.out.println("query pure: " + querystr + "\n" + q.toString());
		    // 3. search
		    int hitsPerPage = 10;
		    
		    String indexpath = System.getProperty("user.dir")+"/src/ressources/index";
		    Directory index = FSDirectory.open(new File(indexpath));
		    
		    IndexReader reader = DirectoryReader.open(index);
		    IndexSearcher searcher = new IndexSearcher(reader);
		    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		    searcher.search(q, collector);
		    ScoreDoc[] hits = collector.topDocs().scoreDocs;
		    // 4. display results
		    System.out.println("Found " + hits.length + " hits.");
		    for(int i=0;i<hits.length;++i) {
		      int docId = hits[i].doc;
		      Document d = searcher.doc(docId);
		      log.info((i + 1) + ". " + d.get("title")+ "\n" + d.get("document") + "\n" + d.get("content")+"\n"+d.get("id")+"\n"+d.get("id_parent")+"\n"+hits[i].score);
		      founded.add(d);
		    }
	
		    // reader can only be closed when there
		    // is no need to access the documents any more.
		    reader.close();
		    return founded;
		}
		catch(Exception e){
			//log.error(e.getMessage());
			return founded;
		}
	}
	
	
  public static void main(String[] args) throws Exception {
	Search search = new Search();
	search.index(System.getProperty("user.dir")+"/src/ressources/Manceau-alain-rai-UIPL.mm");
	
	Writer writer = new Writer();
	writer.proceed(System.getProperty("user.dir")+"/src/ressources/Manceau-alain-rai-UIPL.mm");
    // 2. query
    String querystr = args.length > 0 ? args[0] : "matériel ALCATEL";
    ArrayList<Document> docs = search.search(querystr);
    ArrayList<String> xmls = new ArrayList<String>();
    /**
     * Generate all xml documents according to the lucene doc that match the request
     */
    //Document d = docs.get(0);
    for (Document d : docs){
    	String tmp = Extract.extract(d.get("document"), d.get("id"));
    	xmls.add(tmp);
    }
    
    for (String s : xmls){
    	System.out.println(s);
    }
    
  }
}