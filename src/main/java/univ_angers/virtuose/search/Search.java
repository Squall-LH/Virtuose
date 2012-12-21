package univ_angers.virtuose.search;

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
import java.util.logging.Logger;

import org.apache.lucene.analysis.fr.FrenchAnalyzer;

import univ_angers.virtuose.utils.Writer;

public class Search {
	
	//private static Logger log = Logger.getLogger(Search.class);

	public static void index(String file){
		try{
			FrenchAnalyzer analyzer = new FrenchAnalyzer(Version.LUCENE_40);
			String indexpath = System.getProperty("user.dir")+"/src/ressources/index";
		    Directory index = FSDirectory.open(new File(indexpath));
		    IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
	
		    IndexWriter w = new IndexWriter(index, config);
			
			Writer.proceed(file);
		    ArrayList<Document> documents = new ArrayList<Document> (Writer.docs);
		    
		    for( Document d: documents){
		    	w.addDocument(d);
		    }
		    w.close();
		}
		catch( Exception e){}
		
	}
	
	public static String search( String querystr){
		// the "title" arg specifies the default field to use
	    // when no field is explicitly specified in the query.
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
		      System.out.println((i + 1) + ". " + d.get("title") + "\n" + d.get("content")+"\n"+hits[i].score);
		    }
		    
		    Document d = searcher.doc(hits[0].doc);
		    String result = d.get("content");
	
		    // reader can only be closed when there
		    // is no need to access the documents any more.
		    reader.close();
		    return result;
		}
		catch(Exception e){
			//log.error(e.getMessage());
			return null;
		}
	}
	
	
  public static void main(String[] args) throws IOException, ParseException {
	index(System.getProperty("user.dir")+"/src/ressources/Manceau-alain-rai-UIPL.mm");
	
	Writer.proceed(System.getProperty("user.dir")+"/src/ressources/Manceau-alain-rai-UIPL.mm");
    // 2. query
    String querystr = args.length > 0 ? args[0] : "matériel ALCATEL";
    search(querystr);
  }
}