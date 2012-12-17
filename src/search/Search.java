package search;

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
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.apache.lucene.analysis.fr.FrenchAnalyzer;

public class Search {
  public static void main(String[] args) throws IOException, ParseException {

    // 0. Specify the analyzer for tokenizing text.
    //    The same analyzer should be used for indexing and searching
    FrenchAnalyzer analyzer = new FrenchAnalyzer(Version.LUCENE_40);

    // 1. create the index
    Directory index = new RAMDirectory();

    IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);

    IndexWriter w = new IndexWriter(index, config);
    addDoc(w, "/home/etudiant/lucene_docs_src/Manceau-alain-rai-UIPL_mm6.xml");
    addDoc(w, "/home/etudiant/lucene_docs_src/Manceau-alain-rai-UIPL_mm7.xml");
    addDoc(w, "/home/etudiant/lucene_docs_src/Manceau-alain-rai-UIPL_mm8.xml");
    addDoc(w, "/home/etudiant/lucene_docs_src/Manceau-alain-rai-UIPL_mm9.xml");
    w.close();

    // 2. query
    String querystr = args.length > 0 ? args[0] : "d'affaire";

    // the "title" arg specifies the default field to use
    // when no field is explicitly specified in the query.
    Query q = new QueryParser(Version.LUCENE_40, "content", analyzer).parse(querystr);
    System.out.println("query pure: " + querystr + "\n" + q.toString());
    // 3. search
    int hitsPerPage = 10;
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
      System.out.println((i + 1) + ". " + d.get("title") + "\n" + d.get("content"));
    }

    // reader can only be closed when there
    // is no need to access the documents any more.
    reader.close();
  }

  private static String readFile(String filename) throws IOException {
	   String lineSep = System.getProperty("line.separator");
	   BufferedReader br = new BufferedReader(new FileReader(filename));
	   String nextLine = "";
	   StringBuffer sb = new StringBuffer();
	   while ((nextLine = br.readLine()) != null) {
	     sb.append(nextLine);
	     //
	     // note:
	     //   BufferedReader strips the EOL character
	     //   so we add a new one!
	     //
	     sb.append(lineSep);
	   }
	   return sb.toString();
	}
  
  private static void addDoc(IndexWriter w, String title) throws IOException {
	String content = readFile(title);
	System.out.println(title+" :");
	System.out.println(content+"\n");
    Document doc = new Document();
    doc.add(new TextField("title", title, Field.Store.YES));

    doc.add(new TextField("content", content, Field.Store.YES));
    w.addDocument(doc);
  }
}